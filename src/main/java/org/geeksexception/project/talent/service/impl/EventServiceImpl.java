package org.geeksexception.project.talent.service.impl;

import static org.geeksexception.project.talent.dao.specification.EventSpecification.*;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.EventRepository;
import org.geeksexception.project.talent.dao.specification.criteria.EventCriteria;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	public Long countApprovedEvents() {
		
		return eventRepository.count(eventsMatchingSearchCriteria(new EventCriteria(), EventStatus.APPROVED));
		
	}

	@Override
	public Long countForApprovalEvents() {
		
		return eventRepository.count(eventsMatchingSearchCriteria(new EventCriteria(), EventStatus.FOR_APPROVAL));
		
	}

	@Override
	public Long countDeniedEvents() {
		
		return eventRepository.count(eventsMatchingSearchCriteria(new EventCriteria(), EventStatus.DENIED));
		
	}

	@Override
	public Long countClosedEvents() {
		
		return eventRepository.count(eventsMatchingSearchCriteria(new EventCriteria(), EventStatus.CLOSED));
		
	}

	@Override
	public Long countApprovedEventsByAgency(Long agencyId) {
		
		return eventRepository.count(eventsMatchingSearchCriteria(new EventCriteria(agencyId), EventStatus.APPROVED));
		
	}
	
	@Override
	public Long countForApprovalEventsByAgency(Long agencyId) {

		return eventRepository.count(eventsMatchingSearchCriteria(new EventCriteria(agencyId), EventStatus.FOR_APPROVAL));
		
	}

	@Override
	public Long countDeniedEventsByAgency(Long agencyId) {
		
		return eventRepository.count(eventsMatchingSearchCriteria(new EventCriteria(agencyId), EventStatus.DENIED));
		
	}

	@Override
	public Long countClosedEventsByAgency(Long agencyId) {
		
		return eventRepository.count(eventsMatchingSearchCriteria(new EventCriteria(agencyId), EventStatus.CLOSED));
		
	}

	@Override
	public List<Event> findApprovedEventsOfAgencyNotAppliedByTalent(Long agencyId, Long talentId) {
		
		return eventRepository.findApprovedEventsOfAgencyNotAppliedByTalent(agencyId, talentId);
		
	}

	@Override
	public List<Event> findApprovedEventsOfAgencyAppliedByTalent(Long agencyId, Long talentId) {
		
		return eventRepository.findApprovedEventsOfAgencyAppliedByTalent(agencyId, talentId);
		
	}

	@Override
	public List<Event> searchApprovedEvents(EventCriteria eventCriteria, Integer page, Integer size) {
		
		if(eventCriteria == null) eventCriteria = new EventCriteria();
		List<Event> events = eventRepository.findAll(eventsMatchingSearchCriteria(eventCriteria, EventStatus.APPROVED), new PageRequest(page, size, new Sort(Direction.DESC, "dateCreated"))).getContent();
		
		return events;
		
	}

	@Override
	public List<Event> searchForApprovalEvents(EventCriteria eventCriteria, Integer page, Integer size) {
		
		if(eventCriteria == null) eventCriteria = new EventCriteria();
		List<Event> events = eventRepository.findAll(eventsMatchingSearchCriteria(eventCriteria, EventStatus.FOR_APPROVAL), new PageRequest(page, size, new Sort(Direction.DESC, "dateCreated"))).getContent();
		
		return events;
		
	}

	@Override
	public List<Event> searchDeniedEvents(EventCriteria eventCriteria, Integer page, Integer size) {
		
		if(eventCriteria == null) eventCriteria = new EventCriteria();
		List<Event> events = eventRepository.findAll(eventsMatchingSearchCriteria(eventCriteria, EventStatus.DENIED), new PageRequest(page, size, new Sort(Direction.DESC, "dateCreated"))).getContent();
		
		return events;
		
	}

	@Override
	public List<Event> searchClosedEvents(EventCriteria eventCriteria, Integer page, Integer size) {
		
		if(eventCriteria == null) eventCriteria = new EventCriteria();
		List<Event> events = eventRepository.findAll(eventsMatchingSearchCriteria(eventCriteria, EventStatus.CLOSED), new PageRequest(page, size, new Sort(Direction.DESC, "dateCreated"))).getContent();
		
		return events;
		
	}

}