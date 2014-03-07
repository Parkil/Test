package test.temp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import test.util.ByteBufferUtil;

public class Server1 {
	public static void main(String[] args) throws Exception{
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress("127.0.0.1",6000));
		ssc.configureBlocking(false);
		
		Selector sel = Selector.open();
		ssc.register(sel, SelectionKey.OP_ACCEPT);
		
		while(true) {
			int cnt = sel.selectNow();
			
			if(cnt == 0) {
				continue;
			}
			
			System.out.println("cnt : "+cnt);
			
			Set<SelectionKey> keyset = sel.selectedKeys();
			Iterator<SelectionKey> keyiter = keyset.iterator();

			while(keyiter.hasNext()) {
				SelectionKey key = keyiter.next();
				if(key.isAcceptable()) {
					ServerSocketChannel key_ssc = (ServerSocketChannel)key.channel();
					SocketChannel key_sc = key_ssc.accept();
					System.out.println("Accept");
					key_sc.configureBlocking(false);
					System.out.println("Connected By "+key_sc);
					
					key_sc.register(sel, SelectionKey.OP_READ);
					keyiter.remove();
				}else if(key.isReadable()) {
					System.out.println("Read By Client");
					ByteBuffer temp = ByteBuffer.allocateDirect(1000);
					SocketChannel sc = (SocketChannel)key.channel();
					sc.read(temp);
					temp.flip();

					System.out.println("read data : "+ByteBufferUtil.bb_to_str(temp));
					
					int chk_cnt = sc.read(temp);
					//Client로부터 온 데이터를 다 읽고 Client접속이 끊어지지 않은경우
					if(chk_cnt == 0) {
						key.interestOps(SelectionKey.OP_WRITE);
						key.attach(temp);
					}else if(chk_cnt == -1) { //Client로부터 온 데이터를 다 읽고 Client접속이 끊어진 경우
						sc.close(); //key와 연관된 socket을 close하면 관련 key도 자동으로 cancel처리 된다.
						//key.cancel();
					}
				}else if(key.isWritable()) {
					System.out.println("Send Echo Data To Client");
					SocketChannel sc = (SocketChannel)key.channel();
					sc.write((ByteBuffer)key.attachment());
					sc.close(); //key와 연관된 socket을 close하면 관련 key도 자동으로 cancel처리 된다.
					System.out.println("==============================================");
				}
			}
		}
	}
}
