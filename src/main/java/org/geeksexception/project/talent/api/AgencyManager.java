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

import org.geeksexception.project.talent.model.Agency;
import org.geeksexception.project.talent.service.AgencyService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;

@Path("/agency")
public class AgencyManager {
	
	private @Inject AgencyService agencyService;
	
	public AgencyManager() { }
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/forApproval")
	public List<Agency> findForApprovalAgencies(
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return agencyService.findForApprovalAgencies(page - 1, size);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/denied")
	public List<Agency> findDeniedAgencies(
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return agencyService.findDeniedAgencies(page - 1, size);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/approved")
	public List<Agency> findApprovedAgencies(
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return agencyService.findApprovedAgencies(page - 1, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/approve")
	public void approve(@NotNull(message = "id must not be null") @FormParam("id") Long id) {
		
		agencyService.approveAgency(id);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/deny")
	public void deny(@NotNull(message = "id must not be null") @FormParam("id") Long id, 
			@NotEmpty(message = "admin note must not be empty") @FormParam("adminNote") String adminNote) {
		
		agencyService.denyAgency(id, adminNote);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/setForApproval")
	public void forApproval(@NotNull(message = "id must not be null") @FormParam("id") Long id, 
			@NotEmpty(message = "admin note must not be empty") @FormParam("adminNote") String adminNote) {
		
		agencyService.forApprovalAgency(id, adminNote);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countApprovedAgencies")
	public Integer countApprovedAgencies() {
		
		return agencyService.countApprovedAgencies();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countForApprovalAgencies")
	public Integer countForApprovalAgencies() {
		
		return agencyService.countForApprovalAgencies();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countDeniedAgencies")
	public Integer countDeniedAgencies() {
		
		return agencyService.countDeniedAgencies();
		
	}
	
}