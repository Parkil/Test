package test.jsch;

import com.jcraft.jsch.SftpProgressMonitor;

/*
 * Jsch�� ftp���ε� �Ǵ� �ٿ�ε�� ��Ȳ�� ǥ���ϴ� ���
 */

public class UploadMonitor implements SftpProgressMonitor {
	private long uploadedSize = 0L;
	private long fileSize = 0L;
	
	/*
	 * ���ε� �����Ȳ üũ
	 * true�� ��ȯ�ϸ� ���� �ٿ�ε�/���ε尡 �������̰� false�� ��ȯ�ϸ� �ٿ�ε�/���ε尡 ����Ǿ��ٴ� �ǹ� 
	 * 
	 */
	@Override
	public boolean count(long uploadSize) {
		if(uploadedSize >= fileSize) {
			return false;
		}else {
			uploadedSize += uploadSize;
			//System.out.println("uploadedSize : "+uploadedSize);
			return true;
		}
	}
	
	/*
	 * �ٿ�ε�/���ε尡 �Ϸ�Ǹ� ȣ��
	 */
	@Override
	public void end() {
		System.out.println("end");

	}
	
	/*
	 * ���� �ٿ�ε�/���ε�� ���� 
	 * index : ���ε�/�ٿ�ε� ����
	 * srcPath : ���ε�/�ٿ�ε� �� url
	 * dstPath : ���ε�/�ٿ�ε� ������ url
	 * fileSize : ���ε��� ���� ������
	 */
	@Override
	public void init(int index, String srcPath, String dstPath, long fileSize) {
		System.out.println("init : "+index+"=="+srcPath+"=="+dstPath+"=="+fileSize);
		this.fileSize = fileSize;
	}

}
