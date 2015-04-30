import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test2 {
	
	private static void patternMatcher(String val) throws Exception{ 
		String regex = "RDR_([A-Z]{1,4})_([0-9]{8})([0-9]{3})([0-9]{1}).uf";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(val);
		
		ArrayList<String> list = new ArrayList<String>();
 		
		while(m.find()) {
			//System.out.printf("%s --- %s -- %s --%s\n", m.group(1), m.group(2), m.group(3), m.group(4));
			list.add(m.group(1)+"/"+m.group(3)+"0");
		}
		
		ObjectMapper om = new ObjectMapper();
		System.out.println(om.writeValueAsString(list));
	}
	
	/*
	 * 모니터링 설계
	 * [properties 설계]
	 * 1.main path : /DATA/~
	 * 2.sub path  : /INPUT/UFV/~
	 * 3.각 데이터별 정규식 
	 * 
	 * 1,2,3번을 관리하는 properties가 따로 있어야 함
	 * 각 대메뉴별 사이트 정보도 properties로 구성해야 함
	 */
	public static void main(String[] args) throws Exception{
		String root_path = "D:/DATA";
		String sub_path	 = String.format("/INPUT/UFV/%1$s/%2$s","201406","10");
		
		File root = new File(root_path +  sub_path);
		
		String[] sss =  root.list();
		StringBuffer sb = new StringBuffer();
		for(String val : sss) {
			sb.append(val);
		}
		
		String a = "201406100000";
		String aa = "201406100010";
		String b = "201406102350";
		
		

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		//DAsdf.parse(a);
		Date end   = sdf.parse(aa);
		
		
		
		
		/*
		for(; aa<=bb ; aa+=10) {
			System.out.println(aa);
		}
		
		System.out.println(aa);
		System.out.println(bb);
		*/
		patternMatcher(sb.toString());
	}
}
