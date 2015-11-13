package org.geeksexception.project.talent.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.EventRepository;
import org.geeksexception.project.talent.enums.EventStatus;
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
	public List<Event> findAllApprovedEvents(Integer page, Integer size) {
		
		return eventRepository.findAllApprovedEvents(new PageRequest(page, size));
		
	}

	@Override
	public List<Event> findAllEventsOfAgency(Long id, Integer page, Integer size) {
		
		return eventRepository.findAllEventsOfAgency(id, new PageRequest(page, size));
		
	}
	
	@Override
	public List<Event> findAllApprovedEventsOfAgency(Long id, Integer page, Integer size) {
		
		return eventRepository.findAllApprovedEventsOfAgency(id, new PageRequest(page, size));
		
	}

	@Override
	public List<Event> findAllEventsOfTalent(Long id, Integer page, Integer size) {
		
		return eventRepository.findAllEventsOfTalent(id, new PageRequest(page, size));
		
	}
	
	private void checkEvent(Event event) throws TalentManagementServiceApiException {
		if(event == null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("eventId", "Event not found")));
	}

	@Override
	@Transactional(readOnly = false)
	public void approveEvent(Long id, BigDecimal actualTalentFee) throws TalentManagementServiceApiException {
		
		Event event = eventRepository.findOne(id);
		checkEvent(event);
		
		event.setActualTalentFee(actualTalentFee);
		event.setStatus(EventStatus.APPROVED);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void denyEvent(Long id, String adminNote) throws TalentManagementServiceApiException {
		
		Event event = eventRepository.findOne(id);
		checkEvent(event);
		
		event.setAdminNote(adminNote);
		event.setStatus(EventStatus.DENIED);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void forApprovalEvent(Long id, String adminNote) throws TalentManagementServiceApiException {
		
		Event event = eventRepository.findOne(id);
		checkEvent(event);
		
		event.setAdminNote(adminNote);
		event.setStatus(EventStatus.FOR_APPROVAL);
		
	}

	@Override
	public Integer countApprovedEvents() {
		
		return eventRepository.countEventsByStatus(EventStatus.APPROVED);
		
	}

	@Override
	public Integer countForApprovalEvents() {
		
		return eventRepository.countEventsByStatus(EventStatus.FOR_APPROVAL);
		
	}

	@Override
	public Integer countDeniedEvents() {
		
		return eventRepository.countEventsByStatus(EventStatus.DENIED);
		
	}

	@Override
	public Integer countClosedEvents() {
		
		return eventRepository.countEventsByStatus(EventStatus.CLOSED);
		
	}

	@Override
	public Integer countApprovedEventsByAgency(Long agencyId) {
		
		return eventRepository.countEventsByStatusAndAgency(EventStatus.APPROVED, agencyId);
		
	}

	@Override
	public List<Event> findApprovedEventsOfAgencyNotAppliedByTalent(Long agencyId, Long talentId) {
		
		return eventRepository.findApprovedEventsOfAgencyNotAppliedByTalent(agencyId, talentId);
		
	}

	@Override
	public List<Event> findApprovedEventsOfAgencyAppliedByTalent(Long agencyId, Long talentId) {
		
		return eventRepository.findApprovedEventsOfAgencyAppliedByTalent(agencyId, talentId);
		
	}

}