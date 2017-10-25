package temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import test.url.JavaPost;

public class SMRCall {
	private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();
	
	/**�־��� �Ķ����  Map�� ������� ��ü URL�� �����ؼ� ��ȯ
	 * @param base_url �⺻ URL(?��)
	 * @param queryString_map queryString�� �����ϴ� �Ķ���������� �����ϴ� ��
	 * @return queryString�� ������ ��ü URL
	 */
	private String getUrl(String base_url, Map<String,String> queryString_map) {
		Set<String> keySet = queryString_map.keySet();
		Iterator<String> keyIter = keySet.iterator();
		
		int index = 0;
		QueryString qs = null;
		while(keyIter.hasNext()) {
			String key = keyIter.next();
			String value = queryString_map.get(key);
			
			if(index == 0) {
				qs = new QueryString(key, value);
			}else {
				qs.add(key, value);
			}
			
			index++;
		}
		
		return base_url+"?"+qs;
	}
	
	/**SMR url�� ȣ���ϰ� clipid�� ����Ʈ�������� ��ȯ SMRȣ��� �������� layer�� ��ȯ�Ҽ� �ֱ⶧���� �������� list�� Map�� ��Ƽ� ��ȯ
	 * ����
	 * L1 = S01_22000246876,S01_22000246877....
	 * L2 = S01_22000247550,S01_22000247526....
	 * 
	 * @param base_url SMRȣ�� �⺻ URL
	 * @param queryString_map �Ķ���͸� �����ϴ� map
	 * @returnd �����۵��� clip_id ����  map,�����߻���  null��ȯ
	 */
	public Map<String,List<String>> getSMRData(String base_url, Map<String,String> queryString_map) {
		EtmPoint point = SMRCall.etmMonitor.createPoint("SMRCall : getSMRData()");
		
		Map<String,List<String>> ret_map = new HashMap<String,List<String>>();
		
		String json_str = JavaPost.getPost(getUrl(base_url, queryString_map));
		
		try {
			JSONObject obj = new JSONObject(json_str);

			int response_code = obj.getInt("responsecode"); //�����ڵ� 0(����) 1(����)
			JSONArray arr = (JSONArray)obj.get("recolist");
			
			if(response_code == 1) { //SMR ȣ�⵵�� �����߻���  null��ȯ
				return null;
			}
			
			for(int i=0,length=arr.length() ; i<length ; i++) {
				JSONObject arr_obj = (JSONObject)arr.get(i);
				String layout = arr_obj.getString("layout");
				
				JSONArray arr2 = (JSONArray)arr_obj.get("recoclipid");
				
				List<String> clip_list = new ArrayList<String>();
				for(int j=0,arr_length=arr2.length() ; j < arr_length ; j++) {
					clip_list.add(arr2.getString(j));
				}
				
				ret_map.put(layout, clip_list);
			}
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}finally {
			point.collect();
		}
		
		return ret_map;
	}
	
	public static void main(String[] args) throws Exception{
		String base_url = "http://recommend.smartmediarep.com/smr_reco/media/reco.action";
		
		Map<String,String> queryString_map = new HashMap<String,String>();
		queryString_map.put("version", "1.0");
		queryString_map.put("media", "SMR_MEMBERS");
		queryString_map.put("site", "ALL");
		queryString_map.put("page", "CP_HOME"); //CP_HOME(��ü),
		queryString_map.put("cpid", "C0"); //C0�� ��ü C1,C2�� �� ��ۻ纰
		//queryString_map.put("programid", "S01_V0000330171");
		//queryString_map.put("clipid", "S01_22000148597");
		queryString_map.put("recotype", "SR_TOP");
		queryString_map.put("recocnt", "50");
		
		Map<String,List<String>> map = new SMRCall().getSMRData(base_url, queryString_map);
		System.out.println(map);
	}
}