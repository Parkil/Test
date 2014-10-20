package test.docx4j;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * Excel col부분(A ~ AC or AAU)를 숫자로 변환하는 예제
 */
public class CalExcelRowCol {
	
	/**엑셀 좌표(C4,ASE4...)를 받아서 int배열로 좌표값을 반환
	 * @param excelloc 엑셀좌표
	 * @return 좌표값 int[0] = row int[1] = col
	 * @throws IllegalArgumentException
	 */
	public static int[] getExcelRowCol(String excelloc) throws IllegalArgumentException{
		int convert_result[] = new int[2];
		
		//엑셀좌표를 분할하는 예제 ex) C4 -> group1[C] group2[4]
		Pattern p = Pattern.compile("^([A-Z]{1,})([0-9]{1,})$",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(excelloc);
		
		if(m.find()) {
			String col = m.group(1).toUpperCase();
			int row = Integer.parseInt(m.group(2));
			
			convert_result[0] = row;
			
			byte[] colarr= col.getBytes();
			
			int idx = 0;
			
			/*
			 * 계산식 
			 * 1.모든 문자는 대문자로 계산한다 (A : 65 , Z : 90)
			 * 2.마지막 문자를 제외한 나머지 문자는 26만큼 제곱승한 값 * 자신의 index값(ascii문자값에서 64를 뺀값)을 더한다.
			 * 3.마지막 문자는 자신의 index값만 더한다.
			 * 
			 * ex) AC
			 * ((26)[26의 1승] * 1(A의 index값)) + 3(C의 index값)
			 * 
			 * ex) AAC
			 * ((26)[26의 1승] * 1(A의 index값)) + ((26*26)[26의 2승] * 1(A의 index값)) + 3(C의 index값)
			 */
			for(int i = 0 ; i < colarr.length-1 ; i++) {
				idx += (Math.pow(26, (i+1)))*(colarr[i] - 64);
			}
			
			idx += (colarr[colarr.length-1] - 64);
			
			convert_result[1] = idx;
		}else {
			throw new IllegalArgumentException("this parameter not excel location");
		}
		
		return convert_result;
	}
	
	public static void main(String[] args) {
		int loc[] = CalExcelRowCol.getExcelRowCol("AC4");
		
		System.out.printf("row : %d / col : %d",loc[0],loc[1]);
	}
}
