package test.jetm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import test.util.ByteBufferUtil;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

/*
 * JETM(����ð� ���� API) ��� Ŭ����
 * 
 * =�ʿ� jar=
 * jetm-1.2.3.jar
 * jetm-optional-1.2.3.jar
 * 
 * �غ��۾�
 * 
 * 1.private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor(); ����
 * 2.����ð��� �����ϰ��� �ϴ� �޼ҵ忡 ���� ���� ����
 * 2-1.EtmPoint point = etmMonitor.createPoint("�ش� �޼ҵ� ������ ���ڿ��� �ƹ����Գ� ���� ��"); ����
 * 2-2.point.collect(); ���� �ݵ�� �����Ű�� ���ؼ� try~finally������ �����Ѵ�.
 * 
 */

public class WriteXMLTimeTest {
	private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	private static Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb){
		Map<String, XSSFCellStyle> styles = new HashMap<String, XSSFCellStyle>();
		XSSFDataFormat fmt = wb.createDataFormat();

		XSSFCellStyle style1 = wb.createCellStyle();
		style1.setAlignment(CellStyle.ALIGN_RIGHT);
		style1.setDataFormat(fmt.getFormat("0.0%"));
		styles.put("percent", style1);

		XSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setDataFormat(fmt.getFormat("0.0X"));
		styles.put("coeff", style2);

		XSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(CellStyle.ALIGN_RIGHT);
		style3.setDataFormat(fmt.getFormat("$#,##0.00"));
		styles.put("currency", style3);

		XSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(CellStyle.ALIGN_RIGHT);
		style4.setDataFormat(fmt.getFormat("mmm dd"));
		styles.put("date", style4);

		XSSFCellStyle style5 = wb.createCellStyle();
		XSSFFont headerFont = wb.createFont();
		headerFont.setBold(true);
		style5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style5.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style5.setFont(headerFont);
		styles.put("header", style5);

		return styles;
	}

	//OLD IO �׽�Ʈ
	public void writeXMLOLDIO() {
		EtmPoint point = WriteXMLTimeTest.etmMonitor.createPoint("WriteXMLTimeTest : writeXMLOLDIO()");

		XSSFWorkbook wb = new XSSFWorkbook();

		Map<String, XSSFCellStyle> styles = WriteXMLTimeTest.createStyles(wb);
		File tmp = new File("d:/sheet.xml");
		Writer fw = null;
		try {
			fw = new OutputStreamWriter(new FileOutputStream(tmp));
			WriteXMLTimeTest.generateOLDIO(fw, styles);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			point.collect();
		}
	}

	//NIO�׽�Ʈ
	public void writeXMLNIO() {
		EtmPoint point = WriteXMLTimeTest.etmMonitor.createPoint("WriteXMLTimeTest : writeXMLNIO()");

		XSSFWorkbook wb = new XSSFWorkbook();

		Map<String, XSSFCellStyle> styles = WriteXMLTimeTest.createStyles(wb);
		File tmp = new File("d:/sheet.xml");
		RandomAccessFile fos = null;
		FileChannel fc = null;
		try {
			//fw = new OutputStreamWriter(new FileOutputStream(tmp));
			fos = new RandomAccessFile(tmp, "rw");
			fc = fos.getChannel();
			WriteXMLTimeTest.generateNIO(fc, styles);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fos.close();
				fc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			point.collect();
		}
	}

	//OLDIO�� �̿��Ͽ� ������ ����
	private static void generateOLDIO(Writer out, Map<String, XSSFCellStyle> styles) throws Exception {
		Random rnd = new Random();
		Calendar calendar = Calendar.getInstance();

		WriteXMLOLD sw = new WriteXMLOLD(out);
		sw.beginSheet();

		//insert header row
		sw.insertRow(0);
		int styleIndex = styles.get("header").getIndex();
		sw.createCell(0, "�÷�1", styleIndex);
		sw.createCell(1, "�÷�2", styleIndex);
		sw.createCell(2, "�÷�3", styleIndex);
		sw.createCell(3, "�÷�4", styleIndex);
		sw.createCell(4, "�÷�5", styleIndex);
		sw.createCell(5, "�÷�6", styleIndex);
		sw.createCell(6, "�÷�7", styleIndex);
		sw.createCell(7, "�÷�8", styleIndex);
		sw.createCell(8, "�÷�9", styleIndex);
		sw.createCell(9, "�÷�10", styleIndex);

		sw.endRow();

		//write data rows
		for (int rownum = 1; rownum <= 100000; rownum++) {
			sw.insertRow(rownum);

			sw.createCell(0, "Hello, " + rownum + "!");
			sw.createCell(1, (double)rnd.nextInt(100)/100, styles.get("percent").getIndex());
			sw.createCell(2, (double)rnd.nextInt(10)/10, styles.get("coeff").getIndex());
			sw.createCell(3, rnd.nextInt(10000), styles.get("currency").getIndex());
			sw.createCell(4, calendar, styles.get("date").getIndex());
			sw.createCell(5, "Hello, " + rownum + "!");
			sw.createCell(6, (double)rnd.nextInt(100)/100, styles.get("percent").getIndex());
			sw.createCell(7, (double)rnd.nextInt(10)/10, styles.get("coeff").getIndex());
			sw.createCell(8, rnd.nextInt(10000), styles.get("currency").getIndex());
			sw.createCell(9, calendar, styles.get("date").getIndex());

			sw.endRow();

			calendar.roll(Calendar.DAY_OF_YEAR, 1);
		}
		sw.endSheet();
	}

	//NIO�� �̿��Ͽ� ������ ����
	private static void generateNIO(FileChannel out, Map<String, XSSFCellStyle> styles) throws Exception {
		Random rnd = new Random();
		Calendar calendar = Calendar.getInstance();

		WriteXMLNIO sw = new WriteXMLNIO(out);
		sw.beginSheet();

		//insert header row
		sw.insertRow(0);
		int styleIndex = styles.get("header").getIndex();
		sw.createCell(0, "�÷�1", styleIndex);
		sw.createCell(1, "�÷�2", styleIndex);
		sw.createCell(2, "�÷�3", styleIndex);
		sw.createCell(3, "�÷�4", styleIndex);
		sw.createCell(4, "�÷�5", styleIndex);
		sw.createCell(5, "�÷�6", styleIndex);
		sw.createCell(6, "�÷�7", styleIndex);
		sw.createCell(7, "�÷�8", styleIndex);
		sw.createCell(8, "�÷�9", styleIndex);
		sw.createCell(9, "�÷�10", styleIndex);

		sw.endRow();

		//write data rows
		for (int rownum = 1; rownum <= 100000; rownum++) {
			sw.insertRow(rownum);

			sw.createCell(0, "Hello, " + rownum + "!");
			sw.createCell(1, (double)rnd.nextInt(100)/100, styles.get("percent").getIndex());
			sw.createCell(2, (double)rnd.nextInt(10)/10, styles.get("coeff").getIndex());
			sw.createCell(3, rnd.nextInt(10000), styles.get("currency").getIndex());
			sw.createCell(4, calendar, styles.get("date").getIndex());
			sw.createCell(5, "Hello, " + rownum + "!");
			sw.createCell(6, (double)rnd.nextInt(100)/100, styles.get("percent").getIndex());
			sw.createCell(7, (double)rnd.nextInt(10)/10, styles.get("coeff").getIndex());
			sw.createCell(8, rnd.nextInt(10000), styles.get("currency").getIndex());
			sw.createCell(9, calendar, styles.get("date").getIndex());

			sw.endRow();

			calendar.roll(Calendar.DAY_OF_YEAR, 1);
		}
		sw.endSheet();
	}

	//JAVA ���� IO�� �̿��Ͽ� �����͸� ����
	public static class WriteXMLOLD {
		private final Writer _out;
		private int _rownum;
		private final StringBuffer sb = new StringBuffer();

		public WriteXMLOLD(Writer out){
			_out = out;
		}

		public void beginSheet() throws IOException {
			sb.delete(0, sb.length());
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
			sb.append("<sheetData>\n");

			_out.write(sb.toString());
		}

		public void endSheet() throws IOException {
			sb.delete(0, sb.length());
			sb.append("</sheetData>");
			sb.append("</worksheet>");
			_out.write(sb.toString());
		}

		/**
		 * Insert a new row
		 *
		 * @param rownum 0-based row number
		 */
		public void insertRow(int rownum) throws IOException {
			sb.delete(0,sb.length());
			sb.append("<row r=\""+(rownum+1)+"\">\n");
			_out.write(sb.toString());
			this._rownum = rownum;
		}

		/**
		 * Insert row end marker
		 */
		public void endRow() throws IOException {
			sb.delete(0,sb.length());
			sb.append("</row>\n");
			_out.write(sb.toString());
		}

		public void createCell(int columnIndex, String value, int styleIndex) throws IOException {
			//���� Ư�����ڰ� ������츦 ����Ͽ� CDATA�� ���Ѵ�.
			StringBuffer sb2 = new StringBuffer();
			sb2.append("<![CDATA[");
			sb2.append(value);
			sb2.append("]]>");

			value = sb2.toString();

			String ref = new CellReference(_rownum, columnIndex).formatAsString();
			sb.delete(0,sb.length());
			sb.append("<c r=\""+ref+"\" t=\"inlineStr\"");

			if(styleIndex != -1) {
				sb.append(" s=\""+styleIndex+"\"");
			}
			sb.append(">");
			sb.append("<is><t>"+value+"</t></is>");
			sb.append("</c>");
			_out.write(sb.toString());
		}

		public void createCell(int columnIndex, String value) throws IOException {
			createCell(columnIndex, value, -1);
		}

		public void createCell(int columnIndex, double value, int styleIndex) throws IOException {
			String ref = new CellReference(_rownum, columnIndex).formatAsString();
			sb.delete(0,sb.length());
			sb.append("<c r=\""+ref+"\" t=\"n\"");

			if(styleIndex != -1) {
				sb.append(" s=\""+styleIndex+"\"");
			}
			sb.append(">");
			sb.append("<v>"+value+"</v>");
			sb.append("</c>");
			_out.write(sb.toString());
		}

		public void createCell(int columnIndex, double value) throws IOException {
			createCell(columnIndex, value, -1);
		}

		public void createCell(int columnIndex, Calendar value, int styleIndex) throws IOException {
			createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
		}
	}

	//JAVA NIO�� �̿��Ͽ� �����͸� ����
	public static class WriteXMLNIO {
		private final FileChannel _out;
		private int _rownum;
		private final ByteBuffer temp;

		private int idx = 0;

		public WriteXMLNIO(FileChannel out){
			_out = out;
			ByteBufferUtil.setEncoding("utf-8");
			temp = ByteBuffer.allocateDirect(100000);
		}

		private void write(String input) throws IOException{
			write(input, false);
		}

		/*
		 * nio�̿�� �ٷ� �ٷ� write�� �����ϱ⺸�� buffer�� �̿��Ͽ� buffer�� �� ���� I/O�� �����ϴ°���
		 * ������ ���� ������ ���δ�. �� ������ ��� �����͸��� write�� �ϴ°Ͱ� buffer�� �� ���� write��
		 * �����ϴ°Ͱ� ����ð��� 30���̻� ���̰� ����.
		 */

		private void write(String input, boolean is_force) throws IOException{
			ByteBuffer inputb = ByteBufferUtil.str_to_bb(input);

			boolean is_writefile = (is_force == true) ? true : (temp.position() +  inputb.limit() > temp.capacity());

			if(is_writefile) {
				temp.flip();

				/*
				 *FileChannel�� write()�� ����ϴ°ͺ��ٴ� MappedByteBuffer�� ����ϴ°��� ���������� ���δ�.
				 */
				//				_out.write(temp);
				_out.map(FileChannel.MapMode.READ_WRITE, idx, temp.limit()).put(temp);
				idx += temp.limit();
				temp.clear();
			}
			temp.put(inputb);
		}

		public void beginSheet() throws IOException {
			write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">" );
			write("<sheetData>\n");
		}

		public void endSheet() throws IOException {
			write("</sheetData>");
			write("</worksheet>", true);
		}

		/**
		 * Insert a new row
		 *
		 * @param rownum 0-based row number
		 */
		public void insertRow(int rownum) throws IOException {
			write("<row r=\""+(rownum+1)+"\">\n");
			this._rownum = rownum;
		}

		/**
		 * Insert row end marker
		 */
		public void endRow() throws IOException {
			write("</row>\n");
		}

		public void createCell(int columnIndex, String value, int styleIndex) throws IOException {
			//���� Ư�����ڰ� ������츦 ����Ͽ� CDATA�� ���Ѵ�.
			StringBuffer sb = new StringBuffer();
			sb.append("<![CDATA[");
			sb.append(value);
			sb.append("]]>");

			value = sb.toString();

			String ref = new CellReference(_rownum, columnIndex).formatAsString();
			write("<c r=\""+ref+"\" t=\"inlineStr\"");
			if(styleIndex != -1) {
				write(" s=\""+styleIndex+"\"");
			}
			write(">");
			write("<is><t>"+value+"</t></is>");
			write("</c>");
		}

		public void createCell(int columnIndex, String value) throws IOException {
			createCell(columnIndex, value, -1);
		}

		public void createCell(int columnIndex, double value, int styleIndex) throws IOException {
			String ref = new CellReference(_rownum, columnIndex).formatAsString();
			write("<c r=\""+ref+"\" t=\"n\"");
			if(styleIndex != -1) {
				write(" s=\""+styleIndex+"\"");
			}
			write(">");
			write("<v>"+value+"</v>");
			write("</c>");
		}

		public void createCell(int columnIndex, double value) throws IOException {
			createCell(columnIndex, value, -1);
		}

		public void createCell(int columnIndex, Calendar value, int styleIndex) throws IOException {
			createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
		}
	}
}
