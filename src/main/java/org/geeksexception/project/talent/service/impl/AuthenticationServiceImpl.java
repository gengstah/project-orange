package org.geeksexception.project.talent.service.impl;

import javax.inject.Inject;

import org.geeksexception.project.talent.enums.TalentStatus;
import org.geeksexception.project.talent.enums.UserRole;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.service.AuthenticationService;
import org.geeksexception.project.talent.service.UserService;
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
	
	private @Inject UserService userService;
	
	public AuthenticationServiceImpl() { }
	
	@Override
	public void authenticate(String username, String password) throws TalentManagementServiceApiException {
		
		Authentication request = new UsernamePasswordAuthenticationToken(username, password);
		Authentication result = null;
		
		User user = userService.findUserByEmail(username);
		if(user.getUserRole() == UserRole.ROLE_USER && user.getTalent().getStatus() == TalentStatus.DENIED)
			throw new TalentManagementServiceApiException(
					user.getTalent().getAdminNote(), 
					new Errors()
						.addError(new Error("email", user.getTalent().getAdminNote())));
		
		try {
			result = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);
		} catch(AuthenticationException e) {
			logger.warn("Authentication failed: {}", e.getMessage());
			throw new TalentManagementServiceApiException(
					"Invalid email or password", 
					new Errors()
						.addError(new Error("email", "Invalid email or password")),
					e);
		}

	}

}