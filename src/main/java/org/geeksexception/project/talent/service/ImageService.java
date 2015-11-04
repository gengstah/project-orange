package org.geeksexception.project.talent.service;

import org.geeksexception.project.talent.model.Image;

public interface ImageService {
	
	Image findImageByFileLocation(String fileLocation);
	
}