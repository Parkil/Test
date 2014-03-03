package test.nio;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/*
 * 2번 Accept가 수행된 서버
 * 수행시 첫번째 응답은 수행하시만 그 이후의 접속은 수행되지 않는다.
 * 이 예제는 테스트용으로 작성하였으나 Selector에서 key조작을 잘못할 경우 동일한 증상이 발생할 수 있다. 주의할것
 */
public class DoubleAccept {
	public static void main(String[] args) throws Exception{
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress("127.0.0.1",6666));
		
		SocketChannel sc = ssc.accept();
		System.out.println(sc);
		ssc.accept(); //accept가 중복 실행됨
	}
}
