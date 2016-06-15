package test.excel_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.ibm.db2.jcc.a.p;

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
		
	}
	
	@Override
	public void makeRowContents(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>>... row_data) {
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		
		//타이틀 관련 스타일
		CellStyle title_style = wb.createCellStyle(); 
		title_style.setFont(ExcelUtil.getFont(wb, "맑은 고딕", (short)9, false, true));
		title_style.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
		title_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		title_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		CellStyle value_style = wb.createCellStyle();
		value_style.setFont(ExcelUtil.getFont(wb, "맑은 고딕", (short)9, false, false));
		value_style.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
		
		List<HashMap<String, String>> first_data = row_data[0];
		HashMap<String,String> data = first_data.get(0);
		//[클럽명 ~ 시설명,시설소유자... 행]
		//<> : title, [] : value, empty : value+값 없음, merge[A-1-B-1][] : 병합처리후 value값 입력, pass : 미처리, 그외 empty처리 
		String title[][] = {
			{"<클럽명>", "[club1]", "<클럽창립일>", "[club2]", "<대표자명>", "[club3]", "<홈페이지>", "[club4]"},
			{"<클럽주소>", "merge[B-4-D-4][club5]", "pass", "pass", "<클럽대표번호>", "[club6]", "<법인등록번호>", "[club7]"},
			{"<회원수>","[club8]","<(남)>","[club9]","<(여)>","[club10]","<사업자 등록번호>","[club11]"},
			{"<운영인력>","[club12]","<이사>","[club13]","<감사>","[club14]","<운영위원>","[club15]"},
			{"<사무국장+지도자+사무원>","<지도자>","<Fulltime>","[club16]","<현역선수/은퇴선수>","[club17]","<자격증소지자>","[club18]"},
			{"","","<Parttime>","[club19]","<현역선수/은퇴선수>","[club20]","<자격증소지자>","[club21]"},
			{"","","<자원봉사자>","[club22]","<현역선수/은퇴선수>","[club23]","<자격증소지자>","[club24]"},
			{"","<사무원>","<Fulltime>","[club25]","<Parttime>","[club26]","<자원봉사자>","[club27]"},
			{"<시설명>","<시설소유자>","<시설계약자>","<시설계약형태>","<계약기간>","<수수료유형>","<값>","<기준>"}
		};
		
		Pattern title_pattern = Pattern.compile("^\\<(.*)\\>$");
		Pattern value_pattern = Pattern.compile("^\\[(.*)\\]$");
		Pattern merge_pattern = Pattern.compile("^merge\\[(.*)\\]\\[(.*)\\]$");
		
		Cell cell = null;
		Row row = null;
		short top_line = 0;
		short right_line = 0;
		for(int i = 0,outer_len=title.length ; i<outer_len ; i++) {
			String[] row_arr = title[i];
			row = sheet.createRow(2 + i);
			
			top_line = (i == 0) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
			
			for(int j = 0,inner_len = row_arr.length ; j<inner_len ; j++) {
				right_line = (j == inner_len-1) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
				
				String temp = row_arr[j];
				cell = row.createCell(j);
				
				CellStyle temp_style = wb.createCellStyle();
				
				if(temp.matches(title_pattern.pattern())) { //타이틀
					temp_style.cloneStyleFrom(title_style);
					
					Matcher m = title_pattern.matcher(temp);
					m.find();
					cell.setCellValue(m.group(1));
					cell.setCellStyle(temp_style);
					ExcelUtil.cellDiffLine(temp_style, top_line, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line);
				}else if(temp.matches(value_pattern.pattern())) { //값
					temp_style.cloneStyleFrom(value_style);
					
					Matcher m = value_pattern.matcher(temp);
					m.find();
					cell.setCellValue(data.get(m.group(1)));
					cell.setCellStyle(temp_style);
					ExcelUtil.cellDiffLine(temp_style, top_line, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line);
				}else if(temp.matches(merge_pattern.pattern())) { //병합
					temp_style.cloneStyleFrom(value_style);
					Matcher m = merge_pattern.matcher(temp);
					m.find();
					
					String[] merge_range = m.group(1).split("-");
					cell.setCellValue(data.get(m.group(2)));
					cell.setCellStyle(temp_style);
					
					CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, merge_range[0], merge_range[1], merge_range[2], merge_range[3]));
					sheet.addMergedRegion(cra);
					ExcelUtil.mergeCellDiffLine(cra, sheet, top_line, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line);
				}else if(temp.equals("empty")) { //비어있는 칸
					temp_style.cloneStyleFrom(value_style);
					
					cell.setCellStyle(temp_style);
					ExcelUtil.cellDiffLine(temp_style, top_line, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line);
				}else if(temp.equals("pass")) { //패스
					continue;
				}else { //그외
					temp_style.cloneStyleFrom(value_style);
					
					cell.setCellStyle(temp_style);
					ExcelUtil.cellDiffLine(temp_style, top_line, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line);
				}
			}
		}
		
		//[시설정보 리스트]
		List<HashMap<String, String>> second_data = row_data[1];
		String[] title_2nd_arr = header_data.get("col_title_key2").split(",");
		for(int i = 0,len=second_data.size() ; i<len ; i++) {
			row = sheet.createRow(sheet.getLastRowNum()+1);
			HashMap<String, String> temp = second_data.get(i);
			
			for(int j = 0,inner_len = title_2nd_arr.length ; j<inner_len ; j++) {
				CellStyle temp_style = wb.createCellStyle();
				temp_style.cloneStyleFrom(value_style);
				
				right_line = (j == inner_len-1) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
				cell = row.createCell(j);
				cell.setCellStyle(temp_style);
				cell.setCellValue(temp.get(title_2nd_arr[j]));
				ExcelUtil.cellDiffLine(temp_style, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line);
			}
		}
		
		//[보험정보]
		List<HashMap<String, String>> third_data = row_data[2];
		String[] title_3nd_arr = header_data.get("col_title_key3").split(",");
		for(int i = 1 ; i<=third_data.size() ; i+=2) {
			row = sheet.createRow(sheet.getLastRowNum()+1);
			
			for(int j = 0 ; j<8 ; j++) {
				int list_idx = (i-1)+(j/4);
				int hashmap_idx = j%4;
				
				String value = "";
				if(hashmap_idx == 0) {
					value = "보험"+(list_idx+1);
				}else {
					if(list_idx < third_data.size()) {
						value = third_data.get(list_idx).get(title_3nd_arr[hashmap_idx-1]);
					}
				}
				
				CellStyle temp_style = wb.createCellStyle();
				temp_style.cloneStyleFrom(value_style);
				
				right_line = (j == 7) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
				cell = row.createCell(j);
				cell.setCellStyle(temp_style);
				cell.setCellValue(value);
				ExcelUtil.cellDiffLine(temp_style, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line);
			}
		}
		
		//[종목정보]
		List<HashMap<String, String>> fourth_data = row_data[3];
		int start_row_num = sheet.getLastRowNum()+1;
		for(int i = 0 ; i<fourth_data.size() ; i+=7) {
			System.out.println(i);
			row = sheet.createRow(sheet.getLastRowNum()+1);
			
			short bottom_line = (i+7 > fourth_data.size()) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
			
			String title_value = "운영종목\n\r("+fourth_data.get(0).get("tot_cnt")+"종목)";
			String value = "";
			for(int j = 0 ; j<8 ; j++) {
				if(j == 0) {
					value = title_value;
				}else {
					if((i+j)-1 < fourth_data.size()) {
						value = fourth_data.get((i+j)-1).get("name");
					}else {
						value = "";
					}
				}
				
				CellStyle temp_style = wb.createCellStyle();
				temp_style.cloneStyleFrom(value_style);
				temp_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
				
				right_line = (j == 7) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
				cell = row.createCell(j);
				cell.setCellStyle(temp_style);
				cell.setCellValue(value);
				ExcelUtil.cellDiffLine(temp_style, CellStyle.BORDER_THIN, bottom_line, CellStyle.BORDER_THIN, right_line);
			}
		}
		
		//운영정보 병합처리
		int end_row_num = sheet.getLastRowNum()+1;
		CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, "A", start_row_num+1, "A", end_row_num));
		sheet.addMergedRegion(cra); //셀 병합처리
	}

	@Override
	public void afterHandle(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>> row_data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterHandle(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>>... row_data) {
		for (int cellnum = 0; cellnum < 8; cellnum++) {
			sheet.setColumnWidth(cellnum, 4800);
		}
	}
}
