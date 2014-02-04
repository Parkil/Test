package test.smtpsend;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;


public class EmailSendTest {
	private static EtmMonitor monitor;

	private static void startup() { //측정 시작
		BasicEtmConfigurator.configure();
		EmailSendTest.monitor = EtmManager.getEtmMonitor();
		EmailSendTest.monitor.start();
	}

	private static void shutdown() { //측정종료
		EmailSendTest.monitor.stop();
	}


	public static void main(String[] args) throws Exception{
		EmailSendTest.startup();
		SendSMTPMail a = new SendSMTPMail();

		String recipients[] = {"911513@kotra.or.kr"};
		String subject		= "테스트용";
		String message		= "테스트용";
		String fromName		= "테스트용";
		String fromEmail	= "100000@kotra.or.kr";
		String toCc[]		= {"alkain77@gmail.com"}; //gmail이 CC로 들어갈경우 속도가 상당히 향상됨 gmail cc이전(10207) 이후(218)
		String toBcc[] 		= {"testtesttest@gmail.com","sijang@kotra.or.kr"};
		String aEMAIL_FILE_LIST[] = {""};
		String aEMAIL_FILENAME_LIST[] = {""};
		//String aEMAIL_FILE_LIST[] = {"d:/기타 알아야 할 사항","d:/기타"};
		//String aEMAIL_FILENAME_LIST[] = {"기타 알아야 할 사항","기타"};



		a.sendSSLMessage(recipients, subject, message, fromName, fromEmail, toCc, toBcc, aEMAIL_FILE_LIST, aEMAIL_FILENAME_LIST);

		EmailSendTest.monitor.render(new SimpleTextRenderer()); //Text형식으로 측정결과를 표시
		EmailSendTest.shutdown();
	}
}
