package test.util.filter;

import java.io.File;
import java.io.FileFilter;

/*
 * ���ϰ˻��� ���Ǵ� ����
 * mode = dir  - ���ϸ���Ʈ�� �Ӽ��� ���丮�� �Ӽ��� �˻��Ѵ�.
 * mode = file - ���ϸ���Ʈ�� �Ӽ��� ������ �Ӽ��� �˻��Ѵ�.
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
