package test.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import test.util.filter.DirFilter;
import test.util.filter.ExtNameFilter;

/*
 * Systemier�� GrepFileŬ������ �籸���� Ŭ����
 */
public class FindFile {
	private FindFile(){}

	/**���ڷ� �־��� root ��ο��� extȮ���ڸ� ���� ��� ������ �����Ѵ�.
	 * @param root �˻��� ������ root��θ� ���� ����
	 * @param ext �˻��� ����� Ȯ����
	 * @return root��� + root�Ʒ���ο��� extȮ���ڸ� ���� ���� ����Ʈ
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

	/**���ڷ� �־��� root ��ο��� extȮ���ڸ� ���� ��� ������ �����Ͽ� �迭�� ��ȯ
	 * @param root �˻��� ������ root��θ� ���� ����
	 * @param ext �˻��� ����� Ȯ����
	 * @return root��� + root�Ʒ���ο��� extȮ���ڸ� ���� ������ ������ �迭
	 */
	public static File[] findFileArray(File root, String ext) {
		ArrayList<File> temp = FindFile.findFileList(root,ext);
		return (File[])temp.toArray();
	}

	/**���ڷ� �־��� root ��ο��� ��� ���丮�� �����Ѵ�.
	 * @param root �˻��� ������ root��θ�
	 * @return root��� + root�Ʒ���ο��� �˻��� ��� ���丮
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

	/**���ڷ� �־��� root��ο��� ��� ������ �����Ѵ�.
	 * @param root �˻��� ������ root���
	 * @return root��� + root�Ʒ���ο��� �˻��� ��� ����
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
