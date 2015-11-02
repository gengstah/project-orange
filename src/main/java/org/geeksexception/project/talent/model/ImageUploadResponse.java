package org.geeksexception.project.talent.model;

import java.util.List;

public class ImageUploadResponse {

	private String error;
	
	private List<String> initialPreview;
	
	private List<ImageUploadInitialPreviewConfig> initialPreviewConfig;
	
	public ImageUploadResponse() { }

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<String> getInitialPreview() {
		return initialPreview;
	}

	public void setInitialPreview(List<String> initialPreview) {
		this.initialPreview = initialPreview;
	}

	public List<ImageUploadInitialPreviewConfig> getInitialPreviewConfig() {
		return initialPreviewConfig;
	}

	public void setInitialPreviewConfig(List<ImageUploadInitialPreviewConfig> initialPreviewConfig) {
		this.initialPreviewConfig = initialPreviewConfig;
	}
	
}