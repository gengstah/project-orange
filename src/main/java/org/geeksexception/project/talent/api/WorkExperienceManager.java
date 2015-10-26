package org.geeksexception.project.talent.api;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.geeksexception.project.talent.model.WorkExperience;
import org.geeksexception.project.talent.service.WorkExperienceService;
import org.springframework.http.MediaType;

@Path("/exp")
public class WorkExperienceManager {
	
	private @Inject WorkExperienceService workExperienceService;
	
	public WorkExperienceManager() { }
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public List<WorkExperience> findAll() {
		return workExperienceService.findAll();
	}
	
}