package test.base64png;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.apache.commons.codec.binary.Base64;

import test.util.ByteBufferUtil;
import test.util.FileUtil;


public class Base64PngNIO {
	private static FileUtil util = new FileUtil();
	
	private static void test1() {
		RandomAccessFile readraf = null;
		try {
			readraf = new RandomAccessFile("d:/ice.png","rw");
			FileChannel fc = readraf.getChannel();
			MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE, 0, readraf.length());
		
			ByteBuffer temp = ByteBuffer.allocate((int)readraf.length());
			
			temp.put(mbb);
			temp.flip();
			
			System.out.println(mbb.toString());
			System.out.println(Base64.encodeBase64String(temp.array()));
			
			//==============================================================//
			
			MappedByteBuffer mbb2 = fc.map(MapMode.READ_WRITE, 0, 10000);
			
			ByteBuffer temp2 = ByteBuffer.allocate((int)readraf.length());
			
			temp2.put(mbb2);
			temp2.flip();
			
			System.out.print(Base64.encodeBase64String(temp2.array()));
			
			MappedByteBuffer mbb3 = fc.map(MapMode.READ_WRITE, 10001, 3000);
			
			ByteBuffer temp3 = ByteBuffer.allocate((int)readraf.length());
			
			temp3.put(mbb3);
			temp3.flip();
			
			System.out.println(Base64.encodeBase64String(temp3.array()));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(readraf != null) {
				try {
					readraf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String encodePngToStr(String pngpath) {
		return null;
	}
	
	private static void decodeStrToPng(String txtpath, String destPngPath) {
		
	}
	
	public static void main(String[] args) throws Exception{
		test1();
		/*
		String export_php = util.readFile("d:/export-php.txt");
		String export_custom = util.readFile("d:/export-custom.txt");
		

		export_custom = URLDecoder.decode(export_custom,"utf-8");
		export_php = export_php.replace(System.getProperty("line.separator"), "");
		
		export_custom = export_custom.trim();
		export_php = export_php.trim();
		
		System.out.println("export_php length : "+export_php.length());
		System.out.println("export_custom length : "+export_custom.length());
		
		System.out.println(export_php);
		System.out.println(export_custom);

		
		byte[] buf = Base64.decodeBase64(export_php);
		//byte[] buf = Base64Coder.decodeLines(export_custom);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("d:/bbbbbbb.png"));
		out.write(buf);
		out.flush();
		out.close();
		*/
	}
}
