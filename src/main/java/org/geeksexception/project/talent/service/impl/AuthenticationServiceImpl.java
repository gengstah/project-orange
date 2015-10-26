package org.geeksexception.project.talent.service.impl;

import javax.inject.Inject;

import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private @Inject AuthenticationManager authenticationManager;
	
	public AuthenticationServiceImpl() { }
	
	@Override
	public void authenticate(String username, String password) throws TalentManagementServiceApiException {
		
		Authentication request = new UsernamePasswordAuthenticationToken(username, password);
		Authentication result = null;
		
		try {
			result = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);
		} catch(AuthenticationException e) {
			logger.warn("Authentication failed: {}", e.getMessage());
			throw new TalentManagementServiceApiException(
					"Error while authenticating", 
					new Errors()
						.addError(new Error("email", e.getMessage()))
						.addError(new Error("password", e.getMessage())),
					e);
		}

	}

}