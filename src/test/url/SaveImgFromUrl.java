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
 * 자바에서 Post방식으로 URL을 호출하고 결과값을 받아오는 예제
 */
public class SaveImgFromUrl {
	private static ByteBuffer buffer= ByteBuffer.allocateDirect(1000);
	
	/*
	 * FileChannel.map을 이용하여 MappedByteBuffer를 호출한 상태에서 FileChannel truncate를 실행하게 되면
	 * [java.io.IOException: 요청한 작업은, 사용자가 매핑한 구역이 열려 있는 상태인 파일에서 수행할 수 없습니다]
	 * 오류가 발생한다 이를 해결하기 위해 mappedByteBuffer를 메모리에서 제거한다.
	 */
	public static void unmapMmaped(ByteBuffer buffer) {
		if (buffer instanceof sun.nio.ch.DirectBuffer) {
			sun.misc.Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buffer).cleaner();
			cleaner.clean();
		}
	}

	/**웹 URL데이터를 png파일로 저장
	 * @param weburl 웹 URL
	 * @param path 파일저장 경로
	 */
	public static void getPngFromUrl(String weburl, String path) {
		try {
			URL			url = new URL(weburl);
			InputStream is  = url.openConnection().getInputStream();

			ReadableByteChannel readChannel = Channels.newChannel(is);
			int readsize = 0;
			
			RandomAccessFile raf = new RandomAccessFile(path,"rw");
			FileChannel fc = raf.getChannel();
			
			//최초 용량을 모르기때문에 10MB만큼의 용량을 미리 잡아놓는다.
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
			fc.truncate(mbb.limit()); //mappedByteBuffer의 용량(실제파일크기)만큼 파일을 잘라냄
			
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