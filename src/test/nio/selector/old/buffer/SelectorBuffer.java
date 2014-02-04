package test.nio.selector.old.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import test.util.ByteBufferUtil;
/*
 * 데이터 전송시 Header버퍼를 관리
 *
 * Buffer구조
 * byte - is_header(127)현재 스트림이 header인지 확인(127 로 고정)
 * byte - flag(0:최초 , 1:진행중, 2:완료)(예비)
 * byte - type(0:메시지, 1:파일)
 * long - total_size(message or file의 총용량)
 * long - read_size(수신측에서 읽어들인 용량)(예비)
 * long - write_size(송신측에서 보낸 용량)(예비)
 */
public class SelectorBuffer {
	private ByteBuffer[] buff_arr; //0이 buff_arr[0] 1이 body

	private byte flag;
	private byte type;
	private long total_size;
	private long read_size;
	private long write_size;

	private ByteBuffer reserve = ByteBuffer.allocateDirect(100000); //testcode
	/**
	 * 생성자
	 * Buffer구조
	 * byte - flag(0:최초 , 1:진행중, 2:완료)
	 * byte - type(0:메시지, 1:파일)
	 * long - total_size(message or file의 총용량)
	 * long - read_size(수신측에서 읽어들인 용량)
	 * long - write_size(송신측에서 보낸 용량) 현재는 예비로 돌림
	 */
	public SelectorBuffer() {
		ByteBuffer header 	 = ByteBuffer.allocateDirect(27);
		ByteBuffer body   	 = ByteBuffer.allocateDirect(50000);

		buff_arr = new ByteBuffer[2];
		buff_arr[0] = header;
		buff_arr[1] = body;
	}

	/**header 버퍼의 전체크기를 반환한다.
	 * @return header버퍼의 크기
	 */
	public int header_size() {
		return buff_arr[0].capacity();
	}

	/**body 버퍼의 전체크기를 반환한다.
	 * @return body버퍼의 크기
	 */
	public int body_size() {
		return buff_arr[1].capacity();
	}

	/**Header Buffer에 buffer를 입력 입력후에는 position을 0으로 변경한다.
	 * @param pos buffer를 입력할 위치
	 * @param buf 입력할 버퍼
	 */
	public void putHeader(ByteBuffer buf) {
		buff_arr[0].clear();
		buff_arr[0].put(buf);
		buff_arr[0].flip();
	}

	/**Body Buffer에 buffer를 입력 입력후에는 입력한 데이터의 끝을 limit으로 설정하고 position을 0으로 변경한다.
	 * @param pos buffer를 입력할 위치
	 * @param buf 입력할 버퍼
	 */
	public void putBody(int pos, ByteBuffer buf) {
		buff_arr[1].position(pos);
		buff_arr[1].put(buf);
		buff_arr[1].limit(buff_arr[1].position());
		buff_arr[1].position(0);
	}

	/**
	 * header,body버퍼를 clear한다.
	 */
	public void clearAll() {
		buff_arr[0].clear();
		buff_arr[1].clear();
	}

	/** SocketChannel에서 값을 읽어들임 읽어들인 값에 header정보가 포함될 경우 header 버퍼에 값을 입력한다.
	 * @param sc 데이터를 읽어들일 SocketChannel
	 * @return SocketChannel에서 읽어들인 양
	 * @throws IOException SocketChannel에서 예외발생시
	 */
	public long read(SocketChannel sc) throws IOException{
		long read_size = 0;
		buff_arr[1].clear();

		read_size = sc.read(buff_arr[1]);

		buff_arr[1].flip();

		if(buff_arr[1].limit() == 0) {
			return -1;
		}

		byte is_header = buff_arr[1].get();
		int limit = buff_arr[1].limit();

		//header정보일 경우 header 버퍼에 값을 입력
		if(is_header == (byte)127) {
			buff_arr[1].limit(27);
			buff_arr[1].position(0);

			buff_arr[0].clear();
			buff_arr[0].put(buff_arr[1].slice());
			buff_arr[0].flip();

			buff_arr[1].position(27);
			buff_arr[1].limit(limit);
			reserve.put(buff_arr[1].slice());
			reserve.flip();

			buff_arr[1].clear();
			buff_arr[1].put(reserve);
			buff_arr[1].flip();
		}else {
			buff_arr[1].position(0);
			buff_arr[1].limit(limit);
		}

		return read_size;
	}



	/**SocketChannel을 이용하여 버퍼의 데이터를 전송
	 * @param sc 데이터를 전송하는데 이용할 SocketChannel
	 * @return  SocketChannel로 전송한 양
	 * @throws IOException SocketChannel에서 예외발생시
	 */
	public long write(SocketChannel sc) throws IOException {
		long write_size = 0;
		write_size = sc.write(buff_arr);

		return write_size;
	}

	/**외부 버퍼를 SocketChannel을 이용하여 전송 header버퍼는 메소드 호출전에 설정한 값을 그대로 전송하며 body버퍼에 외부버퍼가 저장되지는 않는다.
	 * @param sc
	 * @param org
	 * @return
	 * @throws IOException
	 */
	public long write(SocketChannel sc, ByteBuffer org) throws IOException {
		buff_arr[0].position(0);
		sc.write(buff_arr[0]); //Header정보 전송
		org.position(0);
		return sc.write(org);
	}

