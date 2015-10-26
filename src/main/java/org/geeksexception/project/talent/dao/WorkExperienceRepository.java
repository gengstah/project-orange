package org.geeksexception.project.talent.dao;

import org.geeksexception.project.talent.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
	
}