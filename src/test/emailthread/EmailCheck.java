package test.emailthread;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

import javax.naming.*;
import javax.naming.directory.*;

import test.util.ByteBufferUtil;

public class EmailCheck {
	private ByteBuffer buffer = ByteBuffer.allocateDirect(1000);
	
	private String readMessage(SocketChannel sc) {
		String temp = "";
		
		buffer.clear();
		try {
			sc.read(buffer);
			buffer.flip();
			temp = ByteBufferUtil.bb_to_str(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temp;
	}
	
	private int checkMessage(SocketChannel sc) {
		String message = readMessage(sc);
		//System.out.print(message);
		message = message.substring(0,3);
		
		return Integer.parseInt(message);
	}
	
	private void writeCLI(SocketChannel sc, String message) {
		try {
			sc.write(ByteBufferUtil.str_to_bb(message+"\r\n"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<String> getMX(String hostName) throws NamingException{
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
	
	public boolean isEmailValid(String email) {
		int pos = email.indexOf('@');
		
		if(pos == -1) return false;
		
		String domain = email.substring(++pos);
		
		ArrayList<String> mxlist = null;

		try {
			mxlist = getMX(domain);
		}catch(NamingException ne) {
			return false;
		}

		if(mxlist.size() == 0) return false;
		SocketChannel sc = null;
		try {
			sc = SocketChannel.open();
			sc.configureBlocking(true);
			sc.socket().setSoTimeout(5000);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int mx = 0 ; mx < mxlist.size() ; mx++) {
			boolean valid = false;
			
			try{
				
				sc.connect(new InetSocketAddress(mxlist.get(mx),25));
				
				int res = checkMessage(sc);
				if(res != 220) {
					throw new Exception("Invalid Header");
				}
				
				writeCLI(sc,"EHLO orbaker.com");
				
				res = checkMessage(sc);
				if(res != 250) {
					throw new Exception("Not ESMTP");
				}

				writeCLI(sc,"MAIL FROM: <tim@orbaker.com>");
				res = checkMessage(sc);
				if(res != 250) {
					throw new Exception("Sender Rejected");
				}
				
				writeCLI(sc,"RCPT TO: <"+email+">");
				res = checkMessage(sc);

				writeCLI(sc,"RSET"); 
				checkMessage(sc);
				writeCLI(sc,"QUIT");
				checkMessage(sc);

				if(res != 250) {
					throw new Exception("Address is not valid");
				}
				
				valid = true;
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
}