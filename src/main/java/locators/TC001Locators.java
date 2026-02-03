package locators;

public class TC001Locators {
	
//	inventory page locators
	public String sauceLabsBackpack = "//a[@data-test='item-4-title-link']";
	public String addBackpackToCartBtn = "//button[@data-test='add-to-cart-sauce-labs-backpack']";
	public String removeItemBtn = "//button[@data-test='remove-sauce-labs-backpack']";
	public String cartIcon = "//a[@data-test='shopping-cart-link']";
	public String cartContainer = "//div[@id='cart_contents_container']";
	
//	cart locators
	public String checkoutBtn = "//button[@data-test='checkout']";
	
//	checkout Section locators
	public String checkoutInfoSection = "//div[@data-test='checkout-info-container']";
	public String fName = "//input[@data-test='firstName']";
	public String lName = "//input[@data-test='lastName']";
	public String zipcode = "//input[@data-test='postalCode']";
	public String continueBtn = "//input[@data-test='continue']";
	public String checkoutSummary = "//div[@data-test='checkout-summary-container']";
	public String finishBtn = "//button[@data-test='finish']";
	
	public String completeOrderMessage = "//h2[@data-test='complete-header']";
}
