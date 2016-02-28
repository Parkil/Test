package test.url;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.Hashtable;

import test.util.ByteBufferUtil;

/*
 * 자바에서 Post방식으로 URL을 호출하고 결과값을 받아오는 예제
 */
public class JavaPost {
	private static ByteBuffer buffer= ByteBuffer.allocateDirect(1000);

	public static void getPostNio(String weburl) {
		try {
			URL			url = new URL(weburl);
			InputStream is  = url.openConnection().getInputStream();

			ReadableByteChannel readChannel = Channels.newChannel(is);


			int readsize = 0;

			do {
				JavaPost.buffer.clear();
				readsize =readChannel.read(JavaPost.buffer);
				JavaPost.buffer.flip();

				String temp = ByteBufferUtil.bb_to_str(JavaPost.buffer);
				System.out.println(temp);
			}while(readsize != -1);

			//int first = temp.indexOf("사용자");
			//int end   = temp.lastIndexOf("</body>");
			//System.out.println(temp.substring(first,end));

			readChannel.close();
			is.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void getPost(String weburl) {
		PrintWriter   out = null;
		BufferedReader br = null;
		InputStream    is = null;

		try {
			URL url				 = new URL(weburl);
			URLConnection webcon = url.openConnection();

			webcon.setDoOutput(true);
			webcon.setUseCaches(true);

			is = webcon.getInputStream();
			br = new BufferedReader(new InputStreamReader(is),8*1024);

			String line = null;
			while((line=br.readLine())!= null ) {
				System.out.println(line);
			}
			/*
			PrintWriter out = new PrintWriter(webcon.getOutputStream());
			out.print("name=이원영&address=경기도 부천시 소사구...&msg=Java's Power");
			 */
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if ( is != null ) {
				try {is.close();}catch(Exception e){}
			}
			if ( out != null ) {
				try {out.close();}catch(Exception e){}
			}
			if ( br != null ) {
				try {br.close();}catch(Exception e){}
			}
		}
	}


	public static String url_encoding(Hashtable<String,String> hash){
		if ( hash == null ) {
			throw new IllegalArgumentException("argument is null");
		}
		Enumeration<String> enu = hash.keys();
		StringBuffer buf = new StringBuffer();
		boolean isFirst = true;

		while(enu.hasMoreElements()){
			if (isFirst) {
				isFirst = false;
			} else {
				buf.append('&');
			}
			String key = enu.nextElement();
			String value = hash.get(key);
			try {
				buf.append(java.net.URLEncoder.encode(key,"utf-8"));
				buf.append('=');
				buf.append(java.net.URLEncoder.encode(value,"utf-8"));
			}catch(UnsupportedEncodingException uee) {
				uee.printStackTrace();
			}
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		Hashtable<String,String> param = new Hashtable<String,String>();
		param.put("key", "AIzaSyAEW_VJlk9jQFycqkF5tDC0O8ui1r405sE");
		param.put("to", "kLw3oLkH_lA:APA91bEZDV9WRVwv306m9Y-ieRPCmPkYzlfS2lx42Txu9qySrS0B4LI--BGmY6OsGLV-Rwq5---cO70OBtglP3ZZugj6ZHzjVZon0hASo1hoqlDbLRPMf9JA5Y7yUNXQyYbIxR5XFqgs");
		param.put("content_available", "true");
		param.put("priority", "high");
		param.put("pwd", "oks123$%^");
		param.put("fromNumber", "01021243262");
		param.put("sendType", "3");
		/*
		ByteBufferUtil.setEncoding("utf-8");
		JavaPost.getPostNio("http://t.datapoint.co.kr/tower/lookup/lookup.info?uid=kotra&apikey=ZeZsc1NYCPJvw%2FnCE9HHGw%3D%3D&pkg=G08001&business=1248100998&cid=admin");
		*/
	}
}