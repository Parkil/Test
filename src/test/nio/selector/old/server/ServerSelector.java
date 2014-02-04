package test.nio.selector.old.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/*
 * Selector + ServerSocketChanel�� �̿��� ����
 * Ŭ���̾�Ʈ���� ���ӽ� �ش������ Thread�� �Ѱܼ� Thread���� I/O�� ó���Ѵ�.
 */
public class ServerSelector {
	public static void main(String[] args) {
		ServerSelector a = new ServerSelector(1234,5);
		a.listen();
	}

	private final Logger log = Logger.getLogger(getClass());
	private ServerSocketChannel ssc;
	private Selector 			sel;
	private final ThreadPool			pool;

	/** ������(��������)
	 * @param port ��Ʈ��ȣ
	 * @param pool_size Ŭ���̾�Ʈ�� ����� �ϴµ� �ʿ��� SocketThread ����
	 */
	public ServerSelector(int port, int pool_size) {
		pool = new ThreadPool(pool_size);
		try {
			ssc = ServerSocketChannel.open();
			sel = Selector.open();

			ssc.socket().bind(new InetSocketAddress(port));
			ssc.configureBlocking(false);
			ssc.register(sel, SelectionKey.OP_ACCEPT);
			log.info("Server Started(Port : "+port+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Ŭ���̾�Ʈ�� ������ �����ϸ� Thread�� �����Ͽ� I/O�� ó���ϰ� �Ѵ�.
	 * ����� ���Ѵ�� �۵��� �ϰ� �ϸ� ���� ���� �����,�������� �߰��Ѵ�.
	 * Selection�� selectNow()�� ����ϰ� �Ǹ� �ܼ� continue�� �ϰ� �Ǵ� Ƚ���� �������� ������ �ֱ� ������ select()�� ����Ѵ�.
	 */
	public void listen() {
		while(true) {
			try {
				sel.select(); //������ �����ɶ����� ��� �Ѵ� ���ð��� ���ְ� ������ selectNow()�� �̿��Ұ�

				Set<SelectionKey> 	  keyset = sel.selectedKeys();
				Iterator<SelectionKey> keyit = keyset.iterator();

				while(keyit.hasNext()) {
					SelectionKey key = keyit.next();

					if(key.isAcceptable()) {
						ServerSocketChannel temp = (ServerSocketChannel)key.channel();
						SocketChannel		sc	 = temp.accept();

						if(sc != null) {
							log.info("Clinet Connected : "+sc);

							sc.configureBlocking(false);
							sc.register(sel, SelectionKey.OP_READ);

							log.info("Socket Channel Registered [HashCode : "+sc.hashCode()+"]");
						}
					}

					if(key.isReadable()) {
						log.info("Readable");
						pool.get().start(key);
						key.cancel(); //���� ���� �ٽ� üũ�Ǿ� Thread�� �ߺ������Ǵ°��� ����

						/*
						 * �ܺ� Thread�� �̿��Ͽ� selectionó���� �� ��쿡�� it.remove(); �����δ� ���ġ ������ �ܺ� Thread���� selection�� ó���� ����
						 * �ش� Key�� select key set���� �ٽ� ������� �ʵ��� ó���ؾ� �Ѵ�.
						 * �̸� ���ؼ� interestOps�� ����ó���ϰ� Thread�� ����� �Ŀ� �ٽ� interestOps�� ������� ��ȯ�Ѵ�.
						 */
						//key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
						//new SocketThread(key);
						//key.interestOps(key.interestOps() & SelectionKey.OP_READ);
						//key.selector().wakeup();
					}else if (key.isWritable()) {
						log.info("Writeable");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
