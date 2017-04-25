package com.cim.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cim.domain.AdInfo;
import com.cim.exceptions.AdCampaignCustomException;
import com.cim.exceptions.ClientError;
import com.cim.service.AdCampaignService;

@RestController
public class AdCampaignController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AdCampaignController.class);

	@Autowired
	private AdCampaignService adCampaignService;
	
	/*
	 * Create new Ad Service
	 * */
	@PostMapping("/ad")
	public ResponseEntity<AdInfo> createCampaign(@RequestBody AdInfo adInfo){
		LOGGER.info(" Create New Ad Campaign Info -> {} ",adInfo.getPartner());
		boolean adActive = adCampaignService.checkAdActiveStatus(adInfo.getPartner());
		if(adActive){
			throw new AdCampaignCustomException(" Ad campaign is currently active! ", HttpStatus.CONFLICT);//409
		}
		AdInfo adInfoNew = adCampaignService.savePartnerAdInfo(adInfo);
		return new ResponseEntity<AdInfo>(adInfoNew, HttpStatus.OK);//200
	}
	
	/*
	 * Retrieve All Ad's Service
	 * */
	@GetMapping("/")
	public ResponseEntity<List<AdInfo>> getAllCampaigns(){
		LOGGER.info(" Retrieve All Campaigns Info  ");
		List<AdInfo> campaignList = adCampaignService.getAllCampaigns();
		if(campaignList.isEmpty()){
			throw new AdCampaignCustomException(" Partner campaigns not found! ", HttpStatus.NOT_FOUND);//404
		}
		//Optional.empty().orElseThrow(() ->  new AdCampaignCustomException(" Partner campaigns not found! ", HttpStatus.NOT_FOUND));
		
		return new ResponseEntity<List<AdInfo>>(campaignList, HttpStatus.OK);//200
	}
	
	/*
	 * Retrieve Partner Ad Service
	 * */
	@GetMapping("/ad/{partnerId}")
	public ResponseEntity<AdInfo> getPartnerCampaign(@PathVariable String partnerId){
		LOGGER.info(" Retrieve Partner Ad Campaign Info -> {} ", partnerId);
		AdInfo activeAd = adCampaignService.getPartnerCampaignInfo(partnerId).
				orElseThrow(() -> new AdCampaignCustomException(" No active campaign found! ", HttpStatus.NOT_FOUND));//404
		
		return new ResponseEntity<AdInfo>(activeAd, HttpStatus.OK);//200
	}
	
	/*
	 * Update Existing Partner Ad Service
	 * */
	@PutMapping("/ad/{partnerId}")
	public ResponseEntity<AdInfo> processExistingAd(@PathVariable String partnerId, @RequestBody AdInfo adInfo){
		LOGGER.info(" Upsert Partner Ad Campaign Info -> {} ", partnerId);
		Optional<AdInfo> existingAd = adCampaignService.getAdCampaign(partnerId);
		existingAd.orElseThrow(() -> new AdCampaignCustomException(" No campaign found! ", HttpStatus.NOT_FOUND));//404)
		AdInfo savedAdInfo = adCampaignService.processExistingCampaign(existingAd, adInfo);
		
		return new ResponseEntity<AdInfo>(savedAdInfo, HttpStatus.OK);//200
	}
	
	/*
	 * Delete Partner Ad Service
	 * */
	@DeleteMapping("/ad/{partnerId}")
	public ResponseEntity<String> deletePartnerAd(@PathVariable String partnerId){
		LOGGER.info(" Delete Partner Ad Campaign Info -> {} ", partnerId);
		adCampaignService.deleteAdCampaign(partnerId);
		return new ResponseEntity<String>("Done", HttpStatus.OK);//200
	}
	
	
	
	
	/* Exception handlers
	 * Common Custom Exception (AdCampaignCustomException)
	 * */
	@ExceptionHandler({ AdCampaignCustomException.class })
    public ResponseEntity<ClientError>  handleException(AdCampaignCustomException e) {
		LOGGER.error(e.getMessage(), e);
		ClientError error = new ClientError(e.getStatusCode().value(), e.getMessage());
		return new ResponseEntity<ClientError>(error, e.getStatusCode());
    }
	@ExceptionHandler({ Exception.class })
    public ResponseEntity<ClientError>  handleException(Exception e) {
		LOGGER.error(e.getMessage(), e);
		ClientError error = new ClientError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong!");
		return new ResponseEntity<ClientError>(error, HttpStatus.INTERNAL_SERVER_ERROR);//500
    }
}
