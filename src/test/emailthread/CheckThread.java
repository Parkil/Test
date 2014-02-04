package test.emailthread;

import test.util.FileUtil;

/*
 * SMTP���� ������ Thread����� ���濹��
 * ���� : 1���� RawDataŬ�������� �� Thread���� Data�� �������� emailüũ�� ���� RawData���� �����͸� �������� ����ȭ ����
 * ���濹�� : RawDataŬ���� / Thread ����ŭ�� �����͸� ������ Deque�� �� Thread���� �й��ϰ� emailüũ�� �й��� Deque���� Data�� ����������
 * 			�����͸� �������� ����ȭ�� �ʿ䰡 ���� Ư��  Thread�� Deque�� �����͸� ��� �������� ��� �ٸ� Thread�� �й�� Deque�� �����͸� �ڿ��� �������� ������� ó��
 * 			�� �̹������ ó���Ұ�� �ٸ� Deque�� �����͸� �����ö�,Deque�� �����Ͱ����� �����Ҷ� ����ȭ�� �ʿ��� ������ ���̸� �ʱ� Deque�й谡 ���������� Thread������ ����� �Ұ����� ����(�̴� Latch�� �̿��ؾ� ��)
 */
public class CheckThread implements Runnable {
	private final FileUtil	valid;
	private final FileUtil	invalid;
	private final EmailCheck	ec;
	private final RawData		rd;

	public CheckThread(RawData rd) {
		valid	= new FileUtil();
		invalid	= new FileUtil();
		ec		= new EmailCheck();
		this.rd = rd;

		new Thread(this).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(rd.size() != 0) {
			String	line	= rd.getData();
			String	email	= line.split(",")[8];
			boolean	result	= ec.isEmailValid(email.trim());

			if(result) {
				valid.writeFileAppend(Thread.currentThread()+".valid.txt",line);
				rd.logger.info("Valid Data Saved [Data : "+line+"]");
			}else {
				invalid.writeFileAppend(Thread.currentThread()+".invalid.txt",line);
				rd.logger.info("Invalid Data Saved [Data : "+line+"]");
			}
		}

		valid.close();
		invalid.close();
	}
}
