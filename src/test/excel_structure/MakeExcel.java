package test.excel_structure;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class MakeExcel {
	private ExcelTemplate template;
	private Map<String,String> header_data;
	private List<HashMap<String,String>> row_data;
	
	public MakeExcel(){}
	public MakeExcel(ExcelTemplate template, Map<String,String> header_data, List<HashMap<String,String>> row_data) {
		this.header_data = header_data;
		this.row_data = row_data;
		this.template = template;
	}
	
	public void setTemplate(ExcelTemplate template) {
		this.template = template;
	}
	
	public void setHeaderData(Map<String,String> header_data) {
		this.header_data = header_data;
	}
	
	public void setRowData(List<HashMap<String,String>> row_data) {
		this.row_data = row_data;
	}
	
	public void createExcel() throws Exception{
		SXSSFWorkbook wb = new SXSSFWorkbook(1000);
		Sheet sh = wb.createSheet();

		template.makeHeader(sh, header_data);
		template.makeRowContents(sh, header_data, row_data);
		
		FileOutputStream out = new FileOutputStream("d:/NewTemplate.xlsx");
		wb.write(out);
		out.close();
	}
}
