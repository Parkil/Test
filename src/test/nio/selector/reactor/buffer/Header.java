package test.nio.selector.reactor.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import test.util.ByteBufferUtil;
/*
 * ������ ���۽� Header���۸� ����
 *
 * Buffer����
 * byte	   - flag(0:���� , 1:������, 2:�Ϸ�)(����)
 * byte	   - type(0:�޽���, 1:��������(clinet->server) 2:��������(server->client))
 * long	   - total_size(message or file�� �ѿ뷮)
 * long	   - read_size(���������� �о���� �뷮)(����)
 * long	   - write_size(�۽������� ���� �뷮)(����)
 * 100byte - ���ϸ�(�������۽ÿ��� ���)
 * 50byte  - ����hash(�������۽ÿ��� ���)
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

	/**header ������ ��üũ�⸦ ��ȯ�Ѵ�.
	 * @return header������ ũ��
	 */
	public int header_size() {
		return header.capacity();
	}

	/** SocketChannel���� ���� �о header���ۿ� ����
	 * @param sc �����͸� �о���� SocketChannel
	 * @return SocketChannel���� �о���� ��
	 * @throws IOException SocketChannel���� ���ܹ߻���
	 */
	public long read(SocketChannel sc) throws IOException{
		long read_size = 0;

		header.clear();
		read_size = sc.read(header);
		header.flip();

		return read_size;
	}

	/**SocketChannel�� �̿��Ͽ� header ������ �����͸� ����
	 * @param sc �����͸� �����ϴµ� �̿��� SocketChannel
	 * @return  SocketChannel�� ������ ��
	 * @throws IOException SocketChannel���� ���ܹ߻���
	 */
	public long write(SocketChannel sc) throws IOException {
		long write_size = 0;
		write_size = sc.write(header);

		return write_size;
	}


	/**header ���۸� �־��� ������ �ʱ�ȭ write_size/read_size�� 0���� �ʱ�ȭ�ϰ� �ʱ�ȭ�Ŀ� setWriteSize/setReadSize�� �̿��Ͽ� ���� �����Ѵ�.
	 * @param flag flag(0:���� , 1:������, 2:�Ϸ�)
	 * @param type type(0:�޽���, 1:����)
	 * @param total_size total_size(message or file�� �ѿ뷮)
	 */
	public void initHeader(byte flag, byte type ,long total_size) {
		header.clear();
		header.put(flag);
		header.put(type);
		header.putLong(total_size);
		header.putLong(0L);
		header.putLong(0L);
		header.position(0);
		header.limit(176); //���� �̸�,hash�� ��� header ������ ũ�⸦ �����ؾ� �ϱ⶧���� flip()�� �ƴ� limit()�� �����Ѵ�.
	}

	/**Header ���ۿ� ����Hash�� ����
	 * @param ���� Hash
	 */
	public void setFileHash(String hash) {
		ByteBuffer temp = ByteBufferUtil.str_to_bb(hash);

		if(temp.limit() > 50) {
			System.out.println("�ؽ����� �ʹ� ŭ");
			return;
		}

		header.limit(176);
		header.position(128);
		header.put(temp);
		header.position(0);
	}

	/**Header ���ۿ� ����� ����Hash�� ��ȯ
	 * @return Header ���ۿ� ����� ����Hash
	 */
	public String getFileHash() {
		header.limit(176);
		header.position(128);
		ByteBufferUtil.setEncoding("utf-8");
		String ret = ByteBufferUtil.bb_to_str(header);
		header.position(0);

		return ret;
	}

	/**Header ���ۿ� ���ϸ��� ����
	 * @param filename ���ϸ�
	 */
	public void setFileName(String filename) {
		ByteBuffer temp = ByteBufferUtil.str_to_bb(filename);

		if(temp.limit() > 100) {
			System.out.println("���� �̸��� ����ũ�⸦ ���");
			return;
		}

		header.limit(176);
		header.position(27);
		header.put(temp);
		header.position(0);
	}

	/**Header ���ۿ� ����� �����̸��� ��ȯ
	 * @return Header ���ۿ� ����� �����̸�
	 */
	public String getFileName() {
		header.position(27);
		header.limit(126);
		ByteBufferUtil.setEncoding("utf-8");
		String ret = ByteBufferUtil.bb_to_str(header);
		header.position(0);

		return ret;
	}

	/**header ���ۿ� write_size���� �Է� �Է��Ŀ��� position�� 0���� ����
	 * @param write_size write_size(�۽������� ���� �뷮)
	 */
	public void setWriteSize(long write_size) {
		header.position(17);
		header.putLong(write_size);
		header.position(0);
	}

	/**header ���ۿ� read_size���� �Է� �Է��Ŀ��� position�� 0���� ����
	 * @param read_size read_size(���������� �о���� �뷮)
	 */
	public void setReadSize(long read_size) {
		header.position(9);
		header.putLong(read_size);
		header.position(0);
	}

	/**header���� flag�� ���� ��ȯ�Ѵ�. ��ȯ�� Header Position�� 0���� �����Ѵ�.
	 * @return flag��
	 */
	public byte getFlag() {
		header.position(0);
		flag = header.get();
		header.position(0);
		return flag;
	}

	/**header���� read_size�� ���� ��ȯ�Ѵ�. ��ȯ�� Header Position�� 0���� �����Ѵ�.
	 * @return read_size��
	 */
	public long getReadSize() {
		header.position(10);
		read_size = header.getLong();
		header.position(0);
		return read_size;
	}

	/**header���� total_size�� ���� ��ȯ�Ѵ�. ��ȯ�� Header Position�� 0���� �����Ѵ�.
	 * @return total_size��
	 */
	public long getTotalSize() {
		header.position(2);
		total_size = header.getLong();
		header.position(0);
		return total_size;
	}

	/**header���� type�� ���� ��ȯ�Ѵ�. ��ȯ�� Header Position�� 0���� �����Ѵ�.
	 * @return type��
	 */
	public byte getType() {
		header.position(1);
		type = header.get();
		header.position(0);
		return type;
	}

	/**header���� write_size�� ���� ��ȯ�Ѵ�. ��ȯ�� Header Position�� 0���� �����Ѵ�.
	 * @return write_size��
	 */
	public long getWriteSize() {
		header.position(18);
		write_size = header.getLong();
		header.position(0);
		return write_size;
	}
}
