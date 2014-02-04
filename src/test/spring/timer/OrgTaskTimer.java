package test.spring.timer;

import java.util.TimerTask;

public class OrgTaskTimer extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Java Timer Task를 이용한 일정시간마다 작업");
	}

}
