package test.nio.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import test.util.ByteBufferUtil;

public class Client {
	public static void main(String[] args) {
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1",7);
		ByteBuffer b = ByteBuffer.allocateDirect(10000);
		SocketChannel sc = null;
		try {
			sc = SocketChannel.open();
			sc.connect(isa);

			b.clear();
			b.put(ByteBufferUtil.str_to_bb("12345"));
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
