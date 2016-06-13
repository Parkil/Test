package test.excel_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunNewTemplate3 {
	public static List<HashMap<String,String>> getRowData() {
		List<HashMap<String, String>> ret_list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> temp = new HashMap<String,String>();
		temp.put("club1", "0");
		temp.put("club2", "1");
		temp.put("club3", "2");
		temp.put("club4", "3");
		temp.put("club5", "4");
		temp.put("club6", "5");
		temp.put("club7", "6");
		temp.put("club8", "7");
		temp.put("club9", "8");
		temp.put("club10", "9");
		temp.put("club11", "10");
		temp.put("club12", "11");
		temp.put("club13", "12");
		temp.put("club14", "13");
		temp.put("club15", "14");
		temp.put("club16", "15");
		temp.put("club17", "16");
		temp.put("club18", "17");
		temp.put("club19", "18");
		temp.put("club20", "19");
		temp.put("club21", "20");
		temp.put("club22", "21");
		temp.put("club23", "22");
		temp.put("club24", "23");
		temp.put("club25", "24");
		temp.put("club26", "25");
		temp.put("club27", "26");
		ret_list.add(temp);
		
		return ret_list;
	}
	
	public static List<HashMap<String,String>> get2ndRowData() {
		List<HashMap<String, String>> ret_list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> temp = null;
		for(int i = 0 ; i < 12 ; i++) {
			temp = new HashMap<String,String>();

			temp.put("facil_name","시설"+String.valueOf(i));
			temp.put("facil_owner",String.valueOf(i));
			temp.put("facil_contractor",String.valueOf(i));
			temp.put("facil_contract_type",String.valueOf(i));
			temp.put("facil_contract_period",String.valueOf(i));
			temp.put("fee_type",String.valueOf(i));
			temp.put("fee",String.valueOf(i));
			temp.put("standard",String.valueOf(i));

			ret_list.add(temp);
		}
		
		return ret_list;
	}
	
	public static List<HashMap<String,String>> get3rdRowData() {
		List<HashMap<String, String>> ret_list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> temp = null;
		for(int i = 0 ; i < 3 ; i++) {
			temp = new HashMap<String,String>();

			temp.put("insur_corp_name","보험회사"+String.valueOf(i));
			temp.put("insur_name","보험"+String.valueOf(i));
			temp.put("insur_period",String.valueOf(i));

			ret_list.add(temp);
		}
		
		return ret_list;
	}
	
	public static List<HashMap<String,String>> get4thRowData() {
		List<HashMap<String, String>> ret_list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> temp = null;
		for(int i = 0 ; i < 20 ; i++) {
			temp = new HashMap<String,String>();

			temp.put("tot_cnt","20");
			temp.put("name","종목"+String.valueOf(i));

			ret_list.add(temp);
		}
		
		return ret_list;
	}
	


	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception{
		
		HashMap<String,String> header_map = new HashMap<String,String>();
		header_map.put("top_title", "00 스포츠클럽 현황");
		header_map.put("col_title_key2", "facil_name,facil_owner,facil_contractor,facil_contract_type,facil_contract_period,fee_type,fee,standard");
		header_map.put("col_title_key3", "insur_corp_name,insur_name,insur_period");
		
		
		MakeExcel me = new MakeExcel();
		me.setTemplate(new NewTemplate3());
		me.setHeaderData(header_map);
		me.setRowDataMulti(getRowData(),get2ndRowData(),get3rdRowData(),get4thRowData());
		
		me.createExcelByMultiData();
	}
}
