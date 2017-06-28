package test.sso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import WiseAccess.SSO;
import test.sso.SeedCipher;

public class SSOUtil {
	/**SSO 사용자 데이터 추출
	 * @param im_server_url IM 서버 URL
	 * @param im_id IM 아이디
	 * @param im_token IM 토큰
	 * @param im_seed_key seed암호화 key
	 * @param target_id 데이터를 추출할 대상 ID
	 * @return
	 */
	public HashMap<String,Object> getSSOInfo(String im_server_url, String im_id, String im_token, String im_seed_key, String target_id) {
		String timeStamp = new Date().getTime() + "";
		HashMap<String,Object> ret_map = new HashMap<String,Object>();
		
		PrintWriter out= null;
		BufferedReader rd = null;
		try {
			SeedCipher seed = new SeedCipher();
			String new_target_id = Base64.encode(seed.encrypt(target_id, im_seed_key.getBytes(), "EUC-KR"));
			im_server_url = im_server_url + "/imapi/result/ruserlist.jsp";
			
			/* POST Parameters setting start */
			Map<String, Object> vars = new LinkedHashMap<String, Object>();
			vars.put("ID"		, im_id);		//필수 - 토큰ID
			vars.put("TOKEN"	, im_token);	//필수 - 토큰 PWD
			vars.put("TIMESTAMP", timeStamp);	//필수
			
			vars.put("RTUID"	, new_target_id);	//필수 - 사용자ID
			vars.put("TEMP"		, null);		//임시데이터 (IM 연동쪽에서 마지막 파라미터 값을 받을때 /n 값이 추가되어 임시데이터를 추가함)
			
			StringBuilder postData = new StringBuilder();
			for(Map.Entry<String,Object> param : vars.entrySet()) {
				if(postData.length() != 0) postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			
			URL url = new URL(im_server_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST"); // POST 호출	
			
			out = new PrintWriter(conn.getOutputStream());
			String sending_data = postData.toString(); //전송할 데이터(파라미터명=값)
			out.println(sending_data);
			out.flush();
			
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));   
	
			String line;
			StringBuffer sb = new StringBuffer();      
			while ((line = rd.readLine()) != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator")); 
			}
			
			ObjectMapper om = new ObjectMapper();
			JsonNode node = om.readValue(sb.toString(), JsonNode.class);
			
			if (node.findPath("responseCode").asText().equals("SUCCESS")) {
				
				String user_id = node.findPath("message").findPath("RTUID").toString();
				
				if(user_id == null || user_id.intern() == "".intern()) {
					ret_map.put("error", "sso info not found target_id:["+target_id+"]");
				}else {
					ret_map.put("success", "sso_info_get_success");
					ret_map.put("name"	, node.findPath("message").findPath("RTNAME"));
					ret_map.put("userEamil"	, node.findPath("message").findPath("RTEMAIL"));
					ret_map.put("userPwd"	, node.findPath("message").findPath("SERVICE_PASSWD"));
					ret_map.put("ci"	, node.findPath("message").findPath("RTSSN"));
					ret_map.put("user_birth" , node.findPath("message").findPath("RTCUSTOMER06"));
					ret_map.put("user_gender" , node.findPath("message").findPath("RTCUSTOMER10"));
					ret_map.put("user_id" , user_id);
					ret_map.put("mobileNumber"	, node.findPath("message").findPath("RTMOBILE"));
					ret_map.put("address1"	, node.findPath("message").findPath("RTCUSTOMER03"));
					ret_map.put("address2"	, node.findPath("message").findPath("RTCUSTOMER05"));
					ret_map.put("zipcode"	, node.findPath("message").findPath("RTCUSTOMER02"));
				}
			} else {
				ret_map.put("error", "sso_info_get_fail");
				ret_map.put("error_msg", node.findPath("message").findPath("RTNAME"));
			}
		}catch(UnsupportedEncodingException e1) {
			ret_map.put("error", "UnsupportedEncodingException");
			ret_map.put("error_msg", e1.getMessage());
		}catch(MalformedURLException e2) {
			ret_map.put("error", "MalformedURLException");
			ret_map.put("error_msg", e2.getMessage());
		}catch(ProtocolException e3) {
			ret_map.put("error", "ProtocolException");
			ret_map.put("error_msg", e3.getMessage());
		}catch(JsonMappingException e4) {
			ret_map.put("error", "JsonMappingException");
			ret_map.put("error_msg", e4.getMessage());
		}catch(IOException e5) {
			ret_map.put("error", "IOException");
			ret_map.put("error_msg", e5.getMessage());
		}finally {
			if(out != null) {
				out.close();
			}
			
			if(rd != null) {
				try {
					rd.close();
				}catch(IOException e) {
					
				}
			}
		}
		
		return ret_map;
	}
	
