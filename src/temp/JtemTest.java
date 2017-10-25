package temp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;

public class JtemTest {
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

		String base_url = "http://recommend.smartmediarep.com/smr_reco/media/reco.action";
		
		Map<String,String> queryString_map = new HashMap<String,String>();
		queryString_map.put("version", "1.0");
		queryString_map.put("media", "SMR_MEMBERS");
		queryString_map.put("site", "ALL");
		queryString_map.put("page", "CP_HOME");
		queryString_map.put("cpid", "C0");
		//queryString_map.put("programid", "S01_V0000330171");
		//queryString_map.put("clipid", "S01_22000148597");
		queryString_map.put("recotype", "SR_TOP");
		queryString_map.put("recocnt", "50");
		
		Map<String,List<String>> map = new SMRCall().getSMRData(base_url, queryString_map);
		System.out.println(map);
		
		monitor.render(new SimpleTextRenderer()); //Text형식으로 측정결과를 표시

		shutdown();
	}
}
