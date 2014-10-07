package test.poi.word;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/*
 * CustomXWPFDocument를 이용하여 이미지 파일을 docx파일에 삽입하는 예제
 */
public class WordInsertPicture {

	public static void main(String[] args) throws IOException {
		// Create a document file
		CustomXWPFDocument document = new CustomXWPFDocument();

		// Adding a file
		try {
			// Working addPicture Code below...
			XWPFParagraph paragraphX = document.createParagraph();
			paragraphX.setAlignment(ParagraphAlignment.CENTER);
			String blipId = paragraphX.getDocument().addPictureData(
					new FileInputStream(new File("d:/2.jpg")), Document.PICTURE_TYPE_JPEG);
			document.createPicture(blipId, document.getNextPicNameNumber(Document.PICTURE_TYPE_PNG), 500, 500, null);
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}

		FileOutputStream outStream = null;

		try {
			String fileName = "d:/test.docx";
			outStream = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			document.write(outStream);
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}