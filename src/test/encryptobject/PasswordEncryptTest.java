package test.encryptobject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/*
 * 패스워드기반의 key를 이용한 암/복호화 테스트
 */
public class PasswordEncryptTest {
	//패스워드앞에 붙이는 랜덤값인 salt를 생성한다.
	private static byte[] genSalt() {
		byte[] salt = new byte[8];
		Random r = new Random();
		r.nextBytes(salt);
		return salt;
	}

	public static String encrypt(String plain_text,String password) {
		String encrypt_text = null;
		//패스워드기반 암호키 생성
		System.out.println("PBE Encryption started");
		PBEKeySpec keyspec = new PBEKeySpec(password.toCharArray());
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey seckey = keyFactory.generateSecret(keyspec);
			System.out.println("PBE Key Generated");

			byte[] salt = PasswordEncryptTest.genSalt();
			PBEParameterSpec spec = new PBEParameterSpec(salt,1000);


			byte[] plain_bytes = plain_text.getBytes("utf-8");

			Cipher c = Cipher.getInstance("PBEWithMD5AndDES");
			c.init(Cipher.ENCRYPT_MODE,seckey,spec);

			System.out.println("Encryption start");
			byte[] encrypt_byte = c.doFinal(plain_bytes);
			System.out.println("Encryption end");

			String salt_str = ConvertBase64.encoding(salt);
			String encrypt_str = ConvertBase64.encoding(encrypt_byte);

			encrypt_text = salt_str+encrypt_str;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
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
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encrypt_text;
	}


	public static String decrypt(String encrypt_text,String password) {
		String decrypt = null;
		System.out.println("PBE Decryption started");

		System.out.println("Seperate salt....");
		String salt = encrypt_text.substring(0,12); //salt의 길이는 salt의 크기에 따라 불규칙적으로 변한다. 실험해보고 salt의 길이를 결정할것
		String encrypt = encrypt_text.substring(12,encrypt_text.length());
		System.out.println("salt : "+salt);


		byte[] salt_array = ConvertBase64.decoding(salt);
		byte[] encrypt_array = ConvertBase64.decoding(encrypt);

		PBEKeySpec keyspec = new PBEKeySpec(password.toCharArray());
		try {
			System.out.println("PBE Key Generated");
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = factory.generateSecret(keyspec);
			PBEParameterSpec spec = new PBEParameterSpec(salt_array,1000);

			Cipher c = Cipher.getInstance("PBEWithMD5AndDES");
			c.init(Cipher.DECRYPT_MODE,key,spec);

			System.out.println("Decryption Start");
			byte[] decrypt_array = c.doFinal(encrypt_array);
			System.out.println("Decryption End");

			decrypt = new String(decrypt_array,"utf-8");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
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
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return decrypt;
	}

	public static void main(String[] args) throws Exception{
		String plain_text = "음화화화화화화화 약오르징? 메롱!=======";

		String encrypt_text = PasswordEncryptTest.encrypt(plain_text,"aaa000");

		System.out.println("암호화 : "+encrypt_text);

		String decrypt_text = PasswordEncryptTest.decrypt(encrypt_text,"aaa000");

		System.out.println("복호화 :"+decrypt_text);
	}
}
