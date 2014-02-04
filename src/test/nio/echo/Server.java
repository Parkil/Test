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
			 * selector�� select��ɰ� Key Iterator�� ���� try~catch���� ó���� �ϰ� �Ǹ�
			 * ������ �߻��ϰų� ������ �Ǵ� ��찡 ����. ������ �����Ұ�
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
						//b.flip(); //flip�� ���⿡�� �����ϸ� ������ �߻��Ѵ�. ����
						//System.out.println("�о���� �� : "+ByteBufferUtil.bb_to_str(b));
					}

					if(key.isWritable()) {
						SocketChannel sc = (SocketChannel)key.channel();
						/*
						 * �̷������� �����ص� select�̺�Ʈ�� ��� �ɷ��� ������ �߻��ϴ� ��찡 �ִ� ����
							if(b.position() != 0) {
								b.flip();
							}*/
						b.flip();
						sc.write(b);
						System.out.println("W");
						//System.out.println("Ŭ���̾�Ʈ�� ���� ũ�� : "+sc.write(b)); //�̷�������� �����Ұ�� select�̺�Ʈ�� ��� �ɷ��� ������ �߻��ϴ� ��찡 �ִ� ����
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

