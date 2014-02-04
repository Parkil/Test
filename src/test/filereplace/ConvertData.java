package test.filereplace;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Hashtable;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import test.util.ByteBufferUtil;

/*
 * 파일로 된 데이터를 정규식이나 Hashtable로 변환
 * 
 */
public class ConvertData {
	private Logger log = Logger.getLogger(getClass());
	private final static ConvertData cd = new ConvertData();

	private Hashtable<String,String> table;
	private Pattern jsppattern;
	private Pattern sqlpattern;
	private boolean is_loaded = false;

	private ConvertData() {}

	public static ConvertData getInstance() {
		if(!cd.is_loaded) {
			cd.log.info("ConvertData Loaded");
			cd.is_loaded = true;
			cd.reload();
		}
		return cd;
	}

	/**
	 * hashtable및 Pattern을 최신파일 내용으로 갱신한다.
	 */
	public void reload() {
		table	   = genHashtable();
		jsppattern = genJspPattern();
		sqlpattern = genSQLPattern();
	}

	/** hashtableraw.txt파일을 읽어들여 파일안의 내용을 데이터로 가지는 Hashtable을 반환한다.
	 * @return hashtableraw.txt 내용을 가지는 Hashtable
	 */
	public Hashtable<String,String> getHashtable() {
		return table;
	}

	/**regexraw.txt의 내용을 읽어들여 jsp에 해당하는 정규식을 반환한다.
	 * @return 정규식을 포함하는 Pattern클래스
	 */
	public Pattern getJspPattern() {
		return jsppattern;
	}

	/**regexraw.txt의 내용을 읽어들여 SQL에 해당하는 정규식을 반환한다.
	 * @return 정규식을 포함하는 Pattern클래스
	 */
	public  Pattern getSQLPattern() {
		return sqlpattern;
	}

	//Hashtable실제 생성로직
	private Hashtable<String,String> genHashtable() {
		FileInputStream  fis = null;
		FileChannel		  in = null;
		MappedByteBuffer mmb = null;

		Hashtable<String,String> table = new Hashtable<String,String>();
		try {
			fis = new FileInputStream(cd.getClass().getResource("").getPath()+"/hashtableraw.txt");
			in  = fis.getChannel();
			mmb = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());

			String content = ByteBufferUtil.bb_to_str(mmb);
			String temp[]  = content.split("\n");

			Pattern p = Pattern.compile(",");

			for(String value : temp) {
				String values[] = p.split(value);
				table.put(values[0], values[1].replaceAll("\n|\r", ""));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
				if(in  != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mmb = null;
		}
		return table;
	}

	//JspPattern 실제 생성로직
	private Pattern genJspPattern() {
		Pattern p = null;

		FileInputStream  fis = null;
		FileChannel		  in = null;
		MappedByteBuffer mmb = null;
		try {
			fis = new FileInputStream(cd.getClass().getResource("").getPath()+"/regexraw.txt");
			in  = fis.getChannel();
			mmb = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
			String		 content = ByteBufferUtil.bb_to_str(mmb);
			content				 = content.replaceAll("\n|\r", "|");

			String jspregex = "name=+[\"']("+content.substring(0,content.length()-1)+")[\"']";
			p				= Pattern.compile(jspregex);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
				if(in  != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mmb = null;
		}

		return p;
	}

	//SQL Pattern 실제생성로직
	private Pattern genSQLPattern() {
		Pattern p = null;

		FileInputStream  fis = null;
		FileChannel		  in = null;
		MappedByteBuffer mmb = null;
		try {
			fis = new FileInputStream(cd.getClass().getResource("").getPath()+"/regexraw.txt");
			in  = fis.getChannel();
			mmb = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
			String		 content = ByteBufferUtil.bb_to_str(mmb);
			content				 = content.replaceAll("\n|\r", "|");

			String sqlregex = "AS( +)("+content.substring(0,content.length()-1)+")";
			p				= Pattern.compile(sqlregex);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
				if(in  != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mmb = null;
		}

		return p;
	}
}

