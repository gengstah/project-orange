package org.geeksexception.project.talent.service;

import java.util.List;

import org.geeksexception.project.talent.model.WorkExperience;

public interface WorkExperienceService {
	
	WorkExperience save(WorkExperience workExperience);
	
	List<WorkExperience> findAll();
	
	WorkExperience findWorkExperienceByName(String name);
	
}