	public String getCookie ( HttpServletRequest request, String sName ) {
		Cookie[] cookies = request.getCookies();	
		if ( cookies != null ) 
		{	
			for (int i=0; i < cookies.length; i++) 
			{
				String name = cookies[i].getName();
				if( name != null && name.equals(sName) ) 
				{
					return cookies[i].getValue();
				}
			}
		}
		return null;	
	}

	/**SSO 로그인 체크
	 * @param sapi_key
	 * @param request
	 * @return
	 */
	public HashMap<String,Object> checkSSO(String sapi_key, HttpServletRequest request) {
		HashMap<String,Object> ret_map = new HashMap<String,Object>();
		
		String s_token = getCookie(request, "ssotoken");
		int chk_result = -1;
		
		if( s_token != null && s_token.length() > 0) {
			//SSO 객체 생성
			SSO sso = new SSO(sapi_key);
			chk_result = sso.verifyToken(s_token, request.getRemoteAddr());
			if( chk_result >= 0 ) {
				ret_map.put("result", "success");						//성공결와
				ret_map.put("ssoID", sso.getValueUserID());				//아이디
				ret_map.put("password", sso.getValue("PWD"));			//비밀번호
				ret_map.put("portalID", sso.getValue("PortalSSOID"));	//포탈 ID
				ret_map.put("ToSportsID", sso.getValue("SatSSOID"));	//토요스포트 ID
				ret_map.put("JidoID", sso.getValue("JidoSSOID"));		//지도자 ID
			} else {
				ret_map.put("result", "error");
				ret_map.put("error_msg", "sso token validate failed");
			}
		}else{
			ret_map.put("result", "error");
			ret_map.put("error_msg", "sso token not found");
		}
		
		return ret_map;
	}
	
