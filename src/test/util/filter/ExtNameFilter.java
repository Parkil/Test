package test.util.filter;

import java.io.File;
import java.io.FilenameFilter;
/*
 * ���ϰ˻��� ���Ǵ� ���� 
 * ���ڷ� ���� ext Ȯ���ڿ� �ش�Ǵ� ���ϸ� ǥ���Ѵ�.
 */
public class ExtNameFilter implements FilenameFilter {
	private final static ExtNameFilter extfilter = new ExtNameFilter();
	private static String ext;
	
	private ExtNameFilter(){}
	
	public static ExtNameFilter getInstance() {
		return extfilter;
	}
	
	public void setExt(String inputext) {
		inputext.replace(".","");
		ext = "."+inputext;
	}

	public boolean accept(File dir, String name) {
		boolean result = name.endsWith(ext);
		return result;
	}

}
