package com.cim.tests;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import com.cim.domain.AdInfo;


/*
 * Functional Testing Test Cases
 * */
public class CimAdCampaignFunctionalTest extends CimAdCampaignBaseConfigTest {

	final AdInfo mockTravelAd = new AdInfo("TRAVEL", 100, "Life on the road! New Series, Monday and Friday 6 PM EST.");
	
	@Test
	public void basicPingTest(){
		given().when().get("/").then().statusCode(anyOf(is(200), is(404)));
		
	}
	
	/*
	 * Test to create a new partner ad.
	 * Expected Result: 200
	 * */	
	@Test
	public void executeTravelAdCampaignTest(){
		given()
        .contentType("application/json")
        .body(mockTravelAd)
        .when().post("/ad").then()
        .statusCode(200);
		
		//CRUD tests
		createPartnerAdWithActiveAdTest();
		getTravelAdCampaignTest();
		updateTravelAdCampaingnTest();
		deleteTravelAdCampaingnTest();
	}
	
	/*
	 * Test to create a new partner ad which is already active.
	 * Expected Result: 409.Conflict
	 * */
	public void createPartnerAdWithActiveAdTest(){
		given()
        .contentType("application/json")
        .body(mockTravelAd)
        .when().post("/ad").then()
        .statusCode(409);
	}
	
	/*
	 * Test to retreive the saved TRAVEL ad campaign
	 * Expected Result: 200, Life on the road -String part match
	 * */
	public void getTravelAdCampaignTest(){
		given()
        .contentType("application/json")
        .when().get("/ad/TRAVEL").then()
        .body("content", containsString("Life on the road"))
        .statusCode(200);
	}
	
	/*
	 * Test to Update the saved TRAVEL ad campaign with content change
	 * Expected Result: 200, People on the road -String part match
	 * */
	public void updateTravelAdCampaingnTest(){
		AdInfo updateTravelAd = new AdInfo("TRAVEL", 100, "People on the road! New Series, Monday and Friday 6 PM EST.");
		given()
        .contentType("application/json")
        .body(updateTravelAd)
        .when().put("/ad/TRAVEL").then()
        .body("content", containsString("People on the road"))
        .statusCode(200);
	}
	
	/*
	 * Test to Delete the saved TRAVEL
	 * Expected Result: 200
	 * */
	public void deleteTravelAdCampaingnTest(){
		given()
        .contentType("application/json")
        .when().delete("/ad/TRAVEL").then()
        .statusCode(200);
	}
	
	@Test
	public void getNonExistentAdCampaingnTest(){
		given()
        .contentType("application/json")
        .when().get("/ad/FOX").then()
        .statusCode(404);
	}
	
	@Test
	public void updateNonExistentAdCampaingnTest(){
		given()
        .contentType("application/json")
        .when().get("/ad/FOX").then()
        .statusCode(404);
	}
	
}
