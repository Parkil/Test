package test.docx4j;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Document;

/*
 * InsertImageByTag.java(apache poi 이용)를 docx4j를 사용하도록 컨버전한 클래스
 * docx의 [image] 문자를 찾아서 문자를 지우고 그 자리에 이미지를 삽입한다.
 * 
 * 참고 docx 문서 구조
 * Body > Paragraph > Run > Table or SdtBlock/SdtRun/CTSdtRow/CTSdtCell
 */
public class ImageAddbyTag {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Logger.getRootLogger().setLevel(Level.ERROR); //docx4j 수행시 나오는 log를 나오지 않게 설정
		ImageAddbyTag t2 = new ImageAddbyTag();
		WordprocessingMLPackage wordMLPackage = t2.loadFile("d:/낙뢰연보word.docx");
		Document d = wordMLPackage.getMainDocumentPart().getContents();
		List<Object> list = d.getBody().getContent();
		List<Object> runlist = t2.searchRunByTag(list);
		
		
		/*
		 * Paragraph에서는 [chart-1.png]로 표시가 되어도 run.getContent() 리스트에서는 {[},{chart-1.png},{]} 이런식으로 표시가 되는 경우가 있기 때문에
		 * 기존 run기준으로 돌아가던 것을 Paragraph기준으로 변경
		 */
		long ratio = 5000;
		String fileName = "";
		
		Pattern p_tag = Pattern.compile("\\[([0-9]{1,4})\\-(chart-[0-9]{1,4})\\]");
		for(Object o : runlist) {
			org.docx4j.wml.P para = (org.docx4j.wml.P)o;
			String para_con = para.toString(); //Paragraph에서 문자열을 가져오기 위해 부득이 폐기된 메소드 사용
			
			Matcher m_tag = p_tag.matcher(para_con);
			t2.delParaContents(para);
			
			while(m_tag.find()) {
				ratio = Long.parseLong(m_tag.group(1));
				fileName = m_tag.group(2);
				
				byte[] imgbyte = t2.getImgByte("d:/tempimg/"+fileName+".png");
				
				t2.insertImage(para, wordMLPackage, imgbyte, ratio, new String[]{fileName, "차트이미지"});
			}
		}
		
		wordMLPackage.save(new File("d:/1.docx"));
	}
	
	/**Paragraph 안의 내용을 삭제
	 * @param run
	 */
	private void delParaContents(org.docx4j.wml.P para) {
		int size = 10; //초기 임의 값
		while(size != 0) {
			para.getContent().remove(0);
			size = para.getContent().size();
		}
	}
	
	/*
	 * paragraph에 이미지를 입력
	 * 
	 * ratio : 이미지 비율 크게 지정할수록 크게 표시된다.
	 * fileInfo : 파일 정보([0] : 파일명 [1] : 파일 툴팁명)
	 */
	public void insertImage(org.docx4j.wml.P para, WordprocessingMLPackage wordMLPackage, byte[] imgbyte, long ratio, String[] fileInfo) {
		org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
		
		BinaryPartAbstractImage imagePart = null;
		Inline inline = null;
		try {
			imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, imgbyte);
			inline = imagePart.createImageInline(fileInfo[0], fileInfo[1], 0, 1, ratio, false);
			//inline = imagePart.createImageInline(fileInfo[0], fileInfo[1], 0, 1, false);
			
			org.docx4j.wml.Drawing drawing = factory.createDrawing();
			para.getContent().add(drawing);
			
			drawing.getAnchorOrInline().add(inline);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * [image]문구가 들어가 있는 run객체를 반환(이미지 삽입대상 반환)
	 */
	public List<Object> searchRunByTag(List<Object> bodylist) {
		List<Object> runlist = new ArrayList<Object>();
		
		Pattern p = Pattern.compile("^\\[(.*)\\]$"); //[~] 정규식
		
		
		for(Object o : bodylist) {
			if(o instanceof org.docx4j.wml.P) {
				String con = o.toString();
				org.docx4j.wml.P para = (org.docx4j.wml.P)o;
				
				Matcher m = p.matcher(con);
				if(m.find()) {
					runlist.add(para);
				}
			}
		}
		
		return runlist;
	}
	
	/*
	 * image파일을 byte배열로 변경
	 */
	public byte[] getImgByte(String path) {
		FileInputStream fis = null;
		FileChannel fc = null;
		
		ByteBuffer temp = null;
		ByteBuffer real = null;
		
		try {
			fis = new FileInputStream(path);
			fc = fis.getChannel();
			
			temp = ByteBuffer.allocate(1024*10);
			real = ByteBuffer.allocate((int)fc.size());
			
			int readsize = -999;
			while(readsize != -1) {
				readsize = fc.read(temp);
				temp.flip();
				real.put(temp);
				temp.clear();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fc != null) {
				try {
					fc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//에러가 발생하여 real이 null일경우 처리로직
		if(real == null) {
			real = ByteBuffer.allocate(1);
		}else {
			real.flip();
		}
		
		return real.array();
	}
	
	/*
	 * docx파일을 불러온다.
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
	
	
	/**
	 * Create image, without specifying width
	 */
	public static org.docx4j.wml.P newImage(WordprocessingMLPackage wordMLPackage, byte[] bytes, String filenameHint,
			String altText, int id1, int id2) throws Exception {

		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

		Inline inline = imagePart.createImageInline(filenameHint, altText, id1, id2, false);

		// Now add the inline in w:p/w:r/w:drawing
		org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
		org.docx4j.wml.P p = factory.createP();
		org.docx4j.wml.R run = factory.createR();
		p.getContent().add(run);
		org.docx4j.wml.Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);

		return p;

	}

	/**
	 * Create image, specifying width in twips
	 */
	public static org.docx4j.wml.P newImage(WordprocessingMLPackage wordMLPackage, byte[] bytes, String filenameHint,
			String altText, int id1, int id2, long cx) throws Exception {

		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

		Inline inline = imagePart.createImageInline(filenameHint, altText, id1, id2, cx, false);

		// Now add the inline in w:p/w:r/w:drawing
		org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
		org.docx4j.wml.P p = factory.createP();
		org.docx4j.wml.R run = factory.createR();
		p.getContent().add(run);
		org.docx4j.wml.Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);

		return p;

	}
}
