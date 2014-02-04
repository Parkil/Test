import java.io.File;
import java.util.ArrayList;

import test.util.FindFile;


public class Test {
	public static void main(String[] args) throws Exception{
		File root = new File("d:/Dev/CustomFrame/workspace/FA50-TIMS/src/main/java/org/rokaf/tims/common");
		File root2 = new File("d:/Dev/CustomFrame/workspace/FA50-TIMS/src/main/webapp/WEB-INF/jsp/org/rokaf/tims/common");
		File root3 = new File("d:/Dev/CustomFrame/workspace/FA50-TIMS/src/main/java/org/rokaf/tims/eztims");
		File root4 = new File("d:/Dev/CustomFrame/workspace/FA50-TIMS/src/main/webapp/WEB-INF/jsp/org/rokaf/tims/eztims");
		ArrayList<File> list = FindFile.findFileList(root4, "jsp");
		
		for(File t : list) {
			String path = t.getPath();
			String name = t.getName();
			String pack = "";
			String clas = "";
			
			
			int first_idx = path.indexOf("\\org");
			int last_idx = path.lastIndexOf('\\');
			
			int ex_idx = name.lastIndexOf('.');
			
			String temp = new String(path.substring(first_idx,last_idx));
			path = "WEB-INF\\jsp"+path.substring(first_idx,last_idx);
			
			
			pack = temp.replace("\\",".");
			clas = name.substring(0,ex_idx);
			
			//System.out.println(clas);
			//System.out.println(path+" , "+pack+" , "+clas+" , "+name);
			System.out.println(path+" , "+clas+" , "+name);
		}
	}
}
