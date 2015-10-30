package org.geeksexception.project.talent.dao;

import java.util.List;

import org.geeksexception.project.talent.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {
	
	@Query("SELECT i FROM Image i WHERE i.talent.id = ?1")
	List<Image> findImagesByTalentId(Long id);
	
}