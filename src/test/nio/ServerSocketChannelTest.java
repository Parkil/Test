package test.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import test.util.ByteBufferUtil;

public class ServerSocketChannelTest implements Runnable{
	private ServerSocketChannel ssc = null;
	private ByteBuffer file_name = ByteBuffer.allocateDirect(1000); //Client�κ��� ������ �����ϴ� ���� 
	
	/**
	 * @param serverport ���� ��Ʈ 
	 */
	public ServerSocketChannelTest(int serverport) {
		try {
			ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(serverport));
			ssc.configureBlocking(false); //non-blocking ó�� 
			
			new Thread(this).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		SocketChannel sc = null;
		FileOutputStream fos = null;
		FileChannel out = null;
		
		while(true) {
			try {
				sc = ssc.accept();
				if(sc == null) { //������ ������ 0.5�ʵ��� sleepó��
					//System.out.println("waiting connection "+ssc.isBlocking());
					Thread.sleep(500); 
				}else { //Client�� �ø� ������ ����ó��
					ByteBuffer connect_message = ByteBufferUtil.str_to_bb("FileServer Connected");
					sc.write(connect_message);
					
					System.out.println("Connected by Client(IP : "+sc.socket().getRemoteSocketAddress()+")");
					
					sc.read(file_name);
					file_name.flip();
					
					fos = new FileOutputStream("111.test");
					out = fos.getChannel();
					
					out.transferFrom(sc, 0, Long.MAX_VALUE); //������ ������ ����
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if(fos != null) fos.close();
					if(out != null) out.close();
					if(sc != null) sc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		new ServerSocketChannelTest(5000);
	}
}