	/**body버퍼의 내용을 문자열로 변환하여 반환
	 * @param charset 변환할 문자열의 character set
	 * @return 변환된 문자열
	 */
	public String getBodyContent(String charset) {
		buff_arr[1].position(0);
		ByteBufferUtil.setEncoding(charset);
		return ByteBufferUtil.bb_to_str(buff_arr[1]);
	}

	/**body버퍼의 내용을 파일에 저장
	 * @param path 파일경로
	 * @param is_append 기존파일뒤에 새로운내용을 붙여쓸지 결정
	 * @return 파일에 쓴 용량
	 */
	public long writeBodyContent(String path, boolean is_append) {
		//body버퍼의 limit가 0일경우 0을 반환
		if(buff_arr[1].limit() == 0) {
			return 0L;
		}

		RandomAccessFile raf = null;
		FileChannel		 fc  = null;
		MappedByteBuffer mbb = null;

		try {
			raf	= new RandomAccessFile(path, "rw");
			fc	= raf.getChannel();

			long start_idx = 0;

			//is_append모드가 true일경우 기존파일의 마지막부터 내용을 입력
			if(is_append) {
				start_idx = fc.size();
			}

			mbb = fc.map(FileChannel.MapMode.READ_WRITE, start_idx, buff_arr[1].limit());
			buff_arr[1].position(0);

			mbb.put(buff_arr[1]);
			mbb.flip();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(fc  != null) {
					fc.close();
				}
				if(raf != null) {
					raf.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mbb = null;
		}
		return buff_arr[1].limit();
	}

	/**인자로 주어진 버퍼에 header,body버퍼의 값을 입력한다.
	 * @param dest header,body버퍼의 값을 입력할 대상버퍼
	 */
	public void setBuffer(ByteBuffer dest) {
		dest.put(buff_arr[0]);
		dest.put(buff_arr[1]);
	}

	/**header 버퍼를 주어진 값으로 초기화 write_size/read_size는 0으로 초기화하고 초기화후에 setWriteSize/setReadSize를 이용하여 값을 설정한다.
	 * @param flag flag(0:최초 , 1:진행중, 2:완료)
	 * @param type type(0:메시지, 1:파일)
	 * @param total_size total_size(message or file의 총용량)
	 */
	public void initHeader(byte flag, byte type ,long total_size) {
		buff_arr[0].clear();
		buff_arr[0].put((byte)127);
		buff_arr[0].put(flag);
		buff_arr[0].put(type);
		buff_arr[0].putLong(total_size);
		buff_arr[0].putLong(0L);
		buff_arr[0].putLong(0L);
		buff_arr[0].flip();
	}

	/**header 버퍼에 write_size값을 입력 입력후에는 position을 0으로 설정
	 * @param write_size write_size(송신측에서 보낸 용량)
	 */
	public void setWriteSize(long write_size) {
		buff_arr[0].position(18);
		buff_arr[0].putLong(write_size);
		buff_arr[0].position(0);
	}

	/**header 버퍼에 read_size값을 입력 입력후에는 position을 0으로 설정
	 * @param read_size read_size(수신측에서 읽어들인 용량)
	 */
	public void setReadSize(long read_size) {
		buff_arr[0].position(10);
		buff_arr[0].putLong(read_size);
		buff_arr[0].position(0);
	}

	/**header에서 flag의 값을 반환한다.
	 * @return flag값
	 */
	public byte getFlag() {
		buff_arr[0].position(1);
		flag = buff_arr[0].get();
		buff_arr[0].position(0);
		return flag;
	}

	/**header에서 read_size의 값을 반환한다.
	 * @return read_size값
	 */
	public long getReadSize() {
		buff_arr[0].position(11);
		read_size = buff_arr[0].getLong();
		buff_arr[0].position(0);
		return read_size;
	}

	/**header에서 total_size의 값을 반환한다.
	 * @return total_size값
	 */
	public long getTotalSize() {
		buff_arr[0].position(3);
		total_size = buff_arr[0].getLong();
		buff_arr[0].position(0);
		return total_size;
	}

	/**header에서 type의 값을 반환한다.
	 * @return type값
	 */
	public byte getType() {
		buff_arr[0].position(2);
		type = buff_arr[0].get();
		buff_arr[0].position(0);
		return type;
	}

	/**header에서 write_size의 값을 반환한다.
	 * @return write_size값
	 */
	public long getWriteSize() {
		buff_arr[0].position(19);
		write_size = buff_arr[0].getLong();
		buff_arr[0].position(0);
		return write_size;
	}

	/**header여부를 반환
	 * @return header 여부 127이면 header 그외의 경우 header가 아님
	 */
	public byte isHeader() {
		buff_arr[0].position(0);
		byte is_header = buff_arr[0].get();
		buff_arr[0].position(0);

		return is_header;
	}
}
