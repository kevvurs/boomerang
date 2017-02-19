package com.boomerang.rnn.service;

import com.boomerang.rnn.dao.GoogleFinance;
import com.boomerang.rnn.data.Window;
import com.boomerang.rnn.util.JsonHandler;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Path("/market")
@Component
public class AppService {
	private static final Logger LOG = Logger.getLogger(AppService.class.getName());
	
	@Autowired
	JsonHandler jsonHandler;
	
	@Autowired
	GoogleFinance googleFinance;
	
	@Value( "${boomerang.app.ping}" )
	String pingStatus;
	
	public AppService() {
		LOG.info("appService instantiated");
	}
	
	@GET
	@Path("/ping")
	@Produces({ MediaType.TEXT_HTML })
	public String ping() {
		return pingStatus;
	}
	
	@GET
	@Path("{symbol}")
	public String stockMarketAnalysis(@PathParam("symbol") String symbol) {
		return pingStatus;
	}
}