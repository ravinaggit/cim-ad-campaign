package com.cim.exceptions;

import org.springframework.http.HttpStatus;

public class AdCampaignCustomException extends RuntimeException {

	private static final long serialVersionUID = -6959929531063939080L;

	private HttpStatus statusCode;
	
	public AdCampaignCustomException(String message, HttpStatus status) {
        super(message);
        this.statusCode = status;
    }

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}
	
}
