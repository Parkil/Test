package test.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import test.util.ByteBufferUtil;

public class ScatterGatherTest {
	private static ByteBuffer[] buffer_array = new ByteBuffer[10];
	private static ByteBuffer test = ByteBuffer.allocate(1000);
	
	static {
		for(int i = 0 ; i<10 ; i++) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(100);
			buffer.order(ByteOrder.BIG_ENDIAN);
			buffer_array[i] = buffer;
		}
	}

	public void loadFile(String path) {
		FileInputStream fis = null;
		FileChannel fc = null;
		try {
			fis = new FileInputStream(path);
			fc = fis.getChannel();
			
			long readsize = fc.read(buffer_array);
			
			System.out.println("============================����============================");
			for (int i = 0 ; i<buffer_array.length ;i++) {
				buffer_array[i].flip();
				test.put(buffer_array[i]);
				buffer_array[i].flip();
				System.out.println(ByteBufferUtil.bb_to_str(buffer_array[i]));
			}
			
			System.out.println("============================����============================");
			test.flip();
			System.out.println(ByteBufferUtil.bb_to_str(test));
			FileOutputStream fos = new FileOutputStream("d:/����.txt");
			FileChannel fc2 = fos.getChannel();
			
			long writesize = fc2.write(buffer_array);
			
			System.out.println("�о����ũ�� : "+readsize);
			System.out.println("�� ũ��: "+writesize);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(fis != null)fis.close();
				if(fc != null)fc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {		
		ScatterGatherTest test = new ScatterGatherTest();
		test.loadFile("d:/ȯ�漳��.txt");
	}
}

	