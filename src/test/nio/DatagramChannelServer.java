package test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import test.util.ByteBufferUtil;

/*
 * DatagramChannel(UDP) 서버 예제
 * UDP는 데이터의 전송순서나 도착을 보증하지 않으므로 이를 프로그램에서 보장해야 한다.
 * 그 방법에 대해서는 나중에 찾아봐야 함
 */
public class DatagramChannelServer {
	private DatagramChannel dgc;
	private final ByteBuffer read_buffer = ByteBuffer.allocateDirect(100000);

	public DatagramChannelServer(String ip,int port) {
		try {
			dgc = DatagramChannel.open();
			dgc.socket().bind(new InetSocketAddress(ip,port));
			dgc.configureBlocking(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int send(String destip,int destport,String message) {
		int sendbyte = 0;
		try {
			sendbyte = dgc.send(ByteBufferUtil.str_to_bb(message),new InetSocketAddress(destip,destport));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendbyte;
	}

	public String read() {
		read_buffer.clear();
		try {
			dgc.receive(read_buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		read_buffer.flip();
		System.out.println(read_buffer.toString());

		return ByteBufferUtil.bb_to_str(read_buffer);
	}


	public static void main(String[] args) throws Exception{
		DatagramChannelServer a = new DatagramChannelServer("127.0.0.1",10000);
		ByteBufferUtil.setEncoding("utf-8");
		while(true) {
			a.read_buffer.clear();
			SocketAddress addr = a.dgc.receive(a.read_buffer);

			if(addr == null) {
				System.out.println("Wait Connection...");
				Thread.sleep(1000);
			}else {
				System.out.println("Incoming Connection : "+addr.toString());
				a.read_buffer.flip();
				System.out.println("Incoming Message : "+ByteBufferUtil.bb_to_str(a.read_buffer));

				a.dgc.connect(addr);
				a.dgc.write(a.read_buffer);
				a.dgc.disconnect(); //이거 빼먹으면 Client가 종료되고 다시 접속할 경우 Hang이 걸림
			}
		}
	}
}
