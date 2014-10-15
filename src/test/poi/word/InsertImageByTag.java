package test.poi.word;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


/*
 * docx에 ${image~} 태그를 달고 그 docx를 읽어들여 tag위치에 이미지를 삽입하는 예제
 */
public class InsertImageByTag {
	/**
	 * @param doc
	 *            XWPFDocument
	 * @param path
	 *            저장할 파일 경로
	 */
	private static void saveDoc(XWPFDocument doc, String path) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			doc.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**특정 paragraph에 이미지를 입력
	 * @param doc CustomXWPFDocument 객체
	 * @param para 이미지를 입력할 paragraph
	 */
	public static int z = 1;
	private static void insertImage(CustomXWPFDocument doc, XWPFParagraph para) {
		System.out.println("insert Image");
		para.setAlignment(ParagraphAlignment.CENTER);
		String blipId;
		
		try {
			//일반적인 word파일(새로만들기한 상태)의 크기 한도는 width가 600 height 가 900임
			BufferedImage bi = ImageIO.read(new File("d:/2.jpg"));
			int width = (bi.getWidth() > 600) ? 600 : bi.getWidth();
			int height = (bi.getHeight() > 900) ? 900 : bi.getHeight();
			
			blipId = para.getDocument().addPictureData(new FileInputStream(new File("d:/2.jpg")), Document.PICTURE_TYPE_JPEG);
			doc.createPicture(blipId, z++, width, height, para);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		CustomXWPFDocument doc = new CustomXWPFDocument(OPCPackage.open("d:/template.docx"));
		//XWPFDocument doc = new XWPFDocument(OPCPackage.open("d:/template.docx"));
		
		Pattern pat = Pattern.compile("^\\$\\{(.*)\\}$", Pattern.MULTILINE); //${~} 정규식
		String imagepos = null;
		
		for(IBodyElement e : doc.getBodyElements()) {
			if(e.getElementType() == BodyElementType.PARAGRAPH) {
				XWPFParagraph p = (XWPFParagraph)e;
				for(XWPFRun r : p.getRuns()) {
					String test = r.getText(0);
					if(test != null) {
						Matcher m = pat.matcher(test);
						if(m.find()) {
							imagepos = m.group();
							r.setText("", 0); //정규식에 해당하는 문구를 제거(뒤에 0을 쓰지 않으면 이미있던 문자열에 입력하는 문자열이 추가된다)
						}
					}
				}
				
				if(imagepos != null) {
					insertImage(doc,p);
					imagepos = null;
				}
			}
		}
	
		//insertParagraph(doc,"페이지3",false);
		saveDoc(doc, "d:/addimg.docx");
	}
}