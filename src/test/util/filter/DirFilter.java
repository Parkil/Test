package test.util.filter;

import java.io.File;
import java.io.FileFilter;

/*
 * 파일검색시 사용되는 필터
 * mode = dir  - 파일리스트중 속성이 디렉토리인 속성만 검색한다.
 * mode = file - 파일리스트중 속성이 파일인 속성만 검색한다.
 */
public class DirFilter implements FileFilter {
	private final static DirFilter dirfilter = new DirFilter();
	private static String mode = "dir";

	private DirFilter(){}

	public static DirFilter getInstance(){
		return dirfilter;
	}

	public void setMode(String inputmode) {
		mode = inputmode;
	}

	public String getMode() {
		return mode;
	}

	public boolean accept(File pathname) {
		boolean result = false;
		if(mode.intern() == "dir".intern()) {
			result = pathname.isDirectory() && (pathname.getPath().indexOf("svn") == -1);
		}else if(mode.intern() == "file".intern()) {
			result = !pathname.isDirectory();
		}

		return result;
	}

}
