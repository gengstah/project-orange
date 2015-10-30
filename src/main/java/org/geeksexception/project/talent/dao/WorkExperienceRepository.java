package org.geeksexception.project.talent.dao;

import org.geeksexception.project.talent.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
	
	@Query("SELECT we FROM WorkExperience we WHERE UPPER(we.name) = UPPER(?1)")
	WorkExperience findWorkExperienceByName(String name);
	
}