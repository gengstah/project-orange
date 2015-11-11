package org.geeksexception.project.talent.service;

import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;

public interface TalentEventService {
	
	void addTalentToEvent(Long talentId, Long eventId) throws TalentManagementServiceApiException;
	
}