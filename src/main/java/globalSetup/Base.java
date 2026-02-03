package globalSetup;

import locators.*;

public interface Base {
	
	ExcelUtility xl = new ExcelUtility();
	BLTestMethods bl = new BLTestMethods();
	GetProperties prop = new GetProperties();
	GeneralMethods com = new GeneralMethods();
	EmailSetup mail = new EmailSetup();
	
	LoginLocators ln = new LoginLocators();
	TC001Locators tc1 = new TC001Locators();
	
}
