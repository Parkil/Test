package test.nio.selector.old.server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import test.nio.selector.old.buffer.SelectorBuffer;
import test.util.ByteBufferUtil;

class SocketThread implements Runnable {
	private final Logger log = Logger.getLogger(getClass());

	private final ThreadPool pool;

	private boolean is_run = true;

	private Selector	  sel;
	private SocketChannel  sc;
	private SelectorBuffer sb;

	//	private ByteBuffer buffer = ByteBuffer.allocateDirect(1000000); //클라이언트로부터 읽어들인 Data를 저장하는 Buffer

	private long read_size		 = 0L;
	private long total_read_size = 0L;

	//	private long block_size = 0L;
	//	private int block_idx = 0;

	SocketThread(ThreadPool tp) {
		pool = tp;
	}

	void start(SelectionKey key) {
		try {
			sel = Selector.open();
			key.channel().register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			sb = new SelectorBuffer();
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}

		new Thread(this).start();
		log.info("SocketChannel Thread Started");
	}

	@Override
	public void run() {
		while(is_run) {
			try {
				sel.select();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			// TODO Auto-generated method stub
			Set<SelectionKey> 	  keyset = sel.selectedKeys();
			Iterator<SelectionKey> keyit = keyset.iterator();

			while(keyit.hasNext()) {
				SelectionKey key = keyit.next();
				keyit.remove();
				if(key.isReadable()) {
					/*
					 * 클라이언트에서 SocketChannel을 close하지 않았으면 '현재 연결은 원격 호스트에 의해 강제로 끊겼습니다' 예외발생
					 * 클라이언트에서 SocketChannel을 close하면 값을 계속 읽음
					 */
					sc = (SocketChannel)key.channel();

					try {
						while(read_size != -1) {
							sb.clearAll();
							read_size 		=  sb.read(sc);

							//read_size가 0(아무것도 읽지 않음) 이거나 -1(스트림 끝)일 경우 loop를 다시 돌게 한다.
							if((read_size == 0 || read_size == -1)) {
								continue;
							}

							/*
							 * Header + 데이터로 받는 방식(Header가 한번만 표시됨)
							 */
							total_read_size  += read_size;
							long total_size   = Long.MAX_VALUE; //임시로 long의 maxvalue를 지정
							boolean is_header = (sb.isHeader() == (byte)127) ? true : false;

							while(total_read_size <= total_size) {
								if(is_header) {
									total_size = sb.getTotalSize();
									System.out.println("=========HEADER=================");
									System.out.println("Flag : "+sb.getFlag());
									System.out.println("Type : "+sb.getType());
									System.out.println("Total Size : "+sb.getTotalSize());
									System.out.println("Read Size : "+sb.getReadSize());
									System.out.println("Write Size : "+sb.getWriteSize());
									System.out.println("================================");
									is_header = false; //header정보를 한번만 출력하도록 조정
								}else {
									total_read_size += sb.read(sc);
								}
								System.out.println("1");
								sb.writeBodyContent("d:/refe", false);
								System.out.println("2");
							}
							/* 데이터를 Header + Data 로 일정크기만큼 분할하여 차례대로 보냈을때 받는 방식(Header가 Data조각마다 붙는다.)
							//최초 데이터 로드시
							if(block_idx == 0) {
								block_size = sb.getWriteSize();
								System.out.println("Flag : "+sb.getFlag());
								System.out.println("Type : "+sb.getType());
								System.out.println("Total Size : "+sb.getTotalSize());
								System.out.println("Read Size : "+sb.getReadSize());
								System.out.println("Write Size : "+sb.getWriteSize());
								System.out.println("----------------CONTENT-------------------");
								System.out.println(sb.getBodyContent("utf-8"));
								System.out.println("----------------CONTENT END-------------------");
								block_idx++;
							}

							sb.setBuffer(buffer);
							if(buffer.position() > block_idx * block_size) {
								int temppos	  = buffer.position();
								int templimit = buffer.capacity();
								int pos	  = (int)(block_idx * block_size);
								int limit = pos+sb.header_size();
								buffer.position(pos);
								buffer.limit(limit);

								sb.putHeader(buffer.slice());

								System.out.println("Flag : "+sb.getFlag());
								System.out.println("Type : "+sb.getType());
								System.out.println("Total Size : "+sb.getTotalSize());
								System.out.println("Read Size : "+sb.getReadSize());
								System.out.println("Write Size : "+sb.getWriteSize());
								System.out.println("----------------CONTENT-------------------");
								System.out.println(sb.getBodyContent("utf-8"));
								System.out.println("----------------CONTENT END-------------------");
								buffer.limit(templimit);
								buffer.position(temppos);
								block_idx++;
							}*/
						}
						sc.write(ByteBufferUtil.str_to_bb("Exec Complete"));
						log.info("Client Disconnected");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						is_run = false;
					} /*finally {

						try {
							sc.close();
							is_run = false;
							log.info("Socket Closed");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}*/
				}

				/*if(key.isWritable()) {
					log.info("Writeable");
				}*/
			}
		}
		log.info("Thread Terminated[Thread HashCode : "+this.toString()+"]");
		pool.put(this);
		log.info("Thread Returned Pool");
	}
}