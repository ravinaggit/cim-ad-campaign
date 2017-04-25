package com.cim.repository;

import org.springframework.data.repository.CrudRepository;

import com.cim.domain.AdInfo;

public interface AdCampaignRepository extends CrudRepository<AdInfo, String> {	

}
