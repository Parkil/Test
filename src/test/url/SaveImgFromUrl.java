package test.url;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.ReadableByteChannel;

/*
 * �ڹٿ��� Post������� URL�� ȣ���ϰ� ������� �޾ƿ��� ����
 */
public class SaveImgFromUrl {
	private static ByteBuffer buffer= ByteBuffer.allocateDirect(1000);
	
	/*
	 * FileChannel.map�� �̿��Ͽ� MappedByteBuffer�� ȣ���� ���¿��� FileChannel truncate�� �����ϰ� �Ǹ�
	 * [java.io.IOException: ��û�� �۾���, ����ڰ� ������ ������ ���� �ִ� ������ ���Ͽ��� ������ �� �����ϴ�]
	 * ������ �߻��Ѵ� �̸� �ذ��ϱ� ���� mappedByteBuffer�� �޸𸮿��� �����Ѵ�.
	 */
	public static void unmapMmaped(ByteBuffer buffer) {
		if (buffer instanceof sun.nio.ch.DirectBuffer) {
			sun.misc.Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buffer).cleaner();
			cleaner.clean();
		}
	}

	/**�� URL�����͸� png���Ϸ� ����
	 * @param weburl �� URL
	 * @param path �������� ���
	 */
	public static void getPngFromUrl(String weburl, String path) {
		try {
			URL			url = new URL(weburl);
			InputStream is  = url.openConnection().getInputStream();

			ReadableByteChannel readChannel = Channels.newChannel(is);
			int readsize = 0;
			
			RandomAccessFile raf = new RandomAccessFile(path,"rw");
			FileChannel fc = raf.getChannel();
			
			//���� �뷮�� �𸣱⶧���� 10MB��ŭ�� �뷮�� �̸� ��Ƴ��´�.
			MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE, 0, (1024 * 1024 * 10));
			
			do {
				buffer.clear();
				readsize =readChannel.read(buffer);
				buffer.flip();

				mbb.put(buffer);
			}while(readsize != -1);
			
			mbb.flip();
			mbb.force();
			
			unmapMmaped(mbb);
			fc.truncate(mbb.limit()); //mappedByteBuffer�� �뷮(��������ũ��)��ŭ ������ �߶�
			
			readChannel.close();
			is.close();
			
			fc.close();
			raf.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String url = "http://192.168.123.23:9090/cgi-bin/site_yit_zoom?date=201410220840&name=YIT&ptype=c&dtype=r&sweepno=1&x1=0&y1=0&x2=0&y2=0&wind=0&cwind=0&vwind=0&bb=&zcx=0&zcy=0&zt=1&zra=200&zrb=1.6&mh=0&windh=2&cappih=1.5&&vmode=0";
		//ByteBufferUtil.setEncoding("utf-8");
		getPngFromUrl(url, "d:/test.png");
	}
}