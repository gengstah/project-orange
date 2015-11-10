package org.geeksexception.project.talent.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.EventRepository;
import org.geeksexception.project.talent.enums.UserRole;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.Event;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.service.EventService;
import org.geeksexception.project.talent.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
	
	private @Inject EventRepository eventRepository;
	
	private @Inject UserService userService;
	
	public EventServiceImpl() { }
	
	@Override
	public Event findEvent(Long id) {
		
		return eventRepository.findOne(id);
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public Event save(Event event) throws TalentManagementServiceApiException {
		
		User user = userService.getLoggedInUser();
		
		if(user == null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("user", "User has no session")));
			
		if(user.getUserRole() != UserRole.ROLE_AGENCY)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("user", "You cannot create an event")));
		
		event.setAgency(user.getAgency());
		return eventRepository.save(event);
		
	}

	@Override
	public List<Event> findAllEvents(Integer page, Integer size) {
		
		return eventRepository.findAllEvents(new PageRequest(page, size));
		
	}

	@Override
	public List<Event> findAllEventsOfAgency(Long id, Integer page, Integer size) {
		
		return eventRepository.findAllEventsOfAgency(id, new PageRequest(page, size));
		
	}

	@Override
	public List<Event> findAllEventsOfTalent(Long id, Integer page, Integer size) {
		
		return eventRepository.findAllEventsOfTalent(id, new PageRequest(page, size));
		
	}

}