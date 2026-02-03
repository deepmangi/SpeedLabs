package glueCode;

import com.microsoft.playwright.Page;

public class InventoryPage {

    private final Page page;

    public InventoryPage(Page page) {
        this.page = page;
    }

    public void open() {
        page.navigate("https://www.saucedemo.com/inventory.html");
    }

    public void addBackpackToCart() {
        page.locator("[data-test='add-to-cart-sauce-labs-backpack']").click();
    }

    public void goToCart() {
        page.locator("[data-test='shopping-cart-link']").click();
    }
}
