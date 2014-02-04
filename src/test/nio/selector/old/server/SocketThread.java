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

	//	private ByteBuffer buffer = ByteBuffer.allocateDirect(1000000); //Ŭ���̾�Ʈ�κ��� �о���� Data�� �����ϴ� Buffer

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
					 * Ŭ���̾�Ʈ���� SocketChannel�� close���� �ʾ����� '���� ������ ���� ȣ��Ʈ�� ���� ������ ������ϴ�' ���ܹ߻�
					 * Ŭ���̾�Ʈ���� SocketChannel�� close�ϸ� ���� ��� ����
					 */
					sc = (SocketChannel)key.channel();

					try {
						while(read_size != -1) {
							sb.clearAll();
							read_size 		=  sb.read(sc);

							//read_size�� 0(�ƹ��͵� ���� ����) �̰ų� -1(��Ʈ�� ��)�� ��� loop�� �ٽ� ���� �Ѵ�.
							if((read_size == 0 || read_size == -1)) {
								continue;
							}

							/*
							 * Header + �����ͷ� �޴� ���(Header�� �ѹ��� ǥ�õ�)
							 */
							total_read_size  += read_size;
							long total_size   = Long.MAX_VALUE; //�ӽ÷� long�� maxvalue�� ����
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
									is_header = false; //header������ �ѹ��� ����ϵ��� ����
								}else {
									total_read_size += sb.read(sc);
								}
								System.out.println("1");
								sb.writeBodyContent("d:/refe", false);
								System.out.println("2");
							}
							/* �����͸� Header + Data �� ����ũ�⸸ŭ �����Ͽ� ���ʴ�� �������� �޴� ���(Header�� Data�������� �ٴ´�.)
							//���� ������ �ε��
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