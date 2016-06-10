package test.excel_structure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

public interface ExcelTemplate {
	//excel header(상단 타이틀 or 컬럼별 타이틀) 생성
	public void makeHeader(Sheet sheet, Map<String,String> header_data);
	
	//excel row부분 생성(단일 데이터)
	public void makeRowContents(Sheet sheet, Map<String, String> header_data, List<HashMap<String,String>> row_data);
	
	//excel 생성후 추가적으로 수정해야 할 사항 처리(필수사항 아님)
	public void afterHandle(Sheet sheet, Map<String,String> raw_data);
}
