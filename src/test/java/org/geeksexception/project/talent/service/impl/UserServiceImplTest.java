package org.geeksexception.project.talent.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.geeksexception.project.talent.model.WorkExperience;
import org.junit.Test;

public class UserServiceImplTest {
	
	@Test
	public void testEliminateDuplicateWorkExperiences() {
		List<WorkExperience> workExperiences = new ArrayList<WorkExperience>();
		workExperiences.add(new WorkExperience("Skin White"));
		workExperiences.add(new WorkExperience("Jack Daniels"));
		workExperiences.add(new WorkExperience("Jack Daniels"));
		workExperiences.add(new WorkExperience("Johnny Walker"));
		workExperiences.add(new WorkExperience("Johnny Walker"));
		
		UserServiceImpl userService = new UserServiceImpl();
		userService.eliminateDuplicateWorkExperiences(workExperiences);
		assertEquals(3, workExperiences.size());
	}
	
}