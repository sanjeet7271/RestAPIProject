package com.nagarro.testCases;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.http.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;
import com.nagarro.constants.FrameworkConstants;
import com.nagarro.constants.StatusCodes;
import com.nagarro.global.BaseTest;
import com.nagarro.restassured.RestAssuredClient;
import com.nagarro.util.CommonUtility;

/**
 * Test cases to place an Order, Fetch the Order, take the Order and Complete or
 * cancel the Order
 * 
 * @author sanjeetpandit
 *
 */
public class TestcasesForOrderPlacedInPresentNFuture extends BaseTest {
	String placeOrderurl;

	Response response;
	int responseCode;
	RestAssuredClient restAssuredClient;
	JsonNode json;
	int id;
	String canceltheOrder;
	String fetchOrderurl;
	String takeTheOrder;
	String completetheOrder;
	CommonUtility utility;
	double calculateamount;

	public TestcasesForOrderPlacedInPresentNFuture() throws IOException {
		super();
	}

	@BeforeMethod(alwaysRun = true)
	public void setUp() throws IOException {
		restAssuredClient = new RestAssuredClient();
		utility = new CommonUtility();

		placeOrderurl = this.hostURL;

	}

	/**
	 * 1.) Test Case:ASSIGNING-> Place New Order in present
	 * 
	 * @Test cases for Success status code, OrderID, distance and fare
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 1, groups = { "Place the new Order" })
	public void testcase_Verify_PlaceNewOrder() throws ParseException, IOException {
		json = utility.readJson();
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
	@Test(priority = 2, groups = { "Cancel the new Order" })
	public void testcases_Verify__CancelOrder() throws ParseException, IOException {
		testcase_Verify_PlaceNewOrder();
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
	 * 2.) Test Case:ASSIGNING-> Assign the Newly Created order after placed the
	 * order
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 3, groups = { "complete the new Order" })
	public void testcases_Verify__FetchTheOrderDetails() throws ParseException, IOException {
		testcase_Verify_PlaceNewOrder();
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
		utility.verify_CalculateFare(sumOfDistance, response);

	}

	/**
	 * 2.) Test Case:ASSIGNING->ONGOINING-> Take the Newly Created order after
	 * assigned
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 4, groups = { "complete the new Order" })
	public void testcases_Verify__TakeTheOrderDetails() {
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
	public void testcases_Verify__CompleteTheOrderDetails() {
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
	 * 2.) Test Case:ASSIGNING->ONGOINING->COMPLETE->ONGOINING take the completed
	 * the Newly Created order
	 * 
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 6, groups = { "take the Order after Ongoing State" })
	public void testcases_Verify__TakeTheOngoingOrder() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		testcases_Verify__TakeTheOrderDetails();
		takeTheOrder = this.hostURL + "/" + id + "/take";
		response = restAssuredClient.requestPutCall(takeTheOrder, "");
		utility.Verify_CustomErrorMessage(response);
	}

	/**
	 * 2.) Test Case:ASSIGNING->ONGOINING->COMPLETE->ONGOINING take the completed
	 * the Newly Created order
	 * 
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 7, groups = { "Take the Order just after completed" })
	public void testcases_Verify__TakeTheCompletedOrder() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		testcases_Verify__TakeTheOrderDetails();
		testcases_Verify__CompleteTheOrderDetails();
		takeTheOrder = this.hostURL + "/" + id + "/take";
		response = restAssuredClient.requestPutCall(takeTheOrder, "");
		utility.Verify_CustomErrorMessage(response);
	}

	/**
	 * 2.) Test Case:ASSIGNING->ONGOINING->cancel->ONGOINING take the cancelled the
	 * Newly Created order
	 * 
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 8, groups = { "Take the Order after Cancelled" })
	public void testcases_Verify__TakeTheCancelledOrder() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		testcases_Verify__TakeTheOrderDetails();
		testcases_Verify__CancelOrder();
		takeTheOrder = this.hostURL + "/" + id + "/take";
		response = restAssuredClient.requestPutCall(takeTheOrder, "");
		utility.Verify_CustomErrorMessage(response);
	}

	/**
	 * Common utility for response code customize code 422
	 */

