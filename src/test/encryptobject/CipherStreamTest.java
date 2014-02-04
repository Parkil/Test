package test.encryptobject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class CipherStreamTest {
	private static CipherInputStream cis;
	private static CipherOutputStream cos;
	private static Cipher c;
	private static SecretKey key;
	private static byte[] iv = new byte[8]; //CipherInput/outputStream�� �ʿ��� IV�Ķ���͸� �����ϴ� ����Ʈ�迭
	private static SecureRandom sr = new SecureRandom();
	
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
		
		if(!orgfile.exists()) {
			throw new FileNotFoundException("plain file path not valid");
		}
		
		sr.nextBytes(iv); //IV�Ķ���͸� ���� ������ ����
		
		try {
			fis = new FileInputStream(orgfile);
			fos = new FileOutputStream(destpath);
			c = Cipher.getInstance(alogrithm);
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			
			c.init(Cipher.ENCRYPT_MODE, key,ivspec);
			
			cos = new CipherOutputStream(fos,c);
			
			int byte_index = 0;
			
			fos.write(iv);
			while((byte_index = fis.read()) != -1) {
				cos.write(byte_index);
			}
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
		
		if(!orgfile.exists()) {
			throw new FileNotFoundException("encrypt file path not valid");
		}
		
		try {
			fis = new FileInputStream(orgfile);
			fis.read(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			
			c = Cipher.getInstance(alrorithm);
			c.init(Cipher.DECRYPT_MODE, key, ivspec);
			
			cis = new CipherInputStream(fis,c);
			
			fos = new FileOutputStream(destpath);
			
			int read_byte = 0;
			while((read_byte = cis.read()) != -1) {
				fos.write(read_byte);
			}
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
