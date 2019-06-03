package temp;

import javax.xml.bind.DatatypeConverter;

/*
 * 오라클 US7ASCII에서 저장된 한글을 Convert하는 방법 --> 문자열을 16진수 문자열로 변경하고 이를 다시 원래 문자열로 변환
 * [방안1]
 * 1.rawtohex 함수를 이용하여 한글을 16진수 문자열로 변환 ex)select rawtohex(comm_cd_nm),comm_cd_nm from BA99010D where comm_cd='71383'
 * 2.1번에 만든 문자열을 DatatypeConverter를 이용하며 byte배열로 변환
 * 3.new String을 이용하여 변경
 * 
 * ^\\s*(\\p{XDigit}+)\\s*$ -- 16진수 문자열 검증 정규식
 */
public class ConvertOracleKor {
	public static void main(String[] args) throws Exception{
		String hexStr = "564F56204F20B7D4B5A5B8F420B1BABBEAC1A1";
		byte[] byteArr = DatatypeConverter.parseHexBinary(hexStr);
		
		System.out.println(hexStr.matches("^\\s*(\\p{XDigit}+)\\s*$"));
		System.out.println(new String(byteArr, "cp949"));
	}
}
