package com.boomerang.os.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boomerang.os.data.UserAuth;
import com.boomerang.os.data.UserInfo;
import com.boomerang.os.util.JsonAgent;
import com.boomerang.os.util.UserException;

@Path("/user")
@Component
public class AccountService {
	
	@GET
	@Path("/ping")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String logout(String request) throws UserException {
		return "OK";
	}
} 
