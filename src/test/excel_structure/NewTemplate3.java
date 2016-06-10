package test.excel_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

//클럽 월별 현황
public class NewTemplate3 implements ExcelTemplate {
	private String merge_range_format_str	= "$%1$s$%2$s:$%3$s$%4$s"; //병합 문자열 포맷
	
	@Override
	public void makeHeader(Sheet sheet, Map<String, String> header_data) {
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		
		//[최상위 타이틀]
		Row title_row = sheet.createRow(0);
		Cell cell = title_row.createCell(0);
		cell.setCellValue(header_data.get("top_title"));
		
		CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, "A", "1", "H", "1"));
		sheet.addMergedRegion(cra); //셀 병합처리
		CellStyle header_style = wb.createCellStyle();
		
		//폰트 지정
		header_style.setFont(ExcelUtil.getFont(wb, "맑은 고딕", (short)28, false, true));
		header_style.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
		header_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		header_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(header_style);
		
		//병합된 셀에 테두리선 처리
		ExcelUtil.mergeCellAllSameLine(cra, sheet, CellStyle.BORDER_MEDIUM);
		
		header_style.setFont(ExcelUtil.getFont(wb, "맑은 고딕", (short)9, false, true));
		header_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		header_style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		header_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		title_row = sheet.createRow(1);
		cell = title_row.createCell(7);
		cell.setCellValue("(2016.04.20 기준)");
		cell.setCellStyle(header_style);
	}

	@Override
	public void makeRowContents(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>> row_data) {
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		
		Row row = sheet.createRow(2);
		Cell cell = null;
		for(int i = 0 ; i<8 ; i++) {
			cell = row.createCell(i);
			
		}
		
		
		/*
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		
		String[] col_title_key_arr = header_data.get("col_title_key").split(",");
		
		Font sum_font = ExcelUtil.getFont(wb, "맑은 고딕", (short)9, false, false);
		CellStyle col_style = wb.createCellStyle(); //cell.getCellStyle()로는 정상작동이 안됨
		ExcelUtil.cellDiffLine(col_style, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		col_style.setFont(sum_font);
		col_style.setAlignment(CellStyle.ALIGN_CENTER);
		col_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		Cell cell = null;
		HashMap<String,String> data = null;
		
		String prev_game_name = "";
		
		int merge_temp = 0;
		
		//실 데이터 입력
		ArrayList<String> merge_data = new ArrayList<String>();
		for(int row_num = 0,row_data_len = row_data.size() ; row_num < row_data_len ; row_num++) { //행
			data = row_data.get(row_num);
			
			//종목 상단 총계 부분(수식없이 칸만 입력)
			if(prev_game_name.intern() != data.get("game_name").intern()) {
				prev_game_name = data.get("game_name");
				
				Row sum_row = sheet.createRow(sheet.getLastRowNum()+1);
				for(int idx = 0,key_len = col_title_key_arr.length ; idx < key_len ; idx++) { 
					cell = sum_row.createCell(idx);
					cell.setCellStyle(col_style);
					if(idx == 0) {
						cell.setCellValue(prev_game_name);
					}else if(idx == 1) {
						cell.setCellValue("계");
					}
				}
				if(merge_temp != 0) {
					merge_data.add("A-"+(merge_temp+1)+"-A-"+sum_row.getRowNum());
				}
				merge_temp = sum_row.getRowNum();
			}
			
			Row data_row = sheet.createRow(sheet.getLastRowNum()+1);
			
			for(int idx = 0,key_len = col_title_key_arr.length ; idx < key_len ; idx++) { 
				cell = data_row.createCell(idx);
				cell.setCellStyle(col_style);
				
				String val = data.get(col_title_key_arr[idx]);
				if(val.matches("^[0-9]+$")) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Integer.parseInt(val));
				}else {
					cell.setCellValue(val);
				}
			}
		}
		
		//종목명을 병합처리
		merge_data.add("A-"+(merge_temp+1)+"-A-"+(sheet.getLastRowNum()+1)); //마지막 병합건
		String[] merge_data_arr = merge_data.toArray(new String[merge_data.size()]);
		ExcelUtil.multiMerge(merge_data_arr, sheet);
		
		int start_row[] = new int[merge_data_arr.length];
		int start_row_idx = 0;
		
		//각 종목별 계행의 수식 처리
		String sum_format = "=SUM(%1$s%2$s:%3$s%4$s)"; //수식 문자열 포맷
		for(String val : merge_data_arr) {
			String temp[] = val.split("-");
			int sum_row = Integer.parseInt(temp[1])-1; //계가 표시되는 행 번호
			int last_row = Integer.parseInt(temp[3]); //해당건의 마지막 번호 
			
			start_row[start_row_idx++] = sum_row+1;
			
			Row row = CellUtil.getRow(sum_row, sheet);
			Cell sum_cell = CellUtil.getCell(row, 2); //운영요일 컬럼의 계 총계
			
			sum_cell.setCellType(Cell.CELL_TYPE_FORMULA);
			sum_cell.setCellFormula(String.format(sum_format, "D", (sum_row+1), "P", (sum_row+1)));
			
			for(int i = 0 ; i<13 ; i++) {
				String cell_idx_str = ExcelUtil.getColIndexStr((i+4));
				
				sum_cell = CellUtil.getCell(row, (i+3)); //지도회수 ~ 60대
				sum_cell.setCellType(Cell.CELL_TYPE_FORMULA);
				sum_cell.setCellFormula(String.format(sum_format, cell_idx_str, (sum_row+2), cell_idx_str, last_row));
			}
		}
		
		//소계 부분 수식처리
		Row row = CellUtil.getRow(5, sheet);
		Cell total_cell = null;
		for(int i = 0 ; i<13 ; i++) {
			String cell_idx_str = ExcelUtil.getColIndexStr((i+4));
			StringBuffer sb = new StringBuffer();
			
			for(int j = 0,len = start_row.length  ; j<len ; j++) {
				sb.append(cell_idx_str);
				sb.append(start_row[j]);
				if(j != len -1) {
					sb.append(",");
				}
			}
			sb.append(",");
			sb.append(cell_idx_str);
			sb.append(7);
			

			total_cell = CellUtil.getCell(row, (i+3)); //지도회수 ~ 60대
			total_cell.setCellType(Cell.CELL_TYPE_FORMULA);
			total_cell.setCellFormula("=SUM("+sb.toString()+")");
		}
		*/
	}

	@Override
	public void afterHandle(Sheet sheet, Map<String, String> raw_data) {
		for (int cellnum = 0; cellnum < 14; cellnum++) {
			sheet.autoSizeColumn(cellnum); //한글의 경우에는 약간잘리는 문제가 있음.
		}
	}
}
