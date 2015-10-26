package org.geeksexception.project.talent.exception;

import org.geeksexception.project.talent.model.Errors;

public class TalentManagementServiceApiException extends Exception {
	
	private static final long serialVersionUID = 983066496162295401L;
	
	private Errors detail;
	
	public TalentManagementServiceApiException() { }
	
	public TalentManagementServiceApiException(String message, Errors detail) {
		super(message);
		this.detail = detail;
	}
	
	public TalentManagementServiceApiException(String message, Errors detail, Throwable cause) {
		super(message, cause);
		this.detail = detail;
	}
	
	public Errors getFaultInfo() { return detail; }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return detail.toString();
	}
	
}