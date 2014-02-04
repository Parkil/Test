package test.nio.selector.reactor.clienttest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.TimerTask;

import test.util.ByteBufferUtil;

/*
 * 테스트 클래스 TimerTask를 이용하여 일정시간마다 서버에 접속하여 특정기능을 수행하도록 함
 */
public class ClientTask extends TimerTask {
	InetSocketAddress isa = new InetSocketAddress("127.0.0.1",7);
	ByteBuffer b = ByteBuffer.allocateDirect(10000);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SocketChannel sc = null;
		try {
			sc = SocketChannel.open();
			sc.connect(isa);
			b.clear();
			sc.read(b);
			b.flip();

			System.out.println("InComing Message : "+ByteBufferUtil.bb_to_str(b));

			b.clear();
			b.put(ByteBufferUtil.str_to_bb("444555666"));
			b.flip();

			sc.write(b);

			b.clear();
			sc.read(b);
			b.flip();

			System.out.println("Echo : "+ByteBufferUtil.bb_to_str(b));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				sc.close();
				System.out.println("Socket Closed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
