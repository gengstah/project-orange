package org.geeksexception.project.talent.service;

import java.util.List;

import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.model.Talent;

public interface TalentService {
	
	List<Talent> findApprovedTalents(Integer page, Integer size);
	
	List<Talent> findForApprovalTalents(Integer page, Integer size);
	
	List<Talent> findApprovedTalentsByClass(TalentClass talentClass, Integer page, Integer size);
	
	void deleteSavedImage(String fileName, String rootLocation);
	
	Talent save(Talent talent);
	
}