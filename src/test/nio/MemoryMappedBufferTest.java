package test.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import test.util.ByteBufferUtil;

/*
 * nio MemoryMappedBuffer 예제
 */
public class MemoryMappedBufferTest {
	public static void main(String[] args) throws Exception{
		RandomAccessFile raf = new RandomAccessFile("d:/1.txt","rw");
		FileChannel rw = raf.getChannel();
		
		ByteBuffer add = ByteBufferUtil.str_to_bb("테스트용");
		
		MappedByteBuffer mbb = rw.map(FileChannel.MapMode.READ_WRITE, 0, 10000);
		//System.out.println(ConvertByteBuffer.str_to_bb("占쌩곤옙占쏙옙占쏙옙"));
		mbb.limit((int) rw.size());
		mbb.put(add);
		mbb.flip();
		mbb.force();
		rw.close();
		raf.close();
		
		/* 특정파일의 전체 라인을 반환
		 List<String> sss = Files.readAllLines(Paths.get("d:/수정계획 등록 시간/slims3.txt") ,Charset.forName("utf-8"));
		 */
	}
}