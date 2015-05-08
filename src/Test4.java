import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Test4 {
	public static void main(String[] args) throws Exception{
		
		HashMap<String,char[]> b = new HashMap<String,char[]>();
		char[] a = {'-','-','-','-','-','-'};
		
		b.put("BRI-00", a.clone());
		a[3] = '1';
		b.put("BRI-01", a);
		
		ObjectMapper om = new ObjectMapper();
		System.out.println(om.writeValueAsString(b));
	}
}
