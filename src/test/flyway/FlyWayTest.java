package test.flyway;

import org.flywaydb.core.Flyway;

/*
 * DB Migration Tool인 FlyWay 테스트 코드
 * Community Edition(무료버전)의 경우 오라클 12.2이상 버전만 지원하니 참고할것
 * Flyway에서 버전을 체크하는 방식이 jdbc jar파일을 기준으로 체크하니 버전이 잘못표시되는 경우에는 오라클 버전에 맞는 jdbc jar파일을 참조할것
 */
public class FlyWayTest {
	public static void main(String[] args) throws Exception{
		Flyway flyway = Flyway.configure().dataSource("jdbc:oracle:thin:@//10.253.43.76:1521/CRM", "US_SWAFCOM", "tlstprP1!").load();
		flyway.info();
		//System.out.println(flyway.info());
	}
}
