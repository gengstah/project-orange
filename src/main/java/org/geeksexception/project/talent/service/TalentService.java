package org.geeksexception.project.talent.service;

import java.util.List;

import org.geeksexception.project.talent.dao.specification.criteria.TalentCriteria;
import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Talent;

public interface TalentService {
	
	List<Talent> findApprovedTalents(Integer page, Integer size);
	
	Integer countApprovedTalents();
	
	List<Talent> findForApprovalTalents(Integer page, Integer size);
	
	Integer countForApprovalTalents();
	
	List<Talent> findDeniedTalents(Integer page, Integer size);
	
	Integer countDeniedTalents();
	
	List<Talent> findApprovedTalentsByClass(TalentClass talentClass, Integer page, Integer size);
	
	List<Talent> searchApprovedTalents(TalentCriteria talentCriteria, Integer page, Integer size);
	
	List<Talent> searchForApprovalTalents(TalentCriteria talentCriteria, Integer page, Integer size);
	
	List<Talent> searchDeniedTalents(TalentCriteria talentCriteria, Integer page, Integer size);
	
	void deleteSavedImage(String fileName, String rootLocation);
	
	Talent save(Talent talent);
	
	void approveTalent(Long id, TalentClass talentClass) throws TalentManagementServiceApiException;
	
	void denyTalent(Long id, String adminNote) throws TalentManagementServiceApiException;
	
	void forApprovalTalent(Long id, String adminNote) throws TalentManagementServiceApiException;
	
	void applyToEvent(Long id) throws TalentManagementServiceApiException;
	
	void withdrawApplicationFromEvent(Long id) throws TalentManagementServiceApiException;
	
}