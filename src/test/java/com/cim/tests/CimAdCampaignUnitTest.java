package com.cim.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cim.controller.AdCampaignController;
import com.cim.domain.AdInfo;
import com.cim.service.AdCampaignService;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * CimAdCampaignUnitTest.java
 * Unit Test Cases.
 * 
 * */
@RunWith(SpringRunner.class)
public class CimAdCampaignUnitTest {

	/*@Rule
	public RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");*/
	
	private MockMvc mockMvc;

	@Mock
	private AdCampaignService adCampaignService;
	
	@InjectMocks
	private  AdCampaignController adCampaignController;
	
	@Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(adCampaignController)
                .build();
    }
	
	/* Get All Campaigns
	 * 200 -Success
	 * */
	@Test
	public void test_get_all_campaigns_success() throws Exception {
		//Prepare and mock the service
	    List<AdInfo> adCampaigns = Arrays.asList(
	            new AdInfo("TRAVEL", 100, LocalDateTime.now(), "Life on the road! New Series, Monday and Friday 6 PM EST."),
	            new AdInfo("FOX", 90, LocalDateTime.now(),  "Breaking News! Streaming Available."));
	    when(adCampaignService.getAllCampaigns()).thenReturn(adCampaigns);
	    
	    mockMvc.perform(get("/"))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
	            .andExpect(jsonPath("$[0].partner", equalTo("TRAVEL")))
	            .andExpect(jsonPath("$[1].partner", equalTo("FOX")));
	    verify(adCampaignService, times(1)).getAllCampaigns();
	    verifyNoMoreInteractions(adCampaignService);
	}
	
	/* Create Partner Ad
	 * 200 -Success 
	 * */
	@Test
	public void test_create_ad_campaign_success() throws Exception {
		AdInfo travelAd = new AdInfo("TRAVEL", 100, LocalDateTime.now(), "Life on the road! New Series, Monday and Friday 6 PM EST.");
		when(adCampaignService.checkAdActiveStatus(travelAd.getPartner())).thenReturn(false);
		when(adCampaignService.savePartnerAdInfo(travelAd)).thenReturn(travelAd);
		
		mockMvc.perform(
	            post("/ad")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .accept(MediaType.APPLICATION_JSON_UTF8)
	            .content(asJsonString(travelAd)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("TRAVEL")));
		
		verify(adCampaignService, times(1)).checkAdActiveStatus(travelAd.getPartner());
	    verify(adCampaignService, times(1)).savePartnerAdInfo(travelAd);
	    verifyNoMoreInteractions(adCampaignService);
	}
	
	/*
	 * Get Partner Ad
	 * 200 -Success
	 * */
	@Test
	public void test_get_ad_campaign_success() throws Exception {
		AdInfo travelAd = new AdInfo("TRAVEL", 100, LocalDateTime.now(), "Life on the road! New Series, Monday and Friday 6 PM EST.");
		when(adCampaignService.getPartnerCampaignInfo(travelAd.getPartner())).thenReturn(Optional.of(travelAd));
		
		mockMvc.perform(
	            get("/ad/{partnerId}", "TRAVEL")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .accept(MediaType.APPLICATION_JSON_UTF8))
	            //.content(asJsonString(travelAd)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("TRAVEL")));
		
	    verify(adCampaignService, times(1)).getPartnerCampaignInfo(travelAd.getPartner());
	    verifyNoMoreInteractions(adCampaignService);
	}
	
	
	/* Get Partner Ad
	 * 404 -Not Found Scenario.
	 * */
	@Test
	public void test_get_ad_campaign_failure() throws Exception {
		when(adCampaignService.getPartnerCampaignInfo("TRAVEL")).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(
				get("/ad/{partnerId}", "TRAVEL"))
				.andExpect(status().isNotFound());
		
	    verify(adCampaignService, times(1)).getPartnerCampaignInfo("TRAVEL");
	    verifyNoMoreInteractions(adCampaignService);
	}
	
	/* Update Partner Ad
	 * 200 -Success.
	 * */
	@Test
	public void test_update_ad_campaign_success() throws Exception {
		AdInfo travelAd = new AdInfo("TRAVEL", 100, LocalDateTime.now(), "Life on the road! New Series, Monday and Friday 6 PM EST.");
		AdInfo updatedTravelAd = new AdInfo("TRAVEL", 200, LocalDateTime.now(), "People on the road! New Series, Monday and Friday 6 PM EST.");
		when(adCampaignService.getAdCampaign(travelAd.getPartner())).thenReturn(Optional.of(travelAd));
		when(adCampaignService.processExistingCampaign(Optional.of(travelAd), updatedTravelAd)).thenReturn(updatedTravelAd);
		
		mockMvc.perform(
	            put("/ad/{partnerId}", "TRAVEL")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .accept(MediaType.APPLICATION_JSON_UTF8)
	            .content(asJsonString(updatedTravelAd)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("People")));
		
	    verify(adCampaignService, times(1)).getAdCampaign("TRAVEL");
	    verify(adCampaignService, times(1)).processExistingCampaign(Optional.of(travelAd), updatedTravelAd);
	    verifyNoMoreInteractions(adCampaignService);
	}
	
	public static String asJsonString(final Object obj) {
        try {
        	return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
}
