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
import java.util.Enumeration;
import java.util.Hashtable;

public class JavaPost {
	public static String getPost(String weburl) {
		PrintWriter   out = null;
		BufferedReader br = null;
		InputStream    is = null;
		
		StringBuffer sb = new StringBuffer();
		try {
			URL url				 = new URL(weburl);
			URLConnection webcon = url.openConnection();

			webcon.setDoOutput(true);
			webcon.setUseCaches(true);

			is = webcon.getInputStream();
			br = new BufferedReader(new InputStreamReader(is),8*1024);

			String line = null;
			while((line=br.readLine())!= null ) {
				sb.append(line);
			}
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
		
		return sb.toString();
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
		JavaPost.getPost("https://welfare.sports.or.kr:8443"+"/job/adm/recruit/makemailtemplate.do?system_id="+"hyounbae");
	}
}