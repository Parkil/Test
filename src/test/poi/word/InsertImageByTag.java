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
 * docx�� ${image~} �±׸� �ް� �� docx�� �о�鿩 tag��ġ�� �̹����� �����ϴ� ����
 */
public class InsertImageByTag {
	/**
	 * @param doc
	 *            XWPFDocument
	 * @param path
	 *            ������ ���� ���
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
	
	/**Ư�� paragraph�� �̹����� �Է�
	 * @param doc CustomXWPFDocument ��ü
	 * @param para �̹����� �Է��� paragraph
	 */
	public static int z = 1;
	private static void insertImage(CustomXWPFDocument doc, XWPFParagraph para) {
		System.out.println("insert Image");
		para.setAlignment(ParagraphAlignment.CENTER);
		String blipId;
		
		try {
			//�Ϲ����� word����(���θ������ ����)�� ũ�� �ѵ��� width�� 600 height �� 900��
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
		
		Pattern pat = Pattern.compile("^\\$\\{(.*)\\}$", Pattern.MULTILINE); //${~} ���Խ�
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
							r.setText("", 0); //���ԽĿ� �ش��ϴ� ������ ����(�ڿ� 0�� ���� ������ �̹��ִ� ���ڿ��� �Է��ϴ� ���ڿ��� �߰��ȴ�)
						}
					}
				}
				
				if(imagepos != null) {
					insertImage(doc,p);
					imagepos = null;
				}
			}
		}
	
		//insertParagraph(doc,"������3",false);
		saveDoc(doc, "d:/addimg.docx");
	}
}