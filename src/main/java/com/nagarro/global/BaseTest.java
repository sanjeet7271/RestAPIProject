package com.nagarro.global;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.testng.Assert;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Global class for reading configuration file and host URL
 * @author sanjeetpandit
 *
 */
public class BaseTest {
	protected Properties prop = null;
	protected String hostURL;
	FileInputStream input = null;
	public static Logger logger = Logger.getLogger(BaseTest.class);
	JsonNode json;
	ObjectMapper mapper;

	/**
	 * 
	 * @throws IOException
	 */
	public BaseTest() throws IOException {
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
