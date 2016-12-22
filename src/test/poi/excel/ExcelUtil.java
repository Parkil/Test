package test.poi.excel;


import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class ExcelUtil {
	private static String merge_range_format_str = "$%1$s$%2$s:$%3$s$%4$s"; //병합 문자열 포맷
	
	//CellStyle을 재활용하기 위한 전역변수
	private static CellStyle setHeaderStyleNoBorder = null;
	private static CellStyle setHeaderStyleBorder = null;
	private static CellStyle setContentStyleNoBorder = null;
	private static CellStyle setContentStyleFirstLineBorder = null;
	private static CellStyle setContentStyleBorder = null;
	
	
	/**여러건의 merge를 순차적으로 처리
	 * @param merge_data A-1-B-1형식의 데이터
	 * @param sheet apahce poi
	 */
	public static List<CellRangeAddress> multiMerge(String[] merge_data, Sheet sheet) {
		
		List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
		for(String data : merge_data) {
			String temp[] = data.split("-");
			
			CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, temp[0], temp[1], temp[2], temp[3]));
			sheet.addMergedRegion(cra);
			list.add(cra);
		}
		
		return list;
	}
	
	
	/**여러건의 merge를 순차적으로 처리후 테두리선을 동일하게 처리
	 * @param merge_data A-1-B-1형식의 데이터
	 * @param sheet
	 * @param line_type 테두리선 유형  CellStyle.BORDER~ 변수 입력
	 */
	public static void multiMergeAllSameLine(String[] merge_data, Sheet sheet, short line_type) {
		
		for(String data : merge_data) {
			String temp[] = data.split("-");
			
			CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, temp[0], temp[1], temp[2], temp[3]));
			sheet.addMergedRegion(cra);
			mergeCellAllSameLine(cra, sheet, line_type);
		}
	}
	
	
	/**여러건의 merge를 순차적으로 처리후 테두리선을 다르게 처리
	 * @param merge_data A-1-B-1형식의 데이터
	 * @param sheet
	 * @param top_line_type 상단라인종류 CellStyle.BORDER~ 변수 입력
	 * @param bottom_line_type 하단라인종류 CellStyle.BORDER~ 변수 입력
	 * @param left_line_type 좌측라인종류 CellStyle.BORDER~ 변수 입력
	 * @param right_line_type 우측라인종류 CellStyle.BORDER~ 변수 입력
	 */
	public static void multiMergeAllDiffLine(String[] merge_data, Sheet sheet, short top_line_type, short bottom_line_type, short left_line_type, short right_line_type) {
		
		for(String data : merge_data) {
			String temp[] = data.split("-");
			
			CellRangeAddress cra = CellRangeAddress.valueOf(String.format(merge_range_format_str, temp[0], temp[1], temp[2], temp[3]));
			sheet.addMergedRegion(cra);
			mergeCellDiffLine(cra, sheet, top_line_type, bottom_line_type, left_line_type, right_line_type);
		}
	}
	
	/** 폰트 객체 반환
	 * @param workbook 해당 excel WorkBook 인터페이스
	 * @param fontName 폰트명
	 * @param font_size 폰트크기
	 * @param is_italic 해당 폰트를 이텔릭체로 표시할것인지 여부
	 * @param is_bold 해당 폰트를 볼드체로 표시할것인지 여부
	 * @return
	 */
	public static Font getFont(Workbook workbook, String fontName, short font_size, boolean is_italic, boolean is_bold) {
		Font font = workbook.createFont();
		XSSFFont xssFont = (XSSFFont) font;
		xssFont.setFontName(fontName);
		xssFont.setFontHeightInPoints(font_size); //폰트 크기 기정
		xssFont.setColor(IndexedColors.BLACK.getIndex());
		xssFont.setItalic(is_italic); //이탤릭체
		xssFont.setBold(is_bold); //볼드 지정
		
		return font;
	}
	
	/** 표준 폰트 반환(이텔릭,볼드 적용안함)
	 * @param workbook workbook 해당 excel WorkBook 인터페이스
	 * @param fontName 폰트명
	 * @param font_size 폰트크기
	 * @return
	 */
	public static Font getDefaultFont(Workbook workbook, String fontName, short font_size) {
		Font font = workbook.createFont();
		XSSFFont xssFont = (XSSFFont) font;
		xssFont.setFontName(fontName);
		xssFont.setFontHeightInPoints(font_size); //폰트 크기 기정
		xssFont.setColor(IndexedColors.BLACK.getIndex());
		xssFont.setItalic(false); //이탤릭체
		xssFont.setBold(false); //볼드 지정
		
		return font;
	}
	
	/**병합된 셀에 Line 처리 (4개면 모두 동일하게 처리)
	 * @param cra 병합된 셀의 범위
	 * @param sheet excel sheet객체 
	 * @param line_type 라인종류 CellStyle.BORDER~ 변수 입력
	 */
	public static void mergeCellAllSameLine(CellRangeAddress cra, Sheet sheet, short line_type) {
		RegionUtil.setBorderTop(line_type, cra, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(line_type, cra, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(line_type, cra, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(line_type, cra, sheet, sheet.getWorkbook());
	}
	
	/**병합된 셀에 Line처리(4개면을 다르게 처리)
	 * @param cra 병합된 셀의 범위
	 * @param sheet excel sheet객체 
	 * @param top_line_type 상단라인종류 CellStyle.BORDER~ 변수 입력
	 * @param bottom_line_type 하단라인종류 CellStyle.BORDER~ 변수 입력
	 * @param left_line_type 좌측라인종류 CellStyle.BORDER~ 변수 입력
	 * @param right_line_type 우측라인종류 CellStyle.BORDER~ 변수 입력
	 */
	public static void mergeCellDiffLine(CellRangeAddress cra, Sheet sheet, short top_line_type, short bottom_line_type, short left_line_type, short right_line_type) {
		RegionUtil.setBorderTop(top_line_type, cra, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(bottom_line_type, cra, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(left_line_type, cra, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(right_line_type, cra, sheet, sheet.getWorkbook());
	}
	
	/**단일 셀 라인처리  (4개면 모두 동일하게 처리)
	 * @param style cell style객체
	 * @param line_type 라인종류 CellStyle.BORDER~ 변수 입력
	 */
	public static void cellAllSameLine(CellStyle style, short line_type) {
		style.setBorderBottom(line_type);
		style.setBorderTop(line_type);
		style.setBorderRight(line_type);
		style.setBorderLeft(line_type);
	}
	
	/**단일 셀 라인처리(4개면을 다르게 처리)
	 * @param style cell style객체
	 * @param top_line_type 상단라인종류 CellStyle.BORDER~ 변수 입력
	 * @param bottom_line_type 하단라인종류 CellStyle.BORDER~ 변수 입력
	 * @param left_line_type 좌측라인종류 CellStyle.BORDER~ 변수 입력
	 * @param right_line_type 우측라인종류 CellStyle.BORDER~ 변수 입력
	 */
	public static void cellDiffLine(CellStyle style, short top_line_type, short bottom_line_type, short left_line_type, short right_line_type) {
		style.setBorderTop(top_line_type);
		style.setBorderBottom(bottom_line_type);
		style.setBorderLeft(left_line_type);
		style.setBorderRight(right_line_type);
	}
	
	/**셀 Border Color 지정(4개면을 동일하게 처리)
	 * @param style
	 * @param color
	 */
	public static void cellAllSameBorderColor(CellStyle style, short color) {
		style.setTopBorderColor(color);
		style.setBottomBorderColor(color);
		style.setLeftBorderColor(color);
		style.setRightBorderColor(color);
	}
	
	/**셀 Border Color 지정(4개면을 다르게 처리)
	 * @param style
	 * @param color
	 */
	public static void cellDiffBorderColor(CellStyle style, short top_color, short bottom_color, short left_color, short right_color) {
		style.setTopBorderColor(top_color);
		style.setBottomBorderColor(bottom_color);
		style.setLeftBorderColor(left_color);
		style.setRightBorderColor(right_color);
	}
	
	/**숫자를 excel의 컬럼 인덱스( A,B,AA....)로 변환
	 * @param index 컬럼 인덱스 숫자
	 * @return 컬럼인덱스문자열
	 */
	public static String getColIndexStr(int index) {
		String ret_str = "";
		if(index <= 26) {
			ret_str = String.valueOf((char)(64+index));
		}else {
			StringBuffer sb = new StringBuffer();
			
			int divider = 0; //제수
			int reserve = 0; //나머지
			
			do {
				divider = index / 26;
				reserve = index % 26;
				index = divider;
				
				sb.append(String.valueOf((char)(64+reserve)));
			}while(divider > 26);
			
			sb.append(String.valueOf((char)(64+divider)));
			
			ret_str = sb.reverse().toString();
		}
		
		return ret_str;
	}
	
	/** Header 셀 스타일 지정(Border Line 없음)
	 * @param wb
	 * @param cell
	 */
	//multi thread환경에서 오작동을 하기 때문에 synchronized를 선언
	public synchronized static void setHeaderStyleNoBorder(Workbook wb, Cell cell) {
		if(setHeaderStyleNoBorder == null) {
			setHeaderStyleNoBorder = wb.createCellStyle();
			
			setHeaderStyleNoBorder.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
			setHeaderStyleNoBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			setHeaderStyleNoBorder.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			setHeaderStyleNoBorder.setFillPattern(CellStyle.SOLID_FOREGROUND);
			setHeaderStyleNoBorder.setFont(getFont(wb, "새굴림", (short)9, false, true)); //폰트지정
		}
		
		cell.setCellStyle(setHeaderStyleNoBorder);
	}
	
	/** Header 셀 스타일 지정(Border Line Medium)
	 * @param wb
	 * @param cell
	 */
	//multi thread환경에서 오작동을 하기 때문에 synchronized를 선언
	public synchronized static void setHeaderStyleBorder(Workbook wb, Cell cell) {
		if(setHeaderStyleBorder == null) {
			setHeaderStyleBorder = wb.createCellStyle();
			setHeaderStyleBorder.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
			setHeaderStyleBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			setHeaderStyleBorder.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			setHeaderStyleBorder.setFillPattern(CellStyle.SOLID_FOREGROUND);
			setHeaderStyleBorder.setFont(getFont(wb, "새굴림", (short)9, false, true)); //폰트지정
			cellAllSameLine(setHeaderStyleBorder, CellStyle.BORDER_MEDIUM);
		}
		
		cell.setCellStyle(setHeaderStyleBorder);
	}
	
	/** 내용 셀 스타일 지정(Border Line 없음)
	 * @param wb
	 * @param cell
	 */
	//multi thread환경에서 오작동을 하기 때문에 synchronized를 선언
	public synchronized static void setContentStyleNoBorder(Workbook wb, Cell cell) {
		if(setContentStyleNoBorder == null) {
			setContentStyleNoBorder = wb.createCellStyle();
			setContentStyleNoBorder.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
			setContentStyleNoBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			setContentStyleNoBorder.setFont(getFont(wb, "새굴림", (short)9, false, false)); //폰트지정
		}
		
		cell.setCellStyle(setContentStyleNoBorder);
	}
	
	/** 내용 셀 스타일 지정(Border Line 위 없음 - 제목행과 맞닿은 행에서 사용)
	 * @param wb
	 * @param cell
	 */
	//multi thread환경에서 오작동을 하기 때문에 synchronized를 선언
	public synchronized static void setContentStyleFirstLineBorder(Workbook wb, Cell cell) {
		if(setContentStyleFirstLineBorder == null) {
			setContentStyleFirstLineBorder = wb.createCellStyle();
			setContentStyleFirstLineBorder.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
			setContentStyleFirstLineBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			setContentStyleFirstLineBorder.setFont(getFont(wb, "새굴림", (short)9, false, false)); //폰트지정
			cellDiffLine(setContentStyleFirstLineBorder, CellStyle.BORDER_NONE, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		}
		
		cell.setCellStyle(setContentStyleFirstLineBorder);
	}
	
	/** 내용 셀 스타일 지정(Border Line 있음)
	 * @param wb
	 * @param cell
	 */
	//multi thread환경에서 오작동을 하기 때문에 synchronized를 선언
	public synchronized static void setContentStyleBorder(Workbook wb, Cell cell) {
		if(setContentStyleBorder == null) {
			setContentStyleBorder = wb.createCellStyle();
			setContentStyleBorder.setAlignment(CellStyle.ALIGN_CENTER); //중앙정렬
			setContentStyleBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			setContentStyleBorder.setFont(getFont(wb, "새굴림", (short)9, false, false)); //폰트지정
			cellAllSameLine(setContentStyleBorder, CellStyle.BORDER_THIN);
		}
		
		cell.setCellStyle(setContentStyleBorder);
	}
}
