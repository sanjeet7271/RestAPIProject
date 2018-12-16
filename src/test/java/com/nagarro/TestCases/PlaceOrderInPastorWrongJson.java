package com.nagarro.TestCases;

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
import com.nagarro.Global.GlobalReader;
import com.nagarro.RestAssured.RestAssuredClient;
import com.nagarro.constants.StatusCode;
/**
 * 
 * @author sanjeetpandit
 *
 */
public class PlaceOrderInPastorWrongJson extends GlobalReader {

	String placeOrderurl;
	ObjectMapper mapper;
	Response response;
	int responseCode;
	RestAssuredClient restAssuredClient;
	JsonNode presentJson, futureJson, pastJson;
	static int id;

	public PlaceOrderInPastorWrongJson() throws IOException {
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
	@Test
	public void placeNewOrderInPastorWrongJson() throws JsonProcessingException, IOException {
		try {
			pastJson = mapper.readTree(new File(
					System.getProperty("user.dir") + "/src/main/resources/JsonData/PlaceOrderInPastorWrongJson.json"));
		} catch (FileNotFoundException e) {
			logger.error("Exception " + e);
			logger.error("Properties file not found.");
			Assert.fail("Properties file not found.");
		}
		response = restAssuredClient.requestPostCall(placeOrderurl, pastJson.toString());
		responseCode = response.getStatusCode();
		String errorMessage = response.getBody().asString();
		logger.error("New Order Id :" + errorMessage);
		Assert.assertEquals(responseCode, StatusCode.RESPONSE_STATUS_CODE_400);
	}

}
