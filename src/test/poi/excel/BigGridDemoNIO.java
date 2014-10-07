/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package test.poi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import test.util.ByteBufferUtil;

/**
 * Demonstrates a workaround you can use to generate large workbooks and avoid OutOfMemory exception.
 *
 * The trick is as follows:
 * 1. create a template workbook, create sheets and global objects such as cell styles, number formats, etc.
 * 2. create an application that streams data in a text file
 * 3. Substitute the sheet in the template with the generated data
 *
 * <p>
 *      Since 3.8-beta3 POI provides a low-memory footprint SXSSF API which implementing the "BigGridDemo" strategy.
 *      XSSF is an API-compatible streaming extension of XSSF to be used when
 *      very large spreadsheets have to be produced, and heap space is limited.
 *      SXSSF achieves its low memory footprint by limiting access to the rows that
 *      are within a sliding window, while XSSF gives access to all rows in the
 *      document. Older rows that are no longer in the window become inaccessible,
 *      as they are written to the disk.
 * </p>
 * See <a "http://poi.apache.org/spreadsheet/how-to.html#sxssf">
 *     http://poi.apache.org/spreadsheet/how-to.html#sxssf</a>.

 *
 * @author Yegor Kozlov
 */
public class BigGridDemoNIO {
	//	private static final String XML_ENCODING = "UTF-8";
	private static ByteBuffer temp = ByteBuffer.allocateDirect(1024*10);

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		// Step 1. Create a template file. Setup sheets and workbook-level objects such as
		// cell styles, number formats, etc.

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Big Grid");

		Map<String, XSSFCellStyle> styles = createStyles(wb);
		//name of the zip entry holding sheet data, e.g. /xl/worksheets/sheet1.xml
		String sheetRef = sheet.getPackagePart().getPartName().getName();

		//save the template
		FileOutputStream os = new FileOutputStream("template.xlsx");
		wb.write(os);
		os.close();

		//Step 2. Generate XML file.
		//File tmp = File.createTempFile("sheet", ".xml");
		File tmp = new File("d:/sheet.xml");
		RandomAccessFile fos = new RandomAccessFile(tmp, "rw");
		FileChannel fc = fos.getChannel();
		generate(fc, styles);
		fos.close();
		fc.close();

