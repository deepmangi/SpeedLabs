package glueCode;

import globalSetup.Base;
import globalSetup.GeneralMethods;
import io.cucumber.java.en.Given;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.assertions.LocatorAssertions;

public class LoginActions implements Base {

	@Given("Navigate to Inventory page in sauce labs with viewport {string}")
	public void login_in_to_on_boarded_admin_for(String string) throws Exception {
		
		String url = prop.getProp("url");
		com.open(url,string);
		
		//verification of inventory page
		Locator inventoryContainer = GeneralMethods.page.locator(ln.inventoryContainer);

		try {
			assertThat(inventoryContainer)
		        .isVisible(new LocatorAssertions.IsVisibleOptions()
		                .setTimeout(2000));
		}catch(TimeoutError e) {
			throw new Exception("Inventory page not loaded within 2 Seconds!");
		}
		
		System.out.println("Successfully Navigated to Inventory page!");
	}

}
