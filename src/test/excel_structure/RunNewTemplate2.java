package test.excel_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RunNewTemplate2 {
	public static List<HashMap<String,String>> getRowData() {
		List<HashMap<String, String>> ret_list = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> temp = null;
		for(int i = 0 ; i < 30 ; i++) {
			temp = new HashMap<String,String>();
			
			if(i >= 0 && i<10) {
				temp.put("game_name","축구");
			}else if(i >= 10 && i<20) {
				temp.put("game_name","배구");
			}else if(i >= 20 && i<30) {
				temp.put("game_name","농구");
			}else {
				temp.put("game_name","야구");
			}
			
			temp.put("facil_name","시설"+String.valueOf(i));
			temp.put("day_string",String.valueOf(i));
			temp.put("num_lectur",String.valueOf(i));
			temp.put("num_attend",String.valueOf(i));
			temp.put("num_mf",String.valueOf(i));
			temp.put("num_paid",String.valueOf(i));
			temp.put("num_free",String.valueOf(i));
			temp.put("num_m",String.valueOf(i));
			temp.put("num_f",String.valueOf(i));
			temp.put("num_19below",String.valueOf(i));
			temp.put("num_20ager",String.valueOf(i));
			temp.put("num_30ager",String.valueOf(i));
			temp.put("num_40ager",String.valueOf(i));
			temp.put("num_50ager",String.valueOf(i));
			temp.put("num_60up",String.valueOf(i));
			temp.put("elite",String.valueOf(i));
			temp.put("family",String.valueOf(i));
			temp.put("sum",String.valueOf(i));
			temp.put("num_ft_lecturer",String.valueOf(i));
			temp.put("num_pt_lecturer",String.valueOf(i));
			temp.put("num_vt_lecturer",String.valueOf(i));
			temp.put("num_ft_office",String.valueOf(i));
			temp.put("num_pt_office",String.valueOf(i));
			temp.put("num_vt_office",String.valueOf(i));
			temp.put("num_manager",String.valueOf(i));
			ret_list.add(temp);
		}
		
		return ret_list;
	}

	public static void main(String[] args) throws Exception{
		HashMap<String,String> header_map = new HashMap<String,String>();
		header_map.put("top_title", "00 스포츠클럽 운영 실적");
		header_map.put("col_title_key", "game_name,facil_name,day_string,num_lectur,num_attend,num_mf,num_paid,num_free,num_m,num_f,num_19below,num_20ager,num_30ager,num_40ager,num_50ager,num_60up,elite,family,sum,num_ft_lecturer,num_pt_lecturer,num_vt_lecturer,num_ft_office,num_pt_office,num_vt_office,num_manager");
		
		MakeExcel me = new MakeExcel();
		me.setTemplate(new NewTemplate2());
		me.setHeaderData(header_map);
		me.setRowData(getRowData());
		
		me.createExcel();
	}
}
