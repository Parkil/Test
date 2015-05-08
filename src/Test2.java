import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test2 {

	private static String[] patternMatcher(String val, String regex) throws Exception{ 
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(val);
		
		char[] time_arr = {'-','-','-','-','-','-'};
		
		HashMap<String,char[]> time_map = new HashMap<String,char[]>();
		
		while(m.find()) {
			
			/*
			 *	m.group(1) : 사이트명
			 *	m.group(3) : 시간
			 *	m.group(4) : 분
			 *
			 *	ex) RDR_JNI_201505050800.uf
			 *	m.group(1) : JNI
			 *	m.group(3) : 08
			 *	m.group(4) : 0 (만약 14분이면 1, 25분이면 2로 처리된다);
			 */
			char[] temp_time_arr = time_map.get(m.group(1)+"/"+m.group(3));
			int min_per10 = Integer.parseInt(m.group(4));
			
			if(temp_time_arr == null) {
				temp_time_arr = time_arr.clone();
				time_map.put(m.group(1)+"/"+m.group(3), temp_time_arr);
			}
			
			temp_time_arr[min_per10] = '1';
		}
		
		ObjectMapper om = new ObjectMapper();
		String zzz = om.writeValueAsString(time_map);
		System.out.println(zzz);
		return null;
	}
	
	private static String[] getFileListStr(String path,String file_regex) throws Exception{
		
		File root = new File(path);
		
		String[] file_list =  root.list();
		Arrays.sort(file_list);
		StringBuffer sb = new StringBuffer();
		for(String file_name : file_list) {
			sb.append(file_name);
		}
		
		return patternMatcher(sb.toString(), file_regex);
	}
	
	public static void main(String[] args) throws Exception{
		String root_path	= "D:/DATA";
		String sub_path		= String.format("/INPUT/UFV/%1$s/%2$s","201505","05"); //1번
		String sub_path2	= String.format("/INPUT/UFF2/%1$s/%2$s","201505","05"); //2번 fqc
		String sub_path2_1	= String.format("/INPUT/UFQ/%1$s/%2$s","201505","05"); //2번 orpg qc
		String sub_path3	= String.format("/OUTPUT/BIN/ZNQC/%1$s/%2$s","201505","05"); //3_1번
		String sub_path3_2	= String.format("/OUTPUT/BIN/ZFQC/%1$s/%2$s","201505","05"); //3_2번
		String sub_path3_3	= String.format("/OUTPUT/BIN/ZOQC/%1$s/%2$s","201505","05"); //3_3번
		
		String file_regex 		= "RDR_([A-Z]{1,4})_([0-9]{8})([0-9]{2})([0-9]{1})([0-9]{1}).uf"; //1번
		String file_regex2		= "RDR_([A-Z]{1,4})_FQC_([0-9]{8})([0-9]{2})([0-9]{1})([0-9]{1}).uf"; //2번 fqc
		String file_regex2_1	= "RDR_([A-Z]{1,4})_QCD_([0-9]{8})([0-9]{2})([0-9]{1})([0-9]{1}).uf"; //2번 orpg qc
		String file_regex3_1	= "RDR_(CNQCZ)_CP15M_([0-9]{8})([0-9]{2})([0-9]{1})([0-9]{1}).bin.gz"; //3번 noqc
		String file_regex3_2	= "RDR_(CFQCZ)_CP15M_([0-9]{8})([0-9]{2})([0-9]{1})([0-9]{1}).bin.gz"; //3번 fqc
		String file_regex3_3	= "RDR_(COQCZ)_CP15M_([0-9]{8})([0-9]{2})([0-9]{1})([0-9]{1}).bin.gz"; //3번 orpg qc
		
		//getFileListStr(root_path+sub_path, file_regex);
		//getFileListStr(root_path+sub_path2, file_regex2);
		//getFileListStr(root_path+sub_path2_1, file_regex2_1);
		//getFileListStr(root_path+sub_path3, file_regex3_1);
		//getFileListStr(root_path+sub_path3_2, file_regex3_2);
		getFileListStr(root_path+sub_path3_3, file_regex3_3);
	}
}
