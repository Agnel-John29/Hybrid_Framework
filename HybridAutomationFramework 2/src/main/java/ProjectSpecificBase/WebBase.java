package ProjectSpecificBase;

import java.net.MalformedURLException;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.Web_Html_Reporter;

public class WebBase extends Web_Html_Reporter {

	@BeforeMethod
	public void precondition() throws MalformedURLException {
		node = test.createNode(testName);
		LaunchBrowser(getConfigurations("browser"));
		driver.manage().window().maximize();

	}

	@AfterMethod(alwaysRun = true)

	public void postcondition() throws Exception {

		driver.quit();

	}

}