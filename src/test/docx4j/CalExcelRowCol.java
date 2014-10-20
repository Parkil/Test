package test.docx4j;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * Excel col�κ�(A ~ AC or AAU)�� ���ڷ� ��ȯ�ϴ� ����
 */
public class CalExcelRowCol {
	
	/**���� ��ǥ(C4,ASE4...)�� �޾Ƽ� int�迭�� ��ǥ���� ��ȯ
	 * @param excelloc ������ǥ
	 * @return ��ǥ�� int[0] = row int[1] = col
	 * @throws IllegalArgumentException
	 */
	public static int[] getExcelRowCol(String excelloc) throws IllegalArgumentException{
		int convert_result[] = new int[2];
		
		//������ǥ�� �����ϴ� ���� ex) C4 -> group1[C] group2[4]
		Pattern p = Pattern.compile("^([A-Z]{1,})([0-9]{1,})$",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(excelloc);
		
		if(m.find()) {
			String col = m.group(1).toUpperCase();
			int row = Integer.parseInt(m.group(2));
			
			convert_result[0] = row;
			
			byte[] colarr= col.getBytes();
			
			int idx = 0;
			
			/*
			 * ���� 
			 * 1.��� ���ڴ� �빮�ڷ� ����Ѵ� (A : 65 , Z : 90)
			 * 2.������ ���ڸ� ������ ������ ���ڴ� 26��ŭ �������� �� * �ڽ��� index��(ascii���ڰ����� 64�� ����)�� ���Ѵ�.
			 * 3.������ ���ڴ� �ڽ��� index���� ���Ѵ�.
			 * 
			 * ex) AC
			 * ((26)[26�� 1��] * 1(A�� index��)) + 3(C�� index��)
			 * 
			 * ex) AAC
			 * ((26)[26�� 1��] * 1(A�� index��)) + ((26*26)[26�� 2��] * 1(A�� index��)) + 3(C�� index��)
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
