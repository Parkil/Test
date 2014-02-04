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
 * Selector + ServerSocketChanel을 이용한 서버
 * 클라이언트에서 접속시 해당소켓을 Thread로 넘겨서 Thread에서 I/O를 처리한다.
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

	/** 생성자(서버시작)
	 * @param port 포트번호
	 * @param pool_size 클라이언트와 통신을 하는데 필요한 SocketThread 개수
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
	 * 클라이언트의 접속을 감지하면 Thread를 생성하여 I/O를 처리하게 한다.
	 * 현재는 무한대로 작동을 하게 하며 추후 서버 재시작,종료기능을 추가한다.
	 * Selection의 selectNow()를 사용하게 되면 단순 continue를 하게 되는 횟수가 많아지는 문제가 있기 때문에 select()를 사용한다.
	 */
	public void listen() {
		while(true) {
			try {
				sel.select(); //접속이 감지될때까지 대기 한다 대기시간을 없애고 싶으면 selectNow()를 이용할것

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
						key.cancel(); //현재 건이 다시 체크되어 Thread가 중복생성되는것을 방지

						/*
						 * 외부 Thread를 이용하여 selection처리를 할 경우에는 it.remove(); 만으로는 충분치 않으며 외부 Thread에서 selection을 처리할 동안
						 * 해당 Key가 select key set에서 다시 수행되지 않도록 처리해야 한다.
						 * 이를 위해서 interestOps를 제거처리하고 Thread가 종료된 후에 다시 interestOps를 원래대로 변환한다.
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
