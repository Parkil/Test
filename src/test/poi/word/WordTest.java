package test.poi.word;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/*
 * xwpf ���� ����
 */
public class WordTest {
	/**
	 * @param doc
	 *            XWPFDocument
	 * @param text
	 *            �Է��� ���ڿ�
	 * @param isStartNewPage
	 *            �Է��� ���ڿ��� ���ο� ���������� ǥ������ ����
	 */
	private static void insertParagraph(XWPFDocument doc, String text, boolean isStartNewPage) {
		XWPFParagraph p = doc.createParagraph();
		p.setPageBreak(isStartNewPage);
		XWPFRun r = p.createRun();
		r.setText(text);
	}

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

	public static String getText() {
		StringBuffer sb = new StringBuffer();
		sb.append("�Ϸ��α�");
		return sb.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		XWPFDocument doc = new XWPFDocument(OPCPackage.open("d:/template.docx"));
		//XWPFWordExtractor ex = new XWPFWordExtractor(doc);

		insertParagraph(doc,"������3",false);
		saveDoc(doc, "d:/a.docx");
	}
}
