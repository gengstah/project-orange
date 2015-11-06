package org.geeksexception.project.talent.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.EventRepository;
import org.geeksexception.project.talent.dao.TalentEventRepository;
import org.geeksexception.project.talent.dao.TalentRepository;
import org.geeksexception.project.talent.enums.EventStatus;
import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.enums.TalentStatus;
import org.geeksexception.project.talent.enums.UserRole;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.Event;
import org.geeksexception.project.talent.model.Image;
import org.geeksexception.project.talent.model.Talent;
import org.geeksexception.project.talent.model.TalentEvent;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.service.ImageService;
import org.geeksexception.project.talent.service.TalentService;
import org.geeksexception.project.talent.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TalentServiceImpl implements TalentService {
	
	private @Inject TalentRepository talentRepository;
	
	private @Inject EventRepository eventRepository;
	
	private @Inject TalentEventRepository talentEventRepository;
	
	private @Inject ImageService imageService;
	
	private @Inject UserService userService;
	
	public TalentServiceImpl() { }
	
	@Override
	public List<Talent> findApprovedTalents(Integer page, Integer size) {
		
		return talentRepository.findApprovedTalents(new PageRequest(page, size));
		
	}

	@Override
	public List<Talent> findForApprovalTalents(Integer page, Integer size) {
		
		return talentRepository.findForApprovalTalents(new PageRequest(page, size));
		
	}

	@Override
	public List<Talent> findDeniedTalents(Integer page, Integer size) {
		
		return talentRepository.findDeniedTalents(new PageRequest(page, size));
		
	}
	
	@Override
	public List<Talent> findApprovedTalentsByClass(TalentClass talentClass, Integer page, Integer size) {
		
		return talentRepository.findApprovedTalentsByClass(talentClass, new PageRequest(page, size));
		
	}

	@Override
	@Transactional(readOnly = false)
	public Talent save(Talent talent) {
		
		return talentRepository.save(talent);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSavedImage(String fileName, String rootLocation) {
		
		String fileLocation = "/img/talents/" + fileName;
		Image image = imageService.findImageByFileLocation(fileLocation);
		User user = userService.getLoggedInUser();
		
		if(currentUserOwnsImage(user, image)) {
			
			image.setTalent(null);
			user.getTalent().getImages().remove(image);
			imageService.delete(image);
			save(user.getTalent());
			File imageFile = new File(rootLocation + "img/talents/" + fileName);
			if(imageFile.exists()) imageFile.delete();
			
			File imageThumbnailFile = new File(rootLocation + "img/talents/thumbnails/" + fileName);
			if(imageThumbnailFile.exists()) imageThumbnailFile.delete();
			
		}
		
	}
	
	private boolean currentUserOwnsImage(User user, Image image) {
		return user.getTalent().equals(image.getTalent());
	}

	private void checkTalent(Talent talent) throws TalentManagementServiceApiException {
		if(talent == null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("talentId", "Talent not found")));
	}
	
	@Override
	@Transactional(readOnly = false)
	public void approveTalent(Long id, TalentClass talentClass) throws TalentManagementServiceApiException {
		
		Talent talent = talentRepository.findOne(id);
		checkTalent(talent);
		
		talent.setStatus(TalentStatus.APPROVED);
		talent.setTalentClass(talentClass);
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public void denyTalent(Long id, String adminNote) throws TalentManagementServiceApiException {
		
		Talent talent = talentRepository.findOne(id);
		checkTalent(talent);
		
		talent.setStatus(TalentStatus.DENIED);
		talent.setAdminNote(adminNote);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void forApprovalTalent(Long id, String adminNote) throws TalentManagementServiceApiException {
		
		Talent talent = talentRepository.findOne(id);
		checkTalent(talent);
		
		talent.setStatus(TalentStatus.FOR_APPROVAL);
		talent.setAdminNote(adminNote);
		
	}

	@Override
	public Integer countApprovedTalents() {
		
		return talentRepository.countTalentsByStatus(TalentStatus.APPROVED);
		
	}

	@Override
	public Integer countForApprovalTalents() {
		
		return talentRepository.countTalentsByStatus(TalentStatus.FOR_APPROVAL);
		
	}

	@Override
	public Integer countDeniedTalents() {
		
		return talentRepository.countTalentsByStatus(TalentStatus.DENIED);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void applyToEvent(Long id) throws TalentManagementServiceApiException {
		
		User user = userService.getLoggedInUser();
		checkIfTalentUser(user);
			
		Event event = eventRepository.findOne(id);
		checkIfEventExists(event);
		
		TalentEvent talentEvent = new TalentEvent(user.getTalent(), event);
		talentEventRepository.save(talentEvent);
		
		instantiateTalentEventsIfNull(user, event);
		
		user.getTalent().getTalentEvents().add(talentEvent);
		event.getTalentEvents().add(talentEvent);
		
	}

	private void instantiateTalentEventsIfNull(User user, Event event) {
		if(user.getTalent().getTalentEvents() == null) user.getTalent().setTalentEvents(new ArrayList<TalentEvent>());
		if(event.getTalentEvents() == null) event.setTalentEvents(new ArrayList<TalentEvent>());
	}

	@Override
	@Transactional(readOnly = false)
	public void withdrawApplicationFromEvent(Long id) throws TalentManagementServiceApiException {
		
		User user = userService.getLoggedInUser();
		checkIfTalentUser(user);
		
		Event event = eventRepository.findOne(id);
		checkIfEventExists(event);
		
		TalentEvent talentEvent = talentEventRepository.findTalentEventByTalentAndEvent(user.getTalent().getId(), event.getId());
		checkIfTalentAppliedToEvent(talentEvent);
		
		user.getTalent().getTalentEvents().remove(talentEvent);
		event.getTalentEvents().remove(talentEvent);
		
		talentEventRepository.delete(talentEvent);
		
	}
	
	private void checkIfTalentUser(User user) throws TalentManagementServiceApiException {
		if(user.getUserRole() != UserRole.ROLE_USER) 
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("eventId", "You must not apply to events")));
	}
	
	private void checkIfEventExists(Event event) throws TalentManagementServiceApiException {
		if(event == null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("event", "Event does not exist")));
		
		if(event.getStatus() != EventStatus.APPROVED)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("event", "Event is not approved")));
	}
	
	private void checkIfTalentAppliedToEvent(TalentEvent talentEvent) throws TalentManagementServiceApiException {
		if(talentEvent == null)
			throw new TalentManagementServiceApiException("Error!", 
					new Errors().addError(new Error("eventId", "You did not applied to this event")));
	}

}