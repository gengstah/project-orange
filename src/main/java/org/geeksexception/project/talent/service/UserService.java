package org.geeksexception.project.talent.service;

import java.util.List;

import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.User;

public interface UserService {
	
	User findUserById(Long id);
	
	User findUserByEmail(String email);
	
	User save(User user) throws TalentManagementServiceApiException;
	
	User getLoggedInUser();
	
	List<User> findTalentUsers(Integer page, Integer size);
	
	List<User> findAgencyUsers(Integer page, Integer size);
	
}