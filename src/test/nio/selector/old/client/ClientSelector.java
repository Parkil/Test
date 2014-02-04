package test.nio.selector.old.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import test.nio.selector.old.buffer.SelectorBuffer;
import test.util.ByteBufferUtil;
import test.util.FileUtil;

public class ClientSelector {
	public	Selector 	sel;

	private boolean readflag  = false;
	private boolean writeflag = true;

	private final Logger log = Logger.getLogger(getClass());

	public ClientSelector(String url, int port) {
		try {
			sel = Selector.open();
			SocketChannel sc	= SocketChannel.open();
			sc.connect(new InetSocketAddress(url, port));
			sc.configureBlocking(false);

			sc.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connect() {
		//while(true) {
		for(int i = 0 ; i< 5; i++) {
			try {
				System.out.println("select : "+sel.select());
				Set<SelectionKey>	  selkeys = sel.keys();
				Iterator<SelectionKey> itkeys = selkeys.iterator();

				while(itkeys.hasNext()) {
					SelectionKey key = itkeys.next();

					if(key.isReadable() && readflag == true) {
						System.out.println("==isReadable==");
						ByteBuffer x = ByteBuffer.allocate(10000);
						SocketChannel sc = (SocketChannel)key.channel();
						System.out.println("읽어들인 크기 : "+sc.read(x));
						x.flip();

						System.out.println("읽어들인 메시지 : "+ByteBufferUtil.bb_to_str(x));
						readflag = false;
						//						SocketChannel sc = (SocketChannel)key.channel();
						//						log.info("Connection established");
					}else if (key.isWritable() && writeflag == true) {
						System.out.println("==isWriteable==");
						SocketChannel sc = (SocketChannel)key.channel();
						FileUtil util = new FileUtil();
						String con = util.readFile("d:/기타", "euc-kr");
						ByteBuffer temp = ByteBufferUtil.str_to_bb(con);

						SelectorBuffer sb = new SelectorBuffer();
						sb.initHeader((byte)0, (byte)1, temp.limit());
						System.out.println("write size : "+sb.write(sc, temp));
						/*
						ByteBuffer x = ByteBuffer.allocate(10000);

						System.out.println("읽어들인 크기 : "+sc.read(x));
						x.flip();

						System.out.println("읽어들인 메시지 : "+ByteBufferUtil.bb_to_str(x));*/
						writeflag = false;
						readflag  = true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//}
	}

	public void close() {
		try {
			Set<SelectionKey>	  selkeys = sel.keys();
			Iterator<SelectionKey> itkeys = selkeys.iterator();

			while(itkeys.hasNext()) {
				SelectionKey key = itkeys.next();
				SocketChannel sc = (SocketChannel)key.channel();
				log.info("SocketChannel [HashCode : "+sc.hashCode()+"] Closed");
				sc.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendFile(String path) {
		/*FileUtil util = new FileUtil();
		String con = util.readFile("d:/바탕화면 문서/기타", "euc-kr");
		ByteBuffer temp = ByteBufferUtil.str_to_bb(con);

		SelectorBuffer sb = new SelectorBuffer();
		sb.initHeader((byte)0, (byte)1, temp.limit());
		System.out.println("write size : "+sb.write(sc, temp));*/
	}

	public static void main(String[] args) throws Exception{
		ClientSelector cs = new ClientSelector("127.0.0.1",1234);
		cs.connect();
		//cs.close();
		/*FileUtil util = new FileUtil();
		String con = util.readFile("d:/바탕화면 문서/기타", "euc-kr");
		ByteBuffer temp = ByteBufferUtil.str_to_bb(con);

		int total_size = temp.limit();
		//		int buffer_size = 0;
		//		int position = 0;
		//		int limit = 0;
		byte flag = 0;
		SocketChannel sc = SocketChannel.open();
		sc.connect(new InetSocketAddress("127.0.0.1",1234));

		SelectorBuffer sb = new SelectorBuffer();

		//Header + Data로 한번에 데이터를 보내는 방식(Header가 최초 한번만 붙는다)
		sb.initHeader(flag, (byte)1, total_size);
		System.out.println("write size : "+sb.write(sc, temp));

		/*
		 * 데이터를 Header + Data 로 일정크기만큼 분할하여 차례대로 보내는 방식(Header가 Data조각마다 붙는다.)
		buffer_size = 30000;

		sb.initHeader(flag, (byte)1, total_size);
		sb.write(sc);

		while(limit < total_size) {
			limit = (position + buffer_size) > total_size ? total_size : position + buffer_size;
			temp.position(position);
			temp.limit(limit);

			//position = 0 (최초전송시)
			//limit == total_size(마지막 전송시)
			//limit != total_size(전송중)
			if(position == 0) {
				flag = 0;
			} else {
				flag = (limit == total_size) ? (byte)2 : (byte)1;
			}
			System.out.println("flag : "+flag);

			sb.setWriteSize(30026);
			sb.putBody(0, temp.slice());

			System.out.println("Write Size : "+sb.write(sc));

			position = (position + buffer_size) > total_size ? position : position + buffer_size;

		}*/
		/*
		sc.close();
		System.out.println("close");*/
	}
}

