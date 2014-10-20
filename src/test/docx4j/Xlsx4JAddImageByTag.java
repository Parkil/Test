package test.docx4j;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.docx4j.dml.spreadsheetdrawing.CTTwoCellAnchor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.Parts;
import org.docx4j.openpackaging.parts.DrawingML.Drawing;
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.xlsx4j.sml.CTXstringWhitespace;
import org.xlsx4j.sml.Cell;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.STCellType;
import org.xlsx4j.sml.SheetData;
import org.xlsx4j.sml.Worksheet;

/*
 * excel에 [image] tag를 입력하면 그부분에 이미지를 입력하는 예제
 */
public class Xlsx4JAddImageByTag {
	/**Cell안의 내용을 반환 
	 * @param cell Cell 객체 
	 * @param sharedStrings 숫자을 제외한 문자열을 저장하고 있는 SharedStrings객체
	 * @return Cell 내용
	 * @throws Docx4JException 
	 * @throws NumberFormatException 
	 */
	private static String getCellValue(Cell cell, SharedStrings sharedStrings) throws NumberFormatException, Docx4JException {
		if(sharedStrings == null) {
			return null;
		}
		
		String value = null;
		if (cell.getT().equals(STCellType.S)) { //문자열 값 반환
			CTXstringWhitespace temp = sharedStrings.getContents().getSi().get(Integer.parseInt(cell.getV())).getT();
			value = temp.getValue();
		} else {
			value = cell.getV(); //순수한 숫자로 이루어진 값 반환
		}
		
		return value;
	}
	
	public static void main(String[] args) throws Exception {
		XlsxDrawing xd = new XlsxDrawing();
		
		Logger.getRootLogger().setLevel(Level.ERROR); //docx4j 수행시 나오는 log를 나오지 않게 설정
		String path = "d:/test.xlsx";
		
		SpreadsheetMLPackage xlsxPkg = SpreadsheetMLPackage.load(new File(path));
		Parts ps = xlsxPkg.getParts();
		HashMap<PartName,Part> map = ps.getParts();
		
		Iterator<PartName> keyiter = map.keySet().iterator();
		
		SharedStrings sharedStrings = null;
		while(keyiter.hasNext()) {
			PartName pn = keyiter.next();
			Part p = map.get(pn);
			
			/*
			 * SharedStrings 지정(숫자외의 모든 문자는 셀안에 직접저장되는게 아니라 SharedStrings.xml에 따로 저장이 된다.)
			 */
			if(p instanceof SharedStrings) {
				sharedStrings = (SharedStrings)p;
			}
			
			ArrayList<CTTwoCellAnchor> anchorlist = new ArrayList<CTTwoCellAnchor>();
			/*
			 * 최초에 excel을 작성하게 되면 sheet1,sheet2,sheet3가 생성된는데
			 * 이게/xl/worksheets/sheet1.xml,/xl/worksheets/sheet2.xml .. 이런식으로 표시가 된다.
			 * 
			 * System.out.println(pn.getName()+" // "+p.getRelationshipsPart()); 로직을 사용하여 이름을 가져올수 있다.
			 */
			if(pn.getName().indexOf("sheet1.xml") != -1) {
				WorksheetPart wsp = (WorksheetPart)p;
				Worksheet ws = wsp.getContents();
				SheetData sd = ws.getSheetData();
				
				Drawing drawingPart = xd.initDrawing(wsp);
				
				for(Row r : sd.getRow()) {
					for(Cell c : r.getC()) {
						
						String cellvalue = getCellValue(c, sharedStrings);
						
						if(cellvalue != null && cellvalue.intern() == "[image]".intern()) {
							String cellloc = c.getR();
							
							ArrayList<String> idlist = xd.getImgFileId(wsp, drawingPart, "d:/jquery-chart1.png");
							
							for(String id : idlist) {
								CTTwoCellAnchor anchor = xd.getAnchor(id, cellloc, 10, 19);
								anchorlist.add(anchor);
							}
						}
					}
				}
				
				drawingPart.setJaxbElement(xd.setAnchor(anchorlist));
				break;
			}
		}
		
		xlsxPkg.save(new File("d:/test_imgadd.xlsx"));
	}
}
