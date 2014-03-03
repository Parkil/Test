package test.nio;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/*
 * 2�� Accept�� ����� ����
 * ����� ù��° ������ �����Ͻø� �� ������ ������ ������� �ʴ´�.
 * �� ������ �׽�Ʈ������ �ۼ��Ͽ����� Selector���� key������ �߸��� ��� ������ ������ �߻��� �� �ִ�. �����Ұ�
 */
public class DoubleAccept {
	public static void main(String[] args) throws Exception{
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress("127.0.0.1",6666));
		
		SocketChannel sc = ssc.accept();
		System.out.println(sc);
		ssc.accept(); //accept�� �ߺ� �����
	}
}
