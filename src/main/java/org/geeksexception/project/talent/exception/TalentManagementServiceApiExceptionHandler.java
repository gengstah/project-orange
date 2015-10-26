package org.geeksexception.project.talent.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class TalentManagementServiceApiExceptionHandler implements 
		ExceptionMapper<TalentManagementServiceApiException> {
	
	public Response toResponse(TalentManagementServiceApiException exception) {
		Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
		
        return Response.status(status).entity(exception)
        		.header("X-TalentManagementServiceApi-Exception", exception)
        		.build();
	}
	
}