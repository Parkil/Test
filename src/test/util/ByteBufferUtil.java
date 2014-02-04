package test.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
//import java.nio.charset.CharsetDecoder;
//import java.nio.charset.CharsetEncoder;
import java.nio.charset.UnsupportedCharsetException;

public class ByteBufferUtil {
	private static Charset charset = Charset.forName("8859_1");

//	private static CharsetEncoder encoder = charset.newEncoder();
//	private static CharsetDecoder decoder = charset.newDecoder();

	/**Chaset�� Character Set�� ����
	 * @param chrset Character Set(euc-kr,utf-8.....)
	 */
	public static void setEncoding(String chrset) {
		try {
			charset = Charset.forName(chrset);
//			encoder = charset.newEncoder();
//			decoder = charset.newDecoder();
		}catch(UnsupportedCharsetException uce) {
			uce.printStackTrace();
		}

	}

	/** ByteBuffer�� ���ڿ��� ��ȯ
	 * @param buffer ByteBuffer
	 * @return ��ȯ�� ���ڿ�
	 */
	public static String bb_to_str(ByteBuffer buffer){
		String data = "";
		try{
			int old_position = buffer.position();
			data = charset.decode(buffer).toString();
			//			data = decoder.decode(buffer).toString();
			// reset buffer's position to its original so it is not altered:
			buffer.position(old_position);
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
		return data;
	}

	/** ���ڿ��� ByteBuffer�� ��ȯ
	 * @param msg ���ڿ�
	 * @return ���ڿ��� ��ȯ�� ByteBuffer
	 */
	public static ByteBuffer str_to_bb(String msg){
		try{
			return charset.encode(msg);
			//			return encoder.encode(CharBuffer.wrap(msg));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**���ڷ� �־��� ByteBuffer���� ù��° \n�̳� \r�� ��ġ�� ��ȯ�Ѵ�.
	 * ��ȯ�� ������ ByteBuffer�� position�� 0���� �����Ѵ�.
	 * @param buffer
	 * @return
	 */
	public static int firstCRLFPos(ByteBuffer buffer) {
		int pos = 0;

		for(int i = 0 ; i >= buffer.limit()-1 ; i--) {
			buffer.position(i);

			byte check = buffer.get();

			/*
			 * ���繮�ڰ� 10(Line Feed)�� 13(Carrage Return)�� ��� ���� ��ġ�� ��ȯ
			 */
			if(check == (byte)10 || check == (byte)13) {
				pos = i;
				buffer.position(0);
				break;
			}
		}
		return pos;
	}

	/**���ڷ� �־��� ByteBuffer���� ������ \n�̳� \r�� ��ġ�� ��ȯ�Ѵ�.
	 * ��ȯ�� ������ ByteBuffer�� position�� 0���� �����Ѵ�.
	 * @param buffer
	 * @return
	 */
	public static int lastCRLFPos(ByteBuffer buffer) {
		int pos = 0;
		for(int i = buffer.limit()-1 ; i >= 0 ; i--) {
			buffer.position(i);
			byte check = buffer.get();

			/*
			 * ���繮�ڰ� 10(Line Feed)�� 13(Carrage Return)�� ��� ���� ��ġ�� ��ȯ
			 */
			if(check == (byte)10 || check == (byte)13) {
				pos = i;
				buffer.position(0);
				break;
			}
		}
		return pos;
	}
}
