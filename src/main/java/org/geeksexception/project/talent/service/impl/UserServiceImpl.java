package org.geeksexception.project.talent.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.UserRepository;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Errors;
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

}