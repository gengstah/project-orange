package org.geeksexception.project.talent.enums;

public enum UserRole {
	ROLE_ADMIN("Admin"), ROLE_AGENCY("Agency"), ROLE_USER("User");
	
	private String description;
	
	private UserRole(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}