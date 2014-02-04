package test.nio.selector.old.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import test.util.ByteBufferUtil;
/*
 * ������ ���۽� Header���۸� ����
 *
 * Buffer����
 * byte - is_header(127)���� ��Ʈ���� header���� Ȯ��(127 �� ����)
 * byte - flag(0:���� , 1:������, 2:�Ϸ�)(����)
 * byte - type(0:�޽���, 1:����)
 * long - total_size(message or file�� �ѿ뷮)
 * long - read_size(���������� �о���� �뷮)(����)
 * long - write_size(�۽������� ���� �뷮)(����)
 */
public class SelectorBuffer {
	private ByteBuffer[] buff_arr; //0�� buff_arr[0] 1�� body

	private byte flag;
	private byte type;
	private long total_size;
	private long read_size;
	private long write_size;

	private ByteBuffer reserve = ByteBuffer.allocateDirect(100000); //testcode
	/**
	 * ������
	 * Buffer����
	 * byte - flag(0:���� , 1:������, 2:�Ϸ�)
	 * byte - type(0:�޽���, 1:����)
	 * long - total_size(message or file�� �ѿ뷮)
	 * long - read_size(���������� �о���� �뷮)
	 * long - write_size(�۽������� ���� �뷮) ����� ����� ����
	 */
	public SelectorBuffer() {
		ByteBuffer header 	 = ByteBuffer.allocateDirect(27);
		ByteBuffer body   	 = ByteBuffer.allocateDirect(50000);

		buff_arr = new ByteBuffer[2];
		buff_arr[0] = header;
		buff_arr[1] = body;
	}

	/**header ������ ��üũ�⸦ ��ȯ�Ѵ�.
	 * @return header������ ũ��
	 */
	public int header_size() {
		return buff_arr[0].capacity();
	}

	/**body ������ ��üũ�⸦ ��ȯ�Ѵ�.
	 * @return body������ ũ��
	 */
	public int body_size() {
		return buff_arr[1].capacity();
	}

	/**Header Buffer�� buffer�� �Է� �Է��Ŀ��� position�� 0���� �����Ѵ�.
	 * @param pos buffer�� �Է��� ��ġ
	 * @param buf �Է��� ����
	 */
	public void putHeader(ByteBuffer buf) {
		buff_arr[0].clear();
		buff_arr[0].put(buf);
		buff_arr[0].flip();
	}

	/**Body Buffer�� buffer�� �Է� �Է��Ŀ��� �Է��� �������� ���� limit���� �����ϰ� position�� 0���� �����Ѵ�.
	 * @param pos buffer�� �Է��� ��ġ
	 * @param buf �Է��� ����
	 */
	public void putBody(int pos, ByteBuffer buf) {
		buff_arr[1].position(pos);
		buff_arr[1].put(buf);
		buff_arr[1].limit(buff_arr[1].position());
		buff_arr[1].position(0);
	}

	/**
	 * header,body���۸� clear�Ѵ�.
	 */
	public void clearAll() {
		buff_arr[0].clear();
		buff_arr[1].clear();
	}

	/** SocketChannel���� ���� �о���� �о���� ���� header������ ���Ե� ��� header ���ۿ� ���� �Է��Ѵ�.
	 * @param sc �����͸� �о���� SocketChannel
	 * @return SocketChannel���� �о���� ��
	 * @throws IOException SocketChannel���� ���ܹ߻���
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

		//header������ ��� header ���ۿ� ���� �Է�
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



	/**SocketChannel�� �̿��Ͽ� ������ �����͸� ����
	 * @param sc �����͸� �����ϴµ� �̿��� SocketChannel
	 * @return  SocketChannel�� ������ ��
	 * @throws IOException SocketChannel���� ���ܹ߻���
	 */
	public long write(SocketChannel sc) throws IOException {
		long write_size = 0;
		write_size = sc.write(buff_arr);

		return write_size;
	}

	/**�ܺ� ���۸� SocketChannel�� �̿��Ͽ� ���� header���۴� �޼ҵ� ȣ������ ������ ���� �״�� �����ϸ� body���ۿ� �ܺι��۰� ��������� �ʴ´�.
	 * @param sc
	 * @param org
	 * @return
	 * @throws IOException
	 */
	public long write(SocketChannel sc, ByteBuffer org) throws IOException {
		buff_arr[0].position(0);
		sc.write(buff_arr[0]); //Header���� ����
		org.position(0);
		return sc.write(org);
	}

