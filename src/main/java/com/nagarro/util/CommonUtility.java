package com.nagarro.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import org.testng.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;
import com.nagarro.constants.FrameworkConstants;
import com.nagarro.constants.StatusCodes;
import com.nagarro.global.BaseTest;

/**
 * Utility for fare calculations and custom status code with error message
 * 
 * @author sanjeetpandit
 *
 */
public class CommonUtility extends BaseTest {
	public CommonUtility() throws IOException {
		super();
	}

	double calculateamount;

	/**
	 * Total cost traveled in normal time
	 * 
	 * @param sumOfDistance
	 * @return TotalCostOfJeourney
	 */
	public double travelCostInNormalTimeStamp(double sumOfDistance) {
		double remainingDistanceForEach200Meter, totalCostForJeourney = 0, remainingCostForEach200Meter, totalCost;
		if (sumOfDistance == FrameworkConstants.ZERO_KM_DISTANCE) {
			totalCostForJeourney = FrameworkConstants.ZERO_KM_DISTANCE;
		} else if (sumOfDistance <= FrameworkConstants.FIRST_2KM_DISTANCE) {
			totalCostForJeourney = FrameworkConstants.FIRST_2KM_DAY_COST;
		} else if (sumOfDistance >= FrameworkConstants.FIRST_2KM_DISTANCE) {
			sumOfDistance = sumOfDistance - FrameworkConstants.FIRST_2KM_DISTANCE;
			remainingDistanceForEach200Meter = sumOfDistance / (FrameworkConstants.DISTANCE200);
			remainingCostForEach200Meter = remainingDistanceForEach200Meter
					* (FrameworkConstants.MORE_THAN_FIRST_2KM_DAY_COST);
			totalCost = FrameworkConstants.FIRST_2KM_DAY_COST + remainingCostForEach200Meter;
			totalCostForJeourney = Double.parseDouble(new DecimalFormat("##.##").format(totalCost));

		}
		return totalCostForJeourney;
	}

	/**
	 * Verifying fare calculations and currency with time stamps
	 * 
	 * @param sumOfDistance
	 * @param response
	 */
	public void verify_CalculateFare(int sumOfDistance, Response response) {
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
				calculateamount = travelCostInBetween9to5TimeStamp(sumOfDistance);
			} else {
				calculateamount = travelCostInNormalTimeStamp(sumOfDistance);
			}
		} else if ((time >= FrameworkConstants.MORE_THAN_9PM && time <= FrameworkConstants.LESS_THAN_11_59PM)
				|| (time >= FrameworkConstants.LESS_THAN_0AM && time <= FrameworkConstants.LESS_THAN_5AM)) {
			calculateamount = travelCostInBetween9to5TimeStamp(sumOfDistance);

		} else {
			calculateamount = travelCostInNormalTimeStamp(sumOfDistance);

		}
		logger.info("Calculted Cost :" + calculateamount);
		Map<String, String> orderFares = response.jsonPath().getMap("fare");
		String amount = orderFares.get("amount");
		String currency = orderFares.get("currency");
		Assert.assertEquals(Double.valueOf(amount), calculateamount);
		Assert.assertEquals(currency, FrameworkConstants.CURRENCY);
	}

	/**
	 * Total cost traveled in between 9pm to 5am
	 * 
	 * @param sumOfDistance
	 * @return TotalCostOfJeourney
	 */
	public double travelCostInBetween9to5TimeStamp(double sumOfDistance) {
		double remainingDistanceForEach200Meter, totalCostForJeourney = 0, totalCost;
		if (sumOfDistance == FrameworkConstants.NIGHT_ZERO_KM_DISTANCE) {
			totalCostForJeourney = FrameworkConstants.NIGHT_ZERO_KM_COST;
		} else if (sumOfDistance <= FrameworkConstants.NIGHT_FIRST_2KM_DISTANCE) {
			totalCostForJeourney = FrameworkConstants.NIGHT_FIRST_2KM_COST;
		} else if (sumOfDistance >= FrameworkConstants.NIGHT_FIRST_2KM_DISTANCE) {
			sumOfDistance = sumOfDistance - FrameworkConstants.NIGHT_FIRST_2KM_DISTANCE;
			remainingDistanceForEach200Meter = sumOfDistance / (FrameworkConstants.DISTANCE200);
			totalCost = FrameworkConstants.NIGHT_FIRST_2KM_COST
					+ (remainingDistanceForEach200Meter * (FrameworkConstants.NIGHT_MORE_THAN_FIRST_2KM_COST));
			totalCostForJeourney = Double.parseDouble(new DecimalFormat("##.##").format(totalCost));

		}
		return totalCostForJeourney;

	}

	/**
	 * Json reader for only 3 stops
	 * 
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JsonNode readJson() throws JsonProcessingException, IOException {
		String jsonReader = prop.getProperty("JsonFileReaderfor3stops");
		JsonNode json = null;
		ObjectMapper mapper = new ObjectMapper();
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
		return json;
	}

	/**
	 * Json reader for only 3 stops
	 * 
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JsonNode readJson_MoreThan3Stops() throws JsonProcessingException, IOException {
		String jsonReader = prop.getProperty("JsonFileReaderfor3stops");
		JsonNode json = null;
		ObjectMapper mapper = new ObjectMapper();
		if (jsonReader.equals("present")) {
			try {
				json = mapper.readTree(new File(System.getProperty("user.dir")
						+ "/src/main/resources/JsonData/PlaceOrderInPresentMoreThan3Stops.json"));
			} catch (FileNotFoundException e) {
				logger.error("Exception " + e);
				logger.error("Properties file not found.");
				Assert.fail("Properties file not found.");
			}
		} else if (jsonReader.equals("future")) {
			try {
				json = mapper.readTree(new File(System.getProperty("user.dir")
						+ "/src/main/resources/JsonData/PlaceOrderInFutureMoreThan3Stops.json"));
			} catch (FileNotFoundException e) {
				logger.error("Exception " + e);
				logger.error("Properties file not found.");
				Assert.fail("Properties file not found.");
			}
		}
		return json;
	}

	/**
	 * Verify Order which are not in Assigning State
	 * 
	 * @param response
	 */
	public void Verify_CustomErrorMessage(Response response) {
		int responseCode = response.getStatusCode();
		logger.info("Status Code--->" + responseCode);
		String errorMessage = response.jsonPath().get("message");
		logger.error("New Order Id :" + errorMessage);
		Assert.assertEquals(errorMessage, FrameworkConstants.ORDER_NOT_IN_ASSIGNING_STATE);
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_422, "Status code is not 422");
	}

	/**
	 * Verify custom status code
	 * 
	 * @param response
	 */
	public void Verify_ResponseCode422(Response response) {
		int responseCode = response.getStatusCode();
		logger.info("Status Code--->" + responseCode);
		RestAssured.defaultParser = Parser.JSON;
		Assert.assertEquals(responseCode, StatusCodes.RESPONSE_STATUS_CODE_422, "Status code is not 422");
	}
}
