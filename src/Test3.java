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
	public static void main(String[] args) throws Exception{
		//File f = new File("C:/Dev/eclipse/workspace/Test/src/YIT_STAT_QLT_201504030300.dat"); //QLT
		File f = new File("C:/Dev/eclipse/workspace/Test/src/YIT_STAT_QTT_201504030300.dat");
		FileInputStream fis = new FileInputStream(f);
		FileChannel fc = fis.getChannel();
		
		MappedByteBuffer mbb = fc.map(MapMode.READ_ONLY, 0, fc.size());
		
		String file_str = ByteBufferUtil.bb_to_str(mbb);
		
		String[] lines = file_str.split("\n|\r");
		
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		
		//Pattern p = Pattern.compile("([0-9]{1,2}hr)\\s{1,}(ACC)\\s{1,}/\\s{1,}(BIAS)\\s{1,}/\\s{1,}(POD)\\s{1,}/\\s{1,}(FAR)\\s{1,}/\\s{1,}(CSI)"); //QLT
		Pattern p = Pattern.compile("([0-9]{1,2}hr)\\s{1,}(Num)\\s{1,}/\\s{1,}(Total Num)\\s{1,}/\\s{1,}(Gauge Ave.)\\s{1,}/\\s{1,}(Radar Ave.)\\s{1,}/\\s{1,}(1-NE)\\s{1,}/\\s{1,}(ME)\\s{1,}/\\s{1,}(BS)\\s{1,}/\\s{1,}(MAE)\\s{1,}/\\s{1,}(RMSE)\\s{1,}/\\s{1,}(CC)"); //QTT
	
		/**/
		HashMap<String,String> temp = null;
		//String[] col_name = new String[5];  //QLT
		String[] col_name = new String[10]; //QTT
		String hr = null;
		
		for(String line : lines) {
			if(line.trim().intern() == "".intern()) {
				continue;
			}
			
			Matcher m = p.matcher(line.trim());
			
			if(m.find()) {
				temp = new HashMap<String,String>();
				list.add(temp);
				
				hr = m.group(1);
				for(int i = 2 ; i<=m.groupCount() ; i++) {
					col_name[i-2] = m.group(i);
				}
				
				continue;
			}else {
				System.out.println(line.trim());
			}
			
			String[] values = line.trim().split("\\s{1,}");
			
			for(int i = 0 ; i<values.length ; i++) {
				temp.put(hr+"-"+col_name[i], values[i]);
			}
		}
		
		ObjectMapper om = new ObjectMapper();
		String result = om.writeValueAsString(list);
		System.out.println(result);
		
		fis.close();
	}
}
