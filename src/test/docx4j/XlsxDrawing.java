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
	
	
	/** WorksheetPart(���� ��Ʈ ��ü)�� �̹����� �����Ҽ� �ֵ��� drawing��ü�� �ʱ�ȭ�Ѵ�
	 * @param wsp ���� ��Ʈ�� ǥ���ϴ� ��ü
	 * @return drawing ��ü
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
	
	/** �̹��������� �о�鿩 �̹��� ���� ID�� ��ȯ�Ѵ�. �� id�� ���߿� drawing content�� �����Ҷ� ����ϰ� �ȴ�.
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
	
	/**���� ID, ��ġ,ũ�������� ������� CTTwoCellAnchor��ü�� ��ȯ�Ѵ�.
	 * ���߿� �̰�ü�� drawing�� �����Ͽ� �̹����� �����Ѵ�.
	 * @param imgRelID getImgFileId()���� ������ ����ID
	 * @param celloc ���� �� ��ġ����(C4,A3.....)
	 * @param widthbycell �̹��� �ʺ�(�� ����)
	 * @param heightbycell �̹��� ����(�� ����)
	 * @return CTTwoCellAnchor ��ü
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
		blip.setEmbed(imgRelID); //getImgFileId()���� ������ ID�� ����
		
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

		// �̹��� ������
		CTMarker from = dmlspreadsheetdrawingObjectFactory.createCTMarker(); 
		twocellanchor.setFrom(from);
		from.setRow(cellrow);
		from.setRowOff( 0 );
		from.setCol(cellcol);
		from.setColOff( 0 );
		
				
		//�̹��� ������
		CTMarker to = dmlspreadsheetdrawingObjectFactory.createCTMarker(); 
		twocellanchor.setTo(to);
		to.setRow(heightbycell);
		to.setRowOff( 0 );
		to.setCol(widthbycell);
		to.setColOff( 0 );
		
		twocellanchor.setEditAs(org.docx4j.dml.spreadsheetdrawing.STEditAs.ONE_CELL);
		
		return twocellanchor;
	}
	
	
	/** CTTwoCellAnchor �� ������ CTDrawing��ü�� ��ȯ
	 * @param anchorlist CTTwoCellAnchor ��ü ����Ʈ
	 * @return CTDrawing��ü
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
