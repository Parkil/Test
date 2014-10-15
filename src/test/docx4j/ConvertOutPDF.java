package test.docx4j;
/*
 *  Copyright 2007-2008, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 

    You may obtain a copy of the License at 

        http://www.apache.org/licenses/LICENSE-2.0 

    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.

 */
import java.io.OutputStream;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

/**
 * Demo of PDF output.
 * 
 * PDF output is via XSL FO.
 * First XSL FO is created, then FOP
 * is used to convert that to PDF.
 * 
 * Don't worry if you get a class not
 * found warning relating to batik. It
 * doesn't matter.
 * 
 * If you don't have logging configured, 
 * your PDF will say "TO HIDE THESE MESSAGES, 
 * TURN OFF debug level logging for 
 * org.docx4j.convert.out.pdf.viaXSLFO".  The thinking is
 * that you need to be able to be warned if there
 * are things in your docx which the PDF output
 * doesn't support...
 * 
 * docx4j used to also support creating
 * PDF via iText and via HTML. As of docx4j 2.5.0, 
 * only viaXSLFO is supported.  The viaIText and 
 * viaHTML source code can be found in src/docx4j-extras directory
 * 
 * @author jharrop
 *
 */

/*
 * docx4j를 이용하여 docx파일을 PDF로 변경하는 예제
 * docx4j를 사용하기 위해서는 다음 API가 필요함
 * slf4j-api-1.7.7.jar
 * slf4j-log4j12-1.7.7.jar
 * commons-io-2.4.jar
 * serializer.jar
 * xalan.jar
 * xercesImpl.jar
 * xml-apis.jar
 * xmlgraphics-commons-1.5.jar(2.0을 사용하게 되면 에러가 발생함)
 * fop.jar
 * avalon-framework-4.1.4.jar
 * batik-all-1.7.jar
 */
public class ConvertOutPDF extends AbstractSample {
	
	/*
	 * NOT WORKING?
	 * 
	 * If you are getting:
	 * 
	 *   "fo:layout-master-set" must be declared before "fo:page-sequence"
	 * 
	 * please check:
	 * 
	 * 1.  the jaxb-xslfo jar is on your classpath
	 * 
	 * 2.  that there is no stack trace earlier in the logs
	 * 
	 * 3.  your JVM has adequate memory, eg
	 * 
	 *           -Xmx1G -XX:MaxPermSize=128m
	 * 
	 */
	
	// Config for non-command line use
	static {
		
		inputfilepath = null; // to generate a docx (and PDF output) containing font samples
		
    	//inputfilepath = System.getProperty("user.dir") + "/sample-docs/word/sample-docx.docx";
		
		//inputfilepath = "d:/test.docx";
		inputfilepath = "d:/test.docx";
    	saveFO = false;
	}
	
	
	// For demo/debugging purposes, save the intermediate XSL FO
	// Don't do this in production!
	static boolean saveFO;
	
