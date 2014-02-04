package test.nio.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
	private InetSocketAddress ia  = null;
	private Selector 		  sel;

	private final ByteBuffer b = ByteBuffer.allocateDirect(10000);

	public static void main(String[] args) {
		Server s = new Server("127.0.0.1",7);
		s.listen();
	}

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

	public void listen() {
		while(true) {
			/*
			 * selector의 select기능과 Key Iterator를 같은 try~catch문에 처리를 하게 되면
			 * 에러가 발생하거나 먹통이 되는 경우가 많다. 각별히 유의할것
			 */
			try {
				sel.select();
			}
			catch (IOException ex) {
				ex.printStackTrace();
				break;
			}

			Set<SelectionKey>		keyset  = sel.selectedKeys();
			Iterator<SelectionKey>	keyiter = keyset.iterator();
			while(keyiter.hasNext()) {
				SelectionKey key = keyiter.next();
				keyiter.remove();

				try {
					if(key.isAcceptable()) {
						ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
						SocketChannel 		sc	= ssc.accept();

						sc.configureBlocking(false);
						sc.register(sel, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
					}

					if(key.isReadable()) {
						SocketChannel sc = (SocketChannel)key.channel();
						b.clear();
						sc.read(b);
						System.out.println("R");
						//b.flip(); //flip을 여기에서 수행하면 에러가 발생한다. 주의
						//System.out.println("읽어들인 값 : "+ByteBufferUtil.bb_to_str(b));
					}

					if(key.isWritable()) {
						SocketChannel sc = (SocketChannel)key.channel();
						/*
						 * 이런식으로 실행해도 select이벤트가 계속 걸려서 에러가 발생하는 경우가 있다 주의
							if(b.position() != 0) {
								b.flip();
							}*/
						b.flip();
						sc.write(b);
						System.out.println("W");
						//System.out.println("클라이언트로 전송 크기 : "+sc.write(b)); //이런방식으로 실행할경우 select이벤트가 계속 걸려서 에러가 발생하는 경우가 있다 주의
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					key.cancel();
					try {
						key.channel().close();
					}
					catch (IOException cex) {}
				}
			}
		}
	}
}

