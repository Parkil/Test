/*
 * Reactor패턴을 적용한 NIO서버
 */
package test.nio.selector.reactor.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;

import test.nio.selector.reactor.emuration.Mode;
import test.nio.selector.reactor.handler.AcceptHandler;
import test.nio.selector.reactor.handler.AcceptProcessor;
import test.nio.selector.reactor.handler.ChannelHandler;

public class Server {
	private InetSocketAddress ia  = null;
	private Selector 		  sel;

	private final HandlerPool hp 		  = new HandlerPool();
	/*
	 * 현재까지의 생각으로는 용도별로 Handler가 다 따로 있어야 할거 같다.
	 * 파일전송용
	 * Echo용
	 * Message전송용 등등등
	 */
	public static void main(String[] args) {
		Server s = new Server("127.0.0.1",7);
		s.listen();
	}

	/**생성자 ServerSocket을 생성하고 클라이언트로부터의 접속을 대기한다.
	 * @param url 서버 URL
	 * @param port 서버 포트
	 */
	public Server(String url, int port) {
		ia = new InetSocketAddress(url, port);

		try {
			sel = Selector.open();

			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.socket().bind(ia);
			ssc.register(sel, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Selector를 이용하여 Event발생(sel.select() 수행결과가 0보다 큼)시 해당 이벤트(read,write,accept)에 해당하는 핸들러를 실행시킨다.
	 */
	private void listen() {
		//HandlerPool에서 유효하지 않은 데이터를 삭제하는 Timer를 작동시킨다.
		Timer			 timer = new Timer(true);
		HandlerOptmizeTask hot = new HandlerOptmizeTask(hp);
		timer.scheduleAtFixedRate(hot, 0, 5000);

		while(true) {
			/*
			 * selector의 select기능과 Key Iterator를 같은 try~catch문에 처리를 하게 되면
			 * 에러가 발생하거나 먹통이 되는 경우가 많다. 각별히 유의할것
			 */
			try {
				sel.select();
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			}

			Set<SelectionKey>		keyset  = sel.selectedKeys();
			Iterator<SelectionKey>	keyiter = keyset.iterator();
			while(keyiter.hasNext()) {
				SelectionKey key = keyiter.next();
				keyiter.remove();

				if(key.isAcceptable()) {
					System.out.println("Accept");
					new AcceptHandler().accept(new AcceptProcessor(), key, Mode.ACCEPT);
				}

				if(key.isValid() && key.isReadable()) {
					ChannelHandler ch = hp.get(key);

					if(ch == null) {
						ch = new ChannelHandler();

						hp.put(key, ch);
					}
					ch.accept(null, key, Mode.READ);
				}

				if(key.isValid() && key.isWritable()) {
					ChannelHandler ch = hp.get(key);

					if(ch == null) {
						ch = new ChannelHandler();
						hp.put(key, ch);
					}
					ch.accept(null, key, Mode.WRITE);
				}
			}
		}
	}
}