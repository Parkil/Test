package test.email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class SMTP {
	private static int hear(BufferedReader in) throws IOException {
		String line = null;
		int res = 0;

		while((line = in.readLine()) != null) {
			String pfx = line.substring(0,3);
			try {
				res = Integer.parseInt(pfx);
			}catch(Exception e) {
				e.printStackTrace();
				res = -1;
			}

			if(line.charAt(3) != '-') {
				break;
			}
		}

		return res;
	}

	private static void say(BufferedWriter wr,String text) throws IOException {
		wr.write(text+"\r\n");
		wr.flush();
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
			mxlist = SMTP.getMX(domain);
		}catch(NamingException ne) {
			return false;
		}

		if(mxlist.size() == 0) {
			return false;
		}
		for(int mx = 0 ; mx < mxlist.size() ; mx++) {
			boolean valid = false;

			try{
				int res;
				SocketChannel sc = SocketChannel.open();
				sc.socket().setSoTimeout(4000);
				//System.out.println(mxlist.get(mx));
				sc.connect(new InetSocketAddress(mxlist.get(mx),25));

				//Socket skt = new Socket((String)mxlist.get(mx),25);

				BufferedReader rdr = new BufferedReader(new InputStreamReader(sc.socket().getInputStream()));
				BufferedWriter wtr = new BufferedWriter(new OutputStreamWriter(sc.socket().getOutputStream()));

				res = SMTP.hear(rdr);
				if(res != 220) {
					throw new Exception("Invalid Header");
				}
				SMTP.say(wtr,"EHLO "+mxlist.get(mx));

				res = SMTP.hear(rdr);
				if(res != 250) {
					throw new Exception("Not ESMTP");
				}

				SMTP.say(wtr,"MAIL FROM: <tim@orbaker.com>");
				res = SMTP.hear(rdr);
				if(res != 250) {
					throw new Exception("Sender Rejected");
				}

				SMTP.say(wtr,"RCPT TO: <"+email+">");
				res = SMTP.hear(rdr);
				System.out.println("RES1 : "+res);

				SMTP.say(wtr,"RCPT TO: <zzzz@gmail.com>");
				res = SMTP.hear(rdr);
				System.out.println("RES2 : "+res);

				SMTP.say(wtr,"RSET");
				SMTP.hear(rdr);

				SMTP.say(wtr,"QUIT");
				SMTP.hear(rdr);

				if(res != 250) {
					throw new Exception("Address is not valid");
				}

				valid = true;

				rdr.close();
				wtr.close();
				sc.close();
			}catch(Exception e) {
				return false;
			}finally {
				if(valid) {
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String testData[] = {
				"alkain77@gmail.com"
		};

		for(String test : testData) {
			boolean result = SMTP.isEmailValid(test.trim());

			System.out.println(result);
			/*Pattern rfc2822 = Pattern.compile(
			        "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
			);

			if (!rfc2822.matcher(test).matches()) {
				System.out.println((index++) +"->"+test);
			}*/
		}

		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
	}
}