package test.encryptobject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/*
 * CipherStreamTest와 동일하나 파일저장시 Nio를 이용한다.
 */
public class CipherStreamTestNio {
	private static CipherInputStream cis;
	private static CipherOutputStream cos;
	private static Cipher c;
	private static SecretKey key;
	private static byte[] iv = new byte[8]; //CipherInput/outputStream에 필요한 IV파라메터를 저장하는 바이트배열
	private static SecureRandom sr = new SecureRandom();
	private static ByteBuffer buffer = ByteBuffer.allocateDirect(10000);
	
	//Cipher 암호화에 필요한 Key생성
	static {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("BlowFish");
			keygen.init(128);
			key = keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
	 * 문서를 암호화하여 지정한 경로에 저장한다.
	 * 
	 * @param orgpath 암호화할 원래 문서 경로
	 * @param destpath 암호화한 문서를 저장할 경로
	 * @param alogrithm 암호화에 쓰이는 알고리즘
	 * @throws FileNotFoundException 원래문서경로를 찾지 못하는 경우
	 */
	static void cipher(String orgpath,String destpath,String alogrithm) throws FileNotFoundException{
		File orgfile = new File(orgpath);
		FileInputStream fis = null; //원 문서를 읽어들일 Stream
		FileOutputStream fos = null; // 암호화된 문서를 저장하는 Stream
		FileChannel in = null;
		WritableByteChannel out = null;
		
		
		if(!orgfile.exists()) {
			throw new FileNotFoundException("plain file path not valid");
		}
		
		sr.nextBytes(iv); //IV파라메터를 랜덤 값으로 설정
		
		try {
			fis = new FileInputStream(orgfile);
			fos = new FileOutputStream(destpath);
			
			in = fis.getChannel();
			

			c = Cipher.getInstance(alogrithm);
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			
			c.init(Cipher.ENCRYPT_MODE, key,ivspec);
			
			cos = new CipherOutputStream(fos,c);
			out = Channels.newChannel(cos);
			
			buffer.put(iv);
			buffer.flip();
			out.write(buffer);
			
			buffer.clear();
			in.read(buffer);
			buffer.flip();
			out.write(buffer);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fis != null)fis.close();
				if(fos != null)fos.close();
				if(cos != null)cos.close();
				if(in != null) in.close();
				if(out != null)out.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 암호화된 문서를 복호화 하여 지정된 경로에 저장한다.
	 * 
	 * @param orgpath 암호화된 문서경로
	 * @param destpath 복호화된 문서가 저장될 경로
	 * @param alrorithm 복호화하는데 사용할 알고리즘
	 * @throws FileNotFoundException 암호화된 문서경로를 찾지 못하는 경우
	 */
	static void decipher(String orgpath,String destpath,String alrorithm) throws FileNotFoundException{
		File orgfile = new File(orgpath);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		ReadableByteChannel in = null;
		FileChannel out = null;
		
		if(!orgfile.exists()) {
			throw new FileNotFoundException("encrypt file path not valid");
		}
		
		try {
			fis = new FileInputStream(orgfile);
			fos = new FileOutputStream(destpath);
			
			out = fos.getChannel();
			
			fis.read(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			
			c = Cipher.getInstance(alrorithm);
			c.init(Cipher.DECRYPT_MODE, key, ivspec);
			
			cis = new CipherInputStream(fis,c);
			in = Channels.newChannel(cis);
			buffer.clear();
			in.read(buffer);
			buffer.flip();
			
			out.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) fis.close();
				if(fos != null) fos.close();
				if(cis != null) cis.close();
				if(in != null) in.close();
				if(out != null) out.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		/*
		 * Padding을 잘못선택하면 복호화시 일부영역의 문자가 소실될 수가 있다 주의할것 
		 */
		cipher("d:/orignal.txt","d:/encrypt.txt","BlowFish/CBC/NoPadding");
		decipher("d:/encrypt.txt","d:/decrypt.txt","BlowFish/CBC/NoPadding");
	}
}
