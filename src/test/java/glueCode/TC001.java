package glueCode;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.assertions.LocatorAssertions;

import globalSetup.Base;
import globalSetup.GeneralMethods;
import io.cucumber.java.en.Then;

public class TC001 implements Base {

	@Then("Add Sauce Labs Backpack to cart")
	public void add_backpack_to_cart() throws Exception {
		try {
			com.waitFor(tc1.sauceLabsBackpack, 10);
		}catch(TimeoutError e) {
			throw new Exception("Sauce Labs Backpack is not available on inventory page!");
		}
			
		boolean backpackAddedToCard = com.elementPresent(tc1.addBackpackToCartBtn);
		
		if(backpackAddedToCard) {
			
			if(com.elementEnabled(tc1.addBackpackToCartBtn)){
				com.click(tc1.addBackpackToCartBtn);
			}else {
				throw new Exception("Unable to click on add to cart,Button is not clickable!");
			}
			
			assertThat(GeneralMethods.page.locator(tc1.removeItemBtn))
	        .isVisible(new LocatorAssertions.IsVisibleOptions()
	                .setTimeout(5000));

			assertThat(GeneralMethods.page.locator(tc1.removeItemBtn))
	        .hasCSS(
	            "color",
	            "rgb(226, 35, 26)",
	            new LocatorAssertions.HasCSSOptions().setTimeout(5000)
	        );
			
			
			System.out.println("Backpack Added to cart!");
			System.out.println("Remove button visible with colour changed to RED");
			
		} else if(!backpackAddedToCard){
			 com.elementPresent(tc1.removeItemBtn);
			 System.out.println("Backpack Already in cart!");
		}
	}
	
	@Then("Navigate to card and complete the purchase")
	public void navigate_to_cart_and_complete_purchase() throws Exception {
		com.click(tc1.cartIcon);
		
		assertThat(GeneralMethods.page.locator(tc1.cartContainer))
        .isVisible(new LocatorAssertions.IsVisibleOptions()
                .setTimeout(5000));
		
		//verifying item present in cart
		try {
			assertThat(GeneralMethods.page.locator(tc1.removeItemBtn))
	        .isVisible(new LocatorAssertions.IsVisibleOptions()
	                .setTimeout(5000));
			System.out.println("Backpack Added to cart!");
		}catch(TimeoutError e) {
			throw new Exception("Sauce lab backpack is not present in cart!");
		}
		
		try {
			assertThat(GeneralMethods.page.locator(tc1.checkoutBtn))
	        .isEnabled(new LocatorAssertions.IsEnabledOptions()
	                .setTimeout(5000));
			
			com.click(tc1.checkoutBtn);
		}catch(TimeoutError e) {
			throw new Exception("Checkout button is not enabled!");
		}
		
		assertThat(GeneralMethods.page.locator(tc1.checkoutInfoSection))
        .isVisible(new LocatorAssertions.IsVisibleOptions()
                .setTimeout(5000));
		
		//filling checkout Forms
		com.type(tc1.fName, "Deep");
		com.type(tc1.lName, "test");
		com.type(tc1.zipcode, "123456");
		
		com.click(tc1.continueBtn);
		
		try {
			assertThat(GeneralMethods.page.locator(tc1.checkoutSummary))
	        .isVisible(new LocatorAssertions.IsVisibleOptions()
	                .setTimeout(5000));
		}catch(TimeoutError e) {
			throw new Exception("Summary section is not visible!");
		}
		
		try {
			assertThat(GeneralMethods.page.locator(tc1.finishBtn))
	        .isEnabled(new LocatorAssertions.IsEnabledOptions()
	                .setTimeout(5000));
			
			com.click(tc1.finishBtn);
		}catch(TimeoutError e) {
			throw new Exception("Finish button is not enabled!");
		}
		
		try {
			assertThat(GeneralMethods.page.locator(tc1.completeOrderMessage))
	        .isVisible(new LocatorAssertions.IsVisibleOptions()
	                .setTimeout(5000));
		}catch(TimeoutError e) {
			throw new Exception("Message for successful order is not displayed!");
		}
	}
}
