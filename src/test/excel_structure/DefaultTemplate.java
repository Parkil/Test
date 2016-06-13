package test.excel_structure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class DefaultTemplate implements ExcelTemplate {
	
	
	@Override
	public void makeHeader(Sheet sheet, Map<String, String> header_data) {
		//최상위 타이틀
		Row title_row = sheet.createRow(0);
		Cell cell = title_row.createCell(0);
		cell.setCellValue(header_data.get("top_title"));
		
		int cel_title_size = header_data.get("col_title_str").split(",").length;
		String range_format_str = "$%1$s$%2$s:$%3$s$%4$s";
		
		
		CellRangeAddress cra = CellRangeAddress.valueOf(String.format(range_format_str, "A", "1", ExcelUtil.getColIndexStr(cel_title_size), "1"));
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		sheet.addMergedRegion(cra); //A1 ~ F1까지 병합처리
		
		CellStyle header_style = wb.createCellStyle();
		
		//폰트 지정
		header_style.setFont(ExcelUtil.getFont(wb, "맑은 고딕", (short)20, false, true));
		header_style.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
		cell.setCellStyle(header_style);
		
		//병합된 셀에 테두리선 처리
		ExcelUtil.mergeCellAllSameLine(cra, sheet, CellStyle.BORDER_MEDIUM);
		
		//컬럼별 타이틀 ,로 구분
		String col_title_arr[] = header_data.get("col_title_str").split(",");
		
		title_row = sheet.createRow(2);
		
		Font title_font = ExcelUtil.getFont(wb, "맑은 고딕", (short)9, false, true);
		for(int i = 0, length = col_title_arr.length ; i < length ; i++) {
			cell = title_row.createCell(i);
			
			short right_line_type = (i == length-1) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
			
			CellStyle col_title_style = wb.createCellStyle(); //cell.getCellStyle()로는 정상작동이 안됨
			ExcelUtil.cellDiffLine(col_title_style, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line_type);
			col_title_style.setFont(title_font);
			col_title_style.setAlignment(CellStyle.ALIGN_CENTER);
			
			cell.setCellStyle(col_title_style);
			cell.setCellValue(col_title_arr[i]);
		}
	}

	@Override
	public void makeRowContents(Sheet sheet, Map<String, String> header_data, List<HashMap<String,String>> row_data) {
		String col_title_key[] = header_data.get("col_title_key").split(",");
		
		Font row_font = ExcelUtil.getDefaultFont(sheet.getWorkbook(), "맑은 고딕", (short)9);
		
		for(int i = 0, length = col_title_key.length ; i < length ; i++) {
			sheet.setColumnWidth(i, 2800);
		}
		int row_cnt = 3;
		for(HashMap<String,String> map : row_data) {
			Row data_row = sheet.createRow(row_cnt++);
			
			for(int i = 0, length = col_title_key.length ; i < length ; i++) {
				Cell cell = data_row.createCell(i);
				
				CellStyle row_style = sheet.getWorkbook().createCellStyle();
				row_style.setFont(row_font);
				
				short right_line_type = (i == length-1) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
				short bottom_line_type = (row_cnt-3 == row_data.size()) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
				ExcelUtil.cellDiffLine(row_style, CellStyle.BORDER_THIN, bottom_line_type, CellStyle.BORDER_THIN, right_line_type);
				
				cell.setCellStyle(row_style);
				cell.setCellValue(Integer.parseInt(map.get(col_title_key[i])));
			}
		}
	}

	@Override
	public void makeRowContents(Sheet sheet, Map<String, String> header_data,
			List<HashMap<String, String>>... row_data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterHandle(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>> row_data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterHandle(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>>... row_data) {
		// TODO Auto-generated method stub
		
	}
}
