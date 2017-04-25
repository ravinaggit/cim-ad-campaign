package com.cim.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cim.domain.AdInfo;

public interface AdCampaignService {
	
	public AdInfo savePartnerAdInfo(AdInfo adInfo);
	
	default boolean isPartnerAdActive(Optional<AdInfo> adInfo) {
		return adInfo.isPresent() ? adInfo.map(x -> LocalDateTime.now().isBefore(x.getDateTime().plusSeconds(x.getDuration()))).get() : false;
	}

	public boolean checkAdActiveStatus(String partnerId);
	
	public List<AdInfo> getAllCampaigns();

	public Optional<AdInfo> getPartnerCampaignInfo(String partnerId);

	public AdInfo processExistingCampaign(Optional<AdInfo> existingAd, AdInfo adInfo);

	public void deleteAdCampaign(String partnerId);

	public Optional<AdInfo> getAdCampaign(String partnerId);
	
}
