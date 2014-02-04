package test.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * 파일에 대한 MD5 Hash를 반환하는 클래스 웹 소스를 전용했음
 */
public class MD5Hash{
	/*
	 * 파일에 대한 MD5해쉬값을 반환
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

	/**파일에 대한 MD5 해쉬 문자열 반환
	 * @param filename 해쉬문자열을 생성할 파일경로
	 * @return 해쉬문자열
	 */
	public static String getMD5Checksum(String filename) {
		byte[] b = MD5Hash.createChecksum(filename);
		StringBuffer sb = new StringBuffer();

		/*
		 * createChecksum에서 생성한 바이트 배열을 문자열로 변환한다.
		 */
		for (int i=0; i < b.length; i++) {
			sb.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
		}

		return sb.toString();
	}
}