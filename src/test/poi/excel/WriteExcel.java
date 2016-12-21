package test.poi.excel;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * 사용 라이브러리
 * dom4j-1.6.1.jar
 * poi-3.9-20121203.jar
 * poi-ooxml-3.9-20121203.jar
 * poi-ooxml-schemas-3.9-20120326.jar
 * stax-api-1.0.1.jar
 * xmlbeans-2.3.0.jar
 */
public class WriteExcel {
	public static void main(String[] args) throws Throwable {
		long start = System.currentTimeMillis();

		Map<String, String> sss = new HashMap<String, String>();
		sss.put("KEY", "10000");
		SXSSFWorkbook wb = new SXSSFWorkbook(10000); // keep 100 rows in memory,
													// exceeding rows will be
													// flushed to disk
		//XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sh = wb.createSheet();
		Row title_row = sh.createRow(0);
		for (int cellnum = 0; cellnum < 11; cellnum++) {
			Cell cell = title_row.createCell(cellnum);

			String title = (cellnum != 10) ? "값" : "합계";
			cell.setCellValue(title);
			
			ExcelUtil.setHeaderStyleBorder(wb, cell);
		}
		
		String sum_format_str = "=SUM(%1$s:%2$s)";
		for (int rownum = 1; rownum <= 2500; rownum++) {
			Row row = sh.createRow(rownum);
			for (int cellnum = 0; cellnum < 11; cellnum++) {
				Cell cell = row.createCell(cellnum);
				
				if(cellnum != 10) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(1);
				}else {
					cell.setCellType(Cell.CELL_TYPE_FORMULA);
					cell.setCellFormula( String.format(sum_format_str, "A"+(rownum+1), "J"+(rownum+1)) );
				}
				
				
				if(rownum == 1) {
					ExcelUtil.setContentStyleFirstLineBorder(wb, cell);
				}else {
					ExcelUtil.setContentStyleBorder(wb, cell);
				}
			}
		}
		
		for (int cellnum = 0; cellnum < 10; cellnum++) {
			sh.autoSizeColumn(cellnum); //셀 크기 자동지정
		}
		
		wb.createSheet();
		FileOutputStream out = new FileOutputStream("d:/sxssf.xlsx");
		wb.write(out);
		out.close();

		// dispose of temporary files backing this workbook on disk
		wb.dispose();
		long end = System.currentTimeMillis();
		System.out.println("소요시간 : " + (end - start) / 1000.0);
	}
}
