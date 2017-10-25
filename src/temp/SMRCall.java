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
	
	/**주어진 파라메터  Map을 기반으로 전체 URL을 생성해서 반환
	 * @param base_url 기본 URL(?전)
	 * @param queryString_map queryString을 구성하는 파라메터정보를 저장하는 맵
	 * @return queryString을 포함한 전체 URL
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
	
	/**SMR url을 호출하고 clipid를 리스트형식으로 반환 SMR호출시 여러개의 layer를 반환할수 있기때문에 여러개의 list를 Map에 담아서 반환
	 * 형식
	 * L1 = S01_22000246876,S01_22000246877....
	 * L2 = S01_22000247550,S01_22000247526....
	 * 
	 * @param base_url SMR호출 기본 URL
	 * @param queryString_map 파라메터를 저장하는 map
	 * @returnd 정상작동시 clip_id 저장  map,오류발생시  null반환
	 */
	public Map<String,List<String>> getSMRData(String base_url, Map<String,String> queryString_map) {
		EtmPoint point = SMRCall.etmMonitor.createPoint("SMRCall : getSMRData()");
		
		Map<String,List<String>> ret_map = new HashMap<String,List<String>>();
		
		String json_str = JavaPost.getPost(getUrl(base_url, queryString_map));
		
		try {
			JSONObject obj = new JSONObject(json_str);

			int response_code = obj.getInt("responsecode"); //응답코드 0(정상) 1(에러)
			JSONArray arr = (JSONArray)obj.get("recolist");
			
			if(response_code == 1) { //SMR 호출도중 에러발생시  null반환
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
		queryString_map.put("page", "CP_HOME"); //CP_HOME(전체),
		queryString_map.put("cpid", "C0"); //C0는 전체 C1,C2는 각 방송사별
		//queryString_map.put("programid", "S01_V0000330171");
		//queryString_map.put("clipid", "S01_22000148597");
		queryString_map.put("recotype", "SR_TOP");
		queryString_map.put("recocnt", "50");
		
		Map<String,List<String>> map = new SMRCall().getSMRData(base_url, queryString_map);
		System.out.println(map);
	}
}