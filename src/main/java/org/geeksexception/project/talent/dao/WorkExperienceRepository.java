package org.geeksexception.project.talent.dao;

import java.util.List;

import org.geeksexception.project.talent.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
	
	@Query("SELECT we FROM WorkExperience we WHERE UPPER(we.name) = UPPER(?1)")
	WorkExperience findWorkExperienceByName(String name);
	
	@Query("SELECT we FROM WorkExperience we JOIN we.talents t WHERE t.id = ?1")
	List<WorkExperience> findWorkExperienceByTalentId(Long id);
	
}