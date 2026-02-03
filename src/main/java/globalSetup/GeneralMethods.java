package globalSetup;

import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;

import io.github.cdimascio.dotenv.Dotenv;

public class GeneralMethods implements Base {

	public static Page page;
	public static Page newPage;
	public static Frame iframe;

	/**
	 * Navigating to URL
	 * 
	 * @param url : String : URL to the site
	 * @return Void
	 * @throws Exception
	 **/
//	public void open(String url) throws Exception {
//		try {
//			GeneralMethods.page = BrowserSetup.getDefaultPage();
//			page.navigate(url);
//		} catch (TimeoutError e) {
//			try {
//				GeneralMethods.page = BrowserSetup.getDefaultPage();
//				page.navigate(url);
//			} catch (TimeoutError e2) {
//				throw new Exception("Cannot navigate to url:- " + url);
//			}
//		}
//	}
	public void open(String url) throws Exception {
	    try {
	        // Check if page exists and is not closed
	        if (GeneralMethods.page == null || GeneralMethods.page.isClosed()) {
	            GeneralMethods.page = BrowserSetup.getDefaultPage("Desktop");
	        }
	        page.navigate(url);
	    } catch (TimeoutError e) {
	        try {
	            // Only get new page if current one is null or closed
	            if (GeneralMethods.page == null || GeneralMethods.page.isClosed()) {
	                GeneralMethods.page = BrowserSetup.getDefaultPage("Desktop");
	            }
	            page.navigate(url);
	        } catch (TimeoutError e2) {
	            throw new Exception("Cannot navigate to url:- " + url);
	        }
	    }
	}
	
	public void open(String url, String viewport) throws Exception {
	    try {
	        // Check if page exists and is not closed
	        if (GeneralMethods.page == null || GeneralMethods.page.isClosed()) {
	            GeneralMethods.page = BrowserSetup.getDefaultPage(viewport);
	        }
	        page.navigate(url);
	    } catch (TimeoutError e) {
	        try {
	            // Only get new page if current one is null or closed
	            if (GeneralMethods.page == null || GeneralMethods.page.isClosed()) {
	                GeneralMethods.page = BrowserSetup.getDefaultPage(viewport);
	            }
	            page.navigate(url);
	        } catch (TimeoutError e2) {
	            throw new Exception("Cannot navigate to url:- " + url);
	        }
	    }
	}
	
	
	/**
	 * Switch to new tab Use "newPage" instead of "Page" to interact with the
	 * elements of new page.
	 * 
	 * @param locator : String : Locator or button or element which opens new tab
	 * @return Void
	 **/
	public void switchTab(String locator) throws InterruptedException {
		newPage = page.context().waitForPage(() -> {
			page.click(locator);
		});
	}

	/**
	 * Comparing the title
	 * 
	 * @param ExpectedTitle : String : Expected title of the page
	 * @throws Exception (Title Mismatch)
	 **/
	public void verifyTitle(String ExpectedTitle) throws Exception {
		String pageTitle = page.title().toLowerCase();

		if (pageTitle == ExpectedTitle.toLowerCase()) {
			print("Title Match");
		} else {
			throw new Exception("Title Mismatch");
		}
	}

	/**
	 * Verifying the URL
	 * 
	 * @param ExpectedUrl : String : Expected title of the page
	 * @throws Exception (Title Mismatch)
	 **/
	public void verifyUrl(String ExpectedUrl) throws Exception {
		String pageurl = page.url().toLowerCase();

		if (pageurl == ExpectedUrl.toLowerCase()) {
			print("Title Match");
		} else {
			throw new Exception("URL Mismatch");
		}
	}

	/**
	 * Navigating to URL
	 * 
	 * @param locator : String : Element locator
	 * @return String
	 **/
	public String getText(String locator) {
		return page.locator(locator).inputValue();// .toLowerCase();
	}

	/**
	 * Get value of any attribute
	 * 
	 * @param locator       : String : Element locator
	 * @param attributeName : String : Name of the desired attribute
	 * @return String
	 **/
	public String getAtr(String locator, String attributeName) {
		return page.getAttribute(locator, attributeName);
	}

	/**
	 * Navigating to URL
	 * 
	 * @param string : String : String to be printed
	 * @return void
	 **/
	public void print(String string) {
		System.out.println(string);
	}

	// find method
	public Locator find(String path) { // removed static
		return page.locator(path);
	}

	/**
	 * Click Method
	 * 
	 * @param locator : String : Element Locator
	 * @return void
	 **/

//	public void click(String locator) {
//		find(locator).click();
//	}
	
	public void clickForcefully(String locator) {
		GeneralMethods.page.locator(locator).click(new Locator.ClickOptions().setForce(true));

	}

//	public void click(String locator, String frameID) {
//		iframe.locator(locator).click();
//	}

	public void keyPress(String keyname) {
		page.keyboard().press(keyname);
	}

