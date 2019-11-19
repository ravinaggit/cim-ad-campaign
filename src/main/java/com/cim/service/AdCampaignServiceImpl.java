package com.cim.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cim.domain.AdInfo;
import com.cim.repository.AdCampaignRepository;

@Service
public class AdCampaignServiceImpl implements AdCampaignService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AdCampaignServiceImpl.class);

	@Autowired
	AdCampaignRepository adCampaignRepository;
	
	@Override
	public AdInfo savePartnerAdInfo(AdInfo newAdInfo) {
		newAdInfo.setDateTime(LocalDateTime.now());
		return adCampaignRepository.save(newAdInfo);
	}

	@Override
	public List<AdInfo> getAllCampaigns() {
		return (List<AdInfo>) adCampaignRepository.findAll();
	}
	
	/*Predicate<String> checkAdActiveStatus = (s) -> {
		Optional<AdInfo> adInfoOptional = Optional.ofNullable(adCampaignRepository.findOne(s));
		return isPartnerAdActive(adInfoOptional);
	};*/
	
	@Override
	public boolean checkAdActiveStatus(String partnerId) {
		Optional<AdInfo> adInfoOptional = getAdCampaign(partnerId);
		return isPartnerAdActive(adInfoOptional);
	}

	@Override
	public Optional<AdInfo> getPartnerCampaignInfo(String partnerId) {
		Optional<AdInfo> adInfoOptional = getAdCampaign(partnerId);
		boolean partnerAdActive = isPartnerAdActive(adInfoOptional);
		LOGGER.debug("Active Partner partner {}, {}", partnerId, partnerAdActive);
		return partnerAdActive ? adInfoOptional : Optional.ofNullable(null);
	}

	@Override
	public AdInfo processExistingCampaign(Optional<AdInfo> existingAd, AdInfo adInfo) {
		existingAd.ifPresent( s -> adCampaignRepository.delete(s));
		adInfo.setDateTime(LocalDateTime.now());
		AdInfo savedAdInfo = adCampaignRepository.save(adInfo);

		return savedAdInfo;
	}
	
	@Override
	public Optional<AdInfo> getAdCampaign(String partnerId) {
		return adCampaignRepository.findById(partnerId);
	}

	@Override
	public void deleteAdCampaign(String partnerId) {
		Optional<AdInfo> existingAd = getAdCampaign(partnerId);
		existingAd.ifPresent( s -> adCampaignRepository.delete(s));
	}

	
}

