package org.geeksexception.project.talent.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.geeksexception.project.talent.model.ReCaptchaResponse;
import org.springframework.http.MediaType;

@Path("/api")
public interface ReCaptchaManager {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/siteverify")
	ReCaptchaResponse verify(@QueryParam("secret") String secret, @QueryParam("response") String response);
	
}