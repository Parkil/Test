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
 * excel�� [image] tag�� �Է��ϸ� �׺κп� �̹����� �Է��ϴ� ����
 */
public class Xlsx4JAddImageByTag {
	/**Cell���� ������ ��ȯ 
	 * @param cell Cell ��ü 
	 * @param sharedStrings ������ ������ ���ڿ��� �����ϰ� �ִ� SharedStrings��ü
	 * @return Cell ����
	 * @throws Docx4JException 
	 * @throws NumberFormatException 
	 */
	private static String getCellValue(Cell cell, SharedStrings sharedStrings) throws NumberFormatException, Docx4JException {
		if(sharedStrings == null) {
			return null;
		}
		
		String value = null;
		if (cell.getT().equals(STCellType.S)) { //���ڿ� �� ��ȯ
			CTXstringWhitespace temp = sharedStrings.getContents().getSi().get(Integer.parseInt(cell.getV())).getT();
			value = temp.getValue();
		} else {
			value = cell.getV(); //������ ���ڷ� �̷���� �� ��ȯ
		}
		
		return value;
	}
	
	public static void main(String[] args) throws Exception {
		XlsxDrawing xd = new XlsxDrawing();
		
		Logger.getRootLogger().setLevel(Level.ERROR); //docx4j ����� ������ log�� ������ �ʰ� ����
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
			 * SharedStrings ����(���ڿ��� ��� ���ڴ� ���ȿ� ��������Ǵ°� �ƴ϶� SharedStrings.xml�� ���� ������ �ȴ�.)
			 */
			if(p instanceof SharedStrings) {
				sharedStrings = (SharedStrings)p;
			}
			
			ArrayList<CTTwoCellAnchor> anchorlist = new ArrayList<CTTwoCellAnchor>();
			/*
			 * ���ʿ� excel�� �ۼ��ϰ� �Ǹ� sheet1,sheet2,sheet3�� �����ȴµ�
			 * �̰�/xl/worksheets/sheet1.xml,/xl/worksheets/sheet2.xml .. �̷������� ǥ�ð� �ȴ�.
			 * 
			 * System.out.println(pn.getName()+" // "+p.getRelationshipsPart()); ������ ����Ͽ� �̸��� �����ü� �ִ�.
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
