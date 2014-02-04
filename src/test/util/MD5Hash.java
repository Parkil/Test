package test.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * ���Ͽ� ���� MD5 Hash�� ��ȯ�ϴ� Ŭ���� �� �ҽ��� ��������
 */
public class MD5Hash{
	/*
	 * ���Ͽ� ���� MD5�ؽ����� ��ȯ
	 */
	private static byte[] createChecksum(String filename) {
		InputStream	  fis	   = null;
		MessageDigest complete = null;

		try {
			fis = new FileInputStream(filename);

			byte[] buffer = new byte[1024];
			complete = MessageDigest.getInstance("MD5");
			int numRead;

			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return complete.digest();
	}

	/**���Ͽ� ���� MD5 �ؽ� ���ڿ� ��ȯ
	 * @param filename �ؽ����ڿ��� ������ ���ϰ��
	 * @return �ؽ����ڿ�
	 */
	public static String getMD5Checksum(String filename) {
		byte[] b = MD5Hash.createChecksum(filename);
		StringBuffer sb = new StringBuffer();

		/*
		 * createChecksum���� ������ ����Ʈ �迭�� ���ڿ��� ��ȯ�Ѵ�.
		 */
		for (int i=0; i < b.length; i++) {
			sb.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
		}

		return sb.toString();
	}
}