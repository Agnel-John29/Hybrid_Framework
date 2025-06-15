package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentTest;

import ProjectSpecificBase.WebBase;

public class OrangeHRMLoginPage extends WebBase {

	public OrangeHRMLoginPage(RemoteWebDriver driver, ExtentTest node) {
		this.driver = driver;
		this.node = node;
	}

	public void go_to_URL() throws Exception {
		try {
			driver.get(getConfigurations("url"));
			reportStep("The user has entered the URL", "pass");

		} catch (Exception e) {
			reportStep("The user was not able to go to the URL beacuse of the exception" + e + "", "fail");
			throw new RuntimeException();

		}

	}

	public void enter_username(String username) throws Exception {

		try {
			waitUntilElementIsDisplayed(20, getLocators("Login", "usernamefield"));
			EnterInput(getLocators("Login", "usernamefield"), username);
			reportStep("The user has entered the username", "pass");
			Thread.sleep(2000);
		} catch (Exception e) {
			reportStep("The user was not able to enter the username because of the exception " + e + "", "fail");

			// System.out.println(e);
			throw new RuntimeException();

		}

	}

	public void enter_password(String password) throws Exception {

		try {
			waitUntilElementIsDisplayed(30, getLocators("Login", "passwordfield"));
			EnterInput(getLocators("Login", "passwordfield"), password);
			reportStep("The user has entered the password", "pass");
			Thread.sleep(2000);
		} catch (Exception e) {
			reportStep("The user was not able to enter the password beacause of the exception" + e + "", "fail");
			throw new RuntimeException();

		}

	}
	public void scrollToElement(String locator) {
		try {
			WebElement element = locateElement(locator); // Locate the element
			Actions actions = new Actions(driver);
			actions.moveToElement(element);
			actions.perform();
		} catch (Exception e) {
			System.out.println("Failed to scroll to the element: " + e.getMessage());
		}
	}
	
	public void pressEnterKey() {

		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.ENTER).build().perform();
	}
	

	public void click_login() throws Exception {

		try {
			waitUntilElementIsDisplayed(60, getLocators("Login", "Login"));
			clickElement(getLocators("Login", "Login"));
			reportStep("The user was able to click the login button", "pass");
			waitUntilElementIsDisplayed(60, getLocators("Login", "create"));
			clickElement(getLocators("Login", "create"));
			Thread.sleep(5000);
			EnterInput(getLocators("Login", "createField"), "Motivational");
			scrollToElement(getLocators("Login", "socialmedia"));
			Thread.sleep(8000);
			

		} catch (Exception e) {
			reportStep("The user was not able to click the login button, because of the exception " + e + "", "pass");
			throw new RuntimeException();

		}

	}

}