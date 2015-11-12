package test.encoding;
import java.lang.reflect.Field;
import java.nio.charset.Charset;


public class StrEncoding {
	public static void main(String [] args) throws Exception{
		/*
		 * 문자열 encoding
		 * Charset.defaultCharset(), - 현재 설정된 기본 Charset 
		 * System.getProperty("file.encoding") - 파일에 설정된 Charset
		 * 
		 * 문자열 encoding 정보
		 * String str = "가가가"; 또는 String str = new String("가가가"); 처럼 문자열을 생성하면 현재 설정된 기본 charset으로 설정된다.
		 * str.getBytes()호출시에도 동일하다. 
		 * 
		 * 주의할점은 아래코드처럼 Charset변경시 현재 Charset과 변경하고자하는 Charset이 호환되지 않으면 문자열이 깨진다.
		 * ex1) String str_new = new String(str.getBytes("8859_1"), "utf-8" or "euc-kr")
		 * ex2) String str_new = new String(str.getBytes(), "utf-8" or "euc-kr")
		 * 
		 * 기본 Charset설정 
		 * - 기본적으로 file.encoding을 따라가지만 이미 기본 Charset이 있을 경우에는 변경되지 않는다 
		 * 이를 이용하여 file.encoding을 변경하고 기본 Charset을 null로 설정하면 기본 Charset이 변경된다.
		 */
		System.out.println(Charset.defaultCharset());
		System.out.println(System.getProperty("file.encoding"));
		
		System.setProperty("file.encoding","euc-kr");
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);
		
		System.out.println(Charset.defaultCharset());
		System.out.println(System.getProperty("file.encoding"));
	}
}
