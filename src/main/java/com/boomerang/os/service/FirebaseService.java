package com.boomerang.os.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Collection;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boomerang.os.dao.FirebaseDAO;
import com.boomerang.os.data.FirebaseConfig;
import com.boomerang.os.util.JsonAgent;
import com.boomerang.os.security.FirebaseConfigFactory;

@Path("/firebase")
@Component
public class FirebaseService {
	private static final Logger LOG = Logger.getLogger(FirebaseService.class.getName());

	@Autowired
	JsonAgent jsonAgent;
	
	@Autowired
	FirebaseConfigFactory firebaseConfigFactory;

	@Autowired
	FirebaseDAO firebaseDAO;
	
	public FirebaseService() {
		LOG.info("firebaseService instantiated");
	}

	@GET
	@Path("/config")
	@Produces({ MediaType.APPLICATION_JSON })
	public String register() {
	  FirebaseConfig config = firebaseConfigFactory.mkConfig();
	  String response = jsonAgent.serialize(config);
	  return response;
	}
	
	@GET
	@Path("/apps")
	@Produces({ MediaType.APPLICATION_JSON })
	public String listApps() {
	  Collection<String> appKeys = firebaseDAO.getAppKeys();
	  String response = jsonAgent.serialize(appKeys);
	  return response;
	}
} 
