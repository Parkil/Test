package test.jetm;

import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;

/*
 * JETM(����ð� ���� API) ���� Ŭ����
 * 
 * =�ʿ� jar=
 * jetm-1.2.3.jar
 * jetm-optional-1.2.3.jar
 */

public class WriteXMLJETM {
	private static EtmMonitor monitor;

	private static void startup() { //���� ����
		BasicEtmConfigurator.configure();
		monitor = EtmManager.getEtmMonitor();
		monitor.start();
	}

	private static void shutdown() { //��������
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

		monitor.render(new SimpleTextRenderer()); //Text�������� ��������� ǥ��

		shutdown();
	}
}
