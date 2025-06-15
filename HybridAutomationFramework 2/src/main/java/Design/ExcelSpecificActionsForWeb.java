package Design;

import java.io.IOException;

public interface ExcelSpecificActionsForWeb {

	/**
	 * This method is used to read the locator values
	 *
	 * @param locatorpath - The locator path is used
	 * @throws IOException
	 * @author Agnel J 
	 */

	public void readLocator(String locatorfilepath, String sheetname) throws IOException;

	/**
	 * This method is used to load the data to the Hashmap
	 *
	 * @param testscriptID - The testscriptID of the testdata sheet
	 * @param testdataname - The testdata name of the testdata sheet
	 * @author Agnel J 
	 * @throws IOException
	 */

	public String testdataload(String sheetname,String testscriptID, String testdataname) throws IOException;

	public String readAndDecryptexcel(String encryptedValue) throws IOException;
	/**
	 * This method is used to load the encrypteddata to the Hashmap
	 *
	 * @param testname - The testname of the method to be used
	 * @author Agnel J 
	 * @throws IOException
	 */

}