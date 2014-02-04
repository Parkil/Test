package test.test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SelServer {
	public static void main(String[] args) throws Exception {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ServerSocket sc = ssc.socket();
		ssc.configureBlocking(false);

		InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 50000);

		sc.bind(isa);

		Selector sel = Selector.open();

		ssc.register(sel, SelectionKey.OP_ACCEPT);

		while (true) {
			int n = sel.select();

			if (n == 0) {
				continue;
			}

			Iterator<SelectionKey> it = sel.selectedKeys().iterator();

			System.out.println("SelectionKeySet size : " + sel.selectedKeys().size());

			while (it.hasNext()) {
				SelectionKey key = it.next();

				/*
				 * Acceptable은 Accept 수행후 Iterator에서만 제거해 주면 중복실행이 방지된다.
				 */
				if (key.isAcceptable()) {
					ServerSocketChannel temp = (ServerSocketChannel) key.channel();
					SocketChannel temp_sc = temp.accept();
					System.out.println("111 : " + temp_sc);
					temp_sc.configureBlocking(false);

					// 해당 소켓 관심분야 등록 설령 지금 필요하지 않아도 관심분야에 등록이되면 모든 로직이 실행되기 때문에 관심분야는 1개씩만 등록할것
					temp_sc.register(sel, SelectionKey.OP_WRITE | SelectionKey.OP_READ);

					it.remove();
				}

				/*
				 * 클라이언트 접속여부와 상관없이 기존에 등록한 관심분야와 동일하면 해당 로직이 실행된다. 
				 * 클라이언트 접속이 끊어져도 Key가 유효한것으로 표시되기 때문에 서버에서 클라이언트 접속이 끊어졌는지 확인하고 해당 Socket을
				 * close하거나(close하면 소켓에 할당된 key가 cancel 됨) Key를 cancel 처리한다.
				 */
				if (key.isReadable()) {
					ByteBuffer sss = ByteBuffer.allocateDirect(1000);
					SocketChannel temp123 = (SocketChannel) key.channel();

					long read_size = temp123.read(sss);
					System.out.println("read Size : " + read_size);

					/*
					 * 클라이언트 소켓이 닫혔는지
					 */
					if (read_size == -1) {
						temp123.close();
					}

					System.out.println("is Readable Valid: " + key.isValid());
					it.remove();
				}
				

				if (key.isWritable()) {
					System.out.println("is Writeable : " + key.isValid());
					// it.remove(); // 실제 상태가 아닌 부분에서 it.remove를 호출하면 java.lang.IllegalStateException 이 발생함
				}
			}
		}
	}
}
