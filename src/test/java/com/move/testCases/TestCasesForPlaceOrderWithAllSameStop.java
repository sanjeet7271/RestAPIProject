package com.move.testCases;

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
import com.move.constants.FrameworkConstants;
import com.move.global.BaseTest;
import com.move.restassured.RestAssuredClient;
import com.move.util.CommonUtility;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

/**
 * Test scenario with same stops
 * 
 * @author sanjeetpandit
 *
 */
public class TestCasesForPlaceOrderWithAllSameStop extends BaseTest {
	String placeOrderurl;
	ObjectMapper mapper;
	Response response;
	int responseCode;
	RestAssuredClient restAssuredClient;
	JsonNode presentJson;
	CommonUtility utility;
	public TestCasesForPlaceOrderWithAllSameStop() throws IOException {
		super();
	}

	/**
	 * Initialize URL
	 * @throws IOException 
	 */
	@BeforeMethod
	public void setUp() throws IOException {
		restAssuredClient = new RestAssuredClient();
		utility = new CommonUtility();
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
		utility.Verify_StatusCode(response);
		int id = response.jsonPath().get("id");
		logger.info("New Order Id :" + id);
		RestAssured.defaultParser = Parser.JSON;
		List<Integer> orderDistances = response.jsonPath().getList("drivingDistancesInMeters");
		logger.info("All distances :" + orderDistances);
		int distance = orderDistances.get(0);
		logger.info("1st distance :" + distance);
		Assert.assertEquals(distance, FrameworkConstants.ZERO_KM_DISTANCE);
		Map<String, String> orderFares = response.jsonPath().getMap("fare");
		String amount = orderFares.get("amount");
		logger.info("amount :" + amount);
		Assert.assertEquals(amount, FrameworkConstants.ZERO_KM_DISTANCE);
	}

}
