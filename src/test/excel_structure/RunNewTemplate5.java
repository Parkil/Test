package test.excel_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RunNewTemplate5 {
	public static List<HashMap<String,String>> getRowData() {
		List<HashMap<String, String>> ret_list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> temp = new HashMap<String,String>();
		for(int i = 1 ; i <= 24 ; i++) {
			for(int j = 1; j<=2 ; j++) {
				temp.put("R"+i+"_"+j, String.valueOf(i+j));
			}
		}
		
		ret_list.add(temp);
		return ret_list;
	}
	


	public static void main(String[] args) throws Exception{
		HashMap<String,String> header_map = new HashMap<String,String>();
		header_map.put("top_title", "재무상태표");
		header_map.put("col_title", "계정과목,계정과목,계정과목,내용설명,당기금액,전기말금액");
		
		MakeExcel me = new MakeExcel();
		me.setTemplate(new NewTemplate5());
		me.setHeaderData(header_map);
		me.setRowData(getRowData());
		
		me.createExcel();
	}
}
