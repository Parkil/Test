package test.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
/*
 * nio MemoryMappedBuffer �׽�Ʈ
 */

import test.util.ByteBufferUtil;

public class MemoryMappedBufferTest {
	public static void main(String[] args) throws Exception{
		RandomAccessFile raf = new RandomAccessFile("d:/1.txt","rw");
		FileChannel rw = raf.getChannel();
		
		ByteBuffer add = ByteBufferUtil.str_to_bb("���ο� �������� �����");
		
		MappedByteBuffer mbb = rw.map(FileChannel.MapMode.READ_WRITE, 0, 10000);
		//System.out.println(ConvertByteBuffer.str_to_bb("�߰�����"));
		mbb.limit((int) rw.size());
		mbb.put(add);
		mbb.flip();
		mbb.force();
		rw.close();
		raf.close();
	}
}
