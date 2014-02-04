package test.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import test.util.ByteBufferUtil;
/*
 * DatagramChannel(UDP) Client ����
 */
public class DatagramChannelClient {
	public static void main(String[] args) throws Exception {
		DatagramChannel dc = DatagramChannel.open();

		ByteBufferUtil.setEncoding("utf-8");
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1",10000);
		dc.connect(isa);
		dc.write(ByteBufferUtil.str_to_bb("12321312")); //connect�����Ŀ� ���ÿ� ������

		ByteBuffer read_buffer = ByteBuffer.allocateDirect(1000);
		dc.read(read_buffer);
		read_buffer.flip();
		System.out.printf("Echo Message : %s",ByteBufferUtil.bb_to_str(read_buffer));
		//dc.send(ByteBufferUtil.str_to_bb("apple1111"), isa); //connect �� write�� ���ÿ� ������ �� ����
		dc.close();
	}
}
