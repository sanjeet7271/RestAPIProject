package com.nagarro.restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
/**
 * Rest-Assured methods GET, POST and PUT call
 * 
 * @author sanjeetpandit
 *
 */
public class RestAssuredClient {
	/**
	 * GET call
	 * 
	 * @param url
	 * @return
	 */
	public Response requestGetCall(String url) {
		Response response = RestAssured.given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
				.log().all().when().get(url).then().contentType(ContentType.JSON).log().all().extract().response();

		return response;

	}

	/**
	 * Post Call
	 * 
	 * @param url
	 * @param entityString
	 * @return
	 */
	public Response requestPostCall(String url, String entityString) {
		Response response = RestAssured.given().body(entityString).contentType(ContentType.JSON).log().all().when()
				.post(url).then().log().all().extract().response();
		return response;

	}

	/**
	 * PUT call
	 * 
	 * @param url
	 * @param entityString
	 * @return
	 */
	public Response requestPutCall(String url, String entityString) {
		Response response = RestAssured.given().body(entityString).contentType(ContentType.JSON).log().all().when()
				.put(url).then().log().all().extract().response();
		return response;

	}

}
