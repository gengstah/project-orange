package org.geeksexception.project.talent.api;

import java.util.List;

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
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
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
	@Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/register")
	public User register(
			@Multipart(value = "user", type = "text/plain") @NotNull @Valid User user, 
			@Multipart(value = "images", type = MediaType.APPLICATION_OCTET_STREAM_VALUE) final List<Attachment> images) 
					throws TalentManagementServiceApiException {
		
		return userService.save(user);
		
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
		if(user != null)
			authenticationService.authenticate(email, password);
		
		return user;			
		
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