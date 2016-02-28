package test.url;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class GcmSender {
	private final static String API_KEY = "AIzaSyDdR1z63qJEa8gc6LRD6fEoTbMjEaAQjHU";
	private final static String REG_ID	= "fUhJRJEvfjg:APA91bESlwsX5fMHnCkqHgI5OWPGAAC2M7B1PWmigjB9mTpkZQOpj63CNlB0k3VRhmwInsCclFgTpP4VJm3_dYZfqcuzjKeyNka4GlCVeaEaEDY46lbLRNLD_vZU0q1bkYpb1lMBMgz9";
	
	
	private JSONObject getJsonParam(String reg_id, String title, String contents) throws JSONException {
		JSONObject param = new JSONObject();
		
		JSONObject notification		= new JSONObject();
		
		JSONObject data		= new JSONObject();
		
		param.put("to", reg_id);
		param.put("priority", "high");
		param.put("notification", notification);
		param.put("data", data);
		
		data.put("title", title);
		data.put("message",contents);
		data.put("sound", "default");
		
		notification.put("title", title);
		notification.put("body", contents);
		notification.put("sound", "default");
		
		return param;
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println(System.getProperty("file.encoding"));
		JSONObject param = new GcmSender().getJsonParam(REG_ID, "Infomation", "개성공단테스트 관련으로 인한 push 테스트");
		System.out.println(param.toString());
		
		URL url = new URL("https://android.googleapis.com/gcm/send");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "key=" + API_KEY);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		
		OutputStream os = conn.getOutputStream();
		os.write(param.toString().getBytes());
		
		InputStream is = conn.getInputStream();
		String resp = IOUtils.toString(is);
		System.out.println(resp);
	}
}
