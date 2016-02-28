package test.apple_push;
import java.util.Date;
import java.util.Map;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;

/*
 * notnoop APNS를 이용한 IOS Push 전송
 */
public class SendByNotnoop {
	public static void main(String[] args) {
		ApnsService service = APNS.newService().withCert("d:/bundle.p12", "aaa000").withSandboxDestination().build();
		
		PayloadBuilder payload = APNS.newPayload().alertBody("홍길동님의 증명서가 승인되었습니다.").sound("default");
		String json = payload.build();
		
		
		System.out.println(json);
		String token = "b05a4576848ac5ca844e898bd9659b9f22355c15b97af3b8c0461aec5bd767be";
		service.push(token, json);
		
		Map<String, Date> inactiveDevices = service.getInactiveDevices();
		for (String deviceToken : inactiveDevices.keySet()) {
			Date inactiveAsOf = inactiveDevices.get(deviceToken);
			System.out.println(inactiveAsOf);
		}
	}
}