	/**
	 * Waits for an element to be visible on the page.
	 *
	 * @param locator : String : locator or other supported selector string
	 * @param timeout : Integer : Timeout in seconds
	 * @return void
	 */
	public void waitFor(String locator, int timeout) {
		page.locator(locator).waitFor(
				new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeout * 1000));
	}

	/**
	 * Type Method
	 * 
	 * @param locator : String : Element Locator
	 * @param value   : String : String to input
	 * @return void
	 **/
	public void type(String locator, String value) {
		find(locator).clear();
		find(locator).scrollIntoViewIfNeeded(); // scroll to field
		// find(locator).type(string, new Locator.TypeOptions().setDelay(90)); //set
		// typing delay to mimic human behavior
		find(locator).fill(value);
		//type(value);
	}

	public void clearText(String locator) {
		find(locator).clear();
	}

	public void uploadfile(String locator, String filePath) {
		page.locator(locator).setInputFiles(Paths.get(filePath));
	}

	/**
	 * Thread.sleep
	 * 
	 * @param timeInMS : Integer : Waiting time in seconds
	 * @return void
	 **/
	public void wait(int timeInS) throws InterruptedException {
		int timeInMS = timeInS * 1000;
		Thread.sleep(timeInMS);
	}

	/**
	 * Selects option from dropdown with <select> tag
	 * 
	 * @param locator : String : locator of the dropdown field
	 * @param values  : String : value mentioned in <value> tag.
	 * @return void
	 **/
	public void select(String locator, String values) {
		page.selectOption(locator, values);
	}

	/**
	 * Waits until element is no longer visible, eg:- waits until page loader is
	 * gone
	 * 
	 * @param xpath             : String : Element path
	 * @param durationInSeconds : Integer : Waiting time in seconds
	 * @return void
	 **/
	public void waitForElementToBeInvisible(String xpath, int durationInSeconds) {
		Locator element = page.locator(xpath);
		element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN)
				.setTimeout(durationInSeconds * 1000));
	}

	// assert true
	public void istrue(String string, boolean bool) {
		Assert.assertTrue(string, bool);
	}

	/**
	 * String comparison
	 * 
	 * @param expected : String : Expected string
	 * @param actual   : String : Actual String
	 * @return void
	 **/
	public void isEqual(String expected, String actual) {
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase());
	}

	/**
	 * Check element presence
	 * 
	 * @param Locator : String : Element Locator
	 * @return Boolean
	 **/
	public boolean elementPresent(String Locator) {
		boolean present = page.isVisible(Locator);
		return present;
	}
	
	/**
	 * Check element Elebled
	 * 
	 * @param Locator : String : Element Locator
	 * @return Boolean
	 **/
	public boolean elementEnabled(String Locator) {
		boolean present = page.isEnabled(Locator);
		return present;
	}

	/**
	 * Check is a checkbox is checked
	 * 
	 * @param Locator : String : Element Locator
	 * @return Boolean
	 **/
	public boolean isChecked(String locator) {
		boolean state = find(locator).isChecked();
		return state;
	}

	/**
	 * Check is a checkbox is checked
	 * 
	 * @param message : String : Text to be logged in the report
	 * @return void
	 **/
	public void log(String message) {
		ExtentCucumberAdapter.addTestStepLog(message);
	}

	private Page originalPage;
	protected BrowserContext context;

	/**
	 * Switch to the latest (newest) window/tab
	 *
	 * @return Page
	 * @throws InterruptedException
	 **/
	public Page switchToNewWindow() throws InterruptedException {
		try {
			List<Page> allPages = context.pages();
			Page newPage = allPages.get(allPages.size() - 1);
			newPage.bringToFront();
			return newPage;
		} catch (NullPointerException e) {
			wait(20);
			List<Page> allPages = context.pages();
			Page newPage = allPages.get(allPages.size() - 1);
			newPage.bringToFront(); // Optional: to make it active
			return newPage;
		}
	}

	/**
	 * Switch back to the original (old) window/tab
	 *
	 * @return void
	 **/
	public void switchToOldWindow() {
		originalPage.bringToFront(); // Make original tab active again
	}

	/**
	 * Race condition for finding two elements.
	 * 
	 * @param String[] selectors : new String[] { Locator 1, Locator 2 }.
	 * @return String : whichever element is found first.
	 * @return null : if none elements appear within specified interval
	 **/
	public String waitForAnyVisible(String[] selectors, int timeoutSeconds) throws InterruptedException {
		long end = System.currentTimeMillis() + timeoutSeconds * 1000;

		while (System.currentTimeMillis() < end) {
			for (String selector : selectors) {
				if (page.locator(selector).isVisible()) {
					return selector; // Return the one that appeared first
				} else {
					Thread.sleep(1000);
				}
			}
		}
		return null; // None appeared within timeout
	}

	/**
	 * Generate random string of desired length with A-z,a-z,1-9 and special
	 * characters
	 * 
	 * @param Length : Integer : Number of characters of desired random string".
	 * @return String
	 */

	public String RandomString(int length) {
		// Define the characters to be used in the random string
		final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		// Use SecureRandom for more secure random number generation
		final SecureRandom RANDOM = new SecureRandom();

		StringBuilder randomchar = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int randomIndex = RANDOM.nextInt(CHARACTERS.length());
			randomchar.append(CHARACTERS.charAt(randomIndex));
		}

		// Generate a random string of X characters
		String randomString = randomchar.toString();
		return randomString;
	}

	/**
	 * Generate random numbers of desired length
	 * 
	 * @param digits : Integer : Number of digits of desired random number".
	 * @return Integer
	 */
	public int RandomNumber(int digits) {
		if (digits <= 0) {
			throw new IllegalArgumentException("Number of digits must be greater than 0");
		}

		int min = (int) Math.pow(10, digits - 1); // Minimum value for the given digits
		int max = (int) Math.pow(10, digits) - 1; // Maximum value for the given digits

		Random ran = new Random();
		int ran_int = ran.nextInt((max - min) + 1) + min; // Generate random number between min and max

		return ran_int;
	}

	public String dateFormatter(String oldFormatDate, String dateInputFormat, String dateOutputFormat)
			throws ParseException {
		SimpleDateFormat inputFormat = new SimpleDateFormat(dateInputFormat);
		SimpleDateFormat outputFormat = new SimpleDateFormat(dateOutputFormat);
		Date date = inputFormat.parse(oldFormatDate);
		String formattedDate = outputFormat.format(date);
		return formattedDate;
	}

	/**
	 * Generate random email with domain radixweb.com
	 * 
	 * @return String
	 */
	public String RandomEmail() {
		Random rand = new Random();
		int n = rand.nextInt(1000) + 1;
		String Remail = "Demo.email" + String.valueOf(n) + "@yopmail.com";
		return Remail;
	}

	public void hoverOn(String locator) {
		page.locator(locator).hover();
	}

	////////////////////////////////////// Iframe
	////////////////////////////////////// methods////////////////////////////////////////////

	/**
	 * Switches to the iframe using the provided locator.
	 * 
	 * @param locator : String : Locator of desired frame to switch
	 */
	public void switchToIframe(String locator) throws InterruptedException {
		int attempts = 0;
		while ((iframe == null || iframe.isDetached()) && attempts < 10) {
			ElementHandle iframeHandle = page.locator(locator).elementHandle();
			iframe = iframeHandle.contentFrame();
			Thread.sleep(1000);
			attempts++;
		}

		if (iframe == null || iframe.isDetached()) {
			throw new RuntimeException("Failed to switch to iframe after multiple attempts");
		}
	}

	/**
	 * Waits for element state to be visible inside a frame
	 * 
	 * @param frameLocator  : String : desires frame ID to switch.
	 * @param locator       : String : Element locator inside the frame.
	 * @param timeInSeconds : Integer : Timeout in seconds.
	 */
	public void waitForFrameElement(String frameLocator, String locator, int timeInSeconds) {
		page.frameLocator(frameLocator).locator(locator).waitFor(
				new Locator.WaitForOptions().setTimeout(timeInSeconds * 1000).setState(WaitForSelectorState.VISIBLE));
	}

	/**
	 * Type methods for element inside frame
	 * 
	 * @param frameLocator : String : desires frame ID to switch.
	 * @param locator      : String : Element locator inside the frame.
	 * @param value        : Integer : String to input.
	 */
	public void type(String frameLocator, String locator, String value) {
		page.frameLocator(frameLocator).locator(locator).clear();
		page.frameLocator(frameLocator).locator(locator).fill(value);
	}

