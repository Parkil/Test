package test.excel_structure;

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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

//손익계산서
public class NewTemplate5 implements ExcelTemplate {
	private String merge_range_format_str	= "$%1$s$%2$s:$%3$s$%4$s"; //병합 문자열 포맷
	
	@Override
	public void makeHeader(Sheet sheet, Map<String, String> header_data) {
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		
		
		//[최상위 타이틀]
		Row title_row = sheet.createRow(0);
		Cell cell = title_row.createCell(0);
		cell.setCellValue(header_data.get("top_title"));
		
		CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, "A", "1", "F", "1"));
		sheet.addMergedRegion(cra); //셀 병합처리
		CellStyle header_style = wb.createCellStyle();
		
		//폰트 지정
		header_style.setFont(ExcelUtil.getFont(wb, "맑은 고딕", (short)22, false, true));
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
		
		//2번째 라인
		title_row = sheet.createRow(1);
		cell = title_row.createCell(5);
		cell.setCellValue("(단위 : 원)");
		cell.setCellStyle(header_style);
		
		//상단 주요 타이틀
		CellStyle title_style = wb.createCellStyle();
		title_style.cloneStyleFrom(header_style);
		title_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		title_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		title_style.setFont(ExcelUtil.getFont(wb, "맑은 고딕", (short)10, false, true));
		
		title_row = sheet.createRow(2);
		String col_title_arr[] = header_data.get("col_title").split(",");
		for(int i = 0,col_title_len = col_title_arr.length ; i<col_title_len ; i++) {
			short right_line = (i == col_title_len -1) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
			cell = title_row.createCell(i);
			cell.setCellValue(col_title_arr[i]);
			
			CellStyle temp = wb.createCellStyle();
			temp.cloneStyleFrom(title_style);
			
			cell.setCellStyle(temp);
			
			if(i == 0 || i == 1 || i == 2) {
				continue;
			}else {
				ExcelUtil.cellDiffLine(temp, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line);
			}
		}
		
		cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, "A", "3", "C", "3"));
		sheet.addMergedRegion(cra); //셀 병합처리
		ExcelUtil.mergeCellDiffLine(cra, sheet, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
	}

	@Override
	public void makeRowContents(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>> row_data) {
		//손익계산서 
		//<> : title, [] : value, empty : value+값 없음, merge[A-1-B-1][] : 병합처리후 value값 입력, merge[A-1-B-1]<> : 병합처리후 타이틀 입력, pass : 미처리, 그외 empty처리, {} : 수식
		//sum,item,total
		String main_data[][] = {
				{"sup_sum","merge[A-4-C-4]<자산>","","","","{=E5+E9}","{=F5+F9}"},
					{"sub_sum","","merge[B-5-C-5]<유동자산>","","","{=SUM(E6:E8)}","{=SUM(F6:F8)}"},
						{"item","","","<현금및현금성자산>","<결산일 현재 현금 및 보통예금 잔액>","[R1_1]","[R1_2]"},
						{"item","","","<미수금>","<강습료 중 결산일 현재 미납된 금액>","[R2_1]","[R2_2]"},
						{"item","","","<기타유동자산>","<결산일 현재 기타 미수된 금액과 사업자가 과세사업자인경우, 아직납부하지 않는 부가가치세 금액>","[R3_1]","[R3_2]"},
					{"sub_sum","","merge[B-9-C-9]<고정자산>","","","{=E10+E13+E24}","{=F10+F13+F24}"},
					{"sub_sum","","merge[B-10-C-10]<투자자산>","","","{=SUM(E11:E12)}","{=SUM(F11:F12)}"},
						{"item","","","<장기투자자산>","<정기적금등. 자유롭게 입출금이 불가능한 예금합계>","[R4_1]","[R4_2]"},
						{"item","","","<기타투자자산>","<기타 투자자산가액>","[R5_1]","[R5_2]"},
					{"sub_sum","","merge[B-13-C-13]<유형자산>","","","{=SUM(E14:E23)}","{=SUM(F14:F23)}"},
						{"item","","","<토지>","<법인소유의 토지금액>","[R5_1]","[R5_2]"},
						{"item","","","<건물>","<법인소유의 건물금액>","[R6_1]","[R6_2]"},
						{"item","","","<(건물국고보조금)>","<기금또는 국고로 건물을 구매한 경우, 건물가액 (-)로 표시함>","[R7_1]","[R7_2]"},
						{"item","","","<(건물감가상각누계액)>","","[R8_1]","[R8_2]"},
						{"item","","","<차량운반구>","<법인소유의 차량가액(할부로 구매시, 계약가액)>","[R9_1]","[R9_2]"},
						{"item","","","<(차량국고보조금)>","<기금 또는 국고로 차량을 구매한 경우, 차량가액 (-)로 표시함>","[R10_1]","[R10_2]"},
						{"item","","","<(차량운반구감가상각누계액)>","","[R11_1]","[R11_2]"},
						{"item","","","<공기구비품>","<사무비품 및 체육공기구의 총금액(컴퓨터, 사무가구 등)>","[R12_1]","[R12_2]"},
						{"item","","","<(국고보조금)>","<기금 또는 국고로 비품을 구매한 경우, 비품가액 (-)로 표시함>","[R13_1]","[R13_2]"},
						{"item","","","<비품감가상각누계액>","","[R14_1]","[R14_2]"},
					{"sub_sum","","merge[B-24-C-24]<무형자산>","","","{=SUM(E25:E27)}","{=SUM(F25:F27)}"},
						{"item","","","<소프트웨어>","<회원관리 및 회계시스템등 소프트웨어 구매금액>","[R14_1]","[R14_2]"},
						{"item","","","<(소프트웨어국고보조금)>","<기금 또는 국고로 소프트웨어를 구매한 경우, 소프트웨어가액(-)로 표시함>","[R15_1]","[R15_2]"},
						{"item","","","<(소프트웨어상각누계액)>","","[R16_1]","[R16_2]"},
				{"sup_sum","merge[A-28-C-28]<부채>","","","","{=E29+E34}","{=F29+F34}"},
					{"sub_sum","","merge[B-29-C-29]<유동부채>","","","{=SUM(E30:E32)}","{=SUM(F30:F32)}"},
						{"item","","","<미지급금>","<결산일 현재 미지급된 비용 및 구입한 자산 중 미지급된 금액(차량구매시, 아직지급되지 않는 할부잔액)>","[R17_1]","[R17_2]"},
						{"item","","","<미반납금>","<결산일 현재, 기금 및 국고 수입분 중 사용하지 않는 잔액과 기금계좌에서 발생한 이자수익>","[R18_1]","[R18_2]"},
						{"item","","","<예수금>","<결산일 현재, 지급되지 않는 4대보험료, 원천세미납분 등. 과세사업자의 경우, 매입부가세금액>","[R19_1]","[R19_2]"},
						{"item","","","<기타유동부채>","","[R20_1]","[R20_2]"},
					{"sub_sum","","merge[B-34-C-34]<고정부채>","","","{=SUM(E35:E36)}","{=SUM(F35:F36)}"},
						{"item","","","<퇴직급여충당금>","<결산일 현재, 직원이 일시에 퇴직하였을때 지급해야할 퇴직금 총액. DC또는 퇴직연금을 가입한 경우 기재하지 아니함>","[R20_1]","[R20_2]"},
						{"item","","","<(퇴직연금운용자산)>","<자체 예치한 퇴직금 통장잔액. DC 또는 퇴직연금을 가입한 경우 기재하지 아니함>","[R21_1]","[R21_2]"},
						{"item","","","<기타장기부채>","","[R22_1]","[R22_2]"},
				{"sup_sum","merge[A-38-C-38]<자본>","","","","{=E39+E41}","{=F39+F41}"},
					{"sub_sum","","merge[B-39-C-39]<출연금>","","","{=E40}","{=F40}"},
						{"item","","","<기본재산>","<출연금 총액>","[R23_1]","[R23_2]"},
					{"sub_sum","","merge[B-41-C-41]<이익잉여금>","","","{=E42}","{=F42}"},
						{"item","","","<미처분이익잉여금>","<당기순이익(또는 당기순손실) 누적합계액. 당기순손실은 (-)로 구분하여 합산함 총액>","[R24_1]","[R24_2]"},
				{"sup_sum","merge[A-43-C-43]<부채 및 자본>","","","","{=E28+E38}","{=F28+F38}"}
		};
		
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		Font row_font = ExcelUtil.getFont(wb, "맑은 고딕", (short)10, false, false);
		
		//대분류에 사용되는 스타일
		CellStyle sup_style = wb.createCellStyle();
		sup_style.setFont(row_font);
		sup_style.setAlignment(CellStyle.ALIGN_CENTER);
		sup_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		sup_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		sup_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		//대분류를 제외한 나머자에 사용되는 스타일
		CellStyle sub_style = wb.createCellStyle();
		sub_style.setFont(row_font);
		sub_style.setAlignment(CellStyle.ALIGN_CENTER);
		sub_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		Pattern title_pattern = Pattern.compile("^\\<(.*)\\>$");
		Pattern value_pattern = Pattern.compile("^\\[(.*)\\]$");
		Pattern formula_pattern = Pattern.compile("^\\{(.*)\\}$");
		Pattern merge_value_pattern = Pattern.compile("^merge\\[(.*)\\]\\[(.*)\\]$");
		Pattern merge_title_pattern = Pattern.compile("^merge\\[(.*)\\]\\<(.*)\\>$");
		
		HashMap<String,String> data_map = row_data.get(0);
		Row row = null;
		Cell cell = null;
		CellStyle temp_style = null;
		String number_regex = "^[0-9]{1,}$"; //숫자판단 정규식
		for(int i=0, outer_len = main_data.length ; i<outer_len ; i++) {
			String[] row_arr = main_data[i];
			row = sheet.createRow(3+i);
			String row_type = row_arr[0]; //해당 행의 유형
			short bottom_line = (i == outer_len -1) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
			
			for(int j=1, inner_len = row_arr.length ; j<inner_len ; j++) {
				short right_line = (j == inner_len-1) ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN;
				cell = row.createCell(j-1);
				temp_style = wb.createCellStyle();
				
				if(row_type.intern() == "sup_sum".intern() || row_type.intern() == "sub_sum".intern()) {
					temp_style.cloneStyleFrom(sup_style);
					
					if(row_type.intern() == "sub_sum".intern() && j == 1) {
						temp_style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
					}
				}else {
					temp_style.cloneStyleFrom(sub_style);
					
					if(j == 1) {
						temp_style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
						temp_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					}else if(j == 2) {
						temp_style.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
						temp_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					}
				}
				
				ExcelUtil.cellDiffLine(temp_style, CellStyle.BORDER_THIN, bottom_line, CellStyle.BORDER_THIN, right_line);
				cell.setCellStyle(temp_style);
				
				if(row_arr[j].matches(title_pattern.pattern())) { //제목
					Matcher m = title_pattern.matcher(row_arr[j]);
					m.find();
					if(m.group(1).matches(number_regex)) {
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					}
					cell.setCellValue(m.group(1));
				}else if(row_arr[j].matches(value_pattern.pattern())) { //값
					Matcher m = value_pattern.matcher(row_arr[j]);
					m.find();
					String key = m.group(1);
					String value = data_map.get(key);
					if(value.matches(number_regex)) {
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Integer.parseInt(value));
					}else {
						cell.setCellValue(value);
					}
				}else if(row_arr[j].matches(formula_pattern.pattern())) { //수식
					Matcher m = formula_pattern.matcher(row_arr[j]);
					m.find();
					cell.setCellType(Cell.CELL_TYPE_FORMULA);
					cell.setCellFormula(m.group(1));
				}else if(row_arr[j].matches(merge_value_pattern.pattern())) { //병합후 값 입력
					Matcher m = merge_value_pattern.matcher(row_arr[j]);
					m.find();
					if(m.group(2).matches(number_regex)) {
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					}
					cell.setCellValue(m.group(2));
					
					String merge_range[] = m.group(1).split("-");
					CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, merge_range[0], merge_range[1], merge_range[2], merge_range[3]));
					sheet.addMergedRegion(cra);
					ExcelUtil.mergeCellDiffLine(cra, sheet, CellStyle.BORDER_THIN, bottom_line, CellStyle.BORDER_THIN, right_line);
					
				}else if(row_arr[j].matches(merge_title_pattern.pattern())) { //병합후 제목 입력
					Matcher m = merge_title_pattern.matcher(row_arr[j]);
					m.find();
					cell.setCellValue(m.group(2));
					
					String merge_range[] = m.group(1).split("-");
					CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, merge_range[0], merge_range[1], merge_range[2], merge_range[3]));
					sheet.addMergedRegion(cra);
					ExcelUtil.mergeCellDiffLine(cra, sheet, CellStyle.BORDER_THIN, bottom_line, CellStyle.BORDER_THIN, right_line);
				}
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
		sheet.setColumnWidth(0, 1500);
		sheet.setColumnWidth(1, 2000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 23000);
	}

	@Override
	public void afterHandle(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>>... row_data) {
		// TODO Auto-generated method stub
		
	}

}
