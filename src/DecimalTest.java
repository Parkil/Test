public class DecimalTest {
	public static String getColIndexStr(int index) {
		String ret_str = "";
		if(index <= 26) {
			ret_str = String.valueOf((char)(64+index));
		}else {
			StringBuffer sb = new StringBuffer();
			
			int divider = 0; //제수
			int reserve = 0; //나머지
			
			do {
				divider = index / 26;
				reserve = index % 26;
				index = divider;
				
				sb.append(String.valueOf((char)(64+reserve)));
			}while(divider > 26);
			
			sb.append(String.valueOf((char)(64+divider)));
			
			ret_str = sb.reverse().toString();
		}
		
		return ret_str;
	}
	
	public static void main(String[] args) {
		System.out.println(getColIndexStr(300));
	}
}