	/**
	 * 3.) Test Case:ASSIGNING->COMPLETE Complete the Newly Created order
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 9, groups = { "complete Order from Assign State" })
	public void testcases_Verify__CompleteTheNewOrder() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		completetheOrder = this.hostURL + "/" + id + "/complete";
		response = restAssuredClient.requestPutCall(completetheOrder, "");
		utility.Verify_ResponseCode422(response);

	}

	/**
	 * 3.) Test Case:ASSIGNING->ONGOINING->COMPLETE->COMPLETE Complete the Newly
	 * Created order and Complete again
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 10, groups = { "Complete the order after Completed" })
	public void testcases_Verify__CompleteTheOrderAfterCompleted() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		testcases_Verify__TakeTheOrderDetails();
		testcases_Verify__CompleteTheOrderDetails();
		completetheOrder = this.hostURL + "/" + id + "/complete";
		response = restAssuredClient.requestPutCall(completetheOrder, "");
		utility.Verify_ResponseCode422(response);
	}

	/**
	 * 4.) Test Case:ASSIGNING->ONGOINING->COMPLETE->CANCELLED Cancel the Newly
	 * Created order and cancel again
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 11, groups = { "Cancel the Order after Completed" })
	public void testcases_Verify__CancelTheOrderAfterCompleted() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		testcases_Verify__TakeTheOrderDetails();
		testcases_Verify__CompleteTheOrderDetails();
		canceltheOrder = this.hostURL + "/" + id + "/cancel";
		response = restAssuredClient.requestPutCall(canceltheOrder, "");
		utility.Verify_ResponseCode422(response);
	}

	/**
	 * 4.) Test Case:ASSIGNING->ONGOINING->COMPLETE->ONGOINING Cancel the Newly
	 * Created order and cancel again
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 12, groups = { "Take The Order after Completed" })
	public void testcases_Verify__TakeTheOrderAfterCompleted() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		testcases_Verify__TakeTheOrderDetails();
		testcases_Verify__CompleteTheOrderDetails();
		takeTheOrder = this.hostURL + "/" + id + "/take";
		response = restAssuredClient.requestPutCall(takeTheOrder, "");
		utility.Verify_ResponseCode422(response);
	}

	/**
	 * 5.) Test Case:ASSIGNING->ONGOINING->CANCELLED->CANCELLED Cancel the Newly
	 * Created order and Cancel again This Test Case failing because response should
	 * get 422 but it giving 200.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 13, groups = { "Cancel the order after cancelled" })
	public void testcases_Verify__CancelTheOrderAfterCancel() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		testcases_Verify__TakeTheOrderDetails();
		testcases_Verify__CancelOrder();
		canceltheOrder = this.hostURL + "/" + id + "/cancel";
		response = restAssuredClient.requestPutCall(canceltheOrder, "");
		utility.Verify_ResponseCode422(response);
	}

	/**
	 * 6.) Test Case:ASSIGNING->ONGOINING->CANCELLED->COMPLETE Cancel the Newly
	 * Created order and complete again
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 14, groups = { "Complete the Order after Cancelled" })
	public void testcases_Verify__CompleteTheOrderAfterCancel() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		testcases_Verify__TakeTheOrderDetails();
		testcases_Verify__CancelOrder();
		completetheOrder = this.hostURL + "/" + id + "/complete";
		response = restAssuredClient.requestPutCall(completetheOrder, "");
		utility.Verify_ResponseCode422(response);
	}

	/**
	 * 4.) Test Case:ASSIGNING->ONGOINING->CANCELLED->ONGOINING Cancel the Newly
	 * Created order and cancel again
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test(priority = 15, groups = { "Take The Order after Cancelled" })
	public void testcases_Verify__TakeTheOrderAfterCancelled() throws ParseException, IOException {
		testcases_Verify__FetchTheOrderDetails();
		testcases_Verify__TakeTheOrderDetails();
		testcases_Verify__CancelOrder();
		takeTheOrder = this.hostURL + "/" + id + "/take";
		response = restAssuredClient.requestPutCall(takeTheOrder, "");
		utility.Verify_CustomErrorMessage(response);
	}

	/**
	 * Test with wrong Order Id
	 */

	@Test(priority = 16, groups = { "place order with wrong body" })
	public void testcases_Verify__FetchTheOrderDetailsNotCreated() {
		String fetchTheOrderWithWrongId = placeOrderurl + "/" + 1224;
		response = restAssuredClient.requestGetCall(fetchTheOrderWithWrongId);
		responseCode = response.getStatusCode();
		RestAssured.defaultParser = Parser.JSON;
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_404, "Status code is not 404");
	}

}
