package test.emailthread;

public class RunThread {
	public static void main(String[] args) {
		RawData	rd	= new RawData("d:/TB_CM_CUST.csv");
		
		for(int i = 0 ; i<20 ; i++) {
			new CheckThread(rd);
		}
	}
}
