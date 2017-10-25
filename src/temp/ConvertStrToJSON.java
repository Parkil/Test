package temp;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * json문자열을 JSONObject로 변경
 */
public class ConvertStrToJSON {

	public static void main(String[] args) throws Exception{
		String json_str = "{\"version\":\"1.0\",\"ttl\":\"60\",\"recodate\":1507742436,\"recolist\":[{\"layout\":\"L1\",\"recoclipid\":[\"S01_22000246876\",\"S01_22000246877\",\"S01_22000246878\",\"S01_22000246882\",\"S01_22000246956\",\"S01_22000247335\",\"S01_22000247348\",\"S01_22000247541\"]}],\"responsecode\":0}";
		JSONObject obj = new JSONObject(json_str);
		
		System.out.println("total : "+obj);
		System.out.println("recolist : "+obj.get("recolist"));
		
		JSONArray arr = (JSONArray)obj.get("recolist");
		JSONObject obj2 = (JSONObject)arr.get(0);
		
		JSONArray arr2 = (JSONArray)obj2.get("recoclipid");
		System.out.println("recoclipid length :"+arr2.length());
		System.out.println("recoclipid index 1 : "+arr2.get(1));
	}
}