		//Step 3. Substitute the template entry with the generated data
		FileOutputStream out = new FileOutputStream("d:/big-grid.xlsx");
		substitute(new File("template.xlsx"), tmp, sheetRef.substring(1), out);
		out.close();

		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
	}

	/**
	 * Create a library of cell styles.
	 */
	public static Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb){
		Map<String, XSSFCellStyle> styles = new HashMap<String, XSSFCellStyle>();
		XSSFDataFormat fmt = wb.createDataFormat();

		XSSFCellStyle style1 = wb.createCellStyle();
		style1.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		style1.setDataFormat(fmt.getFormat("0.0%"));
		styles.put("percent", style1);

		XSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style2.setDataFormat(fmt.getFormat("0.0X"));
		styles.put("coeff", style2);

		XSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		style3.setDataFormat(fmt.getFormat("$#,##0.00"));
		styles.put("currency", style3);

		XSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		style4.setDataFormat(fmt.getFormat("mmm dd"));
		styles.put("date", style4);

		XSSFCellStyle style5 = wb.createCellStyle();
		XSSFFont headerFont = wb.createFont();
		headerFont.setBold(true);
		style5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style5.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style5.setFont(headerFont);
		styles.put("header", style5);

		return styles;
	}

	private static void generate(FileChannel fc, Map<String, XSSFCellStyle> styles) throws Exception {

		Random rnd = new Random();
		Calendar calendar = Calendar.getInstance();

		SpreadsheetWriter sw = new SpreadsheetWriter(fc);
		sw.beginSheet();

		//insert header row
		sw.insertRow(0);
		int styleIndex = styles.get("header").getIndex();
		sw.createCell(0, "컬럼1", styleIndex);
		sw.createCell(1, "컬럼2", styleIndex);
		sw.createCell(2, "컬럼3", styleIndex);
		sw.createCell(3, "컬럼4", styleIndex);
		sw.createCell(4, "컬럼5", styleIndex);
		sw.createCell(5, "컬럼6", styleIndex);
		sw.createCell(6, "컬럼7", styleIndex);
		sw.createCell(7, "컬럼8", styleIndex);
		sw.createCell(8, "컬럼9", styleIndex);
		sw.createCell(9, "컬럼10", styleIndex);

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

	/**
	 *
	 * @param zipfile the template file
	 * @param tmpfile the XML file with the sheet data
	 * @param entry the name of the sheet entry to substitute, e.g. xl/worksheets/sheet1.xml
	 * @param out the stream to write the result to
	 */
	private static void substitute(File zipfile, File tmpfile, String entry, OutputStream out) throws IOException {
		ZipFile zip = new ZipFile(zipfile); //Excel파일내에 여러개의 xml이 들어있기 때문에 ZipFile을 쓴다.

		ZipOutputStream zos = new ZipOutputStream(out);

		/*
		 * java.util.zip의 내용으로는 전체내용을 복사하는 도중에 특정파일내용을 빼거나 수정하는 방법외에는
		 * 압축파일내의 파일을 수정하거나 삭제할수 있는 방법이 존재하지 않음
		 * 
		 * entry(실제 엑셀데이터가 들어있는 xml파일)을 제외한 나머지 파일의 내용을 복사한다.
		 */
		@SuppressWarnings("unchecked")
		Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
		while (en.hasMoreElements()) {
			ZipEntry ze = en.nextElement();
			if(!ze.getName().equals(entry)){
				zos.putNextEntry(new ZipEntry(ze.getName()));
				InputStream is = zip.getInputStream(ze);
				copyStream(is, zos);
				is.close();
			}
		}

		//실제 엑셀이 들어있는 부분을 입력한다.
		zos.putNextEntry(new ZipEntry(entry));
		InputStream is = new FileInputStream(tmpfile);
		copyStream(is, zos);
		is.close();
		zos.close();
	}

	private static void copyStream(InputStream in, OutputStream out) throws IOException {
		/*byte[] chunk = new byte[1024];
		int count;
		while ((count = in.read(chunk)) >=0 ) {
			out.write(chunk,0,count);
		}*/


		ReadableByteChannel rc = Channels.newChannel(in);
		WritableByteChannel wc = Channels.newChannel(out);
		temp.clear();

		while (rc.read(temp) >=0 ) {
			temp.flip();
			wc.write(temp);
			temp.clear();
		}
	}

	/**
	 * Writes spreadsheet data in a Writer.
	 * (YK: in future it may evolve in a full-featured API for streaming data in Excel)
	 */
	public static class SpreadsheetWriter {
		private final FileChannel _out;
		private int _rownum;
		private ByteBuffer temp;

		private int idx = 0;

		public SpreadsheetWriter(FileChannel out){
			_out = out;
			ByteBufferUtil.setEncoding("utf-8");
			temp = ByteBuffer.allocateDirect(100000);
		}

		private void write(String input) throws IOException{
			write(input, false);
		}

		/*
		 * nio이용시 바로 바로 write를 수행하기보다 buffer를 이용하여 buffer가 다 차면 I/O를 수행하는것이
		 * 월등히 높은 성능을 보인다. 이 예제의 경우 데이터마다 write를 하는것과 buffer가 다 차면 write를
		 * 수행하는것과 실행시간이 30초이상 차이가 난다.
		 */

		private void write(String input, boolean is_force) throws IOException{
			ByteBuffer inputb = ByteBufferUtil.str_to_bb(input);

			boolean is_writefile = (is_force == true) ? true : (temp.position() +  inputb.limit() > temp.capacity());

			if(is_writefile) {
				temp.flip();

				/*
				 *FileChannel의 write()를 사용하는것보다는 MappedByteBuffer를 사용하는것이 나은성능을 보인다.
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
			write("</worksheet>");
			write("", true);
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
			//값에 특수문자가 있을경우를 대비하여 CDATA로 감싼다.
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