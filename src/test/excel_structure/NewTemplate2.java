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

//클럽 월별 실적 - 엘리트,가족회원,지도자,사무원,사무국장 부분은 나중에 구현
public class NewTemplate2 implements ExcelTemplate {
	private String merge_range_format_str	= "$%1$s$%2$s:$%3$s$%4$s"; //병합 문자열 포맷
	
	@Override
	public void makeHeader(Sheet sheet, Map<String, String> header_data) {
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		
		//[최상위 타이틀]
		Row title_row = sheet.createRow(0);
		Cell cell = title_row.createCell(0);
		cell.setCellValue(header_data.get("top_title"));
		
		CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, "A", "1", "Z", "1"));
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
		
		//[중간 타이틀]
		String title_row1[] = {"종목","운영시설","운영요일","지도회수","참가인원","회원","운영인력"};
		String title_row2[] = {"월회원","유료회원","무료회원","참여성별","참여계층","엘리트 배출현황","가족회원","계","지도자","사무원","사무국장"};
		String title_row3[] = {"남","여","1~19세","20대","30대","40대","50대","60대","Full time","Part time","자원봉사","Full time","Part time","자원봉사"};
		
		//제목이 들어갈 컬럼 위치
		int title_pos1[] = {0,1,2,3,4,5,18};
		int title_pos2[] = {5,6,7,8,10,16,17,18,19,22,25};
		int title_pos3[] = {8,9,10,11,12,13,14,15,19,20,21,22,23,24};
		
		//병합할 범위
		String merge_data[] = {
			"F-3-R-3","S-3-Z-3","A-3-A-5","B-3-B-5","C-3-C-5","D-3-D-5","E-3-E-5","F-4-F-5","G-4-G-5","H-4-H-5","I-4-J-4",
			"K-4-P-4","Q-4-Q-5","R-4-R-5","S-4-S-5","T-4-V-4","W-4-Y-4","Z-4-Z-5"
		};
		
		header_style.setFont(ExcelUtil.getFont(wb, "맑은 고딕", (short)9, false, true));
		header_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		title_row = sheet.createRow(2);
		for(int i = 0,len = title_row1.length ; i<len ; i++) {
			cell = title_row.createCell(title_pos1[i]);
			
			cell.setCellValue(title_row1[i]);
			cell.setCellStyle(header_style);
		}
		
		title_row = sheet.createRow(3);
		for(int i = 0 ; i<title_row2.length ; i++) {
			cell = title_row.createCell(title_pos2[i]);
			cell.setCellValue(title_row2[i]);
			cell.setCellStyle(header_style);
		}
		
		title_row = sheet.createRow(4);
		ExcelUtil.cellAllSameLine(header_style, CellStyle.BORDER_MEDIUM);
		for(int i = 0 ; i<title_row3.length ; i++) {
			cell = title_row.createCell(title_pos3[i]);
			cell.setCellValue(title_row3[i]);
			cell.setCellStyle(header_style);
		}
		
		ExcelUtil.multiMergeAllSameLine(merge_data, sheet, CellStyle.BORDER_MEDIUM);
		
		//[소계,스포츠클럽 사무처 직원]
		CellStyle sum_style = wb.createCellStyle();
		sum_style.cloneStyleFrom(header_style);
		sum_style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		sum_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		title_row = sheet.createRow(5);
		for(int i = 0 ; i<26 ; i++) {
			cell = title_row.createCell(i);
			if(i == 0) {
				cell.setCellValue("소계");
			}
			cell.setCellStyle(sum_style);
		}
		
		
		title_row = sheet.createRow(6);
		for(int i = 0 ; i<26 ; i++) {
			cell = title_row.createCell(i);
			if(i == 0) {
				cell.setCellValue("스포츠클럽 사무처 직원");
			}
			cell.setCellStyle(sum_style);
		}
		
		String merge_data2[] = {"A-6-C-6","A-7-C-7"};
		ExcelUtil.multiMergeAllSameLine(merge_data2, sheet, CellStyle.BORDER_MEDIUM);
	}

	@Override
	public void makeRowContents(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>> row_data) {
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
	}

	@Override
	public void afterHandle(Sheet sheet, Map<String, String> raw_data) {
		for (int cellnum = 0; cellnum < 14; cellnum++) {
			sheet.autoSizeColumn(cellnum); //한글의 경우에는 약간잘리는 문제가 있음.
		}
	}
}
