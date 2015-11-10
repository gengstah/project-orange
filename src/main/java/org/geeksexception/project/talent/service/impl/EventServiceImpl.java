package org.geeksexception.project.talent.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.EventRepository;
import org.geeksexception.project.talent.model.Event;
import org.geeksexception.project.talent.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
	
	private @Inject EventRepository eventRepository;
	
	public EventServiceImpl() { }
	
	@Override
	public Event findEvent(Long id) {
		
		return eventRepository.findOne(id);
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public Event save(Event event) {
		
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