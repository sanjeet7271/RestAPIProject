package com.move.testCases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.move.global.BaseTest;
import com.move.restassured.RestAssuredClient;
import com.move.util.CommonUtility;

import io.restassured.response.Response;

/**
 * Invalid scenario with passt data and wrong body
 * 
 * @author sanjeetpandit
 *
 */
public class TestCasesForPlaceOrderWithInvalideScenaio extends BaseTest {

	String placeOrderurl;
	ObjectMapper mapper;
	Response response;
	int responseCode;
	RestAssuredClient restAssuredClient;
	JsonNode presentJson, futureJson, pastJson;
	static int id;
	CommonUtility utility;
	public TestCasesForPlaceOrderWithInvalideScenaio() throws IOException {
		super();
	}

	@BeforeMethod
	public void setUp() throws IOException {
		restAssuredClient = new RestAssuredClient();
		utility = new CommonUtility();
		mapper = new ObjectMapper();
		placeOrderurl = this.hostURL;

	}

	/**
	 * Test
	 * 
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@Test(priority = 1)
	public void placeNewOrderInPast() throws JsonProcessingException, IOException {
		try {
			pastJson = mapper.readTree(
					new File(System.getProperty("user.dir") + "/src/main/resources/JsonData/PlaceOrderInPast.json"));
		} catch (FileNotFoundException e) {
			logger.error("Exception " + e);
			logger.error("Properties file not found.");
			Assert.fail("Properties file not found.");
		}
		response = restAssuredClient.requestPostCall(placeOrderurl, pastJson.toString());
		utility.Verify_StatusCode(response);
	}

	@Test(priority = 2)
	public void placeNewOrderWithWronJson() throws JsonProcessingException, IOException {
		try {
			pastJson = mapper.readTree(new File(
					System.getProperty("user.dir") + "/src/main/resources/JsonData/PlaceOrderWithWrongJson.json"));
		} catch (FileNotFoundException e) {
			logger.error("Exception " + e);
			logger.error("Properties file not found.");
			Assert.fail("Properties file not found.");
		}
		response = restAssuredClient.requestPostCall(placeOrderurl, pastJson.toString());
		utility.Verify_StatusCode(response);
	}
}
