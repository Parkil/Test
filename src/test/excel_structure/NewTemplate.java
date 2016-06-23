package test.excel_structure;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class NewTemplate implements ExcelTemplate {
	private String merge_range_format_str	= "$%1$s$%2$s:$%3$s$%4$s"; //병합 문자열 포맷
	private String sum_range_format_str		= "%1$s%2$s:%3$s%4$s"; //sum 문자열 포맷
	
	@Override
	public void makeHeader(Sheet sheet, Map<String, String> header_data) {
		//[최상위 타이틀]
		Row title_row = sheet.createRow(0);
		Cell cell = title_row.createCell(0);
		cell.setCellValue(header_data.get("top_title"));
		
		int cel_title_size = header_data.get("col_title_str").split(",").length;
		String range_format_str = "$%1$s$%2$s:$%3$s$%4$s";
		
		
		CellRangeAddress cra = CellRangeAddress.valueOf(String.format(range_format_str, "A", "1", ExcelUtil.getColIndexStr(cel_title_size), "1"));
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		sheet.addMergedRegion(cra); //셀 병합처리
		
		CellStyle header_style = wb.createCellStyle();
		
		//폰트 지정
		header_style.setFont(ExcelUtil.getFont(wb, "맑은 고딕", (short)20, false, true));
		header_style.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
		header_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		header_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(header_style);
		
		//병합된 셀에 테두리선 처리
		ExcelUtil.mergeCellAllSameLine(cra, sheet, CellStyle.BORDER_MEDIUM);
		
		//[컬럼별 타이틀 ,로 구분]
		String col_title_arr[] = header_data.get("col_title_str").split(",");
		
		title_row = sheet.createRow(2);
		
		Font title_font = ExcelUtil.getFont(wb, "맑은 고딕", (short)11, false, true);
		for(int i = 0, length = col_title_arr.length ; i < length ; i++) {
			cell = title_row.createCell(i);
			
			CellStyle col_title_style = wb.createCellStyle(); //cell.getCellStyle()로는 정상작동이 안됨
			ExcelUtil.cellDiffLine(col_title_style, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
			col_title_style.setFont(title_font);
			col_title_style.setAlignment(CellStyle.ALIGN_CENTER);
			col_title_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			col_title_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			
			cell.setCellStyle(col_title_style);
			cell.setCellValue(col_title_arr[i]);
		}
		
		//[총계 부분]
		Font sum_font = ExcelUtil.getFont(wb, "맑은 고딕", (short)11, false, false);
		String[] col_sub_title_str_arr = header_data.get("col_sub_title_str").split(",");
		int data_total_cnt = Integer.parseInt(header_data.get("data_total_cnt")); //전체 데이터 개수
				
		for(int i = 0, sub_length = col_sub_title_str_arr.length ; i < sub_length ; i++) {
			Row row = sheet.createRow(3+i);
			for(int j = 0, length = col_title_arr.length ; j < length ; j++) {
				cell = row.createCell(j);
				
				CellStyle col_title_style = wb.createCellStyle(); //cell.getCellStyle()로는 정상작동이 안됨
				ExcelUtil.cellDiffLine(col_title_style, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
				col_title_style.setFont(sum_font);
				col_title_style.setAlignment(CellStyle.ALIGN_CENTER);
				col_title_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
				
				cell.setCellStyle(col_title_style);
				
				if(j == 0) {
					cell.setCellValue("총계");
				}else if(j == 1) {
					cell.setCellValue(col_sub_title_str_arr[i]);
				}else {
					cell.setCellType(Cell.CELL_TYPE_FORMULA);
					
					ArrayList<String> list = new ArrayList<String>();
					String colidx_str = ExcelUtil.getColIndexStr(cell.getColumnIndex()+1);
					
					for(int k = 0 ; k < data_total_cnt ; k++) {
						int sum_row_idx = (4+i) + (sub_length * (k+1));
						list.add(colidx_str+sum_row_idx);
					}
					
					String sum_str = Arrays.toString(list.toArray()).replace("[", "").replace("]", "");
					sum_str = "=SUM("+sum_str+")";
					cell.setCellFormula(sum_str);
				}
			}
		}
		
		//셀 병합
		cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, "A", 4, "A", (4 + col_sub_title_str_arr.length -1)));
		sheet.addMergedRegion(cra);
	}

	@Override
	public void makeRowContents(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>> row_data) {
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		
		Font sum_font = ExcelUtil.getFont(wb, "맑은 고딕", (short)11, false, false);
		String[] col_sub_title_str_arr = header_data.get("col_sub_title_str").split(",");
		String[] col_title_str_arr = header_data.get("col_title_str").split(",");
		String[] col_sub_title_key_arr = header_data.get("col_sub_title_key").split(",");
		
		
		/*
		 * apache poi의 인자는 0부터시작한다(0->1행,1->2행.....)
		 */
		Cell cell = null;
		for(int row_cnt = 0,row_len = row_data.size() ; row_cnt < row_len ; row_cnt++) { //데이터 개수 
			int base_row_num = 4+(col_sub_title_str_arr.length * (row_cnt+1)); //해당 데이터의 시작 행 번호
			HashMap<String,String> data = row_data.get(row_cnt);
			
			for(int sub = 0, sub_length = col_sub_title_str_arr.length ; sub < sub_length ; sub++) { //세부 제목
				int row_num = (base_row_num-1) + sub; //실제 행번호
				Row row = sheet.createRow(row_num);
				String key_first = col_sub_title_key_arr[sub];
				
				for(int title_idx = 0,title_len = col_title_str_arr.length ; title_idx < title_len ; title_idx++) { //상단 제목행에 맞춰서 실데이터 배치
					cell = row.createCell(title_idx);
					
					CellStyle col_title_style = wb.createCellStyle(); //cell.getCellStyle()로는 정상작동이 안됨
					ExcelUtil.cellDiffLine(col_title_style, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
					col_title_style.setFont(sum_font);
					col_title_style.setAlignment(CellStyle.ALIGN_CENTER);
					col_title_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					
					cell.setCellStyle(col_title_style);
					
					if(title_idx == 0) { //구분
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue("클럽명"+row_cnt);
					}else if(title_idx == 1) { //실적 세부제목
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(col_sub_title_str_arr[sub]);
					}else if(title_idx == 2) { //총계
						cell.setCellType(Cell.CELL_TYPE_FORMULA);
						String sum_str = "=SUM("+String.format(sum_range_format_str, "D", (row_num+1), "O", (row_num+1))+")";
						cell.setCellFormula(sum_str);
					}else { //실 데이터
						if(key_first.equals("[sum]")) { //클럽별 소계의 경우 수동으로 수식을 집어넣도록 처리
							int r = cell.getRow().getRowNum()+1;
							int c = cell.getColumnIndex()+1;
							
							String col_idx_str = ExcelUtil.getColIndexStr(c);
							
							cell.setCellType(Cell.CELL_TYPE_FORMULA);
							cell.setCellFormula("=SUM("+String.format(sum_range_format_str, col_idx_str, r+1, col_idx_str, r+5)+")");
						}else {
							String hash_key = key_first+(title_idx-2);
							String value = data.get(hash_key);
							int val = (value == null) ? 0 : Integer.parseInt(value);
							
						
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							cell.setCellValue(val);
						}
					}
				}
			}
			
			//셀 병합
			CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, "A", base_row_num, "A", ((base_row_num-1) + col_sub_title_str_arr.length)));
			sheet.addMergedRegion(cra);
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
