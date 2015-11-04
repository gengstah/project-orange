package org.geeksexception.project.talent.service.impl;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.AgencyRepository;
import org.geeksexception.project.talent.service.AgencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AgencyServiceImpl implements AgencyService {
	
	private @Inject AgencyRepository agencyRepository;
	
	public AgencyServiceImpl() { }
	
	

}