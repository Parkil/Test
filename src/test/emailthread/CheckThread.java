package test.emailthread;

import test.util.FileUtil;

/*
 * SMTP로직 수정후 Thread방식을 변경예정
 * 기존 : 1개의 RawData클래스에서 각 Thread마다 Data를 가져가서 email체크를 수행 RawData에서 데이터를 가져갈때 동기화 수행
 * 변경예정 : RawData클래스 / Thread 수만큼의 데이터를 가지는 Deque를 각 Thread마다 분배하고 email체크시 분배한 Deque에서 Data를 가져가게함
 * 			데이터를 가져갈때 동기화할 필요가 없고 특정  Thread가 Deque의 데이터를 모두 소진했을 경우 다른 Thread에 분배된 Deque의 데이터를 뒤에서 가져오는 방식으로 처리
 * 			단 이방식으로 처리할경우 다른 Deque의 데이터를 가져올때,Deque의 데이터개수를 판정할때 동기화가 필요할 것으로 보이며 초기 Deque분배가 끝날때까지 Thread시작을 멈춰야 할것으로 보임(이는 Latch를 이용해야 함)
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
