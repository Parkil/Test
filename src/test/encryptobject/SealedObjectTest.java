package test.encryptobject;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignedObject;

//객체를 SealedObject를 사용하여 암호화 하는 예제
public class SealedObjectTest {
	private static Key publickey 	= null;
	private static Key privatekey 	= null;
	
	static {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
			keyGen.initialize(1024);  //반드시 512~1024사이 여야하고 또한 64의 배수여야 함 아니면 java.security.InvalidParameterException 발생
			KeyPair pair = keyGen.genKeyPair();
			publickey = pair.getPublic();
			privatekey = pair.getPrivate();
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public SignedObject signObject(Serializable obj) {
		SignedObject so = null;
		try {
			java.security.Signature sig = java.security.Signature.getInstance(privatekey.getAlgorithm());
			so = new SignedObject(obj,(PrivateKey)privatekey,sig);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return so;
	}
	
	public Object getsignObject(SignedObject so) {
		Object obj = null;
		
		java.security.Signature sig;
		try {
			sig = java.security.Signature.getInstance(publickey.getAlgorithm());
			try {
				boolean check = so.verify((PublicKey)publickey, sig);
				
				if(!check) {
					throw new SignatureException("Object veriftion failed");
				}
				
				obj = so.getObject();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static void main(String[] args) throws Exception{
		SealedObjectTest a = new SealedObjectTest();
		
		Integer s = new Integer(2);
		SignedObject so = a.signObject(s);
		
		Integer di = (Integer)a.getsignObject(so);
		System.out.println("SignedObject 테스트 : "+di.intValue());
	}
}
