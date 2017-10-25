package temp;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * apahce http 라이브러리를 이용한 SMR호출
 */
public class ApahceHttpSMRCall {

	public String callHttpGet(String base_url, Map<String, String> param_map, int time_out)
			throws ApiCallException, ClientProtocolException, IOException {
		Set<String> keySet = param_map.keySet();
		Iterator<String> keyIter = keySet.iterator();

		List<NameValuePair> param_list = new ArrayList<NameValuePair>();
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = param_map.get(key);

			param_list.add(new BasicNameValuePair(key, value));
		}

		// Connection timeout 설정
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(time_out)
				.setConnectTimeout(time_out).setSocketTimeout(time_out).setCookieSpec(CookieSpecs.STANDARD).build();

		HttpGet httpGet = new HttpGet(base_url + "?" + URLEncodedUtils.format(param_list, "utf-8"));
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

		CloseableHttpResponse response = httpClient.execute(httpGet);

		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new ApiCallException(
					"API호출시 문제가 발생하였습니다. HTTP StatusCode : " + response.getStatusLine().getStatusCode());
		}

		HttpEntity entity = response.getEntity();
		return IOUtils.toString(entity.getContent(), "utf-8");
	}
	
	public String callHttpPost(String base_url, Map<String, String> param_map, int time_out) throws ApiCallException, ClientProtocolException, IOException {
		Set<String> keySet = param_map.keySet();
		Iterator<String> keyIter = keySet.iterator();

		List<NameValuePair> param_list = new ArrayList<NameValuePair>();
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			String value = param_map.get(key);

			param_list.add(new BasicNameValuePair(key, value));
		}

		// Connection timeout 설정
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(time_out)
				.setConnectTimeout(time_out).setSocketTimeout(time_out).setCookieSpec(CookieSpecs.STANDARD).build();

		HttpPost httpPost = new HttpPost(base_url + "?" + URLEncodedUtils.format(param_list, "utf-8"));
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

		CloseableHttpResponse response = httpClient.execute(httpPost);

		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new ApiCallException(
					"API호출시 문제가 발생하였습니다. HTTP StatusCode : " + response.getStatusLine().getStatusCode());
		}

		HttpEntity entity = response.getEntity();
		return IOUtils.toString(entity.getContent(), "utf-8");
	}

	public Map<String, List<String>> getSMRData(String base_url, Map<String, String> queryString_map) {
		Map<String, List<String>> ret_map = new HashMap<String, List<String>>();

		String json_str = null;
		;
		try {
			json_str = callHttpGet(base_url, queryString_map, 3000);
		} catch (ApiCallException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONObject obj = new JSONObject(json_str);

			int response_code = obj.getInt("responsecode"); // 응답코드 0(정상) 1(에러)
			JSONArray arr = (JSONArray) obj.get("recolist");

			if (response_code == 1) { // SMR 호출도중 에러발생시 null반환
				return null;
			}

			for (int i = 0, length = arr.length(); i < length; i++) {
				JSONObject arr_obj = (JSONObject) arr.get(i);
				String layout = arr_obj.getString("layout");

				JSONArray arr2 = (JSONArray) arr_obj.get("recoclipid");

				List<String> clip_list = new ArrayList<String>();
				for (int j = 0, arr_length = arr2.length(); j < arr_length; j++) {
					clip_list.add(arr2.getString(j));
				}

				ret_map.put(layout, clip_list);
			}
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}

		return ret_map;
	}

	public static void main(String[] args) throws Exception {
		/*
		 * http get테스트 코드
		 */
		/*
		String base_url = "http://recommend.smartmediarep.com/smr_reco/media/reco.action";
		
		Map<String,String> queryString_map = new HashMap<String,String>();
		queryString_map.put("version", "1.0"); queryString_map.put("media","SMR_MEMBERS");
		queryString_map.put("site", "ALL");
		queryString_map.put("page", "CP_HOME"); //CP_HOME(전체),
		queryString_map.put("cpid", "C0"); //C0는 전체 C1,C2는 각 방송사별
		//queryString_map.put("programid", "S01_V0000330171");
		//queryString_map.put("clipid", "S01_22000148597");
		queryString_map.put("recotype", "SR_TOP"); queryString_map.put("recocnt","50");
		
		Map<String,List<String>> map = new ApahceHttpSMRCall().getSMRData(base_url,queryString_map);
		System.out.println(map);
		*/
		
		/*
		 * http post 
		 */
		String base_url = "http://medialog.smartmediarep.com/api/v20/tracking/tracking.sb";
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwbGF5ZGF0ZSI6IjIwMTUwNDI5MTAxMDEyIiwiaXNwYXkiOiJOIiwiZ2VuZGVyIjoiMyIsInZvZHR5cGUiOiJDIiwiY3BpZCI6IkMxIiwicmVxdWVzdHRpbWUiOiIyMDE3MTAxOTEzNDMwMCIsImlwIjoiMTcyLjMwLjMyLjYxIiwic2VjdGlvbiI6IjAxIiwibWVkaWEiOiJTTVJfTUVNQkVSUyIsInZlcnNpb24iOiIxLjYiLCJ1dWlkIjoiNGUxNDA5YTA5MDRkODgyNWMxZjRiYzc1MmJhZmJjMTEiLCJ0aWQiOiI0ZTE0MDlhMDkwNGQ4ODI1YzFmNGJjNzUyYmFmYmMxMSIsInBsYXRmb3JtIjoiTU9CSUxFV0VCIiwic2l0ZSI6IlNNUklOSlRCQyIsInRyYWNrcG9pbnQiOiIwIiwiY2xpcGlkIjoiUzAxX0NsaXBJRCIsImNhdGVnb3J5IjoiMDEiLCJhZ2UiOiI5OSIsImNoYW5uZWxpZCI6IlMwMSIsInByb2dyYW1pZCI6IlMwMV9WMDAwMDM1OTkzNiJ9.9fpkrJvFOSBdru2KUINMyGXiDZY1JQF5ZzYmcu1nIrE";

		Map<String,String> queryString_map = new HashMap<String,String>();
		queryString_map.put("medialog", token);
		
		Map<String,List<String>> map = new ApahceHttpSMRCall().getSMRData(base_url, queryString_map);
		System.out.println(map);
		
		/*
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwbGF5ZGF0ZSI6IjIwMTUwNDI5MTAxMDEyIiwiaXNwYXkiOiJOIiwiZ2VuZGVyIjoiMyIsInZvZHR5cGUiOiJDIiwiY3BpZCI6IkMxIiwicmVxdWVzdHRpbWUiOiIyMDE3MTAxOTEzNDMwMCIsImlwIjoiMTcyLjMwLjMyLjYxIiwic2VjdGlvbiI6IjAxIiwibWVkaWEiOiJTTVJfTUVNQkVSUyIsInZlcnNpb24iOiIxLjYiLCJ1dWlkIjoiNGUxNDA5YTA5MDRkODgyNWMxZjRiYzc1MmJhZmJjMTEiLCJ0aWQiOiI0ZTE0MDlhMDkwNGQ4ODI1YzFmNGJjNzUyYmFmYmMxMSIsInBsYXRmb3JtIjoiTU9CSUxFV0VCIiwic2l0ZSI6IlNNUklOSlRCQyIsInRyYWNrcG9pbnQiOiIwIiwiY2xpcGlkIjoiUzAxX0NsaXBJRCIsImNhdGVnb3J5IjoiMDEiLCJhZ2UiOiI5OSIsImNoYW5uZWxpZCI6IlMwMSIsInByb2dyYW1pZCI6IlMwMV9WMDAwMDM1OTkzNiJ9.9fpkrJvFOSBdru2KUINMyGXiDZY1JQF5ZzYmcu1nIrE";
		String base_url = "http://medialog.smartmediarep.com/api/v20/tracking/tracking.sb";
		List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
		parameterList.add(new BasicNameValuePair("medialog", token));
		
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		//커넥션 타임 아웃 설정.
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000)
									.setCookieSpec(CookieSpecs.STANDARD)
									.build();
		
		HttpPost httppost = new HttpPost(base_url + "?" +URLEncodedUtils.format(parameterList, "utf-8"));
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
		response = httpClient.execute(httppost);
		
		System.out.println(response);
		*/
	}
}
