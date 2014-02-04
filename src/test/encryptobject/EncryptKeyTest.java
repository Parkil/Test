package test.encryptobject;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * key를 암/복호화하는 테스트
 */
public class EncryptKeyTest {
	/**
	 * @param key 암호화할 키
	 * @param passwordkey 키를 암호화하는데 쓰이는 키
	 * @param algorithm 암호화할 키의 알고리즘
	 * @return 암호화한 바이트 배열
	 */
	byte[] encryptKey(Key key,Key passwordkey,String algorithm) {
		byte[] result = null;

		try {
			Cipher c = Cipher.getInstance(algorithm);
			c.init(Cipher.WRAP_MODE, passwordkey);
			result = c.wrap(key);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @param encrypt_keybyte 암호화한 키의 바이트배열
	 * @param passwordkey 키를 암화화하는데 쓰이는 키
	 * @param algorithm 암호화한 키의 알고리즘
	 * @param type Cipher.PULBIC_KEY or Cipher.SECRET_KEY
	 * @return 복호화한 키
	 */
	Key decryptKey(byte[] encrypt_keybyte,Key passwordkey,String algorithm,int type) {
		Key key = null;

		try {
			Cipher c = Cipher.getInstance(algorithm);
			c.init(Cipher.UNWRAP_MODE, passwordkey);
			key = c.unwrap(encrypt_keybyte, algorithm, type);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return key;
	}

	/**
	 * @param key 키
	 * @param password 암호화할때 사용할 패스워드
	 * @return 암호화된 키 문자열
	 */
	private static String encryptPasswordKey(SecretKey key,String password) {
		return PasswordEncryptTest.encrypt(ConvertBase64.encoding(key.getEncoded()), password);
	}

	/**
	 * @param encrypted_key 암호화된 키 문자열
	 * @param password 암호화할때 사용한 패스원드
	 * @param algorithm 암호화된 키의 알고리즘
	 * @return 복화화된 키
	 */
	private static SecretKey decryptPasswordKey(String encrypted_key,String password,String algorithm) {
		String temp = PasswordEncryptTest.decrypt(encrypted_key, password);
		byte[] key_byte = ConvertBase64.decoding(temp);
		SecretKeySpec keyspec = new SecretKeySpec(key_byte,algorithm);

		return keyspec;
	}

	public static void main(String[] args) throws Exception{
		KeyGenerator pwkeygen = KeyGenerator.getInstance("BlowFish");
		KeyGenerator keygen = KeyGenerator.getInstance("BlowFish");

		SecretKey pwkey = pwkeygen.generateKey();
		SecretKey key = keygen.generateKey();

		String en_pwkey = encryptPasswordKey(pwkey,"aaa000");

		System.out.println("암호화된 Password Key : "+en_pwkey);

		SecretKey de_pwkey = decryptPasswordKey(en_pwkey,"aaa000","BlowFish");

		System.out.println("복호화된 Password Key : "+de_pwkey);


		EncryptKeyTest a = new EncryptKeyTest();
		byte[] en = a.encryptKey(key,de_pwkey,"BlowFish");

		SecretKey de = (SecretKey)a.decryptKey(en, de_pwkey, "BlowFish", Cipher.SECRET_KEY);

		String aa = "음화화화화화화화화화화";

		Cipher c = Cipher.getInstance("BlowFish");
		c.init(Cipher.ENCRYPT_MODE,de);
		byte[] s = c.doFinal(aa.getBytes("utf-8"));

		System.out.println("실제 암호화 : "+ConvertBase64.encoding(s));

		c.init(Cipher.DECRYPT_MODE, key);

		byte[] t = c.doFinal(s);

		System.out.println("실제 복호화 : "+new String(t,"utf-8"));
	}
}