	/** Portal id <-> 지도자시스템 ID간 동기화 수행(Portal id는 암호화를 수행하고 지도자 시스템 id는 동기화를 수행하지 않음)
	 * @param im_server_url IM 서버 URL
	 * @param im_id IM 아이디
	 * @param im_token IM 토큰
	 * @param im_seed_key seed암호화 key
	 * @param portal_id 포탈 ID
	 * @param slims_id 지도자 시스템 ID
	 * @return
	 */
	public HashMap<String,Object> syncImData(String im_server_url, String im_id, String im_token, String im_seed_key, String portal_id, String slims_id) {
		HashMap<String,Object> ret_map = new HashMap<String,Object>();
		
		im_server_url += "/imapi/result/rusersync.jsp";
		String timeStamp = new Date().getTime() + "";
		String scheck = "LEADER"; // 지도자는 LEADER
		
		PrintWriter out= null;
		BufferedReader rd = null;
		
		try {
			SeedCipher seed = new SeedCipher();
			portal_id = Base64.encode(seed.encrypt(portal_id, im_seed_key.getBytes(), "EUC-KR"));
			
			System.out.println("portal_id : "+portal_id);
			
			/* POST Parameters setting start */			
			Map<String, Object> vars = new LinkedHashMap<String, Object>();
			vars.put("ID"			, im_id);			//필수 - 토큰ID
			vars.put("TOKEN"		, im_token);		//필수 - 토큰 PWD
			vars.put("TIMESTAMP"	, timeStamp);		//필수
			vars.put("RTUID"		, portal_id);		//필수 - 사용자ID
			vars.put("SCHECK"		, scheck);			
			vars.put("SERVICEUSERID", slims_id);		//지도자 ID
			vars.put("TEMP"			, "\n");			//임시데이터 (IM 연동쪽에서 마지막 파라미터 값을 받을때 /n 값이 추가되어 임시데이터를 추가함)
			
			StringBuilder postData = new StringBuilder();
			for(Map.Entry<String,Object> param : vars.entrySet()) {
				if(postData.length() != 0) postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			
			URL url = new URL(im_server_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST"); // POST 호출
			conn.setConnectTimeout(3000);
			
			out = new PrintWriter(conn.getOutputStream());
			String sending_data = postData.toString(); //전송할 데이터(파라미터명=값)
			out.println(sending_data);
			out.flush();
			out.close(); //비운다.
			
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));   

			String line;
			StringBuffer sb = new StringBuffer();      
			while ((line = rd.readLine()) != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator")); 
			}
			rd.close();
					
			ObjectMapper om = new ObjectMapper();
			System.out.println(sb.toString());
			JsonNode node = om.readValue(sb.toString().replace("`",""), JsonNode.class);
			
			if (node.findPath("responseCode").asText().equals("SUCCESS")) {
				ret_map.put("responseCode"	, "success");
				ret_map.put("serviceuserid"	, node.findPath("message").findPath("SERVICEUSERID"));
				ret_map.put("rtuid"	, node.findPath("message").findPath("RTUID"));
				ret_map.put("serviceid"	, node.findPath("message").findPath("SERVICEID"));
				ret_map.put("responseCode"	, "SUCCESS");
			} else {
				ret_map.put("responseCode"	, "fail");
				ret_map.put("error_message"	, node.findPath("message").findPath("RTNAME"));
			}
		}catch(UnsupportedEncodingException e1) {
			ret_map.put("error", "UnsupportedEncodingException");
			ret_map.put("error_msg", e1.getMessage());
		}catch(MalformedURLException e2) {
			ret_map.put("error", "MalformedURLException");
			ret_map.put("error_msg", e2.getMessage());
		}catch(ProtocolException e3) {
			ret_map.put("error", "ProtocolException");
			ret_map.put("error_msg", e3.getMessage());
		}catch(JsonMappingException e4) {
			ret_map.put("error", "JsonMappingException");
			ret_map.put("error_msg", e4.getMessage());
		}catch(IOException e5) {
			ret_map.put("error", "IOException");
			ret_map.put("error_msg", e5.getMessage());
		}finally {
			if(out != null) {
				out.close();
			}
			
			if(rd != null) {
				try {
					rd.close();
				}catch(IOException e) {
					
				}
			}
		}
		
		return ret_map;
	}
	
	public static void main(String[]args) throws Exception{
		/* 사용자 정보 가져오기
		String im_server_url = "http://www.sportal.or.kr";
		String im_id = "sports";
		String im_token = "UaPr96C4lNP7bDTuNeGfKA==";
		String im_seed_key = "KocosaPortal!#$^";
		String target_id = "aaaa0000";
		
		HashMap<String,Object> ret_map = new SSOUtil().getSSOInfo(im_server_url, im_id, im_token, im_seed_key, target_id);
		System.out.println(ret_map);
		*/
		
		/* SSO 로그인
		String sspi_key = "368B184727E89AB69FAF";
		HashMap<String,Object> ret_map = new SSOUtil().checkSSO(sspi_key, null);
		System.out.println(ret_map);
		*/
		
		/* 포탈ID <-> 지도자 시스템 ID간 동기화 수행*/
		String im_server_url = "http://www.sportal.or.kr";
		String im_id = "sports";
		String im_token = "UaPr96C4lNP7bDTuNeGfKA==";
		String im_seed_key = "KocosaPortal!#$^";
		String portal_id = "ofvalor";
		String slims_id = "ofvalor";
		
		HashMap<String,Object> ret_map = new SSOUtil().syncImData(im_server_url, im_id, im_token, im_seed_key, portal_id, slims_id);
		System.out.println(ret_map);
	}
}
