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
 * ���Ϸ� �� �����͸� ���Խ��̳� Hashtable�� ��ȯ
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
	 * hashtable�� Pattern�� �ֽ����� �������� �����Ѵ�.
	 */
	public void reload() {
		table	   = genHashtable();
		jsppattern = genJspPattern();
		sqlpattern = genSQLPattern();
	}

	/** hashtableraw.txt������ �о�鿩 ���Ͼ��� ������ �����ͷ� ������ Hashtable�� ��ȯ�Ѵ�.
	 * @return hashtableraw.txt ������ ������ Hashtable
	 */
	public Hashtable<String,String> getHashtable() {
		return table;
	}

	/**regexraw.txt�� ������ �о�鿩 jsp�� �ش��ϴ� ���Խ��� ��ȯ�Ѵ�.
	 * @return ���Խ��� �����ϴ� PatternŬ����
	 */
	public Pattern getJspPattern() {
		return jsppattern;
	}

	/**regexraw.txt�� ������ �о�鿩 SQL�� �ش��ϴ� ���Խ��� ��ȯ�Ѵ�.
	 * @return ���Խ��� �����ϴ� PatternŬ����
	 */
	public  Pattern getSQLPattern() {
		return sqlpattern;
	}

	//Hashtable���� ��������
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

	//JspPattern ���� ��������
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

	//SQL Pattern ������������
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

