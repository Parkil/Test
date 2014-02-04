import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BulkInsertTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@180.100.100.47:1521:smartcrm", "SCRM_DEV", "SCRM_DEV");

		String sql = "BULK INSERT test FROM 'd:/a.txt' WITH (FIELDTERMINATOR = ',')";

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				//System.out.println(rs.getString(1) + " " + rs.getString(2));
				System.out.println(" Going through data");
			}
		}catch(SQLException e) {
			con.rollback();
			e.printStackTrace();
		}finally {
			con.commit();
			con.close();
		}
	}
}
