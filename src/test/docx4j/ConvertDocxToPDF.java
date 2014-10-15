package test.docx4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.naming.OperationNotSupportedException;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

/*
 * ConvertOutPDF.java���� �ʿ���� ������ �����ϰ� ����� �и��� Ŭ����
 */
public class ConvertDocxToPDF {
	/**docx�� Flat OPC xml�� �о�鿩 WordprocessingMLPackage ��ȯ
	 * @param path
	 * @return
	 */
	public WordprocessingMLPackage loadFile(String path) {
		WordprocessingMLPackage wordMLPackage = null;
		File file = new File(path);
		
		if(file.exists()) {
			System.out.println("loading file from : "+path);
		}else {
			System.out.println("file not exists : "+path);
			return null;
		}
		
		try {
			wordMLPackage = WordprocessingMLPackage.load(file);
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		return wordMLPackage;
	}
	
	
	/** Font Mapper ��ȯ(setFont�� �����ϱ����� ȣ���ؾ� ��)
	 * @param wordMLPackage
	 * @return
	 */
	public Mapper setFontMapper(WordprocessingMLPackage wordMLPackage) {
		Mapper fontMapper = new IdentityPlusMapper();
		try {
			wordMLPackage.setFontMapper(fontMapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fontMapper;
	}
	
	/** ��Ʈ����
	 * @param mapper FontMapper ��ü
	 * @param baseFont �⺻ ��Ʈ
	 * @param altFont altFont�� ������ ��Ʈ�� PDF Export�� ���� baseFont�� ������ ��Ʈ�� ǥ�õ�
	 * @throws OperationNotSupportedException
	 */
	public void setFont(Mapper mapper, String baseFont, String[] altFont) throws OperationNotSupportedException{
		PhysicalFont font = PhysicalFonts.get(baseFont);
		if(font == null) {
			throw new OperationNotSupportedException("this font not supported or existed : "+baseFont);
		}
		
		for(String val : altFont) {
			mapper.put(val, font);
		}
	}
	
	/**FO Setting ����
	 * @param wordMLPackage
	 * @param path DumpFile �������
	 * @param isSaveDump FO Dump���� ���忩��
	 * @return
	 */
	public FOSettings getFoSetting(WordprocessingMLPackage wordMLPackage, String path, boolean isSaveDump) {
		FOSettings foSettings = Docx4J.createFOSettings();
		
		if(isSaveDump) {
			foSettings.setFoDumpFile(new File(path));
		}
		
		foSettings.setWmlPackage(wordMLPackage);
		return foSettings;
	}
	
	/**PDF ����
	 * @param settings FOSettings ��ü
	 * @param path PDF������
	 */
	public void savePDF(FOSettings settings, String path) {
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(path);
			Docx4J.toFO(settings, fos, Docx4J.FLAG_EXPORT_PREFER_XSL);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ConvertDocxToPDF convert = new ConvertDocxToPDF();
		
		WordprocessingMLPackage mlpackage = convert.loadFile("d:/test.docx");
		Mapper mapper = convert.setFontMapper(mlpackage);
		
		try {
			convert.setFont(mapper, "Batang", new String[]{"����","����ü"});
			convert.setFont(mapper, "Gulim", new String[]{"����","����ü"});
			convert.setFont(mapper, "Gungsuh", new String[]{"�ü�","�ü�ü"});
			convert.setFont(mapper, "Arial Unicode MS", new String[]{"Arial Unicode MS"});
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
		
		FOSettings setting = convert.getFoSetting(mlpackage, "d:/test_docx.fo", true);
		convert.savePDF(setting, "d:/Ǫ������.pdf");
	}
}
