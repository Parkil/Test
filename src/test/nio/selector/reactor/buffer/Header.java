package test.nio.selector.reactor.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import test.util.ByteBufferUtil;
/*
 * 데이터 전송시 Header버퍼를 관리
 *
 * Buffer구조
 * byte	   - flag(0:최초 , 1:진행중, 2:완료)(예비)
 * byte	   - type(0:메시지, 1:파일전송(clinet->server) 2:파일전송(server->client))
 * long	   - total_size(message or file의 총용량)
 * long	   - read_size(수신측에서 읽어들인 용량)(예비)
 * long	   - write_size(송신측에서 보낸 용량)(예비)
 * 100byte - 파일명(파일전송시에만 사용)
 * 50byte  - 파일hash(파일전송시에만 사용)
 */
public class Header {
	private byte flag;
	private byte type;
	private long total_size;
	private long read_size;
	private long write_size;

	private final ByteBuffer header;

	public Header() {
		header 	 = ByteBuffer.allocateDirect(176);
	}

	/**header 버퍼의 전체크기를 반환한다.
	 * @return header버퍼의 크기
	 */
	public int header_size() {
		return header.capacity();
	}

	/** SocketChannel에서 값을 읽어서 header버퍼에 저장
	 * @param sc 데이터를 읽어들일 SocketChannel
	 * @return SocketChannel에서 읽어들인 양
	 * @throws IOException SocketChannel에서 예외발생시
	 */
	public long read(SocketChannel sc) throws IOException{
		long read_size = 0;

		header.clear();
		read_size = sc.read(header);
		header.flip();

		return read_size;
	}

	/**SocketChannel을 이용하여 header 버퍼의 데이터를 전송
	 * @param sc 데이터를 전송하는데 이용할 SocketChannel
	 * @return  SocketChannel로 전송한 양
	 * @throws IOException SocketChannel에서 예외발생시
	 */
	public long write(SocketChannel sc) throws IOException {
		long write_size = 0;
		write_size = sc.write(header);

		return write_size;
	}


	/**header 버퍼를 주어진 값으로 초기화 write_size/read_size는 0으로 초기화하고 초기화후에 setWriteSize/setReadSize를 이용하여 값을 설정한다.
	 * @param flag flag(0:최초 , 1:진행중, 2:완료)
	 * @param type type(0:메시지, 1:파일)
	 * @param total_size total_size(message or file의 총용량)
	 */
	public void initHeader(byte flag, byte type ,long total_size) {
		header.clear();
		header.put(flag);
		header.put(type);
		header.putLong(total_size);
		header.putLong(0L);
		header.putLong(0L);
		header.position(0);
		header.limit(176); //파일 이름,hash가 없어도 header 버퍼의 크기를 유지해야 하기때문에 flip()이 아닌 limit()로 설정한다.
	}

	/**Header 버퍼에 파일Hash를 지정
	 * @param 파일 Hash
	 */
	public void setFileHash(String hash) {
		ByteBuffer temp = ByteBufferUtil.str_to_bb(hash);

		if(temp.limit() > 50) {
			System.out.println("해쉬값이 너무 큼");
			return;
		}

		header.limit(176);
		header.position(128);
		header.put(temp);
		header.position(0);
	}

	/**Header 버퍼에 저장된 파일Hash를 반환
	 * @return Header 버퍼에 저장된 파일Hash
	 */
	public String getFileHash() {
		header.limit(176);
		header.position(128);
		ByteBufferUtil.setEncoding("utf-8");
		String ret = ByteBufferUtil.bb_to_str(header);
		header.position(0);

		return ret;
	}

	/**Header 버퍼에 파일명을 지정
	 * @param filename 파일명
	 */
	public void setFileName(String filename) {
		ByteBuffer temp = ByteBufferUtil.str_to_bb(filename);

		if(temp.limit() > 100) {
			System.out.println("파일 이름이 버퍼크기를 벗어남");
			return;
		}

		header.limit(176);
		header.position(27);
		header.put(temp);
		header.position(0);
	}

	/**Header 버퍼에 저장된 파일이름을 반환
	 * @return Header 버퍼에 저장된 파일이름
	 */
	public String getFileName() {
		header.position(27);
		header.limit(126);
		ByteBufferUtil.setEncoding("utf-8");
		String ret = ByteBufferUtil.bb_to_str(header);
		header.position(0);

		return ret;
	}

	/**header 버퍼에 write_size값을 입력 입력후에는 position을 0으로 설정
	 * @param write_size write_size(송신측에서 보낸 용량)
	 */
	public void setWriteSize(long write_size) {
		header.position(17);
		header.putLong(write_size);
		header.position(0);
	}

	/**header 버퍼에 read_size값을 입력 입력후에는 position을 0으로 설정
	 * @param read_size read_size(수신측에서 읽어들인 용량)
	 */
	public void setReadSize(long read_size) {
		header.position(9);
		header.putLong(read_size);
		header.position(0);
	}

	/**header에서 flag의 값을 반환한다. 반환후 Header Position을 0으로 조정한다.
	 * @return flag값
	 */
	public byte getFlag() {
		header.position(0);
		flag = header.get();
		header.position(0);
		return flag;
	}

	/**header에서 read_size의 값을 반환한다. 반환후 Header Position을 0으로 조정한다.
	 * @return read_size값
	 */
	public long getReadSize() {
		header.position(10);
		read_size = header.getLong();
		header.position(0);
		return read_size;
	}

	/**header에서 total_size의 값을 반환한다. 반환후 Header Position을 0으로 조정한다.
	 * @return total_size값
	 */
	public long getTotalSize() {
		header.position(2);
		total_size = header.getLong();
		header.position(0);
		return total_size;
	}

	/**header에서 type의 값을 반환한다. 반환후 Header Position을 0으로 조정한다.
	 * @return type값
	 */
	public byte getType() {
		header.position(1);
		type = header.get();
		header.position(0);
		return type;
	}

	/**header에서 write_size의 값을 반환한다. 반환후 Header Position을 0으로 조정한다.
	 * @return write_size값
	 */
	public long getWriteSize() {
		header.position(18);
		write_size = header.getLong();
		header.position(0);
		return write_size;
	}
}
