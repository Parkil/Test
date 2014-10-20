package test.docx4j;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.docx4j.dml.CTBlip;
import org.docx4j.dml.CTBlipFillProperties;
import org.docx4j.dml.CTGeomGuideList;
import org.docx4j.dml.CTNonVisualDrawingProps;
import org.docx4j.dml.CTNonVisualPictureProperties;
import org.docx4j.dml.CTOfficeArtExtension;
import org.docx4j.dml.CTOfficeArtExtensionList;
import org.docx4j.dml.CTPictureLocking;
import org.docx4j.dml.CTPoint2D;
import org.docx4j.dml.CTPositiveSize2D;
import org.docx4j.dml.CTPresetGeometry2D;
import org.docx4j.dml.CTRelativeRect;
import org.docx4j.dml.CTShapeProperties;
import org.docx4j.dml.CTStretchInfoProperties;
import org.docx4j.dml.CTTransform2D;
import org.docx4j.dml.spreadsheetdrawing.CTAnchorClientData;
import org.docx4j.dml.spreadsheetdrawing.CTDrawing;
import org.docx4j.dml.spreadsheetdrawing.CTMarker;
import org.docx4j.dml.spreadsheetdrawing.CTPicture;
import org.docx4j.dml.spreadsheetdrawing.CTPictureNonVisual;
import org.docx4j.dml.spreadsheetdrawing.CTTwoCellAnchor;
import org.docx4j.openpackaging.parts.DrawingML.Drawing;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.xlsx4j.jaxb.Context;


/*
 * docx4j xlsx image insert logic
 */
public class XlsxDrawing {
	Logger log = Logger.getLogger(getClass());
	
	
	/** WorksheetPart(엑셀 시트 객체)에 이미지를 삽입할수 있도록 drawing객체를 초기화한다
	 * @param wsp 엑셀 시트를 표시하는 객체
	 * @return drawing 객체
	 * @throws Exception
	 */
	public Drawing initDrawing(WorksheetPart wsp) throws Exception{
		// Create Drawing part and add to sheet 
		Drawing drawingPart = new Drawing();
		Relationship drawingRel = wsp.addTargetPart(drawingPart);

		// Add anchor XML to worksheet
		org.xlsx4j.sml.CTDrawing drawing = Context.getsmlObjectFactory().createCTDrawing();
		wsp.getContents().setDrawing(drawing);
		drawing.setId( drawingRel.getId());
		
		return drawingPart;
	}
	
