import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import test.util.ByteBufferUtil;


public class Test3 {
	private static String getValueStr(String path, String regex) throws Exception{
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		FileChannel fc = fis.getChannel();
		
		MappedByteBuffer mbb = fc.map(MapMode.READ_ONLY, 0, fc.size());
		
		String file_str = ByteBufferUtil.bb_to_str(mbb);
		
		String[] lines = file_str.split("\n|\r");
		
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		
		Pattern p = Pattern.compile(regex);
		
		HashMap<String,String> temp = null;
		ArrayList<String> temp_list = new ArrayList<String>();
		
		String hr = null;
		
		for(String line : lines) {
			if(line.trim().intern() == "".intern()) {
				continue;
			}
			
			Matcher m = p.matcher(line.trim());
			
			if(m.find()) {
				temp_list.clear();
				temp = new HashMap<String,String>();
				list.add(temp);
				
				hr = m.group(1);
				
				
				for(int i = 2 ; i<=m.groupCount() ; i++) {
					temp_list.add(m.group(i));
				}
				
				continue;
			}
			
			String[] values = line.trim().split("\\s{1,}");
			String key = null;
			String temp_value = null;
			for(int i = 0 ; i<values.length ; i++) {
				key = hr+"=="+temp_list.get(i);
				temp_value = temp.get(key);
				temp_value = (temp_value == null) ? "" : temp_value;
				temp_value += values[i]+"<br>";
				
				temp.put(key , temp_value);
			}
		}
		
		fis.close();
		fc.close();
		
		ObjectMapper om = new ObjectMapper();
		String result = om.writeValueAsString(list);
		return result;
	}
	
	public static void main(String[] args) throws Exception{
		String path_qlt = "d:/Dev/eclipse/workspace/Test/src/YIT_STAT_QLT_201504030300.dat";
		String path_qtt = "d:/Dev/eclipse/workspace/Test/src/YIT_STAT_QTT_201504030300.dat";
		String path_1ne = "d:/적용/BRI_STAT_NE_201408022300.dat";
		
		String regex_qlt = "([0-9]{1,2}hr)\\s{1,}(ACC)\\s{1,}/\\s{1,}(BIAS)\\s{1,}/\\s{1,}(POD)\\s{1,}/\\s{1,}(FAR)\\s{1,}/\\s{1,}(CSI)";
		String regex_qtt = "([0-9]{1,2}hr)\\s{1,}(Num)\\s{1,}/\\s{1,}(Total Num)\\s{1,}/\\s{1,}(Gauge Ave.)\\s{1,}/\\s{1,}(Radar Ave.)\\s{1,}/\\s{1,}(1-NE)\\s{1,}/\\s{1,}(ME)\\s{1,}/\\s{1,}(BS)\\s{1,}/\\s{1,}(MAE)\\s{1,}/\\s{1,}(RMSE)\\s{1,}/\\s{1,}(CC)";
		String regex_1ne = "([0-9]{1,2}hr)\\s{1,}(1-NE)";
		
		String result = getValueStr(path_1ne,regex_1ne);
		System.out.println(result);
	}
}
