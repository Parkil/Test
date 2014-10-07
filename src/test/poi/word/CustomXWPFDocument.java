package test.poi.word;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;

/*
 * poi를 이용하여 그림을 입력하도록하는 커스텀 클래스
 * 원래 poi를 이용하여 word에 이미지를 입력하는 로직은 다음과 같다
 *	XWPFDocument doc = new XWPFDocument();
	XWPFParagraph p = doc.createParagraph();
	
	XWPFRun r = p.createRun();
	
	r.addPicture(new FileInputStream("d:/2.jpg"), XWPFDocument.PICTURE_TYPE_JPEG, "d:/2.jpg", 400, 400); // 200x200 pixels
	FileOutputStream out = new FileOutputStream("d:/images.docx");
	doc.write(out);
	
	그러나 위의 로직을 이용하여 이미지를 입력하면 word파일을 열때 오류가 발생하는 버그가 있다
	(인터넷 검색시 poi 3.10(2014-10-06현재 안정적인 최신 버전)에서도 동일한 오류가 발생한다고 함)
	
	그래서 인터넷을 검색하여 찾은 클래스가 아래 로직임 메소드를 사용하지 않고 XML로직을 직접입력함
	테스트결과 이미지가 정상적으로 표시되는것을 확인하였음
 */
public class CustomXWPFDocument extends XWPFDocument {
	public CustomXWPFDocument() throws IOException {
		super();
	}
	
	public CustomXWPFDocument(InputStream is) throws IOException {
		super(is);
	}
	
	public CustomXWPFDocument(OPCPackage pkg) throws IOException {
		super(pkg);
	}
	
	/*
	 * 인터넷 소스에서 특정 paragraph에 이미지를 추가할 수 있도록 XWPFParagraph 인자를 임의로 추가
	 */
	public void createPicture(String blipId,int id, int width, int height, XWPFParagraph para) {
		final int EMU = 9525;
		width *= EMU;
		height *= EMU;
		//String blipId = getAllPictures().get(id).getPackageRelationship().getId();

		CTInline inline = null;
		
		if(para == null) {
			inline = createParagraph().createRun().getCTR().addNewDrawing().addNewInline();
		}else {
			inline = para.createRun().getCTR().addNewDrawing().addNewInline();
		}

		String picXml = "" +
				"<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
				"   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
				"      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
				"         <pic:nvPicPr>" +
				"            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>" +
				"            <pic:cNvPicPr/>" +
				"         </pic:nvPicPr>" +
				"         <pic:blipFill>" +
				"            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
				"            <a:stretch>" +
				"               <a:fillRect/>" +
				"            </a:stretch>" +
				"         </pic:blipFill>" +
				"         <pic:spPr>" +
				"            <a:xfrm>" +
				"               <a:off x=\"0\" y=\"0\"/>" +
				"               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>" +
				"            </a:xfrm>" +
				"            <a:prstGeom prst=\"rect\">" +
				"               <a:avLst/>" +
				"            </a:prstGeom>" +
				"         </pic:spPr>" +
				"      </pic:pic>" +
				"   </a:graphicData>" +
				"</a:graphic>";

		//CTGraphicalObjectData graphicData = inline.addNewGraphic().addNewGraphicData();
		XmlToken xmlToken = null;
		try {
			xmlToken = XmlToken.Factory.parse(picXml);
		} catch(XmlException xe) {
			xe.printStackTrace();
		}
		inline.set(xmlToken);
		//graphicData.set(xmlToken);

		inline.setDistT(0);
		inline.setDistB(0);
		inline.setDistL(0);
		inline.setDistR(0);

		CTPositiveSize2D extent = inline.addNewExtent();
		extent.setCx(width);
		extent.setCy(height);
 
		CTNonVisualDrawingProps docPr = inline.addNewDocPr();
		docPr.setId(id);
		docPr.setName("Picture " + id);
		docPr.setDescr("Generated");
	}
}
