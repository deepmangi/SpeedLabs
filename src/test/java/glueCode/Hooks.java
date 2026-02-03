package glueCode;

import java.io.IOException;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.microsoft.playwright.Page;

import globalSetup.BrowserSetup;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

public class Hooks {
	@After
	public void captureFailureScreenshot(Scenario scenario) throws IOException {
		if (scenario.isFailed()) {
			Page page = BrowserSetup.page;
			byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
			String fileName = scenario.getName().replaceAll(" ", "_");
			
			try {
				ExtentCucumberAdapter.addTestStepLog("Failure Screenshot");
				scenario.attach(screenshot, "image/png", fileName);
			} catch (NullPointerException e) {
				System.err.println("Run with testRunner.java to generate reports.");   
			}
		}
	}
}