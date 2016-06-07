package test.excel_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunDefaultTemplate {
	
	public static List<HashMap<String,String>> getRowData() {
		List<HashMap<String, String>> ret_list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> temp = null;
		for(int i = 0 ; i < 100 ; i++) {
			temp = new HashMap<String,String>();
			temp.put("col1", String.valueOf(i));
			temp.put("col2", String.valueOf(i));
			temp.put("col3", String.valueOf(i));
			temp.put("col4", String.valueOf(i));
			temp.put("col5", String.valueOf(i));
			temp.put("col6", String.valueOf(i));
			temp.put("col7", String.valueOf(i));
			ret_list.add(temp);
		}
		
		return ret_list;
	}

	public static void main(String[] args) throws Exception{
		HashMap<String,String> header_map = new HashMap<String,String>();
		header_map.put("top_title", "ㅇㅇ스포츠클럽 지도시간 현황(월별)	");
		header_map.put("col_title_str", "지도일,지도자,종목,시설,시작시간,종료시간");
		header_map.put("col_title_key", "col1,col2,col3,col4,col5,col6");
		
		MakeExcel me = new MakeExcel();
		me.setTemplate(new DefaultTemplate());
		me.setHeaderData(header_map);
		me.setRowData(getRowData());
		
		me.createExcel();
	}
}
