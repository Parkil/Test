package test.url;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * url형식을 이용하여 Google GCM Push 메시지를 보내는 예제
 * 
 * url호출시 JSON형식으로 파라메터를 넘기는 부분과 url호출시 header를 지정하는 부분은 나중에 다른 형식의 url호출시 참고할만하다
 * JSON형식으로 데이터를 넘기는 부분은 java-json.jar api를 이용하였음
 */
public class GcmSender {
	
	private final static String API_KEY = "AIzaSyAEW_VJlk9jQFycqkF5tDC0O8ui1r405sE";
	private final static String REG_ID	= "dCQTFaJWw84:APA91bFn2mks_Qd0XX4YFeP3Q4qugj9d9gRPw5_R7LN4T4w7xIChtgDmCSipHMedfk6NsJM1E5bj5UZdrlF0kYcjrPNG50BryQgwxDzxHrwBweeZ3wwm6NOqxJD0H0r8tzmU6up75QrI";
	
	
	private JSONObject getJsonParam(String reg_id, String title, String contents) throws JSONException {
		JSONObject param = new JSONObject();
		
		//IOS 관련 JSON파라메터
		JSONObject notification		= new JSONObject();
		
		//안드로이드 관련 JSON파라메터
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
		JSONObject param = new GcmSender().getJsonParam(REG_ID, "제목", "내용");
		
		URL url = new URL("https://android.googleapis.com/gcm/send");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//url호출시 header 지정
		conn.setRequestProperty("Authorization", "key=" + API_KEY);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		
		//GCM 메시지 전송
		OutputStream os = conn.getOutputStream();
		os.write(param.toString().getBytes());
		
		//메시지 전송결과 수신
		InputStream is = conn.getInputStream();
		String resp = IOUtils.toString(is);
		System.out.println(resp);
	}
}
