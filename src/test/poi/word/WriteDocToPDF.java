package test.poi.word;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WriteDocToPDF {
	/*
	 * docx������ ������ PDF�� �����ϴ� ����
	 */
	public static void saveDocAsPDF(XWPFDocument doc, String path) throws Exception{
		BaseFont	bf = BaseFont.createFont("HYGoThic-Medium", "UniKS-UCS2-H", BaseFont.NOT_EMBEDDED); //��Ʈ������� ��ü ����
		
		FileOutputStream fos = new FileOutputStream(path);
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, fos);
		document.open();
		
		Font font = new Font(bf, 8, Font.NORMAL); //PDF���� ����� ��Ʈ ����

		for(IBodyElement e : doc.getBodyElements()) {
			if(e.getElementType() == BodyElementType.PARAGRAPH) {
				XWPFParagraph p = (XWPFParagraph)e;
				
				Paragraph para = new Paragraph();
				para.setTabSettings(new TabSettings(50f)); //PDF �� ũ�� ����
				
				for(XWPFRun r : p.getRuns()) {
					String text = r.getText(0);
					if(text == null) {
						text = "";
						para.add(Chunk.TABBING); //���ڰ� ������� PDF���� ��� �ֵ��� ����
					}
					
					/*
					 * xwpf���� �������� �����ų� ���ο� �������� �����ϴ°��� �Ǵ��ϴ� ����� ����� �ִ�.
					 * ���ͳݿ����� r.getCTR().getBrList()�� ����� �ϴµ� ClassNotFoundException�� �߻��Ѵ�.
					 * �׷��� docx�� xml�� �����Ѱ�� ũ�� 2������ �����Ҽ� �� �ִ�.
					 * 
					 * 1. <w:br w:type="page"/> 
					 * �������� ctrl+Enter�� �̿��Ͽ� ������������ ǥ�õǰ� ���ڸ� ���̽Ἥ �������� ����� ��쿡�� ǥ�ð� ���� �ʴ� ������ �ִ�.
					 * 
					 * 2.<w:lastRenderedPageBreak/>
					 * ������������ ù��°�� ���̴� paragraph�� ǥ�ð� �ȴ� Ctrl+Enter�� �ؼ� �������� ����Ȱ��, ���ڸ� ���� �Ἥ �������������
					 * ��� ��� ǥ�ð� �Ǳ⶧���� �̰� ���Ⱑ �����ϴ�.
					 * 
					 * ����� xml�� ���ڿ��� ��ȯ�� indexOf�� �̿��Ͽ� ��ȯ�� �ϰ� ������ �� ���� ����� �������� ��
					 */
					String chkstr = r.getCTR().toString();
					if(chkstr.indexOf("lastRenderedPageBreak") != -1) {
						document.newPage();
					}

					para.add(new Chunk(text,font));
					
					List<XWPFPicture> pictureList = r.getEmbeddedPictures();
					if(pictureList.size() != 0) { //�ش� Paragraph�� �̹����� ������ ���
						//�̹����� PDF�� �Է�
						for(XWPFPicture pic : pictureList) {
							byte[] imgbyte = pic.getPictureData().getData();
							Image img = Image.getInstance(imgbyte);
							img.scaleToFit(500, 500); //�̹��� ũ������
							img.setAlignment(Image.MIDDLE); //�̹��� horizontal align
							para.add(Chunk.NEWLINE); //�̺κ��� ���� Image�� ���ڰ� ��ġ�� ��찡 �����.
							para.add(img);
						}
					}
				}
				document.add(para);
			}
		}
		
		document.close();
		fos.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		XWPFDocument doc = new XWPFDocument(OPCPackage.open("d:/addimg.docx"));
		saveDocAsPDF(doc,"d:/a.pdf");
	}
}