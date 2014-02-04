package test.mybatis;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class UpdateKCSI {
	private final static String resource = "test/mybatis/sqlConfig.xml";
	private static Reader reader;
	private static SqlSessionFactory factory;
	private static String[] rows = {
		"313032058",
		"313032071",
		"313032178",
		"313032143",
		"313032071",
		"313032245",
		"313032225",
		"313032227",
		"313032127",
		"313032245",
		"313032225",
		"313032225",
		"313032225",
		"313032225",
		"313032225",
		"313032143",
		"313032127",
		"313032225",
		"313032178",
		"313032227",
		"313032227",
		"313032127",
		"313032227",
		"313032058",
		"313032143",
		"313032243",
		"313032228",
		"313032179",
		"313032172",
		"313032243",
		"313032172",
		"313032058",
		"313032058",
		"313032127",
		"313032227",
		"313032225",
		"313032225",
		"313032127",
		"313032227",
		"313032225",
		"313032179",
		"313032127",
		"313032228",
		"313032127",
		"313032148",
		"313032228",
		"313032228",
		"313032245",
		"313032227",
		"313032127",
		"313032243",
		"313032225",
		"313032127"
	};

	static {
		UpdateKCSI.init();
		try {
			UpdateKCSI.reader = Resources.getResourceAsReader(UpdateKCSI.resource);
			UpdateKCSI.factory = new SqlSessionFactoryBuilder().build(UpdateKCSI.reader);
			UpdateKCSI.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void init() {
		//		String content = UpdateKCSI.util.readFile(UpdateKCSI.path);
		//		UpdateKCSI.rows = UpdateKCSI.row.split(content);

	}


	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		SqlSession session = UpdateKCSI.factory.openSession(ExecutorType.BATCH, false);

		try {
			HashMap<String,String> param = new HashMap<String,String>();

			session.getConnection().setAutoCommit(false);
			for(String row : UpdateKCSI.rows) {
				param.put("SURVEYID",	"151");
				param.put("CMPGNID",	"CP201211130019");
				param.put("CMPGNCSTID",	row);
				int cnt = session.update("SqlDummy.updateKCSI",param);
				System.out.println(row +" : "+cnt);
			}
		}catch(Exception e) {
			try {
				session.getConnection().rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			try {
				session.getConnection().commit();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		session.close();
		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
	}
}
