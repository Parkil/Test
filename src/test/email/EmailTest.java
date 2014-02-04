package test.email;

import com.jscape.inspect.EmailInspector;

/*
 * JScape api�� �̿��� �̸��� ���� ����
 * jscape api�� �������(30��)�� �ɷ��ֱ� ������ �� ������ ����Ϸ��� jscape������ ���� �ٿ�ε� �޾ƾ� ��
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