//	/**
//	 * Click methods for element inside frame
//	 * 
//	 * @param frameLocator : String : desires frame ID to switch.
//	 * @param locator      : String : Element locator inside the frame.
//	 */
//	public void click(String frameLocator, String locator) {
//		page.frameLocator(frameLocator).locator(locator).click();
//	}
	
	public void click(String... locators) {
	    if (locators.length == 1) {
	        page.locator(locators[0]).click();
	    } else if (locators.length == 2) {
	        page.frameLocator(locators[0]).locator(locators[1]).click();
	    } else {
	        throw new IllegalArgumentException("click() accepts 1 or 2 locators only");
	    }
	}

	/**
	 * Select methods for element inside frame
	 * 
	 * @param frameLocator : String : desires frame ID to switch.
	 * @param locator      : String : Element locator inside the frame with
	 *                     selecttag.
	 * @param value        : String : Value of desired option.
	 */
	public void select(String frameLocator, String locator, String value) {
		page.frameLocator(frameLocator).locator(locator).selectOption(value);
	}

	public void uploadfile(String frameLocator, String locator, String filePath) {
		page.frameLocator(frameLocator).locator(locator).setInputFiles(Paths.get(filePath));
	}

	public void pwInspector() {
		page.pause();
	}

	public static String getEnvData(String key) {
		final Dotenv dotenv = Dotenv.load();
		return dotenv.get(key);
	}
}
