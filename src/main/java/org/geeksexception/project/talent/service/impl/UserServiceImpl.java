package org.geeksexception.project.talent.service.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.cxf.helpers.IOUtils;
import org.geeksexception.project.talent.dao.ImageRepository;
import org.geeksexception.project.talent.dao.UserRepository;
import org.geeksexception.project.talent.dao.WorkExperienceRepository;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.Image;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.model.WorkExperience;
import org.geeksexception.project.talent.service.UserService;
import org.geeksexception.project.talent.util.PasswordUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	
	private @Inject UserRepository userRepository;
	
	private @Inject WorkExperienceRepository workExperienceRepository;
	
	private @Inject ImageRepository imageRepository;
	
	public UserServiceImpl() { }
	
	@Override
	public User findUserById(Long id) {
		
		return userRepository.findOne(id);
		
	}
	
	@Override
	public User findUserByEmail(String email) {
		
		return userRepository.findUserByEmail(email);
		
	}

	@Override
	@Transactional(readOnly = false)
	public User save(User user, String imageRootLocation) throws TalentManagementServiceApiException {
		
		if(findUserByEmail(user.getEmail()) != null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("email", "Email is already in use")));
		
		user.getTalent().setUser(user);
		user.setPassword(PasswordUtil.generatePassword(user.getPassword()));
		
		saveWorkExperiences(user);
		saveImages(user, imageRootLocation);
		
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
	
	private void saveImages(User user, String rootLocation) throws TalentManagementServiceApiException {
		
		if(user.getTalent() != null && user.getTalent().getImages().size() > 0) {
			for(Image image : user.getTalent().getImages()) {
				String data[] = image.getFileLocation().split(",");
				if(data.length == 2) {
					
					String fileType = data[0].split(":")[1].split(";")[0];
					image.setFileType(fileType);
					
					String fileExtension = fileType.split("/")[1];
					String imageName = UUID.randomUUID().toString() + "." + fileExtension;
					String fileName = rootLocation + "img/" + imageName;
					image.setFileLocation("/img/" + imageName);
					
					String base64data = data[1];
					Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(base64data.getBytes(StandardCharsets.UTF_8)));
					OutputStream outputStream = null;
					BufferedOutputStream writer = null;
					
					try {
						outputStream = new FileOutputStream(new File(fileName));
						writer = new BufferedOutputStream(outputStream);
						writer.write(IOUtils.readBytesFromStream(inputStream));
					} catch(IOException e) {
						throw new TalentManagementServiceApiException(
								"Error while registering", 
								new Errors()
									.addError(new Error("user", e.getMessage())), e);
					} finally {
						try {
							if(writer != null) {
								writer.flush();
								writer.close();
							}
							
							if(outputStream != null) {
								outputStream.flush();
								outputStream.close();
							}
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
					
			}
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
	public User getFullProfile() {
		
		User user = getLoggedInUser();
		Long talentId = user.getTalent().getId();
		List<Image> images = imageRepository.findImagesByTalentId(talentId);
		List<WorkExperience> workExperiences = workExperienceRepository.findWorkExperienceByTalentId(talentId);
		
		user.getTalent().setWorkExperiences(workExperiences);
		user.getTalent().setImages(images);
		
		return user;
		
	}

}