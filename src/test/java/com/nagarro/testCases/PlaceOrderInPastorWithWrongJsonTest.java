package com.nagarro.testCases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import com.nagarro.constants.FrameworkConstants;
import com.nagarro.constants.StatusCodes;
import com.nagarro.global.BaseTest;
import com.nagarro.restassured.RestAssuredClient;
/**
 * 
 * @author sanjeetpandit
 *
 */
public class PlaceOrderInPastorWithWrongJsonTest extends BaseTest {

	String placeOrderurl;
	ObjectMapper mapper;
	Response response;
	int responseCode;
	RestAssuredClient restAssuredClient;
	JsonNode presentJson, futureJson, pastJson;
	static int id;

	public PlaceOrderInPastorWithWrongJsonTest() throws IOException {
		super();
	}

	@BeforeMethod
	public void setUp() {
		restAssuredClient = new RestAssuredClient();
		mapper = new ObjectMapper();
		placeOrderurl = this.hostURL;

	}

	/**
	 * Test
	 * 
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@Test(priority=1)
	public void placeNewOrderInPast() throws JsonProcessingException, IOException {
		try {
			pastJson = mapper.readTree(new File(
					System.getProperty("user.dir") + "/src/main/resources/JsonData/PlaceOrderInPast.json"));
		} catch (FileNotFoundException e) {
			logger.error("Exception " + e);
			logger.error("Properties file not found.");
			Assert.fail("Properties file not found.");
		}
		response = restAssuredClient.requestPostCall(placeOrderurl, pastJson.toString());
		responseCode = response.getStatusCode();
		String errorMessage = response.jsonPath().get("message");
		logger.error("New Order Id :" + errorMessage);
		Assert.assertEquals(errorMessage, FrameworkConstants.PAST_ERROR_MESSAGE);
		
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_400);
	}
	@Test(priority=2)
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
		responseCode = response.getStatusCode();
		String errorMessage = response.jsonPath().get("message");
		logger.error("New Order Id :" + errorMessage);
		Assert.assertEquals(errorMessage, FrameworkConstants.WRONG_JSON_ERROR_MESSAGE);
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_400);
	}
}

