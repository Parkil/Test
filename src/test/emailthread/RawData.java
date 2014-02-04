package test.emailthread;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

import test.util.FileUtil;

public class RawData {
	private ArrayList<String>	data;
	Logger						logger	= Logger.getLogger(getClass());
	
	public RawData(String orgpath) {
		String temp[]	= new FileUtil().readFile(orgpath).split("\n");
		data 			= new ArrayList<String>(Arrays.asList(temp));
		logger.info("Raw Data Initalizied Data Size : "+data.size());
	}
	
	/**원 데이터로 부터 데이터를 추출한다.
	 * @return
	 */
	String getData() {
		String ret = null;
		
		while(data.size() == 0) {
			try {
				data.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		synchronized(data) {
			ret = data.remove(0);
			logger.info("get Raw Data data Remain : "+data.size());
			data.notifyAll();
		}
		return ret;
	}
	
	int size() {
		return data.size();
	}
}
