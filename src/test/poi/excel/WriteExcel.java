package test.poi.excel;

import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.ibm.db2.jcc.c.ce;

/*
 * Apache POI 3.9 라이브러리를 이용하여 xlsx형식으로 엑셀을 저장하는 예제
 * xlsx형식으로 많은 데이터를 저장할 경우 outofmemory error가 발생하는 경우가 있으니 주의
 * 
 * 필요 라이브러리
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
		SXSSFWorkbook wb = new SXSSFWorkbook(1000); // keep 100 rows in memory,
													// exceeding rows will be
													// flushed to disk
		Sheet sh = wb.createSheet();

		Row title_row = sh.createRow(0);
		for (int cellnum = 0; cellnum < 11; cellnum++) {
			Cell cell = title_row.createCell(cellnum);

			SXSSFCell test = (SXSSFCell) cell;
			
			String title = (cellnum != 10) ? "제목" : "총계";
			
			test.setCellValue(title);

			Font defaultFont = wb.createFont();
			XSSFFont ttt = (XSSFFont) defaultFont;
			ttt.setFontHeightInPoints((short) 10);
			ttt.setFontName("Arial");
			ttt.setColor(IndexedColors.BLACK.getIndex());
			ttt.setItalic(false);
			ttt.setBold(true);
			// defaultFont.setBoldweight(arg0);

			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(defaultFont);
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			// cellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStyle.setFillPattern(CellStyle.DIAMONDS);
			cell.setCellStyle(cellStyle);
			
			//테스트
			//
		}
		
		String sum_format_str = "=SUM(%1$s:%2$s)";
		for (int rownum = 1; rownum < 10; rownum++) {
			Row row = sh.createRow(rownum);
			for (int cellnum = 0; cellnum < 11; cellnum++) {
				
				if(cellnum != 10) {
					Cell cell = row.createCell(cellnum);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(1);
				}else {
					Cell cell = row.createCell(cellnum);
					cell.setCellType(Cell.CELL_TYPE_FORMULA);
					
					cell.setCellFormula( String.format(sum_format_str, "A"+(rownum+1), "J"+(rownum+1)) );
				}
			}
		}
		
		for (int cellnum = 0; cellnum < 10; cellnum++) {
			sh.autoSizeColumn(cellnum); //한글의 경우에는 약간잘리는 문제가 있음.
		}
		
		FileOutputStream out = new FileOutputStream("d:/sxssf.xlsx");
		wb.write(out);
		out.close();

		// dispose of temporary files backing this workbook on disk
		// wb.dispose();
		long end = System.currentTimeMillis();
		System.out.println("실행 시간 : " + (end - start) / 1000.0);
	}
}
