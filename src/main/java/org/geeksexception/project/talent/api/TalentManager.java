package org.geeksexception.project.talent.api;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.model.Talent;
import org.geeksexception.project.talent.service.TalentService;
import org.springframework.http.MediaType;

@Path("/talent")
public class TalentManager {
	
	private @Inject TalentService talentService;
	
	public TalentManager() { }
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/approved")
	public List<Talent> findApprovedTalents(@QueryParam("page") @Min(1) Integer page, @QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return talentService.findApprovedTalents(page, size);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/for_approval")
	public List<Talent> findForApprovalTalents(@QueryParam("page") @Min(1) Integer page, @QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return talentService.findForApprovalTalents(page, size);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/approved/{talentClass}")
	public List<Talent> findApprovedTalentsByClass(
			@NotNull @PathParam("talentClass") String clazz, 
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		TalentClass talentClass = TalentClass.valueOf(clazz);
		
		if(talentClass == null) return talentService.findApprovedTalents(page, size);
		else return talentService.findApprovedTalentsByClass(talentClass, page, size);
		
	}
	
}