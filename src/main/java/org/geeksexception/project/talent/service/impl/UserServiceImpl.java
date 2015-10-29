package org.geeksexception.project.talent.service.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.cxf.helpers.IOUtils;
import org.geeksexception.project.talent.dao.UserRepository;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.Image;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	
	private @Inject UserRepository userRepository;
	
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
	public User save(User user) throws TalentManagementServiceApiException {
		
		if(findUserByEmail(user.getEmail()) != null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("email", "Email is already in use")));
		
		return userRepository.save(user);
		
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
	public void saveImages(User user, String rootLocation) throws TalentManagementServiceApiException {
		
		if(user.getTalent() != null && user.getTalent().getImages().size() > 0) {
			for(Image image : user.getTalent().getImages()) {
				String data[] = image.getFileLocation().split(",");
				if(data.length == 2) {
					
					String fileType = data[0].split(":")[1].split(";")[0];
					image.setFileType(fileType);
					
					String fileExtension = fileType.split("/")[1];
					String imageName = UUID.randomUUID().toString() + "." + fileExtension;
					String fileName = rootLocation + "img/" + imageName;
					image.setFileLocation(fileName);
					
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

}