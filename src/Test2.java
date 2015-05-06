import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test2 {
	
	/*
	 * UFV,UFF2,UFQ
	 * ZNQC,ZFQC,ZOQC
	 */
	private static void patternMatcher(String val) throws Exception{ 
		String kma_site_str		= "GNG,GSN,KWK,GDK,PSN,MYN,BRI,SSP,KSN,IIA,JNI";
		String mltm_site_str	= "BSL,SBS,IMJ";
		String rokff_site_str	= "KAN,KWJ,TAG,SCN,SAN,SWN,YCN,WNJ,JWN";
		
		String regex = "RDR_([A-Z]{1,4})_([0-9]{8})([0-9]{3})([0-9]{1}).uf";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(val);
		
		//사이트 자료수신
		ArrayList<String> site_kma_list = new ArrayList<String>();
		ArrayList<String> site_mltm_list = new ArrayList<String>();
		ArrayList<String> site_rokff_list = new ArrayList<String>();
 		
		//품질관리 통계
		
		
		//
		while(m.find()) {
			String site = m.group(1);
			//System.out.printf("%s --- %s -- %s --%s\n", m.group(1), m.group(2), m.group(3), m.group(4));
			
			if(kma_site_str.indexOf(site) != -1) {
				site_kma_list.add(site+"/"+m.group(3)+"0");
			}else if(mltm_site_str.indexOf(site) != -1) {
				site_mltm_list.add(site+"/"+m.group(3)+"0");
			}else if(rokff_site_str.indexOf(site) != -1) {
				site_rokff_list.add(site+"/"+m.group(3)+"0");
			}
		}
		
		ObjectMapper om = new ObjectMapper();
		System.out.println(om.writeValueAsString(site_kma_list));
		System.out.println(om.writeValueAsString(site_mltm_list));
		System.out.println(om.writeValueAsString(site_rokff_list));
	}
	
	private static void getFileListStr(String path,String file_regex) throws Exception{
		File root = new File(path);
		
		String[] sss =  root.list();
		StringBuffer sb = new StringBuffer();
		for(String val : sss) {
			sb.append(val);
		}
		
		patternMatcher(sb.toString());
	}
	
	public static void main(String[] args) throws Exception{
		String root_path	= "D:/DATA";
		String sub_path		= String.format("/INPUT/UFV/%1$s/%2$s","201406","10"); //1번
		String sub_path2	= String.format("/INPUT/UFF2/%1$s/%2$s","201406","10"); //2번 fqc
		String sub_path2_1	= String.format("/INPUT/UFQ/%1$s/%2$s","201406","10"); //2번 orpg qc
		String sub_path3	= String.format("/OUTPUT/BIN/ZNQC/%1$s/%2$s","201406","10"); //3_1번
		String sub_path3_2	= String.format("/OUTPUT/BIN/ZFQC/%1$s/%2$s","201406","10"); //3_2번
		String sub_path3_3	= String.format("/OUTPUT/BIN/ZOQC/%1$s/%2$s","201406","10"); //3_3번
		
		String file_regex 		= "RDR_([A-Z]{1,4})_([0-9]{8})([0-9]{3})([0-9]{1}).uf"; //1번
		String file_regex2		= "RDR_([A-Z]{1,4})_FQC_([0-9]{8})([0-9]{3})([0-9]{1}).uf"; //2번 fqc
		String file_regex2_1	= "RDR_([A-Z]{1,4})_QCD_([0-9]{8})([0-9]{3})([0-9]{1}).uf"; //2번 orpg qc
		String file_regex3_1	= "RDR_CNQCZ_CP15M_([0-9]{8})([0-9]{3})([0-9]{1}).uf"; //3번 noqc
		String file_regex3_2	= "RDR_CFQCZ_CP15M_([0-9]{8})([0-9]{3})([0-9]{1}).uf"; //3번 fqc
		String file_regex3_3	= "RDR_COQCZ_CP15M_([0-9]{8})([0-9]{3})([0-9]{1}).uf"; //3번 orpg qc
		
		//getFileListStr(root_path+sub_path, file_regex);
		getFileListStr(root_path+sub_path2, file_regex2);
	}
}
