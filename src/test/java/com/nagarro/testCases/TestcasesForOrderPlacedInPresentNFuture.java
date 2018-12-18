package com.nagarro.testCases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.http.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;
import com.nagarro.constants.FrameworkConstants;
import com.nagarro.constants.StatusCodes;
import com.nagarro.global.BaseTest;
import com.nagarro.restassured.RestAssuredClient;
import com.nagarro.util.FareCalculationService;

/** 
 * Test cases to place an Order, Fetch the Order, take the Order and Complete or
 * cancel the Order
 * 
 * @author sanjeetpandit
 *
 */
public class TestcasesForOrderPlacedInPresentNFuture extends BaseTest {
	String placeOrderurl;
	ObjectMapper mapper;
	Response response;
	int responseCode;
	RestAssuredClient restAssuredClient;
	JsonNode json;
	int id;
	String canceltheOrder;
	String fetchOrderurl;
	String takeTheOrder;
	String completetheOrder;
	FareCalculationService travelCostCalculation;
	double calculateamount;

	public TestcasesForOrderPlacedInPresentNFuture() throws IOException {
		super();
	}

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		restAssuredClient = new RestAssuredClient();
		travelCostCalculation = new FareCalculationService();
		mapper = new ObjectMapper();
		placeOrderurl = this.hostURL;

	}

	/**
	 * 1.) Test Case:ASSIGNING-> Place New Order in present
	 * 
	 * @Test cases for Success status code, OrderID, distance and fare
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 1, groups = { "Cancel the new Order" })
	public void placeNewOrderInPresent() throws ParseException, IOException {
		String jsonReader = prop.getProperty("JsonFileReaderfor3stops");
		if (jsonReader.equals("present")) {
			try {
				json = mapper.readTree(new File(
						System.getProperty("user.dir") + "/src/main/resources/JsonData/PlaceOrderInPresent.json"));
			} catch (FileNotFoundException e) {
				logger.error("Exception " + e);
				logger.error("Properties file not found.");
				Assert.fail("Properties file not found.");
			}
		} else if (jsonReader.equals("future")) {
			try {
				json = mapper.readTree(new File(
						System.getProperty("user.dir") + "/src/main/resources/JsonData/PlaceOrderInFuture.json"));
			} catch (FileNotFoundException e) {
				logger.error("Exception " + e);
				logger.error("Properties file not found.");
				Assert.fail("Properties file not found.");
			}
		}
		response = restAssuredClient.requestPostCall(placeOrderurl, json.toString());
		responseCode = response.getStatusCode();
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_201);
		id = response.jsonPath().get("id");
		if (id <= 0) {
			Assert.assertFalse(true);
		}
		logger.info("New Order Id :" + id);
		RestAssured.defaultParser = Parser.JSON;
		List<Integer> orderDistances = response.jsonPath().getList("drivingDistancesInMeters");
		logger.info("All distances during placing Order :" + orderDistances);
		int sumOfDistance = 0;
		for (Integer distance : orderDistances) {
			sumOfDistance = sumOfDistance + distance;
		}
		logger.info("Total Distance calcution from all stops :" + sumOfDistance);
		Map<String, String> orderFares = response.jsonPath().getMap("fare");
		String amount = orderFares.get("amount");
		logger.info("Total amount during order placed :" + amount);
		String currency = orderFares.get("currency");
		logger.info("Currency during order placed :" + currency);
		Assert.assertEquals(currency, FrameworkConstants.CURRENCY);
	}

	/**
	 * 1.) Test Case:ASSIGNING->CANCELLED Cancel the placed new order
	 * 
	 * @Test cases for Success status code, OrderID, distance and fare
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 2, groups = { "Cancel the new Order" }, dependsOnMethods = "placeNewOrderInPresent")
	public void cancelOrder() {
		canceltheOrder = this.hostURL + "/" + id + "/cancel";
		response = restAssuredClient.requestPutCall(canceltheOrder, "");
		responseCode = response.getStatusCode();
		logger.info("Status Code--->" + responseCode);
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_200, "Status code is not 200");
		id = response.jsonPath().get("id");
		logger.info("New Order Id :" + id);
		RestAssured.defaultParser = Parser.JSON;
		String orderStatus = response.jsonPath().get("status");
		Assert.assertEquals(orderStatus, FrameworkConstants.CANCELLED);

	}

	/**
	 * Common utility of mapping currency and amount
	 */
	public void mapCommonUtility() {
		logger.info("Calculted Cost :" + calculateamount);
		Map<String, String> orderFares = response.jsonPath().getMap("fare");
		String amount = orderFares.get("amount");
		String currency = orderFares.get("currency");
		Assert.assertEquals(Double.valueOf(amount), calculateamount);
		Assert.assertEquals(currency, FrameworkConstants.CURRENCY);
	}

	/**
	 * 2.) Test Case:ASSIGNING-> Assign the Newly Created order after placed the
	 * order
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 3, groups = { "complete the new Order" })
	public void fetchTheOrderDetails() throws ParseException, IOException {
		placeNewOrderInPresent();
		fetchOrderurl = this.hostURL + "/" + id;
		response = restAssuredClient.requestGetCall(fetchOrderurl);
		responseCode = response.getStatusCode();
		id = response.jsonPath().get("id");
		logger.info("New Order Id :" + id);
		RestAssured.defaultParser = Parser.JSON;
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_200, "Status code is not 200");
		String orderStatus = response.jsonPath().get("status");
		Assert.assertEquals(orderStatus, FrameworkConstants.ASSIGNING);
		List<Integer> orderDistances = response.jsonPath().getList("drivingDistancesInMeters");
		logger.info("All distances :" + orderDistances);
		int sumOfDistance = 0;
		for (Integer distance : orderDistances) {
			sumOfDistance = sumOfDistance + distance;
		}
		logger.info("Calculted Distances :" + sumOfDistance);
		String orderDateTime = response.jsonPath().get("orderDateTime");
		logger.info("Calculted Distances :" + orderDateTime);
		String createdTime = response.jsonPath().get("createdTime");
		logger.info("Calculted Distances :" + createdTime);
		String timeStamp = orderDateTime.substring(11, 13);
		double time = Double.parseDouble(timeStamp);
		logger.info("time captured" + time);
		if (createdTime.equals(orderDateTime)) {
			if ((time >= FrameworkConstants.MORE_THAN_9PM && time <= FrameworkConstants.LESS_THAN_11_59PM)
					|| (time >= FrameworkConstants.LESS_THAN_0AM && time <= FrameworkConstants.LESS_THAN_5AM)) {
				calculateamount = travelCostCalculation.travelCostInBetween9to5TimeStamp(sumOfDistance);
				//mapCommonUtility();
			} else {
				calculateamount = travelCostCalculation.travelCostInNormalTimeStamp(sumOfDistance);
				//mapCommonUtility();
			}
		} else if ((time >= FrameworkConstants.MORE_THAN_9PM && time <= FrameworkConstants.LESS_THAN_11_59PM)
				|| (time >= FrameworkConstants.LESS_THAN_0AM && time <= FrameworkConstants.LESS_THAN_5AM)) {
			calculateamount = travelCostCalculation.travelCostInBetween9to5TimeStamp(sumOfDistance);
			//mapCommonUtility();

		} else {
			calculateamount = travelCostCalculation.travelCostInNormalTimeStamp(sumOfDistance);
			//mapCommonUtility();
		}
		mapCommonUtility();
		
	}

	/**
	 * 2.) Test Case:ASSIGNING->ONGOINING-> Take the Newly Created order after
	 * assigned
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 4, groups = { "complete the new Order" })
	public void takeTheOrderDetails() {
		takeTheOrder = this.hostURL + "/" + id + "/take";
		response = restAssuredClient.requestPutCall(takeTheOrder, "");
		responseCode = response.getStatusCode();
		logger.info("Status Code--->" + responseCode);
		id = response.jsonPath().get("id");
		logger.info("New Order Id :" + id);
		RestAssured.defaultParser = Parser.JSON;
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_200, "Status code is not 200");
		String orderStatus = response.jsonPath().get("status");
		Assert.assertEquals(orderStatus, FrameworkConstants.ONGOING);

	}

	/**
	 * 2.) Test Case:ASSIGNING->ONGOINING->COMPLETE Complete the Newly Created order
	 * after Ongoing
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 5, groups = { "complete the new Order" })
	public void CompleteTheOrderDetails() {
		completetheOrder = this.hostURL + "/" + id + "/complete";
		response = restAssuredClient.requestPutCall(completetheOrder, "");
		responseCode = response.getStatusCode();
		logger.info("Status Code--->" + responseCode);
		id = response.jsonPath().get("id");
		logger.info("New Order Id :" + id);
		RestAssured.defaultParser = Parser.JSON;
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_200, "Status code is not 200");
		String orderStatus = response.jsonPath().get("status");
		Assert.assertEquals(orderStatus, FrameworkConstants.COMPLETED);

	}
	/**
	 * 2.) Test Case:ASSIGNING->ONGOINING->COMPLETE->ONGOINING take the completed the Newly Created order
	 * 
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority=6)
	public void takeTheCompletedOrder() throws ParseException, IOException {
		fetchTheOrderDetails();
		takeTheOrderDetails();
		CompleteTheOrderDetails();
		takeTheOrder = this.hostURL + "/" + id + "/take";
		response = restAssuredClient.requestPutCall(takeTheOrder, "");
		responseCode = response.getStatusCode();
		logger.info("Status Code--->" + responseCode);
		String errorMessage = response.jsonPath().get("message");
		logger.error("New Order Id :" + errorMessage);
		Assert.assertEquals(errorMessage, FrameworkConstants.ORDER_NOT_IN_ASSIGNING_STATE);
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_422, "Status code is not 422");
	}
	
	/**
	 * 2.) Test Case:ASSIGNING->ONGOINING->cancel->ONGOINING take the cancelled the Newly Created order
	 * 
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority=7)
	public void takeTheCancelledOrder() throws ParseException, IOException {
		fetchTheOrderDetails();
		takeTheOrderDetails();
		cancelOrder();
		takeTheOrder = this.hostURL + "/" + id + "/take";
		response = restAssuredClient.requestPutCall(takeTheOrder, "");
		responseCode = response.getStatusCode();
		logger.info("Status Code--->" + responseCode);
		String errorMessage = response.jsonPath().get("message");
		logger.error("New Order Id :" + errorMessage);
		Assert.assertEquals(errorMessage, FrameworkConstants.ORDER_NOT_IN_ASSIGNING_STATE);
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_422, "Status code is not 422");
	}
	
	/**
	 * Common utility for response code customize code 422
	 */
	public void CommonUtilityForResponseCode422() {
		responseCode = response.getStatusCode();
		logger.info("Status Code--->" + responseCode);
		RestAssured.defaultParser = Parser.JSON;
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_422, "Status code is not 422");
	}

	/**
	 * 3.) Test Case:ASSIGNING->COMPLETE Complete the Newly Created order
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 8, groups = { "complete the Order just after creation" })
	public void CompleteTheNewOrder() throws ParseException, IOException {
		fetchTheOrderDetails();
		completetheOrder = this.hostURL + "/" + id + "/complete";
		response = restAssuredClient.requestPutCall(completetheOrder, "");
		CommonUtilityForResponseCode422();

	}

	/**
	 * 3.) Test Case:ASSIGNING->ONGOINING->COMPLETE->COMPLETE Complete the Newly
	 * Created order and Complete again
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 9, groups = { "Complete the order after Completed" })
	public void CompleteTheOrderAfterCompleted() throws ParseException, IOException {
		fetchTheOrderDetails();
		takeTheOrderDetails();
		CompleteTheOrderDetails();
		completetheOrder = this.hostURL + "/" + id + "/complete";
		response = restAssuredClient.requestPutCall(completetheOrder, "");
		CommonUtilityForResponseCode422();
	}

	/**
	 * 4.) Test Case:ASSIGNING->ONGOINING->COMPLETE->CANCELLED Cancel the Newly
	 * Created order and cancel again
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 10, groups = { "Cancel the Order after Completed" })
	public void cancelTheOrderAfterCompleted() throws ParseException, IOException {
		fetchTheOrderDetails();
		takeTheOrderDetails();
		CompleteTheOrderDetails();
		canceltheOrder = this.hostURL + "/" + id + "/cancel";
		response = restAssuredClient.requestPutCall(canceltheOrder, "");
		CommonUtilityForResponseCode422();
	}

	/**
	 * 5.) Test Case:ASSIGNING->ONGOINING->CANCELLED->CANCELLED Cancel the Newly
	 * Created order and Cancel again This Test Case failing because response should
	 * get 422 but it giving 200.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 11, groups = { "Cancel the order after cancelled" })
	public void cancelTheOrderAfterCancel() throws ParseException, IOException {
		fetchTheOrderDetails();
		takeTheOrderDetails();
		cancelOrder();
		canceltheOrder = this.hostURL + "/" + id + "/cancel";
		response = restAssuredClient.requestPutCall(canceltheOrder, "");
		CommonUtilityForResponseCode422();
	}

	/**
	 * 6.) Test Case:ASSIGNING->ONGOINING->CANCELLED->COMPLETE Cancel the Newly
	 * Created order and complete again
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 12, groups = { "Complete the Order after Cancelled" })
	public void CompleteTheOrderAfterCancel() throws ParseException, IOException {
		fetchTheOrderDetails();
		takeTheOrderDetails();
		cancelOrder();
		completetheOrder = this.hostURL + "/" + id + "/complete";
		response = restAssuredClient.requestPutCall(completetheOrder, "");
		CommonUtilityForResponseCode422();
	}

	/**
	 * Test with wrong Order Id
	 */

	@Test(priority = 13, groups = { "place order with wrong body" })
	public void fetchTheOrderDetailsNotCreated() {
		String fetchTheOrderWithWrongId = placeOrderurl + "/" + 1224;
		response = restAssuredClient.requestGetCall(fetchTheOrderWithWrongId);
		responseCode = response.getStatusCode();
		RestAssured.defaultParser = Parser.JSON;
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_404, "Status code is not 404");
	}

}
