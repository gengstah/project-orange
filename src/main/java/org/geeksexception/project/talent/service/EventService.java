package org.geeksexception.project.talent.service;

import java.math.BigDecimal;
import java.util.List;

import org.geeksexception.project.talent.dao.specification.criteria.EventCriteria;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Event;

public interface EventService {
	
	Event findEvent(Long id);
	
	Event save(Event event) throws TalentManagementServiceApiException;
	
	List<Event> findAllEvents(Integer page, Integer size);
	
	List<Event> findAllApprovedEvents(Integer page, Integer size);
	
	List<Event> findAllEventsOfAgency(Long id, Integer page, Integer size);
	
	List<Event> findAllApprovedEventsOfAgency(Long id, Integer page, Integer size);
	
	List<Event> findAllEventsOfTalent(Long id, Integer page, Integer size);
	
	List<Event> searchApprovedEvents(EventCriteria eventCriteria, Integer page, Integer size);
	
	List<Event> searchForApprovalEvents(EventCriteria eventCriteria, Integer page, Integer size);
	
	List<Event> searchDeniedEvents(EventCriteria eventCriteria, Integer page, Integer size);
	
	List<Event> searchClosedEvents(EventCriteria eventCriteria, Integer page, Integer size);
	
	void approveEvent(Long id, BigDecimal actualTalentFee) throws TalentManagementServiceApiException;
	
	void denyEvent(Long id, String adminNote) throws TalentManagementServiceApiException;
	
	void forApprovalEvent(Long id, String adminNote) throws TalentManagementServiceApiException;
	
	Long countApprovedEvents();
	
	Long countForApprovalEvents();
	
	Long countDeniedEvents();
	
	Long countClosedEvents();
	
	Long countApprovedEventsByAgency(Long agencyId);
	
	Long countForApprovalEventsByAgency(Long agencyId);
	
	Long countDeniedEventsByAgency(Long agencyId);
	
	Long countClosedEventsByAgency(Long agencyId);
	
	List<Event> findApprovedEventsOfAgencyNotAppliedByTalent(Long agencyId, Long talentId);
	
	List<Event> findApprovedEventsOfAgencyAppliedByTalent(Long agencyId, Long talentId);
	
}