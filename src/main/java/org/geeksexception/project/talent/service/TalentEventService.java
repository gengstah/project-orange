package org.geeksexception.project.talent.service;

import java.util.List;

import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.TalentEvent;

public interface TalentEventService {
	
	void addTalentToEvent(Long talentId, Long eventId) throws TalentManagementServiceApiException;
	
	void removeTalentFromEvent(Long talentId, Long eventId) throws TalentManagementServiceApiException;
	
	TalentEvent findTalentEventByTalentAndEvent(Long talentId, Long eventId);
	
	List<TalentEvent> findAllTalentEventByEventId(Long eventId);
	
	List<TalentEvent> findAllTalentEventByTalentId(Long talentId);
	
}