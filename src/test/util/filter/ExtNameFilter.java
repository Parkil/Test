package test.util.filter;

import java.io.File;
import java.io.FilenameFilter;
/*
 * 파일검색시 사용되는 필터 
 * 인자로 들어온 ext 확장자에 해당되는 파일만 표시한다.
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
