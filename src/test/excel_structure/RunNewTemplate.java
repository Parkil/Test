package test.excel_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RunNewTemplate {
	public static List<HashMap<String,String>> getRowData() {
		List<HashMap<String, String>> ret_list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> temp = null;
		for(int i = 0 ; i < 30 ; i++) {
			temp = new HashMap<String,String>();
			temp.put("club", "club"+(i+1));
			temp.put("A1", String.valueOf(i));
			temp.put("A2", String.valueOf(i));
			temp.put("A3", String.valueOf(i));
			temp.put("A4", String.valueOf(i));
			temp.put("A5", String.valueOf(i));
			
			temp.put("B1", String.valueOf(i));
			temp.put("B2", String.valueOf(i));
			temp.put("B3", String.valueOf(i));
			temp.put("B4", String.valueOf(i));
			temp.put("B5", String.valueOf(i));
			
			temp.put("C1", String.valueOf(i));
			temp.put("C2", String.valueOf(i));
			temp.put("C3", String.valueOf(i));
			temp.put("C4", String.valueOf(i));
			temp.put("C5", String.valueOf(i));
			
			temp.put("D1", String.valueOf(i));
			temp.put("D2", String.valueOf(i));
			temp.put("D3", String.valueOf(i));
			temp.put("D4", String.valueOf(i));
			temp.put("D5", String.valueOf(i));
			
			temp.put("E1", String.valueOf(i));
			temp.put("E2", String.valueOf(i));
			temp.put("E3", String.valueOf(i));
			temp.put("E4", String.valueOf(i));
			temp.put("E5", String.valueOf(i));
			
			temp.put("F1", String.valueOf(i));
			temp.put("F2", String.valueOf(i));
			temp.put("F3", String.valueOf(i));
			temp.put("F4", String.valueOf(i));
			temp.put("F5", String.valueOf(i));
			ret_list.add(temp);
		}
		
		return ret_list;
	}

	public static void main(String[] args) throws Exception{
		HashMap<String,String> header_map = new HashMap<String,String>();
		header_map.put("top_title", "월별 활동회원 실적111");
		header_map.put("col_title_str", "구분,실적,총계,1월,2월,3월,4월,5월,6월,7월,8월,9월,10월,11월,12월");
		header_map.put("col_sub_title_str", "운영인력,지도횟수,회원수,가족수,참가인원"); //행의 서브 타이틀
		//header_map.put("col_sub_title_str", "클럽별 소계,클럽매니저,지도자(정규),지도자(비정규),행정직(정규),행정직(비정규)"); //행의 서브 타이틀
		header_map.put("data_total_cnt", "30"); //실제 데이터 개수
		header_map.put("club_title_key", "club");//클럽명 데이터key
		header_map.put("col_sub_title_key", "A,B,C,D,E"); //A1~12,B1~12,C1~12 이런식으로 key값을 배열
		
		MakeExcel me = new MakeExcel();
		me.setTemplate(new NewTemplate());
		me.setHeaderData(header_map);
		me.setRowData(getRowData());
		
		me.createExcel();
	}
}
