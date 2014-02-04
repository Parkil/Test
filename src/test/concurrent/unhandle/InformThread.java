package test.concurrent.unhandle;
/*
 * Thread ����� ���ܰ� �߻��� ��� ���ܸ� ��Ƽ� ó���ϴ� ����
 * Thread ������� �����ϴ� ���α׷��� ��� ���ܰ� �����Ǹ� �ܼ��� stacktraceó���� �ƴ� �߰� ó���� �ʿ��� ��찡 �ִ�.
 * �� ��� ������ ���� ������� ���ܸ� ��Ƽ� �߰�ó��(�ش��۾� �ٽ� ����,�ش� Thread ����... etc)�� �����Ѵ�.
 */

public class InformThread implements Runnable {
	public void ThreadExit(Runnable a, Exception e) {
		if(e == null) {
			System.out.println("�ش� Thread�� ���������� ����� : "+a.toString());
		}else {
			System.out.println("�ش� Thread���� �����߻� : "+e.toString());
		}
	}

	@SuppressWarnings("null")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String		a	= null;
		Exception	ex	= null;

		try {
			System.out.println(a.indexOf('2')); //���Ƿ� null pointer exception�߻�
		} catch(Exception e) {
			ex = e;
		} finally {
			ThreadExit(this, ex);
		}
	}
}
