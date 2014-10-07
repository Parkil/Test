package test.poi.excel;

import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * Apache POI 3.9 라이브러리를 이용하여 엑셀파일을 읽어들이는 예제
 * 
 * 필요 라이브러리
 * dom4j-1.6.1.jar
 * poi-3.9-20121203.jar
 * poi-ooxml-3.9-20121203.jar
 * poi-ooxml-schemas-3.9-20120326.jar
 * stax-api-1.0.1.jar
 * xmlbeans-2.3.0.jar
 */
public class ReadExcel {
	public static void main(String[] args) throws Exception{
		FileInputStream fis = new FileInputStream("d:/student_input.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		Iterator<Row> row_iter = sheet.iterator();
		
		StringBuffer sb = new StringBuffer();
		Row temp_row = null;
		Cell temp_cell = null;
		String tmp = null;
		while(row_iter.hasNext()) {
			temp_row = row_iter.next();
			
			for(int i = 0 ; i<7 ; i++) {
				temp_cell = temp_row.getCell(i, Row.RETURN_NULL_AND_BLANK);
				
				if(temp_cell == null) {
					sb.append(" -");
				}else {
					switch(temp_cell.getCellType()) {
						case Cell.CELL_TYPE_BLANK : 
							tmp = temp_cell.getStringCellValue();
						break;
						case Cell.CELL_TYPE_BOOLEAN:
							tmp = String.valueOf(temp_cell.getBooleanCellValue());
						break;
						case Cell.CELL_TYPE_ERROR:
							tmp = String.valueOf(temp_cell.getErrorCellValue());
						break;
						case Cell.CELL_TYPE_NUMERIC:
							tmp = String.valueOf((int)temp_cell.getNumericCellValue());
						break;
						case Cell.CELL_TYPE_STRING: 
							tmp = temp_cell.getStringCellValue();
						break;
					}
					sb.append(tmp+"-");
				}
			}
			sb.append("\n");
		}
		
		System.out.println(sb.toString());
	}
}
