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
 * CipherStreamTest�� �����ϳ� ��������� Nio�� �̿��Ѵ�.
 */
public class CipherStreamTestNio {
	private static CipherInputStream cis;
	private static CipherOutputStream cos;
	private static Cipher c;
	private static SecretKey key;
	private static byte[] iv = new byte[8]; //CipherInput/outputStream�� �ʿ��� IV�Ķ���͸� �����ϴ� ����Ʈ�迭
	private static SecureRandom sr = new SecureRandom();
	private static ByteBuffer buffer = ByteBuffer.allocateDirect(10000);
	
	//Cipher ��ȣȭ�� �ʿ��� Key����
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
	 * ������ ��ȣȭ�Ͽ� ������ ��ο� �����Ѵ�.
	 * 
	 * @param orgpath ��ȣȭ�� ���� ���� ���
	 * @param destpath ��ȣȭ�� ������ ������ ���
	 * @param alogrithm ��ȣȭ�� ���̴� �˰���
	 * @throws FileNotFoundException ����������θ� ã�� ���ϴ� ���
	 */
	static void cipher(String orgpath,String destpath,String alogrithm) throws FileNotFoundException{
		File orgfile = new File(orgpath);
		FileInputStream fis = null; //�� ������ �о���� Stream
		FileOutputStream fos = null; // ��ȣȭ�� ������ �����ϴ� Stream
		FileChannel in = null;
		WritableByteChannel out = null;
		
		
		if(!orgfile.exists()) {
			throw new FileNotFoundException("plain file path not valid");
		}
		
		sr.nextBytes(iv); //IV�Ķ���͸� ���� ������ ����
		
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
	 * ��ȣȭ�� ������ ��ȣȭ �Ͽ� ������ ��ο� �����Ѵ�.
	 * 
	 * @param orgpath ��ȣȭ�� �������
	 * @param destpath ��ȣȭ�� ������ ����� ���
	 * @param alrorithm ��ȣȭ�ϴµ� ����� �˰���
	 * @throws FileNotFoundException ��ȣȭ�� ������θ� ã�� ���ϴ� ���
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
		 * Padding�� �߸������ϸ� ��ȣȭ�� �Ϻο����� ���ڰ� �ҽǵ� ���� �ִ� �����Ұ� 
		 */
		cipher("d:/orignal.txt","d:/encrypt.txt","BlowFish/CBC/NoPadding");
		decipher("d:/encrypt.txt","d:/decrypt.txt","BlowFish/CBC/NoPadding");
	}
}
