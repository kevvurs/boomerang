package com.boomerang.core;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Path("/diag")
@Component
public class DiagService {
	private static final Logger LOG = Logger.getLogger(DiagService.class.getName());
	
	public DiagService() {
		LOG.info("diagService instantiated");
	}
	
	@Value( "${boomerang.app.ping}" )
	private String appPing;
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN })
	public String ping() {
		LOG.info("Ping invoked");
		return appPing;
	}
}