	/** 이미지파일을 읽어들여 이미지 파일 ID를 반환한다. 이 id는 나중에 drawing content를 저장할때 사용하게 된다.
	 * @param wsp
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getImgFileId(WorksheetPart wsp, Drawing drawingPart, String path) throws Exception{
		ArrayList<String> idlist = new ArrayList<String>();
		
		File file = new File(path);
		if(!file.exists()) {
			throw new Exception("file not exits : "+path);
		}
			
		BinaryPartAbstractImage imagePart  = BinaryPartAbstractImage.createImagePart(wsp.getPackage(), drawingPart, 
				FileUtils.readFileToByteArray(file));
		List<Relationship> list = imagePart.getSourceRelationships();
		
		for(Relationship rel : list) {
			idlist.add(rel.getId());
		}
		
		return idlist;
	}
	
	/**파일 ID, 위치,크기정보를 기반으로 CTTwoCellAnchor객체를 반환한다.
	 * 나중에 이객체를 drawing에 저장하여 이미지를 삽입한다.
	 * @param imgRelID getImgFileId()에서 가져온 파일ID
	 * @param celloc 엑셀 셀 위치정보(C4,A3.....)
	 * @param widthbycell 이미지 너비(셀 기준)
	 * @param heightbycell 이미지 높이(셀 기준)
	 * @return CTTwoCellAnchor 객체
	 */
	public CTTwoCellAnchor getAnchor(String imgRelID, String celloc, int widthbycell, int heightbycell) {
		int cellloc[] = CalExcelRowCol.getExcelRowCol(celloc);
		
		org.docx4j.dml.spreadsheetdrawing.ObjectFactory dmlspreadsheetdrawingObjectFactory = new org.docx4j.dml.spreadsheetdrawing.ObjectFactory();
		CTTwoCellAnchor twocellanchor = dmlspreadsheetdrawingObjectFactory.createCTTwoCellAnchor();
		
		// Create object for clientData
		CTAnchorClientData anchorclientdata = dmlspreadsheetdrawingObjectFactory.createCTAnchorClientData();
		twocellanchor.setClientData(anchorclientdata);
		
		// Create object for pic
		CTPicture picture = dmlspreadsheetdrawingObjectFactory.createCTPicture();
		twocellanchor.setPic(picture);
		org.docx4j.dml.ObjectFactory dmlObjectFactory = new org.docx4j.dml.ObjectFactory();
		
		// Create object for blipFill
		CTBlipFillProperties blipfillproperties = dmlObjectFactory.createCTBlipFillProperties();
		picture.setBlipFill(blipfillproperties);
		
		// Create object for blip
		CTBlip blip = dmlObjectFactory.createCTBlip();
		blipfillproperties.setBlip(blip);
		blip.setCstate(org.docx4j.dml.STBlipCompression.NONE);
		blip.setEmbed(imgRelID); //getImgFileId()에서 가져온 ID를 지정
		
		// Create object for extLst
		CTOfficeArtExtensionList officeartextensionlist = dmlObjectFactory.createCTOfficeArtExtensionList();
		blip.setExtLst(officeartextensionlist);
		
		// Create object for ext
		CTOfficeArtExtension officeartextension = dmlObjectFactory.createCTOfficeArtExtension();
		officeartextensionlist.getExt().add(officeartextension);
		officeartextension.setUri("{28A0092B-C50C-407E-A947-70E740481C1C}");
		blip.setLink("");
		
		// Create object for stretch
		CTStretchInfoProperties stretchinfoproperties = dmlObjectFactory.createCTStretchInfoProperties();
		blipfillproperties.setStretch(stretchinfoproperties);
		
		// Create object for fillRect
		CTRelativeRect relativerect = dmlObjectFactory.createCTRelativeRect();
		stretchinfoproperties.setFillRect(relativerect);
		relativerect.setR(new Integer(0));
		relativerect.setT(new Integer(0));
		relativerect.setL(new Integer(0));
		relativerect.setB(new Integer(0));
		
		// Create object for spPr
		CTShapeProperties shapeproperties = dmlObjectFactory.createCTShapeProperties();
		picture.setSpPr(shapeproperties);
		
		// Create object for xfrm
		CTTransform2D transform2d = dmlObjectFactory.createCTTransform2D();
		shapeproperties.setXfrm(transform2d);
		transform2d.setRot(new Integer(0));
		
		// Create object for off
		CTPoint2D point2d = dmlObjectFactory.createCTPoint2D();
		transform2d.setOff(point2d);
		point2d.setY(4);
		point2d.setX(4);
		
		// Create object for ext
		CTPositiveSize2D positivesize2d = dmlObjectFactory.createCTPositiveSize2D();
		transform2d.setExt(positivesize2d);
		positivesize2d.setCx(500);
		positivesize2d.setCy(500);
		
		// Create object for prstGeom
		CTPresetGeometry2D presetgeometry2d = dmlObjectFactory.createCTPresetGeometry2D();
		shapeproperties.setPrstGeom(presetgeometry2d);
		
		// Create object for avLst
		CTGeomGuideList geomguidelist = dmlObjectFactory.createCTGeomGuideList();
		presetgeometry2d.setAvLst(geomguidelist);
		presetgeometry2d.setPrst(org.docx4j.dml.STShapeType.RECT);
		
		// Create object for nvPicPr
		CTPictureNonVisual picturenonvisual = dmlspreadsheetdrawingObjectFactory.createCTPictureNonVisual();
		picture.setNvPicPr(picturenonvisual);
		
		// Create object for cNvPr
		CTNonVisualDrawingProps nonvisualdrawingprops = dmlObjectFactory.createCTNonVisualDrawingProps();
		picturenonvisual.setCNvPr(nonvisualdrawingprops);
		nonvisualdrawingprops.setDescr("");
		nonvisualdrawingprops.setName("jquery chart image");
		nonvisualdrawingprops.setId(2);
		
		// Create object for cNvPicPr
		CTNonVisualPictureProperties nonvisualpictureproperties = dmlObjectFactory.createCTNonVisualPictureProperties();
		picturenonvisual.setCNvPicPr(nonvisualpictureproperties);
		// Create object for picLocks
		CTPictureLocking picturelocking = dmlObjectFactory.createCTPictureLocking();
		nonvisualpictureproperties.setPicLocks(picturelocking);
		picture.setMacro("");
		
		
		int cellrow = cellloc[0] - 1;
		int cellcol = cellloc[1] - 1;
		widthbycell = widthbycell	+ cellcol;
		heightbycell = heightbycell + cellrow;

		// 이미지 시작점
		CTMarker from = dmlspreadsheetdrawingObjectFactory.createCTMarker(); 
		twocellanchor.setFrom(from);
		from.setRow(cellrow);
		from.setRowOff( 0 );
		from.setCol(cellcol);
		from.setColOff( 0 );
		
				
		//이미지 종료점
		CTMarker to = dmlspreadsheetdrawingObjectFactory.createCTMarker(); 
		twocellanchor.setTo(to);
		to.setRow(heightbycell);
		to.setRowOff( 0 );
		to.setCol(widthbycell);
		to.setColOff( 0 );
		
		twocellanchor.setEditAs(org.docx4j.dml.spreadsheetdrawing.STEditAs.ONE_CELL);
		
		return twocellanchor;
	}
	
	
	/** CTTwoCellAnchor 를 저장한 CTDrawing객체를 반환
	 * @param anchorlist CTTwoCellAnchor 객체 리스트
	 * @return CTDrawing객체
	 */
	public CTDrawing setAnchor(ArrayList<CTTwoCellAnchor> anchorlist) {
		org.docx4j.dml.spreadsheetdrawing.ObjectFactory dmlspreadsheetdrawingObjectFactory = new org.docx4j.dml.spreadsheetdrawing.ObjectFactory();
		CTDrawing drawing = dmlspreadsheetdrawingObjectFactory.createCTDrawing();
		
		for(CTTwoCellAnchor anchor : anchorlist) {
			drawing.getEGAnchor().add(anchor);
		}
		
		return drawing;
	}
}
