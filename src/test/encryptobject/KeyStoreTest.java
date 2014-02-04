package test.encryptobject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;

/*
 * ���� ���������� privatekey�� �����ϴ� ����� ���� ���ϹǷ�(2012-05-29) ����� ����� �� ����. ����Ϸ��� privatekey,Secretkey�� ��밡��
 */
public class KeyStoreTest {
	//Ű���� ������ ���ϸ�
	private final static String filename = "my.keystore";
	private final static File file = new File(filename);
	private static KeyStore ks = null;

	static {
		try {
			ks = KeyStore.getInstance("JKS");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}

	public Key loadKey(String alias,String password) {

		//������ Ű�� ������ ������ ������� ���� ������ �����Ѵ�.
		if(file.exists() == false) {
			saveKey(password);
		}

		//FileInputStream fis = new FileInputStream(file);
		//ks.load(fis, password.toCharArray());

		return null;
	}



	private void saveKey(String password) {
		FileOutputStream fos = null;
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("Blowfish");
			keygen.init(128);

			keygen.generateKey();

			fos = new FileOutputStream(file);
			ks.store(fos, password.toCharArray());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
