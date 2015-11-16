package test.apple_push;
import java.util.Date;
import java.util.Map;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;

/*
 * JavaPNS를 이용한 IOS Push 전송
 * 
 * p12 인증서 작성법
 * 
 * 필요항목
 * 1.apple에서 받은 인증서 파일 aps_development.cer
 * 2.1번 인증서에서 추출한 private key private.p12
 * 
 * [작성방법]
 * 1)인증서 pem 작성
 * openssl x509 -in aps_development.cer -inform der -out PushChatCert.pem
 * 
 * 2)key pem 작성(private key 암호 필요)
 * openssl pkcs12 -nocerts -out PushChatKey.pem -in private.p12
 * 
 * 3) 1),2)번에서 작성한 key,인증서 pem을 기반으로 p12인증서 작성
 * openssl pkcs12 -export -in PushChatCert.pem -inkey PushChatKey.pem -out bundle.p12
 */
public class SendByJavaPNS {
	public static void main(String[] args) {
		ApnsService service = APNS.newService().withCert("d:/bundle.p12", "aaa000").withSandboxDestination().build();
		
		PayloadBuilder payload = APNS.newPayload().alertBody("김개똥님의 증명서가 승인되었습니다.").sound("default");
		String json = payload.build();
		
		
		String token = "807f0c79b04d88916577936bb97ecc691c026ca6b495a053f939affd768fdf51";
		service.push(token, json);
		
		Map<String, Date> inactiveDevices = service.getInactiveDevices();
		for (String deviceToken : inactiveDevices.keySet()) {
			Date inactiveAsOf = inactiveDevices.get(deviceToken);
		}
	}
}
