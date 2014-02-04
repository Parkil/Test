package test.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import test.util.ByteBufferUtil;
import test.util.FileUtil;

public class SocketChannelTest {
	private SocketChannel sc = null;
	private ByteBuffer header_buffer = ByteBuffer.allocateDirect(40);
	
	public SocketChannelTest(String ip,int serverport) {
		try {
			sc = SocketChannel.open();
			sc.connect(new InetSocketAddress(ip,serverport));
			sc.configureBlocking(true);
			//sc.socket().setSendBufferSize(1520201);
			//sc.socket().setReceiveBufferSize(1520201);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean tranfserFile(String path) {
		boolean result = false;
		File file = new File(path);
		
		if(!file.exists()) { //�ش��ο� ������ �������� ������� false ��ȯ
			result = false;
		}
		
		try {
			ByteBuffer receive_message = ByteBuffer.allocateDirect(100);
			sc.read(receive_message);
			receive_message.flip();
			
			System.out.println(ByteBufferUtil.bb_to_str(receive_message));
			
			sc.write(ByteBufferUtil.str_to_bb(file.getName()));
			
			FileInputStream fis = new FileInputStream(file);
			FileChannel in = fis.getChannel();
			
			System.out.println("sendsize : "+in.size());
			
			in.transferTo(0, in.size(), sc);
			
			result = true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			result = false;
		} finally {
			try {
				sc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public int writeMessage(String message) {
		int  writebyte  = 0;
		long total_size = 0;
		long total_server_read_size = 0L;
		try {
			ByteBuffer send_buffer = ByteBufferUtil.str_to_bb(message);
			
			total_size = send_buffer.limit();
			
			header_buffer.clear();
			header_buffer.putLong(total_size);
			header_buffer.flip();
			
			int header = sc.write(header_buffer); //��뷮 �޽����� ������� loop�� ������ ���� �޽���ũ�⸦ ó���� �ٿ��� ������.
			int body   = sc.write(send_buffer);
			writebyte = header+body;
			
			header_buffer.clear();
			
			int read_size = 0;
			
			header_buffer.clear();
			do {
				read_size = sc.read(header_buffer);
			} while(read_size == 0);
			
			header_buffer.flip();
			
			LongBuffer lb = header_buffer.asLongBuffer();
			long server_send_size = lb.get();
			
			total_server_read_size += server_send_size;
			System.out.println("�������� �о���� �뷮 : "+total_server_read_size+"/"+total_size);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			/*try {
				sc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		return writebyte;
	}
	
	
	public static void main(String[] args) throws Exception{
		SocketChannelTest test = new SocketChannelTest("127.0.0.1",1234);
		
		FileUtil util = new FileUtil();
		String content = util.readFile("d:/output(dk-rainbow).log");
		
		test.writeMessage(content);
	}
}