    public static void main(String[] args) 
            throws Exception {

		try {
			getInputFilePath(args);
		} catch (IllegalArgumentException e) {
		}
		
		// Font regex (optional)
		// Set regex if you want to restrict to some defined subset of fonts
		// Here we have to do this before calling createContent,
		// since that discovers fonts
		String regex = null;
		// Windows:
		// String
		// regex=".*(calibri|camb|cour|arial|symb|times|Times|zapf).*";
		//regex=".*(calibri|camb|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
		// Mac
		// String
		// regex=".*(Courier New|Arial|Times New Roman|Comic Sans|Georgia|Impact|Lucida Console|Lucida Sans Unicode|Palatino Linotype|Tahoma|Trebuchet|Verdana|Symbol|Webdings|Wingdings|MS Sans Serif|MS Serif).*";
		PhysicalFonts.setRegex(regex);

		// Document loading (required)
		WordprocessingMLPackage wordMLPackage;
		if (inputfilepath==null) {
			// Create a docx
			System.out.println("No imput path passed, creating dummy document");
			 wordMLPackage = WordprocessingMLPackage.createPackage();
			 SampleDocument.createContent(wordMLPackage.getMainDocumentPart());	
		} else {
			// Load .docx or Flat OPC .xml
			System.out.println("Loading file from " + inputfilepath);
			wordMLPackage = WordprocessingMLPackage.load(new java.io.File(inputfilepath));
		}
		
		// Set up font mapper (optional)
		Mapper fontMapper = new IdentityPlusMapper();
		wordMLPackage.setFontMapper(fontMapper);
		
		/*
		 * docx4j에서 폰트를 인식하는 방법
		 * 1.OS에 있는 font
		 * 2.문서에 포함되어 있는 font - 저장시 저장옵션에서 파일의 글꼴포함 옵션을 활성화하여 저장하여야 함
		 * 
		 * FontMapper 지정
		 * Mapper fontMapper = new IdentityPlusMapper();
		 * wordMLPackage.setFontMapper(fontMapper);
		 * 
		 * 기본 폰트 지정
		 * PhysicalFont font = PhysicalFonts.get("Batang"); 
		 * 
		 * 기본폰트로 표시할 폰트명 지정
		 * fontMapper.put("궁서", font);
		 * fontMapper.put("굴림", font);
		 * 
		 * 위와 같이 지정할 경우 docx에 굴림체 문자가 있으면 바탕체로 표시가 된다.
		 * 주의할점은 PhysicalFont에 지정할때는 영문으로 fontMapper로 지정할때는 한글로 입력해야 정상작동함
		 * 
		 * 한글 폰트명의 경우 영문으로 작성해야 인식함
		 * 바탕 : Batang / BatangChe
		 * 굴림 : Gulim / GulimChe
		 * 궁서 : Gungsuh / GungsuhChe
		 * 새굴림 : New Gulim
		 * 맑은 고딕 : Malgun Gothic
		 */
		PhysicalFont font = PhysicalFonts.get("Batang"); 
		System.out.println("font===================>>>"+font);
		if (font!=null) {
			fontMapper.put("바탕", font);
			fontMapper.put("바탕체", font);
			fontMapper.put("Book Antiqua", font);
		}
		
		PhysicalFont font2 = PhysicalFonts.get("Gulim"); 
		if (font2!=null) {
			fontMapper.put("굴림", font2);
			fontMapper.put("굴림체", font2);
		}
		
		PhysicalFont font3 = PhysicalFonts.get("Gungsuh"); 
		if (font3!=null) {
			fontMapper.put("궁서", font3);
			fontMapper.put("궁서체", font3);
		}
		
		PhysicalFont font4 = PhysicalFonts.get("Arial Unicode MS"); 
		if (font4!=null) {
			fontMapper.put("Arial Unicode MS", font4);
		}
		
		PhysicalFont font5 = PhysicalFonts.get("HyhwpEQ"); 
		if (font5!=null) {
			fontMapper.put("HyhwpEQ", font5);
		}
		
		//fontMapper.put("Libian SC Regular", PhysicalFonts.get("SimSun"));

		// FO exporter setup (required)
		// .. the FOSettings object
    	FOSettings foSettings = Docx4J.createFOSettings();
		if (saveFO) {
			foSettings.setFoDumpFile(new java.io.File(inputfilepath + ".fo"));
		}
		foSettings.setWmlPackage(wordMLPackage);
		
		// Document format: 
		// The default implementation of the FORenderer that uses Apache Fop will output
		// a PDF document if nothing is passed via 
		// foSettings.setApacheFopMime(apacheFopMime)
		// apacheFopMime can be any of the output formats defined in org.apache.fop.apps.MimeConstants eg org.apache.fop.apps.MimeConstants.MIME_FOP_IF or
		// FOSettings.INTERNAL_FO_MIME if you want the fo document as the result.
		//foSettings.setApacheFopMime(FOSettings.INTERNAL_FO_MIME);
		
		// exporter writes to an OutputStream.		
		String outputfilepath;
		if (inputfilepath==null) {
			outputfilepath = System.getProperty("user.dir") + "/OUT_FontContent.pdf";
		} else {
			outputfilepath = inputfilepath + ".pdf";
		}
		OutputStream os = new java.io.FileOutputStream(outputfilepath);
    	
		// Specify whether PDF export uses XSLT or not to create the FO
		// (XSLT takes longer, but is more complete).
		
		// Don't care what type of exporter you use
		Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
		
		// Prefer the exporter, that uses a xsl transformation
		// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
		
		// Prefer the exporter, that doesn't use a xsl transformation (= uses a visitor)
		// .. faster, but not yet at feature parity
		// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);
    	
		System.out.println("Saved: " + outputfilepath);
    }
    
    
}