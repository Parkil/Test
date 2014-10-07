package test.poi.excel;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/*
 * Apache POI 3.9 라이브러리를 이용하여 xlsx형식으로 엑셀을 저장하는 예제
 * xlsx형식으로 많은 데이터를 저장할 경우 outofmemory error가 발생하는 경우가 있으니 주의
 * 
 * 필요 라이브러리
 * dom4j-1.6.1.jar
 * poi-3.9-20121203.jar
 * poi-ooxml-3.9-20121203.jar
 * poi-ooxml-schemas-3.9-20120326.jar
 * stax-api-1.0.1.jar
 * xmlbeans-2.3.0.jar
 */
public class WriteExcel {
	public static void main(String[] args) throws Throwable {
		long start = System.currentTimeMillis();

		SXSSFWorkbook wb = new SXSSFWorkbook(1000); // keep 100 rows in memory, exceeding rows will be flushed to disk
		Sheet sh = wb.createSheet();
		for(int rownum = 0; rownum < 100000; rownum++){
			Row row = sh.createRow(rownum);
			for(int cellnum = 0; cellnum < 10; cellnum++){
				Cell cell = row.createCell(cellnum);
				String address = new CellReference(cell).formatAsString();
				cell.setCellValue(address);
			}

		}

		// Rows with rownum < 900 are flushed and not accessible
		//        for(int rownum = 0; rownum < 900; rownum++){
		//          Assert.assertNull(sh.getRow(rownum));
		//        }

		// ther last 100 rows are still in memory
		//        for(int rownum = 900; rownum < 1000; rownum++){
		//            Assert.assertNotNull(sh.getRow(rownum));
		//        }

		FileOutputStream out = new FileOutputStream("d:/sxssf.xlsx");
		wb.write(out);
		out.close();

		// dispose of temporary files backing this workbook on disk
		//        wb.dispose();
		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
	}
}

