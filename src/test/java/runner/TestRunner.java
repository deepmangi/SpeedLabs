
package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {
		           "pretty",
		           "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
		           "listeners.TestListener" 
		         },
		features = "src/test/resources/TestCases",
		glue = {"glueCode"}
		)

public class TestRunner {

}

