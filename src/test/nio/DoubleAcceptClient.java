package test.nio;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/*
 * DoubleAccept Ŭ������ Ŭ���̾�Ʈ
 */
public class DoubleAcceptClient {
	public static void main(String[] args) throws Exception {
		SocketChannel sc = SocketChannel.open();
		sc.connect(new InetSocketAddress("127.0.0.1",6666));
		sc.close();
	}
}
