package Design;

import org.openqa.selenium.WebElement;

public interface WebSpecificActions {
	/**
	 * This method is used to get the webelements
	 *
	 * @param locator  - This is the locator type and locator value of the web element
	 * @author Agnel J 
	 * @return webElement
	 */
	public WebElement locateElement(String locator);


	/**
	 * This method is used to find whether the element is displayed
	 * @param locator - The locator and locator value of the element
	 * @author Agnel J 
	 * @return boolean
	 */

	public boolean elementIsDisplayed(String locator);

	/**
	 * This method is used verify the text of a element
	 *
	 * @param locator  - This is the locator type of the web element
	 * @param element  - This is the webelement for which the text is to be verified
	 * @param expected - This is the Expected text of the element to be verified
	 * @author Agnel J 
	 * @return boolean
	 */

	public boolean verifyElementtext(String locator, String expected);

	/**
	 * This method is used verify whether the text contains a part of the Expected
	 * text
	 *
	 * @param locator  - This is the webelement of the element to be displayed
	 * @param expected - This is the Expected text of the element to be verified
	 * @author Agnel J 
	 * @return
	 */

	public boolean verifyElementContainsText(String locator,String expected);

	/**
	 * This method is used to click the element
	 *
	 * @param locator      - The locator of the Element to be clicked
	 * @param locatorvalue - The locator value of the locator to be clicked
	 * @author Agnel J 
	 */

	public void clickElement(String locator);

	/**
	 * This method is used to enter the input in a field
	 *
	 * @param locator      - The locator type and locator value of the Element to be Entered.
	 * @param locatorvalue - The value of the locator type
	 * @param Text         - The Value of the text to be entered
	 * @author Agnel J 
	 */

	public void EnterInput(String locator, String text);

	/**
	 * This method is used to get the text of the webelement
	 *
	 * @param locator      - The locator type and locator value of the Element to be retreived
	 * @param locatorvalue - The value of the locator type
	 * @author Agnel J .
	 */

	public String getElementText(String locator);

	/**
	 * This method is used to launch the browser
	 *
	 * @param browser - name of the browser to be launched.
	 */
	public void LaunchBrowser(String browser);

	/**
	 * This method is used to check whether the particular element is enabled
	 *
	 * @param locator     - locatortype and locator value of the element
	 * @param locatovalue - The value of the locator
	 * @author Agnel J
	 */

	public Boolean isEnabled(String locator);

	/**
	 * This method is used to check whether the particular element is selected
	 *
	 * @param locator     - The locator type and locator value of the element
	 * @param locatovalue - The value of the locator
	 * @author Agnel J
	 */

	public Boolean isSelected(String locator);

	/**
	 * This method is used to check whether the entered text in the field is cleared
	 *
	 * @param locator     - The locator type and locator value of the element
	 * @param locatovalue - The value of the locator
	 * @author Agnel J
	 */

	public void clearInput(String locator);

	/**
	 * This method is used to check whether the element is displayed
	 *
	 * @param locator      - The locator type and locator value of the element

	 * @author Agnel J
	 */

	public boolean isDisplayed(String locator);

	/**
	 * This method is used to select the visible text in a dropdown
	 *
	 * @param locator      - The locator type and locator value of the element
	 * @param locatorvalue - The value of the locator
	 * @param visibletext  - The visible text to be selected
	 * @author Agnel J
	 */

	public void selectDropDownByVisibleText(String locator, String visibletext);

	/**
	 * This method is used to select the dropdown using value
	 *
	 * @param locator      - The locator type and locator value of the element

	 * @param value        - The value of the dropdown
	 * @author Agnel J 
	 */

	public void selectDropDownByValue(String locator, String value);

	/**
	 * This method is used to select the dropdown using index
	 *
	 * @param locator      - The locator type and locator value of the element

	 * @param index        - The index of the dropdown value.
	 * @author Agnel J 
	 */

	public void selectDropdownByindex(String locator, int index);

	/**
	 * This method is used to get the color of the element
	 *
	 * @param locator      - The locator type and locator value of the element
	 * @param locatorvalue - The value of the locator
	 * @author Agnel J 
	 */

	public String getElementColour(String locator);

	/**
	 * This method is used to switch to frame
	 *
	 * @param index - The index of the frame to be switched.
	 * @author Agnel J 
	 */

	public void switchToFrameUsingIndex(int index);

	/**
	 * This method is used to switch to frame using webelement
	 *
	 * @param locator      - The locator type and locator value of the element
	 * @author Agnel J 
	 */

	public void switchToFrameUsingLocator(String locator);

	/**
	 * This method is used to switch to the default window or to come out of a
	 * frame.
	 *
	 * @author Agnel J 
	 */

	public void switchToDefaultContent();

	/**
	 * This method is used to switch to windows using index
	 *
	 * @param index - The index of the window to be switched
	 * @author Agnel J .
	 */

	public void switchToWindowUsingIndex(int index);

