package org.geeksexception.project.talent.service;

import java.util.List;

import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Event;

public interface EventService {
	
	Event findEvent(Long id);
	
	Event save(Event event) throws TalentManagementServiceApiException;
	
	List<Event> findAllEvents(Integer page, Integer size);
	
	List<Event> findAllEventsOfAgency(Long id, Integer page, Integer size);
	
	List<Event> findAllEventsOfTalent(Long id, Integer page, Integer size);
	
}