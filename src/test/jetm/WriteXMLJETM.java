package test.jetm;

import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;

/*
 * JETM(실행시간 측정 API) 측정 클래스
 * 
 * =필요 jar=
 * jetm-1.2.3.jar
 * jetm-optional-1.2.3.jar
 */

public class WriteXMLJETM {
	private static EtmMonitor monitor;

	private static void startup() { //측정 시작
		BasicEtmConfigurator.configure();
		monitor = EtmManager.getEtmMonitor();
		monitor.start();
	}

	private static void shutdown() { //측정종료
		monitor.stop();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		startup();

		WriteXMLTimeTest t6 = new WriteXMLTimeTest();

		t6.writeXMLOLDIO();
		t6.writeXMLOLDIO();
		t6.writeXMLOLDIO();
		t6.writeXMLOLDIO();
		t6.writeXMLOLDIO();

		t6.writeXMLNIO();
		t6.writeXMLNIO();
		t6.writeXMLNIO();
		t6.writeXMLNIO();
		t6.writeXMLNIO();

		monitor.render(new SimpleTextRenderer()); //Text형식으로 측정결과를 표시

		shutdown();
	}
}
