package org.geeksexception.project.talent.service;

import java.util.List;

import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.User;

public interface UserService {
	
	User findUserById(Long id);
	
	User findUserByEmail(String email);
	
	User save(User user, String imageTempLocation, String reCaptchaResponse) throws TalentManagementServiceApiException;
	
	User update(User user, String imageTempLocation) throws TalentManagementServiceApiException;
	
	User changePassword(String oldPassword, String newPassword) throws TalentManagementServiceApiException;
	
	User getLoggedInUser();
	
	User getFullProfile(String email);
	
	List<User> findTalentUsers(Integer page, Integer size);
	
	List<User> findAgencyUsers(Integer page, Integer size);
	
}