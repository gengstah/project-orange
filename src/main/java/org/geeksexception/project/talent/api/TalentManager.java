package org.geeksexception.project.talent.api;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Talent;
import org.geeksexception.project.talent.service.TalentService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;

@Path("/talent")
public class TalentManager {
	
	private @Inject TalentService talentService;
	
	public TalentManager() { }
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/forApproval")
	public List<Talent> findForApprovalTalents(
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return talentService.findForApprovalTalents(page - 1, size);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/denied")
	public List<Talent> findDeniedTalents(
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return talentService.findDeniedTalents(page - 1, size);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/approved")
	public List<Talent> findApprovedTalents(
			@QueryParam("talentClass") String clazz, 
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		if(clazz != null && !clazz.trim().equals("")) {
			TalentClass talentClass = TalentClass.valueOf(clazz);
			return talentService.findApprovedTalentsByClass(talentClass, page, size);
		} else {
			return talentService.findApprovedTalents(page - 1, size);
		}
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/approve")
	public void approve(@NotNull(message = "id must not be null") @FormParam("id") Long id, 
			@NotEmpty(message = "talent class must not be empty") @FormParam("talentClass") String clazz) throws TalentManagementServiceApiException {
		
		TalentClass talentClass = TalentClass.valueOf(clazz);
		
		talentService.approveTalent(id, talentClass);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/deny")
	public void deny(@NotNull(message = "id must not be null") @FormParam("id") Long id, 
			@NotEmpty(message = "admin note must not be empty") @FormParam("adminNote") String adminNote) throws TalentManagementServiceApiException {
		
		talentService.denyTalent(id, adminNote);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/setForApproval")
	public void forApproval(@NotNull(message = "id must not be null") @FormParam("id") Long id, 
			@NotEmpty(message = "admin note must not be empty") @FormParam("adminNote") String adminNote) throws TalentManagementServiceApiException {
		
		talentService.forApprovalTalent(id, adminNote);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countApprovedTalents")
	public Integer countApprovedTalents() {
		
		return talentService.countApprovedTalents();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countForApprovalTalents")
	public Integer countForApprovalTalents() {
		
		return talentService.countForApprovalTalents();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countDeniedTalents")
	public Integer countDeniedTalents() {
		
		return talentService.countDeniedTalents();
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/apply")
	public void applyToEvent(@NotNull(message = "event id must not be null") @FormParam("eventId") Long eventId) throws TalentManagementServiceApiException {
		
		talentService.applyToEvent(eventId);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/withdraw")
	public void withdrawApplicationFromEvent(@NotNull(message = "event id must not be null") @FormParam("eventId") Long eventId) throws TalentManagementServiceApiException {
		
		talentService.withdrawApplicationFromEvent(eventId);
		
	}
	
}