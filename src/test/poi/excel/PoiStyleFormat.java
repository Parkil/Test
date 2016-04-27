package test.poi.excel;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

/*
 * apache poi excel에서 스타일 및 cell format을 지정하는 예제
 */
public class PoiStyleFormat {
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();

		Map<String, String> sss = new HashMap<String, String>();
		sss.put("KEY", "10000");
		SXSSFWorkbook wb = new SXSSFWorkbook(2000); 
		Sheet sh = wb.createSheet();
		
		//셀 병합 인자순서는 (range first_row,last_row,first_col,last_col) 이다
		//sh.addMergedRegion(new CellRangeAddress(1,1,1,5)); //merge cell
		
		CellStyle cellStyle = wb.createCellStyle();
		Row title_row = sh.createRow(0);
		for (int cellnum = 0; cellnum < 27; cellnum++) {
			Cell cell = title_row.createCell(cellnum);

			SXSSFCell test = (SXSSFCell) cell;

			test.setCellValue("제목111");
			
			//폰트지정
			Font defaultFont = wb.createFont();
			XSSFFont ttt = (XSSFFont) defaultFont;
			ttt.setFontHeightInPoints((short) 10);
			ttt.setFontName("Arial");
			ttt.setColor(IndexedColors.BLACK.getIndex());
			ttt.setItalic(false); //이탤릭체
			ttt.setBold(true); //볼드 지정
			
			//테두리 선 지정
			cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle.setFont(defaultFont);
			
			//background로 처리하면 글자가 잘 안보이는 문제가 있으므로 foreground로 지정할 것
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			// cellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(cellStyle);
		}
		
		
		DataFormat format = wb.createDataFormat();
		CellStyle cellStyle2 = wb.createCellStyle();
		cellStyle2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle2.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle2.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle2.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle2.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle2.setDataFormat(format.getFormat("#,##0")); //숫자 포맷 지정
		
		for (int rownum = 1; rownum < 5; rownum++) {
			Row row = sh.createRow(rownum);
			for (int cellnum = 0; cellnum < 27; cellnum++) {
				Cell cell = row.createCell(cellnum);
				
				
				//cell format이 지정되어 있어도 cell type이 숫자가 아니면 자동으로 적용을 안함
				if(cellnum % 2 != 0) {
					cell.setCellValue("가가가");
				}else {
					cell.setCellValue(1000000);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				}
				
				
				cell.setCellStyle(cellStyle2);
			}

		}
		

		FileOutputStream out = new FileOutputStream("d:/sxssf.xlsx");
		wb.write(out);
		out.close();

		long end = System.currentTimeMillis();
		System.out.println("실행 시간 : " + (end - start) / 1000.0);
	}
}