import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
/*
 * Selenium - Export Test Case As - Java / JUnit 4 / Remote Control�� Export�� �׽�Ʈ���̽�
 * selenium-server-standalone-2.35.0.jar �� �����ϰ� �׽�Ʈ ���̽��� ������ ���������� ������
 */
public class A {
	private Selenium selenium;

	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome",
				"http://www.armaholic.com/");
		selenium.start();
	}

	@Test
	public void testA() throws Exception {
		selenium.open("/list.php?c=arma2_oa_files_scenarios_campaigns&s=title&w=asc&d=30");
		selenium.click("css=a > font");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Downloads");
		selenium.waitForPageToLoad("30000");
		selenium.click("css=img[alt=\"Arma 2: Operation Arrowhead and Arma 2: Combined Operations Files\"]");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
