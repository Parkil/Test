import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
/*
 * Selenium - Export Test Case As - Java / JUnit 4 / WebDriver로 Export한 테스트케이스
 * 상단 url창이 빈칸으로 남아 있으면서 timeout 에러가남 추후 확인 필요
 */
public class B {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private final StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://www.armaholic.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testB() throws Exception {
		driver.get(baseUrl
				+ "/list.php?c=arma2_oa_files_scenarios_campaigns&s=title&w=asc&d=30");
		driver.findElement(By.cssSelector("a > font")).click();
		driver.findElement(By.linkText("Downloads")).click();
		driver.findElement(
				By.cssSelector("img[alt=\"Arma 2: Operation Arrowhead and Arma 2: Combined Operations Files\"]"))
				.click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
