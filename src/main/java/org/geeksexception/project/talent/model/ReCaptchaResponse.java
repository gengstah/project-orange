package org.geeksexception.project.talent.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReCaptchaResponse {
	
	private boolean success;
	
	private List<String> errorCodes;
	
	public ReCaptchaResponse() { }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	@JsonProperty("error-codes")
	public List<String> getErrorCodes() {
		return errorCodes;
	}
	
	@JsonProperty("error-codes")
	public void setErrorCodes(List<String> errorCodes) {
		this.errorCodes = errorCodes;
	}
	
}