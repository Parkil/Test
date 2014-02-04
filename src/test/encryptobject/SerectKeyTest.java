package test.encryptobject;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/*
 * 단일키를 이용한 암/복호화 테스트
 */
public class SerectKeyTest {
	public static void main(String[] args) throws Exception{
		//SecretKeyStore.saveKey();
		SecretKey key = (SecretKey)new SecretKeyStore().loadKey();
		
		Cipher c = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE,key);
		
		String 	plain_text  	=	"800113-1652828";
		byte[]	plain_bytes 	=	plain_text.getBytes("utf-8");
		
		byte[]	encrypt_bytes	=	c.doFinal(plain_bytes);
		String	encrypt_texts	=	ConvertBase64.encoding(encrypt_bytes);
		
		System.out.println("암호화 텍스트 : "+encrypt_texts);
		
		
		encrypt_bytes			=	ConvertBase64.decoding(encrypt_texts);
		c.init(Cipher.DECRYPT_MODE,key);
		
		byte[]	decrypt_bytes	=	c.doFinal(encrypt_bytes);
		String	decrypt_texts	=	new String(decrypt_bytes,"utf-8");
		System.out.println("복호화 텍스트 : "+decrypt_texts);
	}
}
