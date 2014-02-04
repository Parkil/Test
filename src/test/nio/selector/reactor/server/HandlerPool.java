/*
 * 각 이벤트 핸들러를 저장하는 Pool
 */
package test.nio.selector.reactor.server;

import java.nio.channels.SelectionKey;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import test.nio.selector.reactor.handler.ChannelHandler;

class HandlerPool {
	private final ConcurrentHashMap<SelectionKey, ChannelHandler> pool = new ConcurrentHashMap<SelectionKey, ChannelHandler>();

	/**Pool에 이벤트 핸들러를 저장한다.
	 * @param key 핸들러에 해당하는 SelectionKey
	 * @param handler 이벤트 핸들러
	 */
	public void put(SelectionKey key, ChannelHandler handler) {
		pool.put(key, handler);
	}

	/**Key에 해당하는 이벤트 핸들러를 반환한다.
	 * @param key
	 * @return
	 */
	public ChannelHandler get(SelectionKey key) {
		return pool.get(key);
	}

	/**Key에 해당하는 이벤트 핸들러를 Pool에서 삭제한다.
	 * @param key
	 */
	public ChannelHandler remove(SelectionKey key) {
		return pool.remove(key);
	}

	/**Pool의 KeySet을 반환한다.
	 * @return Pool의 KeySet
	 */
	public Set<SelectionKey> getKeySet() {
		return pool.keySet();
	}
}
