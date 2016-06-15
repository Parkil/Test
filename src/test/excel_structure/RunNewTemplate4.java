package test.excel_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RunNewTemplate4 {
	public static List<HashMap<String,String>> getRowData() {
		List<HashMap<String, String>> ret_list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> temp = new HashMap<String,String>();
		for(int i = 1 ; i <= 30 ; i++) {
			for(int j = 1; j<=12 ; j++) {
				temp.put("R"+i+"-"+j, String.valueOf(i+j));
			}
		}
		
		ret_list.add(temp);
		return ret_list;
	}
	


	public static void main(String[] args) throws Exception{
		HashMap<String,String> header_map = new HashMap<String,String>();
		header_map.put("top_title", "손익계산서");
		header_map.put("col_title", "계정과목,계정과목,설명,누적합계,1,2,3,4,5,6,7,8,9,10,11,12");
		header_map.put("col_title_key", "game_name,facil_name,day_string,num_lectur,num_attend,num_mf,num_paid,num_free,num_m,num_f,num_19below,num_20ager,num_30ager,num_40ager,num_50ager,num_60up");
		
		MakeExcel me = new MakeExcel();
		me.setTemplate(new NewTemplate4());
		me.setHeaderData(header_map);
		me.setRowData(getRowData());
		
		me.createExcel();
	}
}
