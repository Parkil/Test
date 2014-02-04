import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.Type;

import test.util.ByteBufferUtil;
import test.util.FindFile;


public class RefelectionTest {
	public static void main(String[] args) throws Exception{
		File root = new File("d:/문서분리/FA50-TIMS/FA50-TIMS/target/classes/org/rokaf/tims/common/co/web");
		ArrayList<File> list = FindFile.findFileList(root, "class");
		
		RandomAccessFile raf = new RandomAccessFile("d:/common.co.web.txt","rw");
		FileChannel fc = raf.getChannel();
		
		
		int z = 0;
		StringBuffer sb = new StringBuffer();
		for(File f : list) {
			ClassParser cp = new ClassParser(f.getPath());
			ClassGen cg = new ClassGen(cp.parse());
			
			String str = cg.getClassName();
			String str_arr[] = str.split("\\.");
			
			String classname = str_arr[str_arr.length-1];
			
			Field[] f_arr	= cg.getFields();
			Method[] m_arr	= cg.getMethods();
			
			for(Field fe : f_arr) {
				String f_name = fe.toString();
				String temp[] = f_name.split(" ");
				
				String access = temp[0];
				String return_type = fe.getType().toString();
				String temp2[] = return_type.split("\\.");
				return_type = temp2[temp2.length-1];
				
				String name = fe.getName();
				
				sb.append(z++);
				sb.append("|");
				sb.append(str);
				sb.append("|");
				sb.append(classname);
				sb.append("|");
				sb.append("FIELD");
				sb.append("|");
				sb.append(name);
				sb.append("|");
				sb.append(access);
				sb.append("|");
				sb.append(return_type);
				sb.append("|");
				sb.append("N/A");
				sb.append("\n");
			}
			
			for(Method m : m_arr) {
				if(m.toString().indexOf("<init>") != -1) {
					continue;
				}
				
				String access = m.toString().split(" ")[0];
				if(access.length() > 9) {
					access = "default";
				}
				
				String ret_temp[] = m.getReturnType().toString().split("\\.");
				String ret_type = ret_temp[ret_temp.length - 1];
				
				String method_name = m.getName();
				
				Type type_arr[] = m.getArgumentTypes();
				String parameter = "";
				String type_frg_arr[] = null;
				
				if(type_arr.length == 1) {
					type_frg_arr = type_arr[0].toString().split("\\.");
					parameter = type_frg_arr[type_frg_arr.length - 1];
				}else {
					for(Type t : type_arr) {
						String type_str = t.toString();
						
						if(type_str.indexOf("Model") != -1
								|| type_str.indexOf("BindingResult") != -1
								|| type_str.indexOf("SessionStatus") != -1
								|| type_str.indexOf("MultipartHttpServletRequest") != -1
								|| type_str.indexOf("String") != -1) { //스프링 관련 파라메터는 제외
							continue;
						}
						
						type_frg_arr = type_str.split("\\.");
						parameter += type_frg_arr[type_frg_arr.length - 1]+",";
					}
					
					if("".equals(parameter)) {
						parameter = "void,";
					}
				}
				
				sb.append(z++);
				sb.append("|");
				sb.append(str);
				sb.append("|");
				sb.append(classname);
				sb.append("|");
				sb.append("METHOD");
				sb.append("|");
				sb.append(method_name);
				sb.append("|");
				sb.append(access);
				sb.append("|");
				sb.append(ret_type);
				sb.append("|");
				sb.append(parameter.substring(0, parameter.length() - 1));
				sb.append("|");
				sb.append("None");
				sb.append("\n");
				//System.out.println(access+" "+ret_type+" "+method_name+" "+parameter);
			}
		}
		System.out.println(sb.toString());
		ByteBuffer temp = ByteBufferUtil.str_to_bb(sb.toString());
		MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE, 0, temp.limit());
		mbb.put(temp);
		mbb.flip();
		mbb.force();
		fc.close();
		raf.close();
	}
}
