package org.geeksexception.project.talent.service.impl;

import javax.inject.Inject;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.ImageUploadResponse;
import org.geeksexception.project.talent.service.ImageUploadService;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {
	
	private @Inject AmazonS3 s3Client;
	
	public ImageUploadServiceImpl() { }

	@Override
	public ImageUploadResponse uploadImage(Attachment attachment, String sessionId, String tempLocation)
			throws TalentManagementServiceApiException {
		
		
		
		return null;
	}
	
	/*@Override
	public ImageUploadResponse uploadImage(Attachment attachment, String sessionId, String tempLocation)
			throws TalentManagementServiceApiException {
		
		DataHandler handler = attachment.getDataHandler();
		String absolutePath = tempLocation + sessionId;
		
		ImageUploadResponse response = null;
		File newTempFolder = new File(absolutePath);
		if(newTempFolder.exists() || newTempFolder.mkdir()) {
			String fileNameExtension = handler.getContentType().split("/")[1];
			String fileName = UUID.randomUUID().toString() + "." + fileNameExtension;
			
			File newImage = new File(absolutePath + "/" + fileName);
			OutputStream outputStream = null;
			BufferedOutputStream writer = null;
			try {
				newImage.createNewFile();
				outputStream = new FileOutputStream(newImage);
				writer = new BufferedOutputStream(outputStream);
				writer.write(IOUtils.readBytesFromStream(handler.getInputStream()));
				response = new ImageUploadResponse();
				
				List<String> initialPreview = new ArrayList<String>();
				initialPreview.add("<img src='/img/temp/" + sessionId + "/" + fileName + "' class='file-preview-image' alt='" + fileName + "' title='" + fileName + "'>");
				
				ImageUploadInitialPreviewConfig imageUploadInitialPreviewConfig = new ImageUploadInitialPreviewConfig();
				imageUploadInitialPreviewConfig.setUrl("/api/service/user/deleteImage/" + fileName);
				List<ImageUploadInitialPreviewConfig> imageUploadInitialPreviewConfigs = new ArrayList<ImageUploadInitialPreviewConfig>();
				imageUploadInitialPreviewConfigs.add(imageUploadInitialPreviewConfig);
				
				response.setInitialPreview(initialPreview);
				response.setInitialPreviewConfig(imageUploadInitialPreviewConfigs);
			} catch (IOException e) {
				throw new TalentManagementServiceApiException(
						"Unable to upload image", 
						new Errors()
							.addError(new Error("image", "Unable to upload image")));
			} finally {
				try {
					if(writer != null) {
						writer.flush();
						writer.close();
					}
					
					if(outputStream != null) {
						outputStream.flush();
						outputStream.close();
					}
				} catch(IOException e) {  }
			}
		}
		
		return response;
		
	}*/

}