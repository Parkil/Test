package test.nio;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import test.util.ByteBufferUtil;

/*
 * MappedByteBuffer 문제
 * MappedByteBuffer 생성시 FileChannel을 생성한 FileInputStream/OutputStream/RandomAccessFile을 close해도
 * MappedByteBuffer가 같이 소멸되지 않고 가비지컬렉션이 될때까지는 유지가 된다. 
 * 
 * 이 때문에 문제가 되는 부분이 MappedByteBuffer도 파일 Lock과 관련이 있기 때문에 MappedByteBuffer가 존재하는
 * 동안에는 파일 삭제가 되지 않는다.
 * 
 * JDK API에서는 1개의 파일에서 파일 읽기/쓰기를 제외한 파일관련 수정사항을 동시에 수행할때에는
 * MappedByteBuffer를 사용하지 않도록 권장하고 있음. 
 */
public class MappedByteBufferProblem {
	public static void main(String[] args) throws Exception{
		File f = new File("d:/a.txt");
		RandomAccessFile raf = new RandomAccessFile(f,"rw");
		FileChannel fc = raf.getChannel();
		
		MappedByteBuffer mbb = fc.map(MapMode.PRIVATE, 0, fc.size());
		mbb.clear();
		
		ByteBuffer temp = ByteBuffer.allocateDirect(1000);
		int read_size = 0;
		while((read_size = fc.read(temp)) != -1) {
			temp.flip();
			System.out.println("read_size : "+read_size);
			System.out.println("data	  : "+ByteBufferUtil.bb_to_str(temp));
		}
		
		fc.close();
		raf.close();
		Thread.sleep(50000);
		System.out.println("End");
	}
}
