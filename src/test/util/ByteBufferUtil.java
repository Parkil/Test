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

	/**Chaset의 Character Set을 지정
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

	/** ByteBuffer를 문자열로 전환
	 * @param buffer ByteBuffer
	 * @return 변환된 문자열
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

	/** 문자열을 ByteBuffer로 전환
	 * @param msg 문자열
	 * @return 문자열을 변환한 ByteBuffer
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

	/**인자로 주어진 ByteBuffer에서 첫번째 \n이나 \r의 위치를 반환한다.
	 * 반환후 인자의 ByteBuffer의 position을 0으로 변경한다.
	 * @param buffer
	 * @return
	 */
	public static int firstCRLFPos(ByteBuffer buffer) {
		int pos = 0;

		for(int i = 0 ; i >= buffer.limit()-1 ; i--) {
			buffer.position(i);

			byte check = buffer.get();

			/*
			 * 현재문자가 10(Line Feed)나 13(Carrage Return)일 경우 현재 위치를 반환
			 */
			if(check == (byte)10 || check == (byte)13) {
				pos = i;
				buffer.position(0);
				break;
			}
		}
		return pos;
	}

	/**인자로 주어진 ByteBuffer에서 마지막 \n이나 \r의 위치를 반환한다.
	 * 반환후 인자의 ByteBuffer의 position을 0으로 변경한다.
	 * @param buffer
	 * @return
	 */
	public static int lastCRLFPos(ByteBuffer buffer) {
		int pos = 0;
		for(int i = buffer.limit()-1 ; i >= 0 ; i--) {
			buffer.position(i);
			byte check = buffer.get();

			/*
			 * 현재문자가 10(Line Feed)나 13(Carrage Return)일 경우 현재 위치를 반환
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
