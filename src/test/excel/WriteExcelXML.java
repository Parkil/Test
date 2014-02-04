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
	private Element  sheetData; //�������� sheetData Element�� �ٱ� ������ sheetData�� ���������� ���� �����Ѵ�.

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

	//xlsx Excel ������ �⺻������ ����
	private void init() {
		doc = docBuilder.newDocument();
		Element worksheet = doc.createElementNS("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "worksheet"); //xml �±׿� xmlns �Ӽ� �߰�
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

		s.createCell(e, 1,  "�÷�1");
		s.createCell(e, 2,  "�÷�2");
		s.createCell(e, 3,  "�÷�3");
		s.createCell(e, 4,  "�÷�4");
		s.createCell(e, 5,  "�÷�5");
		s.createCell(e, 6,  "�÷�6");
		s.createCell(e, 7,  "�÷�7");
		s.createCell(e, 8,  "�÷�8");
		s.createCell(e, 9,  "�÷�9");
		s.createCell(e, 10, "�÷�10");

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

		System.out.println("���������� : "+s.saveXml("d:/file.xml"));
		long end = System.currentTimeMillis();
		System.out.println( "���� �ð� : " + ( end - start )/1000.0 );
	}
}