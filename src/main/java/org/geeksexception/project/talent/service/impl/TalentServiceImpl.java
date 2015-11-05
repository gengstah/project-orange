package org.geeksexception.project.talent.service.impl;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.TalentRepository;
import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.model.Image;
import org.geeksexception.project.talent.model.Talent;
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

}