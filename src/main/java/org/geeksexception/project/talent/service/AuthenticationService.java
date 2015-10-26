package org.geeksexception.project.talent.service;

import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;

public interface AuthenticationService {
	
	void authenticate(String username, String password) throws TalentManagementServiceApiException;
	
}