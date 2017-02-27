package test.digit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * 진수변환 예제
 */
public class Digit {
	public static void main(String[] args) throws IOException {
		
		int raw_num = 702; //10진수
		
		/*
		 * 10진수를 특정진수로 변경하는 예제
		 * 
		 * 10진수를 변환하고자 하는 진수로 나눔(밑의 예제는 26진수)
		 * 1. 702 / 26 -제수 27 나머지 0
		 * 2. 27(1번에서의 제수) / 26 - 제수 1 나머지 0
		 * 
		 * 전체 나머지 + 마지막 제수가 진수임 
		 * 위의 경우에는 100
		 */
		List<Integer> digit_list = new ArrayList<Integer>();
		
		int divider = 0; //제수
		int reserve = 0; //나머지
		
		do {
			divider = raw_num / 26;
			reserve = raw_num % 26;
			raw_num = divider;
			
			digit_list.add(reserve); //나머지를 입력
		}while(divider > 26);
		
		digit_list.add(divider); //마지막 제수를 입력
		
		//해당진수를 10진수로 변경
		int tot = 0;
		for(int i = 0 ; i<digit_list.size() ; i++) {
			tot += (Math.pow(26, i) * digit_list.get(i).intValue()); 
		}
		System.out.println(tot);
	}
}
