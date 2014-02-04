/*
 * �����ð����� HandlerPool�� üũ�Ͽ� �ش� SelectionKey�� invalid �� ��� HandlerPool���� �����Ѵ�.
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
		// HandlerPool�� ���� KeySet�� �����Ͽ� Loop �� ���鼭 SelectionKey�� ��ȿ���� ������� �ش� Key �� Handler Pool���� �����Ѵ�.
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

