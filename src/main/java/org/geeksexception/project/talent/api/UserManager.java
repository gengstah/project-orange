package org.geeksexception.project.talent.api;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.service.AuthenticationService;
import org.geeksexception.project.talent.service.UserService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

@Path("/user")
public class UserManager {
	
	private @Context MessageContext context;
	
	private @Inject UserService userService;
	
	private @Inject AuthenticationService authenticationService;
	
	public UserManager() { }
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public User getLoggedInUser() {
		
		return userService.getLoggedInUser();
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/register")
	public User register(@NotNull(message = "There is no user object to register") @Valid User user) throws TalentManagementServiceApiException {
		
		String clearPassword = user.getPassword();
		User savedUser = userService.save(user, context.getServletContext().getRealPath("/"), user.getReCaptchaResponse());
		
		if(savedUser != null) authenticateAndLoadUser(savedUser.getEmail(), clearPassword);
		
		return savedUser;
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/authenticate")
	public User authenticateAndLoadUser(
			@NotEmpty(message = "email must not be empty") @FormParam("email") String email, 
			@NotEmpty(message = "password must not be empty") @FormParam("password") String password) 
			throws TalentManagementServiceApiException {
		
		User user = userService.findUserByEmail(email);
		if(user == null)
			throw new TalentManagementServiceApiException(
					"Invalid email or password", 
					new Errors()
						.addError(new Error("email", "Invalid email or password")));
		else
			authenticationService.authenticate(email, password);
		
		return user;
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/profile")
	public User viewFullProfile() {
		
		return userService.getFullProfile();
		
	}
	
	@GET
	@Consumes(MediaType.ALL_VALUE)
	@Path("/logout")
	public void logout() {
		
		HttpSession session = context.getHttpServletRequest().getSession(false);
		if(session != null) session.invalidate();
		
		SecurityContextHolder.clearContext();
		
	}
	
}