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
				 * Acceptable�� Accept ������ Iterator������ ������ �ָ� �ߺ������� �����ȴ�.
				 */
				if (key.isAcceptable()) {
					ServerSocketChannel temp = (ServerSocketChannel) key.channel();
					SocketChannel temp_sc = temp.accept();
					System.out.println("111 : " + temp_sc);
					temp_sc.configureBlocking(false);

					// �ش� ���� ���ɺо� ��� ���� ���� �ʿ����� �ʾƵ� ���ɺо߿� ����̵Ǹ� ��� ������ ����Ǳ� ������ ���ɺоߴ� 1������ ����Ұ�
					temp_sc.register(sel, SelectionKey.OP_WRITE | SelectionKey.OP_READ);

					it.remove();
				}

				/*
				 * Ŭ���̾�Ʈ ���ӿ��ο� ������� ������ ����� ���ɺо߿� �����ϸ� �ش� ������ ����ȴ�. 
				 * Ŭ���̾�Ʈ ������ �������� Key�� ��ȿ�Ѱ����� ǥ�õǱ� ������ �������� Ŭ���̾�Ʈ ������ ���������� Ȯ���ϰ� �ش� Socket��
				 * close�ϰų�(close�ϸ� ���Ͽ� �Ҵ�� key�� cancel ��) Key�� cancel ó���Ѵ�.
				 */
				if (key.isReadable()) {
					ByteBuffer sss = ByteBuffer.allocateDirect(1000);
					SocketChannel temp123 = (SocketChannel) key.channel();

					long read_size = temp123.read(sss);
					System.out.println("read Size : " + read_size);

					/*
					 * Ŭ���̾�Ʈ ������ ��������
					 */
					if (read_size == -1) {
						temp123.close();
					}

					System.out.println("is Readable Valid: " + key.isValid());
					it.remove();
				}
				

				if (key.isWritable()) {
					System.out.println("is Writeable : " + key.isValid());
					// it.remove(); // ���� ���°� �ƴ� �κп��� it.remove�� ȣ���ϸ� java.lang.IllegalStateException �� �߻���
				}
			}
		}
	}
}
