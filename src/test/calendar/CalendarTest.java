package test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarTest {
	public static void main(String[] args) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse("2017-06-02"));
		System.out.println("DAY_OF_WEEK : "+c.get(Calendar.DAY_OF_WEEK)); //1주 중 몇번째 날인지
		System.out.println("DAY_OF_WEEK_IN_MONTH : "+c.get(Calendar.DAY_OF_WEEK_IN_MONTH)); //현재일자가 이번달 몇번째 주인지(해당되는 달만 계산)
		System.out.println("WEEK_OF_MONTH : "+c.get(Calendar.WEEK_OF_MONTH)); //현재 일자가 이번달 몇번째 주인지 (전달과 연계하여 계산)
		System.out.println(c.getActualMinimum(Calendar.DAY_OF_MONTH)); //첫날
		System.out.println(c.getActualMaximum(Calendar.DAY_OF_MONTH)); //마지막날
	}
}
