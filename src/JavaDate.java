import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class JavaDate {
	public static void main(String[] args) {
		//ObjectMapper om = new ObjectMapper();
		Calendar cal = Calendar.getInstance();
		
		int[] week_in_month = new int[5];
		int[] day_of_week = new int[5];
		
		int idx = 0;
		for(int i = 13; i<=17 ; i++) {
			cal.set(2015, 6, i); //2015-07
			
			/*
			System.out.println(cal.getTime());
			System.out.println("day of month : "+cal.get(Calendar.DAY_OF_MONTH)); //현재날자
			System.out.println("day of week : "+cal.get(Calendar.DAY_OF_WEEK)); //일(1)~토(7)
			System.out.println("day of week_in_month : "+cal.get(Calendar.DAY_OF_WEEK_IN_MONTH)); //주차수(전월날자 무시)
			System.out.println("week of month : "+cal.get(Calendar.WEEK_OF_MONTH)); //주차수(1주차,2주차......)
			*/
			week_in_month[idx]	= cal.get(Calendar.WEEK_OF_MONTH);
			day_of_week[idx]	= cal.get(Calendar.DAY_OF_WEEK);
			idx++;
		}
		
		cal.set(2015, 7, 1); //2015-08-01
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int j = 0 ; j<5 ; j++) {
			cal.set(Calendar.WEEK_OF_MONTH, week_in_month[j]);
			cal.set(Calendar.DAY_OF_WEEK, day_of_week[j]);
			
			System.out.println("convert Date : "+sdf.format(cal.getTime()));
		}
		
		System.out.println(Arrays.toString(week_in_month));
		System.out.println(Arrays.toString(day_of_week));
		
		cal.set(2015, 3, 1); //2015-04-01
		cal.set(Calendar.WEEK_OF_MONTH,4);
		cal.set(Calendar.DAY_OF_WEEK, 5);
		System.out.println(cal.getTime());
	}
}
