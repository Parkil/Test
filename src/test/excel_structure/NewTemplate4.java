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

//손익계산서
public class NewTemplate4 implements ExcelTemplate {
	private String merge_range_format_str	= "$%1$s$%2$s:$%3$s$%4$s"; //병합 문자열 포맷
	
	@Override
	public void makeHeader(Sheet sheet, Map<String, String> header_data) {
		SXSSFWorkbook wb = (SXSSFWorkbook)sheet.getWorkbook();
		
		
		//[최상위 타이틀]
		Row title_row = sheet.createRow(0);
		Cell cell = title_row.createCell(0);
		cell.setCellValue(header_data.get("top_title"));
		
		CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, "A", "1", "P", "1"));
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
		cell = title_row.createCell(15);
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
			
			if(i == 0 || i == 1) {
				continue;
			}else {
				ExcelUtil.cellDiffLine(temp, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, right_line);
			}
		}
		
		cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, "A", "3", "B", "3"));
		sheet.addMergedRegion(cra); //셀 병합처리
		ExcelUtil.mergeCellDiffLine(cra, sheet, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
	}

	@Override
	public void makeRowContents(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>> row_data) {
		//손익계산서 
		//<> : title, [] : value, empty : value+값 없음, merge[A-1-B-1][] : 병합처리후 value값 입력, merge[A-1-B-1]<> : 병합처리후 타이틀 입력, pass : 미처리, 그외 empty처리, {} : 수식
		//sum,item,total
		String main_data[][] = {
			{"sum", "merge[A-4-B-4]<자체수입>","pass","empty","{=SUM(E4:P4)}","{=SUM(E5:E8)}","{=SUM(F5:F8)}","{=SUM(G5:G8)}","{=SUM(H5:H8)}","{=SUM(I5:I8)}","{=SUM(J5:J8)}","{=SUM(K5:K8)}","{=SUM(L5:L8)}","{=SUM(M5:M8)}","{=SUM(N5:N8)}","{=SUM(O5:O8)}","{=SUM(P5:P8)}"},
				{"item","","<강습료수익>","<당해년도 수입된 강습료총액+미수된 강습료 수익-전년도 미수된 강습료중 당해연도 입금된 강습료>","{=SUM(E5:P5)}","[R1-1]","[R1-2]","[R1-3]","[R1-4]","[R1-5]","[R1-6]","[R1-7]","[R1-8]","[R1-9]","[R1-10]","[R1-11]","[R1-12]"},
				{"item","","<사용료수익>","<헬스, 수영, 체육관 등 시설이용료로 계산식은 강습료수익과 동일하게 처리함>","{=SUM(E6:P6)}","[R2-1]","[R2-2]","[R2-3]","[R2-4]","[R2-5]","[R2-6]","[R2-7]","[R2-8]","[R2-9]","[R2-10]","[R2-11]","[R2-12]"},
				{"item","","<임대료수익>","<장소 임대에 따른 수익으로 계산식은 강습료수익과 동일하게 처리함>","{=SUM(E7:P7)}","[R3-1]","[R3-2]","[R3-3]","[R3-4]","[R3-5]","[R3-6]","[R3-7]","[R3-8]","[R3-9]","[R3-10]","[R3-11]","[R3-12]"},
				{"item","","<기타사업수익>","<기타 자체수익금액>","{=SUM(E8:P8)}","[R4-1]","[R4-2]","[R4-3]","[R4-4]","[R4-5]","[R4-6]","[R4-7]","[R4-8]","[R4-9]","[R4-10]","[R4-11]","[R4-12]"},
			{"sum", "merge[A-9-B-9]<사업비용>", "pass","empty","{=SUM(E9:P9)}","{=SUM(E10:E26)}","{=SUM(F10:F26)}","{=SUM(G10:G26)}","{=SUM(H10:H26)}","{=SUM(I10:I26)}","{=SUM(J10:J26)}","{=SUM(K10:K26)}","{=SUM(L10:L26)}","{=SUM(M10:M26)}","{=SUM(N10:N26)}","{=SUM(O10:O26)}","{=SUM(P10:P26)}"},
				{"item","","<급여>","<당해연도 정직원에 대한 급여총액>","{=SUM(E10:P10)}","[R5-1]","[R5-2]","[R5-3]","[R5-4]","[R5-5]","[R5-6]","[R5-7]","[R5-8]","[R5-9]","[R5-10]","[R5-11]","[R5-12]"},
				{"item","","<기타직보수>","<당해연도 계약직에 대한 급여총액>","{=SUM(E11:P11)}","[R6-1]","[R6-2]","[R6-3]","[R6-4]","[R6-5]","[R6-6]","[R6-7]","[R6-8]","[R6-9]","[R6-10]","[R6-11]","[R6-12]"},
				{"item","","<퇴직급여>","<퇴직금으로 적립된 금액+추가적인 퇴직금 지급액>","{=SUM(E12:P12)}","[R7-1]","[R7-2]","[R7-3]","[R7-4]","[R7-5]","[R7-6]","[R7-7]","[R7-8]","[R7-9]","[R7-10]","[R7-11]","[R7-12]"},
				{"item","","<잡급>","<시간강사, 자원봉사자 및  일용직등에게 지급한 금액>","{=SUM(E13:P13)}","[R8-1]","[R8-2]","[R8-3]","[R8-4]","[R8-5]","[R8-6]","[R8-7]","[R8-8]","[R8-9]","[R8-10]","[R8-11]","[R8-12]"},
				{"item","","<복리후생비>","<식비 및 기타 복리후생지출금액>","{=SUM(E14:P14)}","[R9-1]","[R9-2]","[R9-3]","[R9-4]","[R9-5]","[R9-6]","[R9-7]","[R9-8]","[R9-9]","[R9-10]","[R9-11]","[R9-12]"},
				{"item","","<여비교통비>","<출장관련해서 지급된 금액(식비등)>","{=SUM(E15:P15)}","[R10-1]","[R10-2]","[R10-3]","[R10-4]","[R10-5]","[R10-6]","[R10-7]","[R10-8]","[R10-9]","[R10-10]","[R10-11]","[R10-12]"},
				{"item","","<공공요금 및 제세>","<전기요금, 수도료등의 공공요금과 세금지급액>","{=SUM(E16:P16)}","[R11-1]","[R11-2]","[R11-3]","[R11-4]","[R11-5]","[R11-6]","[R11-7]","[R11-8]","[R11-9]","[R11-10]","[R11-11]","[R11-12]"},
				{"item","","<감가상각비>","<자체회계를 재원으로 취득한 고정자산에 대한 감가상각비>","{=SUM(E17:P17)}","[R12-1]","[R12-2]","[R12-3]","[R12-4]","[R12-5]","[R12-6]","[R12-7]","[R12-8]","[R12-9]","[R12-10]","[R12-11]","[R12-12]"},
				{"item","","<지급임차료>","<지급된 장소임대료, 차량임대료, 각종임대료 금액>","{=SUM(E18:P18)}","[R13-1]","[R13-2]","[R13-3]","[R13-4]","[R13-5]","[R13-6]","[R13-7]","[R13-8]","[R13-9]","[R13-10]","[R13-11]","[R13-12]"},
				{"item","","<보험료>","<당해연도 지급된 보험금액>","{=SUM(E19:P19)}","[R14-1]","[R14-2]","[R14-3]","[R14-4]","[R14-5]","[R14-6]","[R14-7]","[R14-8]","[R14-9]","[R14-10]","[R14-11]","[R14-12]"},
				{"item","","<차량유지비>","<차량관련하여 지출된 유류대, 통행료, 수선비등. 차량구매로 지급된 금액은 포함하지 아니함>","{=SUM(E20:P20)}","[R15-1]","[R15-2]","[R15-3]","[R15-4]","[R15-5]","[R15-6]","[R15-7]","[R15-8]","[R15-9]","[R15-10]","[R15-11]","[R15-12]"},
				{"item","","<교육훈련비>","<교육및훈련과 관련해서 지급된 금액>","{=SUM(E21:P21)}","[R16-1]","[R16-2]","[R16-3]","[R16-4]","[R16-5]","[R16-6]","[R16-7]","[R16-8]","[R16-9]","[R16-10]","[R16-11]","[R16-12]"},
				{"item","","<소모품비>","<자산으로 처리하지 않는 비품, 소모품 금액>","{=SUM(E22:P22)}","[R17-1]","[R17-2]","[R17-3]","[R17-4]","[R17-5]","[R17-6]","[R17-7]","[R17-8]","[R17-9]","[R17-10]","[R17-11]","[R17-12]"},
				{"item","","<지급수수료>","<각종 지급수수료 금액(수수료, 변호사비용, 회계비용, 은행이체료 등)>","{=SUM(E23:P23)}","[R18-1]","[R18-2]","[R18-3]","[R18-4]","[R18-5]","[R18-6]","[R18-7]","[R18-8]","[R18-9]","[R18-10]","[R18-11]","[R18-12]"},
				{"item","","<광고선전비>","<광고 및 홍보관련비용>","{=SUM(E24:P24)}","[R19-1]","[R19-2]","[R19-3]","[R19-4]","[R19-5]","[R19-6]","[R19-7]","[R19-8]","[R19-9]","[R19-10]","[R19-11]","[R19-12]"},
				{"item","","<업무추진비>","<업무진행목적으로 지출한 접대성격의 금액>","{=SUM(E25:P25)}","[R20-1]","[R20-2]","[R20-3]","[R20-4]","[R20-5]","[R20-6]","[R20-7]","[R20-8]","[R20-9]","[R20-10]","[R20-11]","[R20-12]"},
				{"item","","<기타운영비>","<사업비용중 위에 포함되지 않는 기타비용임.금액이 크지 않아야 함>","{=SUM(E26:P26)}","[R21-1]","[R21-2]","[R21-3]","[R21-4]","[R21-5]","[R21-6]","[R21-7]","[R21-8]","[R21-9]","[R21-10]","[R21-11]","[R21-12]"},
			{"sum", "merge[A-27-B-27]<사업총이익>","","","{=SUM(E27:P27)}","{=E4-E9}","{=F4-F9}","{=G4-G9}","{=H4-H9}","{=I4-I9}","{=J4-J9}","{=K4-K9}","{=L4-L9}","{=M4-M9}","{=N4-N9}","{=O4-O9}","{=P4-P9}"},
			{"sum", "merge[A-28-B-28]<사업외수입>","","","{=SUM(E29:E33)}","{=SUM(F29:F33)}","{=SUM(G29:G33)}","{=SUM(H29:H33)}","{=SUM(I29:I33)}","{=SUM(J29:J33)}","{=SUM(K29:K33)}","{=SUM(L29:L33)}","{=SUM(M29:M33)}","{=SUM(N29:N33)}","{=SUM(O29:O33)}","{=SUM(P29:P33)}","{=SUM(P29:P33)}"},
				{"item","","<기금보조금>","<당기기금보조금수령액-결산시점 미사용액(반납). 결산시점까지 사용금액만 입력함>","{=SUM(E29:P29)}","[R22-1]","[R22-2]","[R22-3]","[R22-4]","[R22-5]","[R22-6]","[R22-7]","[R22-8]","[R22-9]","[R22-10]","[R22-11]","[R22-12]"},
				{"item","","<국고보조금>","<지방비등 국고보조금: 당기국고보조금수령액-결산시점 미사용액(반납)>","{=SUM(E30:P30)}","[R23-1]","[R23-2]","[R23-3]","[R23-4]","[R23-5]","[R23-6]","[R23-7]","[R23-8]","[R23-9]","[R23-10]","[R23-11]","[R23-12]"},
				{"item","","<기부금수익>","<기부금 수령액>","{=SUM(E31:P31)}","[R24-1]","[R24-2]","[R24-3]","[R24-4]","[R24-5]","[R24-6]","[R24-7]","[R24-8]","[R24-9]","[R24-10]","[R24-11]","[R24-12]"},
				{"item","","<이자수익>","<이자수입-기금등 반환대상이자는 제외함. 기본재산 및 자체회계계좌의 발생한 이자수익을 계상함>","{=SUM(E32:P32)}","[R25-1]","[R25-2]","[R25-3]","[R25-4]","[R25-5]","[R25-6]","[R25-7]","[R25-8]","[R25-9]","[R25-10]","[R25-11]","[R25-12]"},
				{"item","","<기타수익>","<위의 수익을 제외한 나머지 수익>","{=SUM(E33:P33)}","[R26-1]","[R26-2]","[R26-3]","[R26-4]","[R26-5]","[R26-6]","[R26-7]","[R26-8]","[R26-9]","[R26-10]","[R26-11]","[R26-12]"},
			{"sum", "merge[A-34-B-34]<사업외비용>","","","{=SUM(E34:P34)}","{=SUM(E35:E36)}","{=SUM(F35:F36)}","{=SUM(G35:G36)}","{=SUM(H35:H36)}","{=SUM(I35:I36)}","{=SUM(J35:J36)}","{=SUM(K35:K36)}","{=SUM(L35:L36)}","{=SUM(M35:M36)}","{=SUM(N35:N36)}","{=SUM(O35:O36)}","{=SUM(P35:P36)}"},
				{"item","","<이자비용>","<이자비용>","{=SUM(E35:P35)}","[R27-1]","[R27-2]","[R27-3]","[R27-4]","[R27-5]","[R27-6]","[R27-7]","[R27-8]","[R27-9]","[R27-10]","[R27-11]","[R27-12]"},
				{"item","","<기타비용>","<기타 사업과 무관하게 발생한 비용>","{=SUM(E36:P36)}","[R28-1]","[R28-2]","[R28-3]","[R28-4]","[R28-5]","[R28-6]","[R28-7]","[R28-8]","[R28-9]","[R28-10]","[R28-11]","[R28-12]"},
			{"sum", "merge[A-37-B-37]<사업이익>","","","{=SUM(E37:P37)}","{=E27+E28-E34}","{=F27+F28-F34}","{=G27+G28-G34}","{=H27+H28-H34}","{=I27+I28-I34}","{=J27+J28-J34}","{=K27+K28-K34}","{=L27+L28-L34}","{=M27+M28-M34}","{=N27+N28-N34}","{=O27+O28-O34}","{=P27+P28-P34}"},
			{"total","merge[A-38-B-38]<법인세비용>","pass","","{=SUM(E37:P37)}","[R29-1]","[R29-2]","[R29-3]","[R29-4]","[R29-5]","[R29-6]","[R29-7]","[R29-8]","[R29-9]","[R29-10]","[R29-11]","[R29-12]"},
			{"sum", "merge[A-39-B-39]<당기순이익>","pass","","{=SUM(E39:P39)}","{=E37-E38}","{=F37-F38}","{=G37-G38}","{=H37-H38}","{=I37-I38}","{=J37-J38}","{=K37-K38}","{=L37-L38}","{=M37-M38}","{=N37-N38}","{=O37-O38}","{=P37-P38}"},
			{"total", "merge[A-40-B-40]<자립율>","pass", "<자체수익/사업비용 으로 연차경과시, 상승해야함>","{=D4/D9}","{=E4/E9}","{=F4/F9}","{=G4/G9}","{=H4/H9}","{=I4/I9}","{=J4/J9}","{=K4/K9}","{=L4/L9}","{=M4/M9}","{=N4/N9}","{=O4/O9}","{=P4/P9}"},
			{"total", "merge[A-41-B-41]<기본재산 예금잔액>","pass","<외부에 예치한 기본재산 예금잔액>","{=D4/D9}","{=E4/E9}","{=F4/F9}","{=G4/G9}","{=H4/H9}","{=I4/I9}","{=J4/J9}","{=K4/K9}","{=L4/L9}","{=M4/M9}","{=N4/N9}","{=O4/O9}","{=P4/P9}"}
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
				
				if(row_type.intern() == "sum".intern()) {
					temp_style.cloneStyleFrom(sup_style);
				} else if(row_type.intern() == "item".intern() && j==1) {
					temp_style.cloneStyleFrom(sup_style);
				} else {
					temp_style.cloneStyleFrom(sub_style);
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
		sheet.setColumnWidth(1, 4800);
		sheet.setColumnWidth(2, 20000);
	}

	@Override
	public void afterHandle(Sheet sheet, Map<String, String> header_data, List<HashMap<String, String>>... row_data) {
		// TODO Auto-generated method stub
		
	}

}
