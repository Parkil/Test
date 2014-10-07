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
	 * docx문서의 내용을 PDF로 저장하는 예제
	 */
	public static void saveDocAsPDF(XWPFDocument doc, String path) throws Exception{
		BaseFont	bf = BaseFont.createFont("HYGoThic-Medium", "UniKS-UCS2-H", BaseFont.NOT_EMBEDDED); //폰트에사용할 서체 지정
		
		FileOutputStream fos = new FileOutputStream(path);
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, fos);
		document.open();
		
		Font font = new Font(bf, 8, Font.NORMAL); //PDF에서 사용할 폰트 지정

		for(IBodyElement e : doc.getBodyElements()) {
			if(e.getElementType() == BodyElementType.PARAGRAPH) {
				XWPFParagraph p = (XWPFParagraph)e;
				
				Paragraph para = new Paragraph();
				para.setTabSettings(new TabSettings(50f)); //PDF 탭 크기 지정
				
				for(XWPFRun r : p.getRuns()) {
					String text = r.getText(0);
					if(text == null) {
						text = "";
						para.add(Chunk.TABBING); //문자가 없을경우 PDF탭을 대신 넣도록 설정
					}
					
					/*
					 * xwpf에서 페이지가 끝났거나 새로운 페이지가 시작하는것을 판단하는 방법은 몇가지가 있다.
					 * 인터넷에서는 r.getCTR().getBrList()를 쓰라고 하는데 ClassNotFoundException이 발생한다.
					 * 그래서 docx의 xml을 조사한결과 크게 2가지로 구분할수 가 있다.
					 * 
					 * 1. <w:br w:type="page"/> 
					 * 페이지를 ctrl+Enter를 이용하여 변경했을때만 표시되고 글자를 많이써서 페이지가 변경된 경우에는 표시가 되지 않는 문제가 있다.
					 * 
					 * 2.<w:lastRenderedPageBreak/>
					 * 페이지변경후 첫번째로 쓰이는 paragraph에 표시가 된다 Ctrl+Enter를 해서 페이지가 변경된경우, 글자를 많이 써서 페이지가변경된
					 * 경우 모두 표시가 되기때문에 이게 쓰기가 적합하다.
					 * 
					 * 현재는 xml을 문자열로 반환후 indexOf를 이용하여 반환을 하고 있으나 더 나은 방법이 있을지도 모름
					 */
					String chkstr = r.getCTR().toString();
					if(chkstr.indexOf("lastRenderedPageBreak") != -1) {
						document.newPage();
					}

					para.add(new Chunk(text,font));
					
					List<XWPFPicture> pictureList = r.getEmbeddedPictures();
					if(pictureList.size() != 0) { //해당 Paragraph에 이미지가 존재할 경우
						//이미지를 PDF에 입력
						for(XWPFPicture pic : pictureList) {
							byte[] imgbyte = pic.getPictureData().getData();
							Image img = Image.getInstance(imgbyte);
							img.scaleToFit(500, 500); //이미지 크기지정
							img.setAlignment(Image.MIDDLE); //이미지 horizontal align
							para.add(Chunk.NEWLINE); //이부분을 빼면 Image와 문자가 겹치는 경우가 생긴다.
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