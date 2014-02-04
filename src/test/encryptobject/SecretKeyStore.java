package test.encryptobject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SecretKeyStore {
	//키값을 저장할 파일명
	private final static String filename = "d:/scrm.keystore";
	private final static File file = new File(filename);
	private final static ByteBuffer buffer = ByteBuffer.allocate(1000);
	private final ByteBufferUtil util = new ByteBufferUtil();
	private final ConvertBase64 base64 = new ConvertBase64();
	
	public void saveKey() {
		FileOutputStream fos = null;
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("Blowfish");
			keygen.init(128);
			
			SecretKey key = keygen.generateKey();
			
			fos = new FileOutputStream(file);
			FileChannel write = fos.getChannel();
			String temp = base64.encoding(key.getEncoded());
			write.write(util.str_to_bb(temp));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fos != null) fos.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public Key loadKey() {
		SecretKey key = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();
			
			buffer.clear();
			fc.read(buffer);
			buffer.flip();
			
			String temp = util.bb_to_str(buffer);
			byte[] keyarr = base64.decoding(temp);
			key = new SecretKeySpec(keyarr, "Blowfish");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) fis.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return key;
	}
	
	private class ByteBufferUtil {
		private Charset charset = Charset.forName("8859_1"); 
	
		/** 문자열을 ByteBuffer로 전환
		 * @param msg 문자열
		 * @return 문자열을 변환한 ByteBuffer
		 */
		public ByteBuffer str_to_bb(String msg){ 
			try{ 
				return charset.encode(msg);
			}catch(Exception e){
				e.printStackTrace();
			} 
			return null; 
		} 
		
		/** ByteBuffer를 문자열로 전환
		 * @param buffer ByteBuffer
		 * @return 변환된 문자열
		 */
		public String bb_to_str(ByteBuffer buffer){ 
			String data = ""; 
			try{ 
				int old_position = buffer.position(); 
				data = charset.decode(buffer).toString();
				buffer.position(old_position);   
			}catch (Exception e){ 
				e.printStackTrace(); 
				return ""; 
			}
			return data; 
		}
	}
	
	private class ConvertBase64 {
		private final BASE64Encoder encode = new BASE64Encoder();
		private final BASE64Decoder decode = new BASE64Decoder();
		
		private ConvertBase64(){}
		
		public String encoding(byte[] bytes) {
			return encode.encode(bytes);
		}
		
		public byte[] decoding(String str) {
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
}
