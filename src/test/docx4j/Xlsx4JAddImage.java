package test.docx4j;
import java.io.File;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.docx4j.XmlUtils;
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
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.DrawingML.Drawing;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;

/**
 * Example showing how to add an image to a worksheet
 * 
 * @author jharrop
 * 
 * docx4j를 이용하여 엑셀파일에 이미지를 추가하는 예제
 *
 */
public class Xlsx4JAddImage {

	public static void main(String[] args) throws Exception {
		String outputfilepath	= "d:/OUT_Xlsx4JAddImage.xlsx";
		String imagefilePath	= "d:/jquery-chart1.png";
		String imagefilePath2	= "d:/dice.png";


		SpreadsheetMLPackage pkg = SpreadsheetMLPackage.createPackage();
		WorksheetPart worksheet = pkg.createWorksheetPart(new PartName("/xl/worksheets/sheet1.xml"), "Sheet1", 1);

		// Create Drawing part and add to sheet
		Drawing drawingPart = new Drawing();
		Relationship drawingRel = worksheet.addTargetPart(drawingPart);

		// Add anchor XML to worksheet
		org.xlsx4j.sml.CTDrawing drawing = org.xlsx4j.jaxb.Context.getsmlObjectFactory().createCTDrawing();
		worksheet.getJaxbElement().setDrawing(drawing);
		drawing.setId(drawingRel.getId());

		// Create image part and add to Drawing part
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(pkg, drawingPart,
				FileUtils.readFileToByteArray(new File(imagefilePath)));
		String imageRelID = imagePart.getSourceRelationship().getId();
		
		BinaryPartAbstractImage imagePart2 = BinaryPartAbstractImage.createImagePart(pkg, drawingPart,
				FileUtils.readFileToByteArray(new File(imagefilePath2)));
		String imageRelID2 = imagePart2.getSourceRelationship().getId();

		//단일 이미지 업로드
		//drawingPart.setJaxbElement(buildDrawingPartContentUsingCode(imageRelID));
		
		//다중 이미지 업로드
		drawingPart.setJaxbElement(buildMultiDrawingPartContentUsingCode(imageRelID, imageRelID2));
		
		// Save the xlsx
		SaveToZipFile saver = new SaveToZipFile(pkg);
		saver.save(outputfilepath);
		System.out.println("\n\n done .. " + outputfilepath);
	}
	
	/**여러개의 이미지를 삽입하는 예제 현재는 동일한 위치에 이미지가 삽입되며 다른위치에 이미지를 삽입하게 하려면 CTMarker마다 다른값을 주어야 함
	 * @param id
	 * @return
	 */
	public static CTDrawing buildMultiDrawingPartContentUsingCode(String... id) {
		org.docx4j.dml.spreadsheetdrawing.ObjectFactory dmlspreadsheetdrawingObjectFactory = new org.docx4j.dml.spreadsheetdrawing.ObjectFactory();

		CTDrawing drawing = dmlspreadsheetdrawingObjectFactory.createCTDrawing();
		
		for(String idval : id) {
			CTTwoCellAnchor twocellanchor = dmlspreadsheetdrawingObjectFactory.createCTTwoCellAnchor();
			drawing.getEGAnchor().add(twocellanchor);
			
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
			blip.setEmbed(idval);
			
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
			nonvisualdrawingprops.setName("Picture 1");
			nonvisualdrawingprops.setId(2);
			
			// Create object for cNvPicPr
			CTNonVisualPictureProperties nonvisualpictureproperties = dmlObjectFactory.createCTNonVisualPictureProperties();
			picturenonvisual.setCNvPicPr(nonvisualpictureproperties);
			// Create object for picLocks
			CTPictureLocking picturelocking = dmlObjectFactory.createCTPictureLocking();
			nonvisualpictureproperties.setPicLocks(picturelocking);
			picture.setMacro("");

			CTMarker marker = dmlspreadsheetdrawingObjectFactory.createCTMarker(); 
			twocellanchor.setTo(marker); 
			marker.setCol(9+5);
			marker.setColOff( 0 );
			marker.setRow(18+5);
			marker.setRowOff( 0 );

			// 이미지 시작점
			CTMarker marker2 = dmlspreadsheetdrawingObjectFactory.createCTMarker(); 
			twocellanchor.setFrom(marker2); 
			marker2.setCol(5);
			marker2.setColOff( 0 );
			marker2.setRow(5);
			marker2.setRowOff( 0 );
			twocellanchor.setEditAs(org.docx4j.dml.spreadsheetdrawing.STEditAs.ONE_CELL);
		}
		
		return drawing;
	}
	
