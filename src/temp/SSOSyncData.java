package temp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

//import com.rathontech.common.constants.RtimConstants;
import com.rathontech.common.crypt.RathonCrypt;

public class SSOSyncData {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RestTemplate restTemplate = new RestTemplate();

	private RathonCrypt rc = new RathonCrypt();

	private String url = "http://211.232.186.91:8090/rtim-manager/api/svc/accSyncProcess";

	public void SSOSync() throws Exception{
		logger.debug("=================================================================================");
		logger.debug("============================== accSyncProcess(기존계정동기화) ======================");

		// 필수입력
		String id = "sports";
		String token = "UaPr96C4lNP7bDTuNeGfKA==";
		String timeStamp = new Date().getTime() + "";
		String systemId = "ljsworld5"; // 시스템 ID(API 호출할때 시스템의 로그인 ID)
		String serviceId = "LEADER"; // 서비스ID

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ID", id); // 필수
		paramMap.put("TOKEN", token); // 필수
		paramMap.put("TIMESTAMP", timeStamp); // 필수
		paramMap.put("SYSTEMID", systemId); // 필수
		paramMap.put("SERVICEID", serviceId); // 필수

		// 메세지 보안 적용 헤더 담기전에 꼭 필수로 해줘야한다 안할시 메세지 보안 실패 메세지 보안 하기위해서는 라톤 크립스 써야함
		rc.generateHMac(paramMap);
		ObjectMapper om = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity request = new HttpEntity(om.writeValueAsBytes(paramMap), headers);

		final ResponseEntity<byte[]> result = restTemplate.exchange(url, HttpMethod.POST, request, byte[].class);

		JsonNode node = om.readValue(result.getBody(), JsonNode.class);

		System.out.println("Account Sync ServiceID : " + node);
		
		System.out.println(node.findPath("responseCode").asText());
		/*
		if (RtimConstants.SUCCESS.equals(node.findPath("responseCode").asText())) {
			System.out.println("SUCCESS");

			Map<String, String> message = om.readValue(node.findPath("message"),
					new TypeReference<HashMap<String, String>>() {
					});
			System.out.println("MESSAGE : " + message);
		} else {
			System.out.println("FAILED");
			System.out.println("ERROR MESSAGE : " + node.findPath("errMsg").asText());
		}*/

		logger.debug("=================================================================================");
	}

	public static void main(String[] args) throws Exception{
		SSOSyncData temp = new SSOSyncData();
		temp.SSOSync();
	}
}
