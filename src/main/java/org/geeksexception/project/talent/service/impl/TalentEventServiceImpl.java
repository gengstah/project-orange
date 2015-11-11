package org.geeksexception.project.talent.service.impl;

import java.util.ArrayList;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.EventRepository;
import org.geeksexception.project.talent.dao.TalentEventRepository;
import org.geeksexception.project.talent.dao.TalentRepository;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.Event;
import org.geeksexception.project.talent.model.Talent;
import org.geeksexception.project.talent.model.TalentEvent;
import org.geeksexception.project.talent.service.TalentEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TalentEventServiceImpl implements TalentEventService {
	
	private @Inject EventRepository eventRepository;
	
	private @Inject TalentRepository talentRepository;
	
	private @Inject TalentEventRepository talentEventRepository;
	
	public TalentEventServiceImpl() { }
	
	@Override
	@Transactional(readOnly = false)
	public void addTalentToEvent(Long talentId, Long eventId) throws TalentManagementServiceApiException {
		
		Talent talent = talentRepository.findOne(talentId);
		checkTalent(talent);
		
		Event event = eventRepository.findOne(eventId);
		checkEvent(event);
		
		TalentEvent talentEvent = new TalentEvent(talent, event);
		talentEventRepository.save(talentEvent);
		
		instantiateTalentEventsIfNull(talent, event);
		
		talent.getTalentEvents().add(talentEvent);
		event.getTalentEvents().add(talentEvent);

	}
	
	private void checkTalent(Talent talent) throws TalentManagementServiceApiException {
		if(talent == null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("talentId", "Talent not found")));
	}
	
	private void checkEvent(Event event) throws TalentManagementServiceApiException {
		if(event == null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("eventId", "Event not found")));
	}
	
	private void instantiateTalentEventsIfNull(Talent talent, Event event) {
		if(talent.getTalentEvents() == null) talent.setTalentEvents(new ArrayList<TalentEvent>());
		if(event.getTalentEvents() == null) event.setTalentEvents(new ArrayList<TalentEvent>());
	}

}