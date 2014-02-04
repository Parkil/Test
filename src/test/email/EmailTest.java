package test.email;

import com.jscape.inspect.EmailInspector;

/*
 * JScape api를 이용한 이메일 검증 예제
 * jscape api는 사용제한(30일)이 걸려있기 때문에 이 예제를 사용하려면 jscape예제를 따로 다운로드 받아야 함
 */
public class EmailTest{

	public static void main(String[] args) throws Exception {
		String email = null;
		String dns = null;

		// This example reads the email address from the console.
		email = "ayhhoseefed@yahoo.co.kr";

		// Your DNS hostname is necessary to verify that a domain name is real.
		dns = "ns2.yahoo.com";

		// Create an EmailInspector image. Debugging throws a number of messages to the console.
		EmailInspector inspector = new EmailInspector();
		inspector.setDebug(true);

		// Set the DNS level and validation level, then validate the address you entered.
		inspector.setNameserver(dns);
		inspector.setEmailInspectionLevel(EmailInspector.USER_VALIDATION);
		inspector.validate(email);
	}
}