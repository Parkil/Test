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
 * poi�� �̿��Ͽ� �׸��� �Է��ϵ����ϴ� Ŀ���� Ŭ����
 * ���� poi�� �̿��Ͽ� word�� �̹����� �Է��ϴ� ������ ������ ����
 *	XWPFDocument doc = new XWPFDocument();
	XWPFParagraph p = doc.createParagraph();
	
	XWPFRun r = p.createRun();
	
	r.addPicture(new FileInputStream("d:/2.jpg"), XWPFDocument.PICTURE_TYPE_JPEG, "d:/2.jpg", 400, 400); // 200x200 pixels
	FileOutputStream out = new FileOutputStream("d:/images.docx");
	doc.write(out);
	
	�׷��� ���� ������ �̿��Ͽ� �̹����� �Է��ϸ� word������ ���� ������ �߻��ϴ� ���װ� �ִ�
	(���ͳ� �˻��� poi 3.10(2014-10-06���� �������� �ֽ� ����)������ ������ ������ �߻��Ѵٰ� ��)
	
	�׷��� ���ͳ��� �˻��Ͽ� ã�� Ŭ������ �Ʒ� ������ �޼ҵ带 ������� �ʰ� XML������ �����Է���
	�׽�Ʈ��� �̹����� ���������� ǥ�õǴ°��� Ȯ���Ͽ���
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
	 * ���ͳ� �ҽ����� Ư�� paragraph�� �̹����� �߰��� �� �ֵ��� XWPFParagraph ���ڸ� ���Ƿ� �߰�
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