	/**
	 * This method is used to switch to the window using title
	 *
	 * @param title - The title of the window to be switched.
	 * @author Agnel J 
	 */

	public void switchToWindowUsingTitle(String title);

	/**
	 * This method is used to get the title of the window using the index
	 *
	 * @param index - The index of the window
	 * @author Agnel J .
	 */

	public String getCurrentWindowTitle(int index);

	/**
	 * This method is used to accept the alert displayed
	 *
	 * @author Agnel J 
	 */

	public void acceptAlert();

	/**
	 * This method is used to dismiss the alert displayed
	 *
	 * @author Agnel J 
	 */

	public void dismissAlert();

	/**
	 * This method is used to get the Alert text
	 *
	 * @author Agnel J 
	 */

	public String getAlertText();

	/**
	 * This method is used to perform a double click action
	 *
	 * @param locator      - The locator type and locator value of the element
	 * @param locatorValue - This is the Value of the locator.
	 * @author Agnel J 
	 */

	public void doubleClick(String locator);

	/**
	 * This method is used to perform a drag and Drop function
	 *
	 * @param locator       - This is the locator type of the web element

	 * @param locator2      - This is the locator type of the web element

	 */

	public void dragAndDrop(String locator, String locator2);

	/**
	 * This method is used to send a text to an alert
	 *
	 * @param alerttext - The text to be sent to an alert
	 * @author Agnel J 
	 */

	public void sendAlertText(String alerttext);

	/**
	 * This method will wait till element is invisible
	 *
	 * @param waittime     - The time to wait
	 * @param locator      - The locator type and locator value of the element

	 * @author Agnel J 
	 */

	public void waitUntilInvisibilityOfElement(int waitime, String locator);

	/**
	 * This method will wait till element is clickable
	 *
	 * @param waittime     - The time to wait
	 * @param locator      - The locator type and locator value of the element
	 * @author Agnel J 
	 */

	public void waitUntilElementIsClickable(int waitime, String locator);

	/**
	 * This method is used to wait till a element appear
	 *
	 * @param waittime     - The time to wait
	 * @param locator      - The locator type and locator value of the element
	 * @author Agnel J 
	 */

	public void waitUntilElementIsDisplayed(int waitime, String locator);

	/**
	 * This method will wait until all the elements are located
	 * @param waittime     - The time to wait
	 * @param locator      - The locator type and locator value of the element
	 * @author Agnel J  *
	 */

	public void waitUntilPresenceOfAllElementsLocated(int waitime, String locator);

	/**
	 * This method will wait until all the elements are located
	 *
	 * @param waittime     - The time to wait
	 * @param locator      - The locator type and locator value of the element
	 * @author Agnel J  *
	 */

	public void waitUntilElementToBeSelected(int waitime, String locator);

	/**
	 * This method will wait until the elemnt is located
	 *
	 * @param waittime     - The time to wait
	 * @param locator      - The locator type of the element to wait for
	 * @param locatorvalue - The locatorvalue of the element to wait for.
	 * @author Agnel J 
	 */

	public void waitUntilPresenceofElementlocated(int waitime, String locator);

	/**
	 * This method will waituntil all the element appears
	 *
	 * @param waittime     - The time to wait
	 * @param locator      - The locator type of the element to wait for
	 * @author Agnel J 
	 */

	public void waitUntilVisisblityOfAllElementsLocated(int waitime, String locator);

	/**
	 * This method will waituntil all the element disappears
	 *
	 * @param waittime     - The time to wait
	 * @param locator      - The locator type and locator value of the element to wait for

	 * @author Agnel J 
	 */

	public void waitUntilInvisibliltyOfAllElementsLocated(int waitime, String locator);

	/**
	 * This method will waituntil all the element disappears
	 *
	 * @param waittime     - The time to wait
	 * @param locator      - The locator type and locator value of the element to wait for
	 * @author Agnel J 
	 */
	public void waituntilFrameIsVisibleAndSwitchToIt(int waitime, String locator);
	/**
	 * This method is used to get the configurations from the properties file
	 * @param key     - The key of the key value pair in the properties file
	 * @author Agnel J
	 */
	public String getConfigurations(String key);
	/**
	 * This method is used to click the element
	 *
	 * @param locator      - The locator of the Element to be clicked
	 * @param waitime      -  The time to wait until element is displayed.
	 * @param locatorvalue - The locator value of the locator to be clicked
	 * @author Agnel J 
	 */
	
	
	public String getLocators(String module,String locator) ;
	
	public void clickElement(String locator, int waittime);


	/**
	 * This method is used to enter the input in a field
	 *
	 * @param locator      - The locator type and locator value of the Element to be Entered.
	 * @param waitime      -  The time to wait until element is displayed.
	 * @param Text         - The Value of the text to be entered
	 * @author Agnel J 
	 */
	public void EnterInput(String locator, String text, int waittime);


	public void refreshBrowser();
	/**
	 * This method is used to refresh the browser
	 *
	 * @author Agnel J 
	 */



}