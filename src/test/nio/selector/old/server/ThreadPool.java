package test.nio.selector.old.server;

import java.util.concurrent.CopyOnWriteArrayList;

class ThreadPool {
	private CopyOnWriteArrayList<SocketThread> list = new CopyOnWriteArrayList<SocketThread>();

	ThreadPool(int size) {
		for(int i = 0 ; i<size ; i++) {
			SocketThread st = new SocketThread(this);
			list.add(st);
		}
	}

	SocketThread get() {
		if(list.size() == 0) {
			return null;
		}else {
			return list.remove(0);
		}
	}

	void put(SocketThread st) {
		list.add(st);
	}

	int size() {
		return list.size();
	}
}
