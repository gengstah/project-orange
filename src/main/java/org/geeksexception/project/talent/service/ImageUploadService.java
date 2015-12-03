package org.geeksexception.project.talent.service;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.ImageUploadResponse;

public interface ImageUploadService {
	
	ImageUploadResponse uploadImage(Attachment attachment, String sessionId, String tempLocation) throws TalentManagementServiceApiException;
	
	void deleteImage(String sessionId, String fileName);
	
}