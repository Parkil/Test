package temp;

import com.rathontech.common.crypt.RathonCrypt;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SSOGetData {
	private static final Logger logger = LoggerFactory.getLogger(SSOGetData.class);
	private RathonCrypt rc;
	// private boolean loadSuccess;

	public JsonNode imUserApi(String uid) throws Exception {
		/*
		 * if (!this.loadSuccess) { return "설정을 불러오는데 실패했습니다."; }
		 */
		String aID = "sports";
		String aTOKEN = "UaPr96C4lNP7bDTuNeGfKA==";

		rc = new RathonCrypt();
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

		Map<String, String> vars = new HashMap<String, String>();
		Map<String, String> map = new HashMap<String, String>();

		vars.put("ID", aID);
		vars.put("TOKEN", aTOKEN);
		vars.put("TIMESTAMP", String.valueOf(new Date().getTime()));
		vars.put("RTUID", uid);

		logger.info("IM Api Set Data");

		String url = "http://211.232.186.91:8090/rtim-manager/api/user/getUserMasterInfo";

		this.rc.generateHMac(vars);

		ObjectMapper om = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity request = null;
		JsonNode node = null;

		try {
			request = new HttpEntity(om.writeValueAsBytes(vars), headers);
		} catch (IOException e) {
			logger.info("Generate IM Api Request");
		}
		try {
			ResponseEntity<byte[]> result = restTemplate.exchange(url, HttpMethod.POST, request, byte[].class);
			logger.error("Get IM Response Data");
			try {
				node = om.readValue(result.getBody(), JsonNode.class);
				System.out.println("check: " + node);
			} catch (IOException e) {
				logger.error("IOException: {}", e.getMessage());
				map.put("OPException", "응답 데이터를 읽는데 실패했습니다.");
				node = om.readTree(om.writeValueAsBytes(map));
				return node;
			}
		} catch (Exception e) {
			logger.error("HttpClientErrorException: {}", e.getMessage());
			map.put("HttpClientErrorException", "서버가 응답하지 않습니다.");
			node = om.readTree(om.writeValueAsBytes(map));
			return node;
		}

		if (node.findPath("responseCode").asText().equals("SUCCESS")) {
			System.out.println("SUCCESS");
			return node;
		}
		String errMsg = node.findPath("errMsg").asText();
		System.out.println("FAILED");
		System.out.println("ERROR MESSAGE : " + errMsg);
		return node;
	}

	public static void main(String[] args) throws Exception{
		SSOGetData sso = new SSOGetData();
		sso.imUserApi("ljsworld5");
	}
}
