package org.geeksexception.project.talent.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.WorkExperienceRepository;
import org.geeksexception.project.talent.model.WorkExperience;
import org.geeksexception.project.talent.service.WorkExperienceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WorkExperienceServiceImpl implements WorkExperienceService {
	
	private @Inject WorkExperienceRepository workExperienceRepository;
	
	public WorkExperienceServiceImpl() { }
	
	@Override
	public List<WorkExperience> findAll() {
		
		return workExperienceRepository.findAll();
		
	}

}