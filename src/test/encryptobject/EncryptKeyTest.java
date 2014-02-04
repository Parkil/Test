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
 * key�� ��/��ȣȭ�ϴ� �׽�Ʈ
 */
public class EncryptKeyTest {
	/**
	 * @param key ��ȣȭ�� Ű
	 * @param passwordkey Ű�� ��ȣȭ�ϴµ� ���̴� Ű
	 * @param algorithm ��ȣȭ�� Ű�� �˰���
	 * @return ��ȣȭ�� ����Ʈ �迭
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
	 * @param encrypt_keybyte ��ȣȭ�� Ű�� ����Ʈ�迭
	 * @param passwordkey Ű�� ��ȭȭ�ϴµ� ���̴� Ű
	 * @param algorithm ��ȣȭ�� Ű�� �˰���
	 * @param type Cipher.PULBIC_KEY or Cipher.SECRET_KEY
	 * @return ��ȣȭ�� Ű
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
	 * @param key Ű
	 * @param password ��ȣȭ�Ҷ� ����� �н�����
	 * @return ��ȣȭ�� Ű ���ڿ�
	 */
	private static String encryptPasswordKey(SecretKey key,String password) {
		return PasswordEncryptTest.encrypt(ConvertBase64.encoding(key.getEncoded()), password);
	}

	/**
	 * @param encrypted_key ��ȣȭ�� Ű ���ڿ�
	 * @param password ��ȣȭ�Ҷ� ����� �н�����
	 * @param algorithm ��ȣȭ�� Ű�� �˰���
	 * @return ��ȭȭ�� Ű
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

		System.out.println("��ȣȭ�� Password Key : "+en_pwkey);

		SecretKey de_pwkey = decryptPasswordKey(en_pwkey,"aaa000","BlowFish");

		System.out.println("��ȣȭ�� Password Key : "+de_pwkey);


		EncryptKeyTest a = new EncryptKeyTest();
		byte[] en = a.encryptKey(key,de_pwkey,"BlowFish");

		SecretKey de = (SecretKey)a.decryptKey(en, de_pwkey, "BlowFish", Cipher.SECRET_KEY);

		String aa = "��ȭȭȭȭȭȭȭȭȭȭ";

		Cipher c = Cipher.getInstance("BlowFish");
		c.init(Cipher.ENCRYPT_MODE,de);
		byte[] s = c.doFinal(aa.getBytes("utf-8"));

		System.out.println("���� ��ȣȭ : "+ConvertBase64.encoding(s));

		c.init(Cipher.DECRYPT_MODE, key);

		byte[] t = c.doFinal(s);

		System.out.println("���� ��ȣȭ : "+new String(t,"utf-8"));
	}
}
