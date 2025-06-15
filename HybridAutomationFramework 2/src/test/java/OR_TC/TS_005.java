package OR_TC;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ProjectSpecificBase.WebBase;
import pages.OrangeHRMLoginPage;

public class TS_005 extends WebBase {

	@BeforeTest
	public void setValues() {

		testName = "TS_REP_001";
		testDescription = "Test the service and response";
		category = "Smoke_test";
		testAuthor = "Agnel";
	}
	@Test
	public void tS_SI_001() throws Exception {
		OrangeHRMLoginPage loginPage = new OrangeHRMLoginPage(driver, node);
        loginPage.go_to_URL();
      
        // Analyze network traffic 
        analyzeNetworkData("/Users/agnelj/Downloads/Service_Report_Seraphine_AI.xlsx");
	}
}   
  
           