	/**
	 * This code generated using http://webapp.docx4java.org/OnlineDemo/PartsList.html
	 * "Method 1"
	 */
	public static CTDrawing buildDrawingPartContentUsingCode(String imageRelID) {

		org.docx4j.dml.spreadsheetdrawing.ObjectFactory dmlspreadsheetdrawingObjectFactory = new org.docx4j.dml.spreadsheetdrawing.ObjectFactory();

		CTDrawing drawing = dmlspreadsheetdrawingObjectFactory.createCTDrawing();
		// JAXBElement<org.docx4j.dml.spreadsheetdrawing.CTDrawing>
		// drawingWrapped =
		// dmlspreadsheetdrawingObjectFactory.createWsDr(drawing);
		// Create object for twoCellAnchor
		CTTwoCellAnchor twocellanchor = dmlspreadsheetdrawingObjectFactory.createCTTwoCellAnchor();
		drawing.getEGAnchor().add(twocellanchor);
		
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
		blip.setEmbed(imageRelID);
		
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
		nonvisualdrawingprops.setName("Picture 1");
		nonvisualdrawingprops.setId(2);
		
		// Create object for cNvPicPr
		CTNonVisualPictureProperties nonvisualpictureproperties = dmlObjectFactory.createCTNonVisualPictureProperties();
		picturenonvisual.setCNvPicPr(nonvisualpictureproperties);
		// Create object for picLocks
		CTPictureLocking picturelocking = dmlObjectFactory.createCTPictureLocking();
		nonvisualpictureproperties.setPicLocks(picturelocking);
		picture.setMacro("");

		/*
		 * 이미지 크기 지정
		 * marker.setCol(),setRow()			- 셀 크기에 기반한 이미지 지정
		 * marker.setColOff(),setRowOff()	- 미세지정(거의 50000 이상은 지정해야 육안으로 확인 가능)
		 * 
		 * ex)
		 * 	marker.setCol(9),setRow(18) - 셀 9 * 18만큼의 크기로 이미지크기를 조정(만약 셀크기를 임의로 늘렸다면 늘어난 셀 길이 만큼 이미지크기가 조정됨)
		 *  marker.setColOff(50000),setRowOff(50000) - setCol(),setRow()에서 지정한 크기에 지정한 만큼 이미지크기를 더 늘린다.
		 *  
		 *  위대로 지정했으면 이미지 크기는 
		 *  1.셀 9*18 크기로 이미지크기가 지정되고 
		 *  2.1번에서 지정된 크기에 추가로 2번에서 지정한 크기만큼 이미지 크기가 추가로 늘어난다.
		 *  
		 *  아래 로직은 이미지를 원하는 위치에 그리기 위하여 동일한 로직을 2번 실행한다.
		 *  이미지 시작점,종료점을 지정하면 되는데 주의점은 종료점지정시 원래크기에 시작점을 더해야 한다.
		 *  ex) 원래 이미지 크기 col-9 row-18
		 *  	이미지 시작점 col-5 row-5
		 *  위의 경우 종료점을 col-(9+5) row-(18+5)로 조정해 주어야 이미지가 정상적으로 표시된다.
		 */
            //이미지 종료점
		CTMarker marker = dmlspreadsheetdrawingObjectFactory.createCTMarker(); 
		twocellanchor.setTo(marker); 
		marker.setCol(9+5);
		marker.setColOff( 0 );
		marker.setRow(18+5);
		marker.setRowOff( 0 );

		// 이미지 시작점
		CTMarker marker2 = dmlspreadsheetdrawingObjectFactory.createCTMarker(); 
		twocellanchor.setFrom(marker2); 
		marker2.setCol(5);
		marker2.setColOff( 0 );
		marker2.setRow(5);
		marker2.setRowOff( 0 );
		twocellanchor.setEditAs(org.docx4j.dml.spreadsheetdrawing.STEditAs.ONE_CELL);

//		return drawingWrapped;
		return drawing;
	}

	/**
	 * This code generated using http://webapp.docx4java.org/OnlineDemo/PartsList.html
	 * "Method 2"
	 */
	public static CTDrawing buildDrawingPartContentFromXmlString(String imageRelID) throws JAXBException {
		
		String openXML = "<xdr:wsDr xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:a14=\"http://schemas.microsoft.com/office/drawing/2010/main\" xmlns:xdr=\"http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
				+ "<xdr:twoCellAnchor editAs=\"oneCell\">"
					+ "<xdr:from>" //이미지 시작점
						+ "<xdr:col>20</xdr:col>"
						+ "<xdr:colOff>0</xdr:colOff>"
						+ "<xdr:row>20</xdr:row>"
						+ "<xdr:rowOff>0</xdr:rowOff>"
					+"</xdr:from>"
					+ "<xdr:to>" //이미지 종료점
						+ "<xdr:col>27</xdr:col>"
						+ "<xdr:colOff>104775</xdr:colOff>"
						+ "<xdr:row>27</xdr:row>"
						+ "<xdr:rowOff>142875</xdr:rowOff>"
					+"</xdr:to>"
					+ "<xdr:pic>"
						+ "<xdr:nvPicPr>"
							+ "<xdr:cNvPr id=\"2\" name=\"Picture 1\"/>"
							+ "<xdr:cNvPicPr>"
								+ "<a:picLocks noChangeAspect=\"1\"/>"
								+"</xdr:cNvPicPr>"
					+"</xdr:nvPicPr>"
						+ "<xdr:blipFill>"
							+ "<a:blip r:embed=\"" + imageRelID + "\">"
								+ "<a:extLst>"
									+ "<a:ext uri=\"{28A0092B-C50C-407E-A947-70E740481C1C}\">"
										+ "<a14:useLocalDpi val=\"0\"/>"
										+"</a:ext>"
									+"</a:extLst>"
							+"</a:blip>"
							+ "<a:stretch>"
								+ "<a:fillRect/>"
							+"</a:stretch>"
						+"</xdr:blipFill>"
						+ "<xdr:spPr>"
							+ "<a:xfrm>"
								+ "<a:off x=\"0\" y=\"0\"/>"
								+ "<a:ext cx=\"714375\" cy=\"714375\"/>"
							+"</a:xfrm>"
							+ "<a:prstGeom prst=\"rect\">"
								+ "<a:avLst/>"
							+"</a:prstGeom>"
						+"</xdr:spPr>"
					+"</xdr:pic>"
					+ "<xdr:clientData/>"
				+"</xdr:twoCellAnchor>"
			+"</xdr:wsDr>";
		
		return (CTDrawing)XmlUtils.unwrap(XmlUtils.unmarshalString(openXML));
	}
	
}