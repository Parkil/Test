package test.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import test.util.ByteBufferUtil;

public class SelClient {
	public static void main(String[] args) throws Exception {
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 50000);
		SocketChannel sc = SocketChannel.open(isa);
		sc.configureBlocking(false);

		ByteBuffer ss = ByteBufferUtil.str_to_bb("Send Message");
		sc.write(ss);
		sc.close();
	}
}
