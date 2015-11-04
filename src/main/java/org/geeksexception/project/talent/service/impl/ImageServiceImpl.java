package org.geeksexception.project.talent.service.impl;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.ImageRepository;
import org.geeksexception.project.talent.model.Image;
import org.geeksexception.project.talent.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {
	
	private @Inject ImageRepository imageRepository;
	
	public ImageServiceImpl() { }
	
	@Override
	public Image findImageByFileLocation(String fileLocation) {
		
		return imageRepository.findImageByFileLocation(fileLocation);
		
	}

}