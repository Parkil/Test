import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import test.util.FileUtil;


public class DBTest {
	public static void main(String[] args) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@180.100.100.47:1521:smartcrm", "SCRM", "SMARTCRM");

		try {
			Statement stmt = con.createStatement();
			con.setAutoCommit(false);

			FileUtil util = new FileUtil();
			String data = util.readFile("d:/corpgbn", "euc-kr");
			String[] datas = data.split("\n");
			int execcnt = 0;
			for(String z : datas) {
				System.out.println(z);
				execcnt += stmt.executeUpdate(z);
			}
			System.out.println("총 업데이트수 : "+execcnt);
		}catch(SQLException e) {
			con.rollback();
			e.printStackTrace();
		}finally {
			con.commit();
			con.close();
		}
	}
}