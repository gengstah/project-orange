package org.geeksexception.project.talent.api;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.ImageUploadResponse;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.service.AuthenticationService;
import org.geeksexception.project.talent.service.ImageUploadService;
import org.geeksexception.project.talent.service.TalentService;
import org.geeksexception.project.talent.service.UserService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

@Path("/user")
public class UserManager {
	
	private @Context MessageContext context;
	
	private @Inject UserService userService;
	
	private @Inject AuthenticationService authenticationService;
	
	private @Inject ImageUploadService imageUploadService;
	
	private @Inject TalentService talentService;
	
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
	@Path("/registerTalent")
	public User registerTalentProfile(@NotNull(message = "There is no user object to register") @Valid User user) throws TalentManagementServiceApiException {
		
		String clearPassword = user.getPassword();
		User savedUser = userService.saveTalentUser(user, context.getHttpServletRequest().getSession().getId(), user.getReCaptchaResponse());
		
		if(savedUser != null) authenticateAndLoadUser(savedUser.getEmail(), clearPassword);
		
		return savedUser;
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/registerAgency")
	public User registerAgencyProfile(@NotNull(message = "There is no user object to register") @Valid User user) throws TalentManagementServiceApiException {
		
		String clearPassword = user.getPassword();
		User savedUser = userService.saveAgencyUser(user, user.getReCaptchaResponse());
		
		if(savedUser != null) authenticateAndLoadUser(savedUser.getEmail(), clearPassword);
		
		return savedUser;
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/updateTalent")
	public User updateTalentProfile(@NotNull(message = "There is no user object to register") @Valid User user) throws TalentManagementServiceApiException {
		
		return userService.updateTalentUser(user, context.getHttpServletRequest().getSession().getId());
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/updateAgency")
	public User updateAgencyProfile(@NotNull(message = "There is no user object to register") @Valid User user) throws TalentManagementServiceApiException {
		
		return userService.updateAgencyUser(user);
		
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
	public User viewFullProfile(@QueryParam("email") String email) {
		
		return userService.getFullProfile(email);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/changepassword")
	public User changePassword(
			@NotEmpty(message = "old password must not be empty") @FormParam("oldPassword") String oldPassword, 
			@NotEmpty(message = "new password must not be empty") @Size(min = 8, message = "new password must consist of at least 8 characters") @FormParam("newPassword") String newPassword) 
			throws TalentManagementServiceApiException {
		
		return userService.changePassword(oldPassword, newPassword);
		
	}
	
	@GET
	@Consumes(MediaType.ALL_VALUE)
	@Path("/logout")
	public void logout() {
		
		HttpSession session = context.getHttpServletRequest().getSession(false);
		if(session != null) session.invalidate();
		
		SecurityContextHolder.clearContext();
		
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/uploadImage")
	public ImageUploadResponse uploadPicture(Attachment attachment) throws TalentManagementServiceApiException {
		
		HttpSession session = context.getHttpServletRequest().getSession();
		String sessionId = session.getId();
		String tempLocation = context.getServletContext().getRealPath("/") + "img/temp/";
		
		return imageUploadService.uploadImage(attachment, sessionId, tempLocation);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/deleteImage/{fileName}")
	public void deleteImage(@NotNull @PathParam("fileName") String fileName) {
		
		HttpSession session = context.getHttpServletRequest().getSession();
		String sessionId = session.getId();
		
		imageUploadService.deleteImage(sessionId, fileName);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/deleteSavedImage/{fileName}")
	public void deleteSavedImage(@NotNull @PathParam("fileName") String fileName) {
		
		HttpSession session = context.getHttpServletRequest().getSession();
		String rootLocation = session.getServletContext().getRealPath("/");
		talentService.deleteSavedImage(fileName, rootLocation);
		
	}
	
}