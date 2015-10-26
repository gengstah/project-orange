package org.geeksexception.project.talent.dao;

import java.util.List;

import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.model.Talent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TalentRepository extends JpaRepository<Talent, Long> {
	
	@Query("SELECT t FROM Talent t WHERE t.status = 'APPROVED'")
	List<Talent> findApprovedTalents(Pageable pageable);
	
	@Query("SELECT t FROM Talent t WHERE t.status = 'FOR_APPROVAL'")
	List<Talent> findForApprovalTalents(Pageable pageable);
	
	@Query("SELECT t FROM Talent t WHERE t.status = 'APPROVED' AND t.talentClass = ?1")
	List<Talent> findApprovedTalentsByClass(TalentClass talentClass, Pageable pageable);
	
}