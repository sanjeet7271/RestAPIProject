package com.nagarro.Global;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;

/**
 * 
 * @author sanjeetpandit
 *
 */
public class GlobalReader {
	public Properties prop = null;
	public String hostURL;
	FileInputStream input = null;
	public static Logger logger = Logger.getLogger(GlobalReader.class);

	/**
	 * 
	 * @throws IOException
	 */
	public GlobalReader() throws IOException {
		try {
			FileInputStream input = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/resources/config/config.properties");
			prop = new Properties();
			prop.load(input);
			hostURL = prop.getProperty("URL");
			input.close();
		} catch (FileNotFoundException e) {
			logger.error("Exception " + e);
			logger.error("Properties file not found.");
			Assert.fail("Properties file not found.");
		}
	}
}
