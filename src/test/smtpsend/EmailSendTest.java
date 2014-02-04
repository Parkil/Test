package test.smtpsend;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;


public class EmailSendTest {
	private static EtmMonitor monitor;

	private static void startup() { //���� ����
		BasicEtmConfigurator.configure();
		EmailSendTest.monitor = EtmManager.getEtmMonitor();
		EmailSendTest.monitor.start();
	}

	private static void shutdown() { //��������
		EmailSendTest.monitor.stop();
	}


	public static void main(String[] args) throws Exception{
		EmailSendTest.startup();
		SendSMTPMail a = new SendSMTPMail();

		String recipients[] = {"911513@kotra.or.kr"};
		String subject		= "�׽�Ʈ��";
		String message		= "�׽�Ʈ��";
		String fromName		= "�׽�Ʈ��";
		String fromEmail	= "100000@kotra.or.kr";
		String toCc[]		= {"alkain77@gmail.com"}; //gmail�� CC�� ����� �ӵ��� ����� ���� gmail cc����(10207) ����(218)
		String toBcc[] 		= {"testtesttest@gmail.com","sijang@kotra.or.kr"};
		String aEMAIL_FILE_LIST[] = {""};
		String aEMAIL_FILENAME_LIST[] = {""};
		//String aEMAIL_FILE_LIST[] = {"d:/��Ÿ �˾ƾ� �� ����","d:/��Ÿ"};
		//String aEMAIL_FILENAME_LIST[] = {"��Ÿ �˾ƾ� �� ����","��Ÿ"};



		a.sendSSLMessage(recipients, subject, message, fromName, fromEmail, toCc, toBcc, aEMAIL_FILE_LIST, aEMAIL_FILENAME_LIST);

		EmailSendTest.monitor.render(new SimpleTextRenderer()); //Text�������� ��������� ǥ��
		EmailSendTest.shutdown();
	}
}
