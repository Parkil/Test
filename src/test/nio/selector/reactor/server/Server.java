/*
 * Reactor������ ������ NIO����
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
	 * ��������� �������δ� �뵵���� Handler�� �� ���� �־�� �Ұ� ����.
	 * �������ۿ�
	 * Echo��
	 * Message���ۿ� ����
	 */
	public static void main(String[] args) {
		Server s = new Server("127.0.0.1",7);
		s.listen();
	}

	/**������ ServerSocket�� �����ϰ� Ŭ���̾�Ʈ�κ����� ������ ����Ѵ�.
	 * @param url ���� URL
	 * @param port ���� ��Ʈ
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
	 * Selector�� �̿��Ͽ� Event�߻�(sel.select() �������� 0���� ŭ)�� �ش� �̺�Ʈ(read,write,accept)�� �ش��ϴ� �ڵ鷯�� �����Ų��.
	 */
	private void listen() {
		//HandlerPool���� ��ȿ���� ���� �����͸� �����ϴ� Timer�� �۵���Ų��.
		Timer			 timer = new Timer(true);
		HandlerOptmizeTask hot = new HandlerOptmizeTask(hp);
		timer.scheduleAtFixedRate(hot, 0, 5000);

		while(true) {
			/*
			 * selector�� select��ɰ� Key Iterator�� ���� try~catch���� ó���� �ϰ� �Ǹ�
			 * ������ �߻��ϰų� ������ �Ǵ� ��찡 ����. ������ �����Ұ�
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