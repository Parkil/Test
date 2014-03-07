package test.temp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import test.util.ByteBufferUtil;

public class Client {
	public static void main(String[] args) throws Exception {
		SocketChannel sc = SocketChannel.open();
		sc.connect(new InetSocketAddress("127.0.0.1",6000));
		
		sc.write(ByteBufferUtil.str_to_bb("Echo Message111"));
		
		ByteBuffer temp = ByteBuffer.allocateDirect(1000);
		
		sc.read(temp);
		temp.flip();
		System.out.println("Echo : "+ByteBufferUtil.bb_to_str(temp));
		
		sc.close();
	}
}
