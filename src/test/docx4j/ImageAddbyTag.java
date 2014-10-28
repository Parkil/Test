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
 * InsertImageByTag.java(apache poi �̿�)�� docx4j�� ����ϵ��� �������� Ŭ����
 * docx�� [image] ���ڸ� ã�Ƽ� ���ڸ� ����� �� �ڸ��� �̹����� �����Ѵ�.
 * 
 * ���� docx ���� ����
 * Body > Paragraph > Run > Table or SdtBlock/SdtRun/CTSdtRow/CTSdtCell
 */
public class ImageAddbyTag {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Logger.getRootLogger().setLevel(Level.ERROR); //docx4j ����� ������ log�� ������ �ʰ� ����
		ImageAddbyTag t2 = new ImageAddbyTag();
		WordprocessingMLPackage wordMLPackage = t2.loadFile("d:/���ڿ���word.docx");
		Document d = wordMLPackage.getMainDocumentPart().getContents();
		List<Object> list = d.getBody().getContent();
		List<Object> runlist = t2.searchRunByTag(list);
		
		
		/*
		 * Paragraph������ [chart-1.png]�� ǥ�ð� �Ǿ run.getContent() ����Ʈ������ {[},{chart-1.png},{]} �̷������� ǥ�ð� �Ǵ� ��찡 �ֱ� ������
		 * ���� run�������� ���ư��� ���� Paragraph�������� ����
		 */
		long ratio = 5000;
		String fileName = "";
		
		Pattern p_tag = Pattern.compile("\\[([0-9]{1,4})\\-(chart-[0-9]{1,4})\\]");
		for(Object o : runlist) {
			org.docx4j.wml.P para = (org.docx4j.wml.P)o;
			String para_con = para.toString(); //Paragraph���� ���ڿ��� �������� ���� �ε��� ���� �޼ҵ� ���
			
			Matcher m_tag = p_tag.matcher(para_con);
			t2.delParaContents(para);
			
			while(m_tag.find()) {
				ratio = Long.parseLong(m_tag.group(1));
				fileName = m_tag.group(2);
				
				byte[] imgbyte = t2.getImgByte("d:/tempimg/"+fileName+".png");
				
				t2.insertImage(para, wordMLPackage, imgbyte, ratio, new String[]{fileName, "��Ʈ�̹���"});
			}
		}
		
		wordMLPackage.save(new File("d:/1.docx"));
	}
	
	/**Paragraph ���� ������ ����
	 * @param run
	 */
	private void delParaContents(org.docx4j.wml.P para) {
		int size = 10; //�ʱ� ���� ��
		while(size != 0) {
			para.getContent().remove(0);
			size = para.getContent().size();
		}
	}
	
	/*
	 * paragraph�� �̹����� �Է�
	 * 
	 * ratio : �̹��� ���� ũ�� �����Ҽ��� ũ�� ǥ�õȴ�.
	 * fileInfo : ���� ����([0] : ���ϸ� [1] : ���� ������)
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
	 * [image]������ �� �ִ� run��ü�� ��ȯ(�̹��� ���Դ�� ��ȯ)
	 */
	public List<Object> searchRunByTag(List<Object> bodylist) {
		List<Object> runlist = new ArrayList<Object>();
		
		Pattern p = Pattern.compile("^\\[(.*)\\]$"); //[~] ���Խ�
		
		
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
	 * image������ byte�迭�� ����
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
		
		//������ �߻��Ͽ� real�� null�ϰ�� ó������
		if(real == null) {
			real = ByteBuffer.allocate(1);
		}else {
			real.flip();
		}
		
		return real.array();
	}
	
	/*
	 * docx������ �ҷ��´�.
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
