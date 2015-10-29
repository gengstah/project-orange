package org.geeksexception.project.talent.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.service.AuthenticationService;
import org.geeksexception.project.talent.service.UserService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

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
			@Multipart(value = "user", type = MediaType.TEXT_PLAIN_VALUE) @NotNull String userJson, 
			@Multipart(value = "images") final List<Attachment> images) 
					throws TalentManagementServiceApiException {
		
		validateImages(images);
		User user = convertJsonStringToObject(userJson);
		validateBean(user);
		
		for(int imageIndex = 0;imageIndex < images.size();imageIndex++) {
			Attachment image = images.get(imageIndex);
			String imageName = image.getContentDisposition().getParameter("filename");
			if (imageName == null) {
	            imageName = UUID.randomUUID().toString();
	        }
			
			OutputStream outputStream = null;
			InputStream inputStream = null;
			try {
				outputStream = new FileOutputStream(new File(context.getServletContext().getRealPath("/") + "img/" + user.getTalent().getFirstName() + "-" + user.getTalent().getLastName() + "-" + imageName + ".jpg"));
				inputStream = image.getDataHandler().getInputStream();
				//IOUtils.copy(inputStream, outputStream);
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(inputStream != null) inputStream.close();
					if(outputStream != null) {
						outputStream.flush();
						outputStream.close();
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return userService.save(user);
		
	}

	private void validateImages(final List<Attachment> images) throws TalentManagementServiceApiException {
		if(images == null || images.size() == 0)
			throw new TalentManagementServiceApiException(
					"No image attached", 
					new Errors()
						.addError(new Error("images", "No image attached")));
	}

	private User convertJsonStringToObject(String userJson) throws TalentManagementServiceApiException {
		
		User user = null;
		try {
			user = new ObjectMapper().readValue(userJson, User.class);
		} catch (IOException e) {
			throw new TalentManagementServiceApiException(
					"Error while parsing json string", 
					new Errors()
						.addError(new Error("user", e.getMessage())),
					e);
		}
		
		if(user != null && user.getTalent() != null) user.getTalent().setUser(user);
		
		return user;
		
	}

	private void validateBean(User user) throws TalentManagementServiceApiException {
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<User>> errors = validator.validate(user);
		if(errors.size() > 0) {
			Errors appErrors = new Errors();
			for(ConstraintViolation<User> error : errors) {
				appErrors.addError(new Error(error.getPropertyPath().toString(), error.getMessage()));
			}
			
			throw new TalentManagementServiceApiException(
					"Error while parsing json string", appErrors);
		}
		
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