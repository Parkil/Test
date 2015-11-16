package test.apple_push;

import java.io.InputStream;

import javapns.Push;
import javapns.notification.PushNotificationPayload;

/*
 * notnoop APNS를 이용한 IOS Push 전송
 * 
 * 해당 api를 사용하기 위해서는 bouncy castle 암호화 provider가 필요함
 */
public class SendByNotnoop {
	public static void main(String[] args) throws Exception {
		
		//apple에 push를 보내기 위한 인증서
		InputStream keyStore = new SendByNotnoop().getClass().getResourceAsStream("bundle.p12");
		
		//단순 push message 전송 (5번째 파라메터가 token)
		//Push.alert("Hello World!", keyStore, "aaa000", false, "807f0c79b04d88916577936bb97ecc691c026ca6b495a053f939affd768fdf51");
		
		//payload 클래스를 이용하여 json으로 파라메터 구성후 전송
		PushNotificationPayload payload = PushNotificationPayload.complex();
		payload.addAlert("김기광님의 출입신청이 승인되었습니다.");
		payload.addSound("default");

		System.out.println(payload.getPayload().toString());
		Push.payload(payload, keyStore, "aaa000", false, "807f0c79b04d88916577936bb97ecc691c026ca6b495a053f939affd768fdf51");
	}
}