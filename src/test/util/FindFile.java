package test.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import test.util.filter.DirFilter;
import test.util.filter.ExtNameFilter;

/*
 * Systemier의 GrepFile클래스를 재구성한 클래스
 */
public class FindFile {
	private FindFile(){}

	/**인자로 주어진 root 경로에서 ext확장자를 가진 모든 파일을 추출한다.
	 * @param root 검색을 시작할 root경로를 가진 파일
	 * @param ext 검색에 사용할 확장자
	 * @return root경로 + root아래경로에서 ext확장자를 가진 파일 리스트
	 */
	public static ArrayList<File> findFileList(File root, String ext) {
		ExtNameFilter.getInstance().setExt(ext);
		ArrayList<File> listFile = new ArrayList<File>();

		DirFilter.getInstance().setMode("dir");

		File files[]	= root.listFiles(ExtNameFilter.getInstance());
		File dirs[]		= root.listFiles(DirFilter.getInstance());

		listFile.addAll(Arrays.asList(files));

		for(File dir : dirs) {
			listFile.addAll(FindFile.findFileList(dir,ext));
		}

		return listFile;
	}

	/**인자로 주어진 root 경로에서 ext확장자를 가진 모든 파일을 추출하여 배열로 반환
	 * @param root 검색을 시작할 root경로를 가진 파일
	 * @param ext 검색에 사용할 확장자
	 * @return root경로 + root아래경로에서 ext확장자를 가진 파일을 저장한 배열
	 */
	public static File[] findFileArray(File root, String ext) {
		ArrayList<File> temp = FindFile.findFileList(root,ext);
		return (File[])temp.toArray();
	}

	/**인자로 주어진 root 경로에서 모든 디렉토리를 추출한다.
	 * @param root 검색을 시작할 root경로를
	 * @return root경로 + root아래경로에서 검색한 모든 디렉토리
	 */
	public static ArrayList<File> findFilePath(File root) {
		ArrayList<File> listFile = new ArrayList<File>();

		DirFilter.getInstance().setMode("dir");

		File dirs[]		= root.listFiles(DirFilter.getInstance());
		listFile.addAll(Arrays.asList(dirs));

		for(File dir : dirs) {
			listFile.addAll(FindFile.findFilePath(dir));
		}

		return listFile;
	}

	/**인자로 주어진 root경로에서 모든 파일을 추출한다.
	 * @param root 검색을 시작할 root경로
	 * @return root경로 + root아래경로에서 검색한 모든 파일
	 */
	public static ArrayList<File> findAllFile(File root) {
		ArrayList<File> listFile = new ArrayList<File>();

		DirFilter.getInstance().setMode("file");
		File files[]	= root.listFiles(DirFilter.getInstance());

		DirFilter.getInstance().setMode("dir");
		File dirs[]		= root.listFiles(DirFilter.getInstance());

		listFile.addAll(Arrays.asList(files));

		for(File dir : dirs) {
			listFile.addAll(FindFile.findAllFile(dir));
		}

		return listFile;
	}
}
