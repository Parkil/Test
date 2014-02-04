package test.mybatis;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import test.util.FileUtil;

public class InsertEximHist {
	private final static String resource = "test/mybatis/sqlConfig.xml";
	private static Reader reader;
	private static SqlSessionFactory factory;
	private static FileUtil util = new FileUtil();
	private static String[] rows = null;
	private static Pattern row = Pattern.compile("\n");
	private static Pattern spe = Pattern.compile("\\|");

	//	private static List<String> paramList;


	private static String path = "d:/13exp-05.txt";
	private static String eximdate = "2013-05-01";
	//private static String path = "d:/13exp-04.txt";
	//private static String eximdate = "2013-04-01";
	static {
		try {
			InsertEximHist.reader = Resources.getResourceAsReader(InsertEximHist.resource);
			InsertEximHist.factory = new SqlSessionFactoryBuilder().build(InsertEximHist.reader);
			InsertEximHist.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void init() {
		String content = InsertEximHist.util.readFile(InsertEximHist.path,"euc-kr");
		InsertEximHist.rows = InsertEximHist.row.split(content);
	}


	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		SqlSession session = InsertEximHist.factory.openSession(ExecutorType.BATCH, false);
		String[] value = null;

		try {
			HashMap<String,String> param = new HashMap<String,String>();
			int i = 0;
			session.getConnection().setAutoCommit(false);
			for(String row : InsertEximHist.rows) {
				value = InsertEximHist.spe.split(row);
				param.put("BSNO",		value[2]);
				param.put("EXIMDATE",	InsertEximHist.eximdate);
				param.put("HSCD",		value[7]);
				param.put("COUNTRYCD",	value[9]);
				param.put("CONAME",		value[0]);
				param.put("CONAME_EN",	" ");
				param.put("EXIMGBN",	"1");
				param.put("EXIMAMT",	value[10]);
				param.put("CUR",		"USD");
				param.put("REPNAME",	value[1]);
				param.put("ZIPID",		value[3]);
				param.put("ADDRESS",	value[4]);
				param.put("TELNO",		value[5]);
				param.put("EMAIL",		value[6]);
				param.put("INTRITEM",	value[8]);
				session.insert("SqlDummy.insertEximHist",param);
				System.out.println(++i);
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				session.getConnection().rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			try {
				session.flushStatements();
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
