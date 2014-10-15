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
 * ConvertOutPDF.java에서 필요없는 로직을 제거하고 기능을 분리한 클래스
 */
public class ConvertDocxToPDF {
	/**docx나 Flat OPC xml을 읽어들여 WordprocessingMLPackage 반환
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
	
	
	/** Font Mapper 반환(setFont를 지정하기전에 호출해야 함)
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
	
	/** 폰트지정
	 * @param mapper FontMapper 객체
	 * @param baseFont 기본 폰트
	 * @param altFont altFont에 지정된 폰트는 PDF Export시 전부 baseFont에 지정된 폰트로 표시됨
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
	
	/**FO Setting 지정
	 * @param wordMLPackage
	 * @param path DumpFile 지정경로
	 * @param isSaveDump FO Dump파일 저장여부
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
	
	/**PDF 저장
	 * @param settings FOSettings 객체
	 * @param path PDF저장경로
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
			convert.setFont(mapper, "Batang", new String[]{"바탕","바탕체"});
			convert.setFont(mapper, "Gulim", new String[]{"굴림","굴림체"});
			convert.setFont(mapper, "Gungsuh", new String[]{"궁서","궁서체"});
			convert.setFont(mapper, "Arial Unicode MS", new String[]{"Arial Unicode MS"});
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
		
		FOSettings setting = convert.getFoSetting(mlpackage, "d:/test_docx.fo", true);
		convert.savePDF(setting, "d:/푸하하하.pdf");
	}
}
