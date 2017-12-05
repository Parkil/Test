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

import temp.ApiCallException;

public class FileJokerTest {

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

		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(time_out)
				.setConnectTimeout(time_out).setSocketTimeout(time_out).setCookieSpec(CookieSpecs.STANDARD).build();

		HttpGet httpGet = new HttpGet(base_url + "?" + URLEncodedUtils.format(param_list, "utf-8"));
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

		CloseableHttpResponse response = httpClient.execute(httpGet);
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			String contents = IOUtils.toString(entity.getContent(), "utf-8");
			int file_not_found_idx = contents.indexOf("File Not Found");
			int get_download_link_idx = contents.indexOf("Download Now");
			
			//System.out.println("file_not_found_idx : "+file_not_found_idx);
			//System.out.println("get_download_link_idx : "+get_download_link_idx);
			
			if(file_not_found_idx == -1 && get_download_link_idx != -1) {
				return base_url;
			}else {
				return "200 but file not found";
			}
		}else {
			return "404";
		}
	}

	public static void main(String[] args) throws Exception {
		Map<String,String> queryString_map = new HashMap<String,String>();
		String url = new FileJokerTest().callHttpGet("https://filejoker.net/r8hpikegfwfk", queryString_map, 3000);
		System.out.println(url);
	}
}
