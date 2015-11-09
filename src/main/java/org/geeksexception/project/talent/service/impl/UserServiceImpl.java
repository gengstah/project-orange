package org.geeksexception.project.talent.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.geeksexception.project.talent.api.ReCaptchaManager;
import org.geeksexception.project.talent.dao.AgencyRepository;
import org.geeksexception.project.talent.dao.ImageRepository;
import org.geeksexception.project.talent.dao.TalentRepository;
import org.geeksexception.project.talent.dao.UserRepository;
import org.geeksexception.project.talent.dao.WorkExperienceRepository;
import org.geeksexception.project.talent.enums.UserRole;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.Image;
import org.geeksexception.project.talent.model.ReCaptchaResponse;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.model.WorkExperience;
import org.geeksexception.project.talent.service.ImageService;
import org.geeksexception.project.talent.service.UserService;
import org.geeksexception.project.talent.util.PasswordUtil;
import org.imgscalr.Scalr;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@PropertySource("classpath:/reCaptcha.properties")
public class UserServiceImpl implements UserService {
	
	public static int THUMB_WIDTH = 77;
	
	public static int THUMB_HEIGHT = 57;
	
	private @Inject UserRepository userRepository;
	
	private @Inject TalentRepository talentRepository;
	
	private @Inject AgencyRepository agencyRepository;
	
	private @Inject WorkExperienceRepository workExperienceRepository;
	
	private @Inject ImageRepository imageRepository;
	
	private @Inject ImageService imageService;
	
	private @Inject ReCaptchaManager reCaptchaManager;
	
	private @Inject Environment env;
	
	public UserServiceImpl() { }
	
	@Override
	public User findUserById(Long id) {
		
		return userRepository.findOne(id);
		
	}
	
	@Override
	public User findUserByEmail(String email) {
		
		return userRepository.findUserByEmail(email);
		
	}
	
