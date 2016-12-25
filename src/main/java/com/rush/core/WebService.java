package com.rush.core;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Path("/service")
@Component
public class WebService {
	private static final Logger LOG = Logger.getLogger(WebService.class);
	private static final String PING = "OK";
	
	@Value( "${rush.version}" )
	private String appVersion;
	
	@GET
    public String ping() {
        LOG.info("Ping invoked");
        return PING;
    }
}
