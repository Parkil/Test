package test.excel;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.ss.util.CellReference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteExcelXML {
	private static DocumentBuilder docBuilder;

	private Document doc;
	private Element  sheetData; //행정보가 sheetData Element에 붙기 때문에 sheetData는 전역변수로 따로 설정한다.

	static {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WriteExcelXML() {
		init();
	}

	//xlsx Excel 형식의 기본형식을 생성
	private void init() {
		doc = docBuilder.newDocument();
		Element worksheet = doc.createElementNS("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "worksheet"); //xml 태그에 xmlns 속성 추가
		doc.appendChild(worksheet);

		sheetData = doc.createElement("sheetData");
		worksheet.appendChild(sheetData);
	}

	public Element createRow(int row_idx) {
		Element row = doc.createElement("row");
		sheetData.appendChild(row);
		row.setAttribute("r", String.valueOf(row_idx));
		return row;
	}

	public void createCell(Element row, int col_idx, String value) {
		Element cell = doc.createElement("c");
		row.appendChild(cell);
		cell.setAttribute("r", new CellReference(Integer.parseInt(row.getAttribute("r"))-1, col_idx-1).formatAsString());
		cell.setAttribute("t", "inlineStr");
		cell.setAttribute("s", "5");

		Element is = doc.createElement("is");
		cell.appendChild(is);

		Element t = doc.createElement("t");
		t.setTextContent(value);
		is.appendChild(t);
	}

	public boolean saveXml(String path) {
		boolean save_result = false;
		try {
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
			save_result = true;
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return save_result;
	}

	public static void main(String argv[]) throws TransformerException {
		long start = System.currentTimeMillis();

		Random rnd = new Random();
		Calendar calendar = Calendar.getInstance();

		WriteExcelXML s = new WriteExcelXML();
		Element e = s.createRow(1);

		s.createCell(e, 1,  "컬럼1");
		s.createCell(e, 2,  "컬럼2");
		s.createCell(e, 3,  "컬럼3");
		s.createCell(e, 4,  "컬럼4");
		s.createCell(e, 5,  "컬럼5");
		s.createCell(e, 6,  "컬럼6");
		s.createCell(e, 7,  "컬럼7");
		s.createCell(e, 8,  "컬럼8");
		s.createCell(e, 9,  "컬럼9");
		s.createCell(e, 10, "컬럼10");

		for(int i = 1 ; i<=10000 ; i++) {
			e = s.createRow(i+1);

			s.createCell(e, 1,  "Hello, " + i + "!");
			s.createCell(e, 2,  String.valueOf((double)rnd.nextInt(100)/100));
			s.createCell(e, 3,  String.valueOf((double)rnd.nextInt(10)/10));
			s.createCell(e, 4,  String.valueOf(rnd.nextInt(10000)));
			s.createCell(e, 5,  calendar.getTime().toString());
			s.createCell(e, 6,  "Hello, " + i + "!");
			s.createCell(e, 7,  String.valueOf((double)rnd.nextInt(100)/100));
			s.createCell(e, 8,  String.valueOf((double)rnd.nextInt(10)/10));
			s.createCell(e, 9,  String.valueOf(rnd.nextInt(10000)));
			s.createCell(e, 10, calendar.getTime().toString());
		}

		System.out.println("파일저장결과 : "+s.saveXml("d:/file.xml"));
		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
	}
}