	/**body������ ������ ���ڿ��� ��ȯ�Ͽ� ��ȯ
	 * @param charset ��ȯ�� ���ڿ��� character set
	 * @return ��ȯ�� ���ڿ�
	 */
	public String getBodyContent(String charset) {
		buff_arr[1].position(0);
		ByteBufferUtil.setEncoding(charset);
		return ByteBufferUtil.bb_to_str(buff_arr[1]);
	}

	/**body������ ������ ���Ͽ� ����
	 * @param path ���ϰ��
	 * @param is_append �������ϵڿ� ���ο���� �ٿ����� ����
	 * @return ���Ͽ� �� �뷮
	 */
	public long writeBodyContent(String path, boolean is_append) {
		//body������ limit�� 0�ϰ�� 0�� ��ȯ
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

			//is_append��尡 true�ϰ�� ���������� ���������� ������ �Է�
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

	/**���ڷ� �־��� ���ۿ� header,body������ ���� �Է��Ѵ�.
	 * @param dest header,body������ ���� �Է��� ������
	 */
	public void setBuffer(ByteBuffer dest) {
		dest.put(buff_arr[0]);
		dest.put(buff_arr[1]);
	}

	/**header ���۸� �־��� ������ �ʱ�ȭ write_size/read_size�� 0���� �ʱ�ȭ�ϰ� �ʱ�ȭ�Ŀ� setWriteSize/setReadSize�� �̿��Ͽ� ���� �����Ѵ�.
	 * @param flag flag(0:���� , 1:������, 2:�Ϸ�)
	 * @param type type(0:�޽���, 1:����)
	 * @param total_size total_size(message or file�� �ѿ뷮)
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

	/**header ���ۿ� write_size���� �Է� �Է��Ŀ��� position�� 0���� ����
	 * @param write_size write_size(�۽������� ���� �뷮)
	 */
	public void setWriteSize(long write_size) {
		buff_arr[0].position(18);
		buff_arr[0].putLong(write_size);
		buff_arr[0].position(0);
	}

	/**header ���ۿ� read_size���� �Է� �Է��Ŀ��� position�� 0���� ����
	 * @param read_size read_size(���������� �о���� �뷮)
	 */
	public void setReadSize(long read_size) {
		buff_arr[0].position(10);
		buff_arr[0].putLong(read_size);
		buff_arr[0].position(0);
	}

	/**header���� flag�� ���� ��ȯ�Ѵ�.
	 * @return flag��
	 */
	public byte getFlag() {
		buff_arr[0].position(1);
		flag = buff_arr[0].get();
		buff_arr[0].position(0);
		return flag;
	}

	/**header���� read_size�� ���� ��ȯ�Ѵ�.
	 * @return read_size��
	 */
	public long getReadSize() {
		buff_arr[0].position(11);
		read_size = buff_arr[0].getLong();
		buff_arr[0].position(0);
		return read_size;
	}

	/**header���� total_size�� ���� ��ȯ�Ѵ�.
	 * @return total_size��
	 */
	public long getTotalSize() {
		buff_arr[0].position(3);
		total_size = buff_arr[0].getLong();
		buff_arr[0].position(0);
		return total_size;
	}

	/**header���� type�� ���� ��ȯ�Ѵ�.
	 * @return type��
	 */
	public byte getType() {
		buff_arr[0].position(2);
		type = buff_arr[0].get();
		buff_arr[0].position(0);
		return type;
	}

	/**header���� write_size�� ���� ��ȯ�Ѵ�.
	 * @return write_size��
	 */
	public long getWriteSize() {
		buff_arr[0].position(19);
		write_size = buff_arr[0].getLong();
		buff_arr[0].position(0);
		return write_size;
	}

	/**header���θ� ��ȯ
	 * @return header ���� 127�̸� header �׿��� ��� header�� �ƴ�
	 */
	public byte isHeader() {
		buff_arr[0].position(0);
		byte is_header = buff_arr[0].get();
		buff_arr[0].position(0);

		return is_header;
	}
}
