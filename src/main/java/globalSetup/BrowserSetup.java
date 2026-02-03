package globalSetup;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BrowserSetup {

	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static double viewportWidth;
	static double viewportHeight;

	public static Page page;
	protected static Playwright pw;
	protected static Browser browser;
	protected static BrowserContext browsercontext;


	@Before
	public static Page getDefaultPage(String viewPort) {

		switch (viewPort) {
		case "Desktop":
			viewportWidth = screenSize.getWidth();
			viewportHeight = screenSize.getHeight();
			break;
		case "Mobile":
			viewportWidth = 390;
			viewportHeight = 844;
			break;
		}
		
		pw = Playwright.create();

		browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome"));

		browsercontext = browser.newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get("login.json"))
				.setViewportSize((int) viewportWidth, (int) viewportHeight)
//	                .setViewportSize(390,844)
				.setAcceptDownloads(true));

		browsercontext.addInitScript(
				"localStorage.setItem('logged-in', 'true');" + "localStorage.setItem('user-name', 'standard_user');");
		return page = browsercontext.newPage();
	}

	@After
	public void teardown() throws IOException {
		if (browsercontext != null)
			browsercontext.close();
		if (pw != null)
			pw.close();
	}
}