package globalSetup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GetProperties extends GeneralMethods{
	
	private Properties prop;

	/**
	 * This method is used to load the properties from config.properties file
	 * @return it returns Properties prop object
	 */
	public Properties init_prop() {

		prop = new Properties();
		try {
			String projectPath = System.getProperty("user.dir");
			String propFilePath = projectPath + "/src/test/resources/Properties/data.properties";
			FileInputStream ip = new FileInputStream(propFilePath);
			prop.load(ip);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public String getProp(String string) throws IOException {
		String projectPath = System.getProperty("user.dir");
		String propFilePath = projectPath + "/src/test/resources/Properties/data.properties";
		
		FileReader datareader=new FileReader(propFilePath);
		Properties props=new Properties();
		props.load(datareader);
		return props.getProperty(string);
	}
}