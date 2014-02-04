/*
 * 일정시간마다 HandlerPool을 체크하여 해당 SelectionKey가 invalid 일 경우 HandlerPool에서 삭제한다.
 */
package test.nio.selector.reactor.server;

import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;

class HandlerOptmizeTask extends TimerTask {
	private HandlerPool hp = null;

	HandlerOptmizeTask(){}

	HandlerOptmizeTask(HandlerPool hp) {
		this.hp = hp;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// HandlerPool에 대한 KeySet을 추출하여 Loop 를 돌면서 SelectionKey가 유효하지 않은경우 해당 Key 를 Handler Pool에서 삭제한다.
		Set<SelectionKey> 	   set	   = hp.getKeySet();
		Iterator<SelectionKey> keyiter = set.iterator();

		System.out.println("Checking Invalid SelectionKey");

		SelectionKey key = null;
		while(keyiter.hasNext()) {
			key = keyiter.next();

			if(!key.isValid()) {
				hp.remove(key);
				System.out.println("Invalid Key Removed");
			}
		}
	}
}

