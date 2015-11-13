package org.geeksexception.project.talent.service;

import java.math.BigDecimal;
import java.util.List;

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
	
	void approveEvent(Long id, BigDecimal actualTalentFee) throws TalentManagementServiceApiException;
	
	void denyEvent(Long id, String adminNote) throws TalentManagementServiceApiException;
	
	void forApprovalEvent(Long id, String adminNote) throws TalentManagementServiceApiException;
	
	Integer countApprovedEvents();
	
	Integer countForApprovalEvents();
	
	Integer countDeniedEvents();
	
	Integer countClosedEvents();
	
	Integer countApprovedEventsByAgency(Long agencyId);
	
	List<Event> findApprovedEventsOfAgencyNotAppliedByTalent(Long agencyId, Long talentId);
	
	List<Event> findApprovedEventsOfAgencyAppliedByTalent(Long agencyId, Long talentId);
	
}