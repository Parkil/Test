package test.encryptobject;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/*
 * BASE64인코딩/디코딩 테스트
 */
public class ConvertBase64 {
	private final static BASE64Encoder encode = new BASE64Encoder();
	private final static BASE64Decoder decode = new BASE64Decoder();
	
	private ConvertBase64(){}
	
	public static String encoding(byte[] bytes) {
		return encode.encode(bytes);
	}
	
	public static byte[] decoding(String str) {
		byte[] bytes = null;
		try {
			bytes = decode.decodeBuffer(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bytes;
	}
}
