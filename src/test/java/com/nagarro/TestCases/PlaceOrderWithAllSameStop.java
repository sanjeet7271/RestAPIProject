package com.nagarro.TestCases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;
import com.nagarro.Global.GlobalReader;
import com.nagarro.RestAssured.RestAssuredClient;
import com.nagarro.constants.FrameworkConstants;
import com.nagarro.constants.StatusCode;

/**
 * 
 * @author sanjeetpandit
 *
 */
public class PlaceOrderWithAllSameStop extends GlobalReader {
	String placeOrderurl;
	ObjectMapper mapper;
	Response response;
	int responseCode;
	RestAssuredClient restAssuredClient;
	JsonNode presentJson;

	public PlaceOrderWithAllSameStop() throws IOException {
		super();
	}

	/**
	 * Initialize URL
	 */
	@BeforeMethod
	public void setUp() {
		restAssuredClient = new RestAssuredClient();
		mapper = new ObjectMapper();
		placeOrderurl = this.hostURL;

	}

	/**
	 * Test for given all points are same
	 * 
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@Test
	public void placeNewOrderWithAllSameStop() throws JsonProcessingException, IOException {
		try {
			presentJson = mapper.readTree(new File(
					System.getProperty("user.dir") + "/src/main/resources/JsonData/PlaceOrderWithAllSameStop.json"));
		} catch (FileNotFoundException e) {
			logger.error("Exception " + e);
			logger.error("Properties file not found.");
			Assert.fail("Properties file not found.");
		}
		response = restAssuredClient.requestPostCall(placeOrderurl, presentJson.toString());
		responseCode = response.getStatusCode();
		Assert.assertEquals(responseCode, StatusCode.RESPONSE_STATUS_CODE_201);
		int id = response.jsonPath().get("id");
		logger.info("New Order Id :" + id);
		RestAssured.defaultParser = Parser.JSON;
		List<Integer> orderDistances = response.jsonPath().getList("drivingDistancesInMeters");
		logger.info("All distances :" + orderDistances);
		int distance = orderDistances.get(0);
		logger.info("1st distance :" + distance);
		Assert.assertEquals(distance, FrameworkConstants.ZEROKMDISTANCE);
		Map<String, String> orderFares = response.jsonPath().getMap("fare");
		String amount = orderFares.get("amount");
		logger.info("amount :" + amount);
		Assert.assertEquals(amount, FrameworkConstants.ZEROKMCOST);
	}

}
