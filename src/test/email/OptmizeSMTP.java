package test.email;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import test.util.ByteBufferUtil;

public class OptmizeSMTP {
	private static ByteBuffer buffer = ByteBuffer.allocateDirect(1000);

	private static String readMessage(SocketChannel sc) {
		String temp = "";

		OptmizeSMTP.buffer.clear();
		try {
			sc.read(OptmizeSMTP.buffer);
			OptmizeSMTP.buffer.flip();
			temp = ByteBufferUtil.bb_to_str(OptmizeSMTP.buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return temp;
	}

	private static int checkMessage(SocketChannel sc) {
		String message = OptmizeSMTP.readMessage(sc);
		//System.out.print(message);
		message = message.substring(0,3);

		return Integer.parseInt(message);
	}

	private static void writeCLI(SocketChannel sc, String message) {
		try {
			sc.write(ByteBufferUtil.str_to_bb(message+"\r\n"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<String> getMX(String hostName) throws NamingException{
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		DirContext ictx = new InitialDirContext( env );

		Attributes attrs = ictx.getAttributes( hostName, new String[] {"MX"});
		Attribute attr = attrs.get("MX");

		if ((attr == null) || (attr.size() == 0)) {
			attrs = ictx.getAttributes(hostName, new String[]{"MX"});
			attr = attrs.get("A");

			if(attr == null) {
				throw new NamingException("No Match for name '"+hostName+"'");
			}
		}

		ArrayList<String> res = new ArrayList<String>();
		NamingEnumeration<String> en = (NamingEnumeration<String>)attr.getAll();

		while(en.hasMore()) {
			String x = en.next();
			String f[] = x.split(" ");

			if(f[1].endsWith(".")) {
				f[1] = f[1].substring(0,(f[1].length()-1));
			}

			res.add(f[1]);
		}

		return res;
	}

	public static boolean isEmailValid(String email) {
		int pos = email.indexOf('@');

		if(pos == -1) {
			return false;
		}

		String domain = email.substring(++pos);

		ArrayList<String> mxlist = null;

		try {
			mxlist = OptmizeSMTP.getMX(domain);
		}catch(NamingException ne) {
			return false;
		}

		if(mxlist.size() == 0) {
			return false;
		}

		SocketChannel sc = null;
		try {
			sc = SocketChannel.open();
			sc.configureBlocking(true);
			sc.socket().setSoTimeout(5000);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		boolean checkresult = false;

		for(int mx = 0 ; mx < mxlist.size() ; mx++) {
			try{
				/*
				 * 1건 체크시 mx리스트 개수만큼 새로 connection을 맺고 있으며 이는 상당한 부하를 가져오고 있음
				 * Connection Pool을 이용하여 기존 mx서버 Connection을 유지하는 방향으로 수정할 필요가 있음
				 * mx 리스트를 가져오는 것도 네트워크를 이용하면 이도 Pool기법으로 재활용 필요가 있음
				 */
				sc.connect(new InetSocketAddress(mxlist.get(mx),25));

				int res = OptmizeSMTP.checkMessage(sc);
				if(res != 220) {
					throw new Exception("Invalid Header");
				}

				OptmizeSMTP.writeCLI(sc,"EHLO orbaker.com");

				res = OptmizeSMTP.checkMessage(sc);
				if(res != 250) {
					throw new Exception("Not ESMTP");
				}

				OptmizeSMTP.writeCLI(sc,"MAIL FROM: <alkain77@gmail.com>");
				res = OptmizeSMTP.checkMessage(sc);
				if(res != 250) {
					throw new Exception("Sender Rejected");
				}

				OptmizeSMTP.writeCLI(sc,"RCPT TO: <"+email+">");
				res = OptmizeSMTP.checkMessage(sc);

				OptmizeSMTP.writeCLI(sc,"RSET");
				OptmizeSMTP.checkMessage(sc);
				OptmizeSMTP.writeCLI(sc,"QUIT");
				OptmizeSMTP.checkMessage(sc);

				if(res != 250) {
					throw new Exception("Address is not valid");
				}

				checkresult = true;
				break;
			}catch(Exception e) {
				e.printStackTrace();
				checkresult = false;
				continue;
			}
		}

		try {
			if(sc != null) {
				sc.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return checkresult;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String testData[] = {
				"sfewsefes@yahoo.co.kr"
		};

		for(String test : testData) {
			System.out.println(test + " is Valid ? "+OptmizeSMTP.isEmailValid(test));
		}

		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
	}
}