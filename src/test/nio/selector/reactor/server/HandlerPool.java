/*
 * �� �̺�Ʈ �ڵ鷯�� �����ϴ� Pool
 */
package test.nio.selector.reactor.server;

import java.nio.channels.SelectionKey;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import test.nio.selector.reactor.handler.ChannelHandler;

class HandlerPool {
	private final ConcurrentHashMap<SelectionKey, ChannelHandler> pool = new ConcurrentHashMap<SelectionKey, ChannelHandler>();

	/**Pool�� �̺�Ʈ �ڵ鷯�� �����Ѵ�.
	 * @param key �ڵ鷯�� �ش��ϴ� SelectionKey
	 * @param handler �̺�Ʈ �ڵ鷯
	 */
	public void put(SelectionKey key, ChannelHandler handler) {
		pool.put(key, handler);
	}

	/**Key�� �ش��ϴ� �̺�Ʈ �ڵ鷯�� ��ȯ�Ѵ�.
	 * @param key
	 * @return
	 */
	public ChannelHandler get(SelectionKey key) {
		return pool.get(key);
	}

	/**Key�� �ش��ϴ� �̺�Ʈ �ڵ鷯�� Pool���� �����Ѵ�.
	 * @param key
	 */
	public ChannelHandler remove(SelectionKey key) {
		return pool.remove(key);
	}

	/**Pool�� KeySet�� ��ȯ�Ѵ�.
	 * @return Pool�� KeySet
	 */
	public Set<SelectionKey> getKeySet() {
		return pool.keySet();
	}
}
