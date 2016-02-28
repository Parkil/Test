package test.apple_push;

import java.io.InputStream;

import javapns.Push;
import javapns.notification.PushNotificationPayload;
import javapns.notification.transmission.PushQueue;

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
 * 
 * 해당 api를 사용하기 위해서는 bouncy castle 암호화 provider가 필요함
 */
public class SendByJavaPNS {
	private static InputStream keyStore = new SendByJavaPNS().getClass().getResourceAsStream("bundle.p12");
	
	public static void main(String[] args) throws Exception {
		//단순 push message 전송 (5번째 파라메터가 token)
		//Push.alert("Hello World!", keyStore, "aaa000", false, "8bedba86c73892abdd2718f6f20ba48e9a5961b7ea087e34a872daa6ad9321f1");
		
		
		//payload 클래스를 이용하여 json으로 파라메터 구성후 전송
		PushNotificationPayload payload = PushNotificationPayload.complex();
		payload.addAlert("홍길동님의 출입신청이 승인되었습니다.");
		payload.addSound("default");

		System.out.println(payload.getPayload().toString());
		//Push.payload(payload, keyStore, "aaa000", false, "02dd5fc9b4045db351067c9bbc2d31688c13ca75fc4e7032cbd7ef09c0bd4462");
		
		PushQueue queue = Push.queue(keyStore, "aaa000", false, 2);
		
		queue.add(payload,"8bedba86c73892abdd2718f6f20ba48e9a5961b7ea087e34a872daa6ad9321f1");
		queue.start();
		
	}
}