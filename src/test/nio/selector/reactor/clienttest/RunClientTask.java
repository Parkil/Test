package test.nio.selector.reactor.clienttest;

import java.util.Timer;

/*
 * TimerTask ½ÇÇà
 */
public class RunClientTask {
	public static void main(String[] args){
		Timer			 timer = new Timer(true);
		ClientTask ct1 = new ClientTask();
		ClientTask ct2 = new ClientTask();
		ClientTask ct3 = new ClientTask();
		ClientTask ct4 = new ClientTask();
		ClientTask ct5 = new ClientTask();
		ClientTask ct6 = new ClientTask();

		timer.scheduleAtFixedRate(ct1, 0, 10000);
		timer.scheduleAtFixedRate(ct2, 0, 10000);
		timer.scheduleAtFixedRate(ct3, 0, 10000);
		timer.scheduleAtFixedRate(ct4, 0, 10000);
		timer.scheduleAtFixedRate(ct5, 0, 10000);
		timer.scheduleAtFixedRate(ct6, 0, 10000);

		while(true){}
	}
}