	private void validateEmailIfExisting(User user) throws TalentManagementServiceApiException {
		if(findUserByEmail(user.getEmail()) != null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("email", "Email is already in use")));
	}

	private void validateReCaptchaResponse(String reCaptchaResponse) throws TalentManagementServiceApiException {
		ReCaptchaResponse response = reCaptchaManager.verify(env.getProperty("secret"), reCaptchaResponse);
		
		if(StringUtils.isEmpty(reCaptchaResponse) || !response.isSuccess())
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("recaptcha", "Please solve the reCaptcha")));
	}
	
	@Override
	@Transactional(readOnly = false)
	public User saveTalentUser(User user, String imageTempLocation, String reCaptchaResponse) throws TalentManagementServiceApiException {
		
		validateReCaptchaResponse(reCaptchaResponse);
		validateEmailIfExisting(user);
		
		user.getTalent().setUser(user);
		user.setPassword(PasswordUtil.generatePassword(user.getPassword()));
		user.setUserRole(UserRole.ROLE_USER);
		
		saveWorkExperiences(user);
		saveImages(user, imageTempLocation);
		
		return userRepository.save(user);
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public User saveAgencyUser(User user, String reCaptchaResponse) throws TalentManagementServiceApiException{
		
		validateReCaptchaResponse(reCaptchaResponse);
		validateEmailIfExisting(user);
		
		user.getAgency().setUser(user);
		user.setPassword(PasswordUtil.generatePassword(user.getPassword()));
		user.setUserRole(UserRole.ROLE_AGENCY);
		
		return userRepository.save(user);
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public User updateTalentUser(User user, String imageTempLocation) throws TalentManagementServiceApiException {
		
		User savedUser = getLoggedInUser();
		user.setPassword(savedUser.getPassword());
		user.getTalent().setImages(savedUser.getTalent().getImages());
		saveWorkExperiences(user);
		saveImages(user, imageTempLocation);
		
		return userRepository.save(user);
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public User updateAgencyUser(User user) throws TalentManagementServiceApiException {
		
		User savedUser = getLoggedInUser();
		user.setPassword(savedUser.getPassword());
		
		return userRepository.save(user);
		
	}
	
	private void saveWorkExperiences(User user) {
		
		List<WorkExperience> workExperiences = eliminateDuplicateWorkExperiences(user.getTalent().getWorkExperiences());
		List<WorkExperience> savedWorkExperiences = new ArrayList<WorkExperience>();
		
		for(WorkExperience workExperience : workExperiences) {
			WorkExperience savedWorkExperience = workExperienceRepository.findWorkExperienceByName(workExperience.getName().trim());
			if(savedWorkExperience == null) {
				workExperience.setName(workExperience.getName().trim());
				savedWorkExperience = workExperienceRepository.save(workExperience);
			}
			savedWorkExperiences.add(savedWorkExperience);
		}
		
		user.getTalent().setWorkExperiences(savedWorkExperiences);
		
	}
	
	List<WorkExperience> eliminateDuplicateWorkExperiences(List<WorkExperience> workExperiences) {
		
		Set<WorkExperience> workExperienceSet = new HashSet<WorkExperience>();
		workExperienceSet.addAll(workExperiences);
		workExperiences.clear();
		workExperiences.addAll(workExperienceSet);
		
		return workExperiences;
		
	}
	
	private void saveImages(User user, String imageTempLocation) throws TalentManagementServiceApiException {
		
		File temp = new File(imageTempLocation);
		File thumbnailTemp = new File(imageTempLocation + "/thumbnails");
		if(temp.exists()) {
			File[] files = temp.listFiles();
			thumbnailTemp.mkdir();
			
			if(files.length == 0)
				throw new TalentManagementServiceApiException(
						"Please upload at least 1 image", 
						new Errors()
							.addError(new Error("image", "Please upload at least 1 image")));
			
			if(user.getTalent().getImages() == null) user.getTalent().setImages(new ArrayList<Image>());
			for(File file : files) {
				Image image = new Image("/img/talents/" + file.getName(), "/img/talents/thumbnails/" + file.getName());
				image.setTalent(user.getTalent());
				imageService.save(image);
				user.getTalent().getImages().add(image);
				
				try {
					BufferedImage srcImg = ImageIO.read(file);
					BufferedImage dstImg = Scalr.resize(srcImg, Scalr.Method.QUALITY, THUMB_WIDTH, THUMB_HEIGHT);
					ImageIO.write(dstImg, "jpg", new File(imageTempLocation + "/thumbnails/" + file.getName()));
				} catch(IOException e) {
					throw new TalentManagementServiceApiException(
							"Error while creating thumbnails", 
							new Errors()
								.addError(new Error("image", "Error while creating thumbnails")), e);
				}
			}
			
			try {
				FileUtils.copyDirectory(temp, new File(temp.getParentFile().getParentFile().getAbsolutePath() + "/talents"));
				FileUtils.copyDirectory(thumbnailTemp, new File(temp.getParentFile().getParentFile().getAbsolutePath() + "/talents/thumbnails"));
				FileUtils.deleteDirectory(temp);
			} catch (IOException e) {
				throw new TalentManagementServiceApiException(
						"Error while copying images from temp directory to main directory", 
						new Errors()
							.addError(new Error("image", "Error while copying images from temp directory to main directory")), e);
			}
			
		} else if(user.getTalent().getImages() != null) {
			
		} else {
			throw new TalentManagementServiceApiException(
					"Please upload at least 1 image", 
					new Errors()
						.addError(new Error("image", "Please upload at least 1 image")));
		}
		
	}

	@Override
	public User getLoggedInUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return findUserByEmail(authentication.getName());
		
	}

	@Override
	public List<User> findTalentUsers(Integer page, Integer size) {
		
		return userRepository.findTalentUsers(new PageRequest(page, size));
		
	}

	@Override
	public List<User> findAgencyUsers(Integer page, Integer size) {
		
		return userRepository.findAgencyUsers(new PageRequest(page, size));
		
	}

	@Override
	public User getFullProfile(String email) {
		
		User user;
		
		if(email == null || email.trim().equals("")) user = getLoggedInUser();
		else user = findUserByEmail(email);
		
		if(user.getUserRole() == UserRole.ROLE_USER) {
			Long talentId = user.getTalent().getId();
			List<Image> images = imageRepository.findImagesByTalentId(talentId);
			List<WorkExperience> workExperiences = workExperienceRepository.findWorkExperienceByTalentId(talentId);
			
			user.getTalent().setWorkExperiences(workExperiences);
			user.getTalent().setImages(images);
			
			user.getTalent().setEventSize(talentRepository.countEvents(user.getTalent()));
		} else if(user.getUserRole() == UserRole.ROLE_AGENCY) {
			user.getAgency().setEventSize(agencyRepository.countEvents(user.getAgency()));
		}
		
		user.setFollowerSize(userRepository.countFollowers(user));
		user.setFollowingSize(userRepository.countFollowing(user));
		
		return user;
		
	}

	@Override
	@Transactional(readOnly = false)
	public User changePassword(String oldPassword, String newPassword) throws TalentManagementServiceApiException {
		
		User user = getLoggedInUser();
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
		
		if(!encoder.matches(oldPassword, user.getPassword()))
			throw new TalentManagementServiceApiException("Old password is incorrect", 
					new Errors().addError(new Error("password", "Old password is incorrect")));
		
		user.setPassword(PasswordUtil.generatePassword(newPassword));
		user.setReCaptchaResponse("Not required");
		user = userRepository.save(user);
		
		return user;
		
	}
	
}