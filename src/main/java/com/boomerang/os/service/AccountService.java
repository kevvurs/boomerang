package com.boomerang.os.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boomerang.os.dao.UserData;
import com.boomerang.os.data.UserAuth;
import com.boomerang.os.data.UserInfo;
import com.boomerang.os.util.JsonAgent;
import com.boomerang.os.util.UserException;

@Path("/user")
@Component
public class AccountService {
	private static final Logger LOG = Logger.getLogger(AccountService.class.getName());
	
	@Autowired
	UserData userData;
	
	@Autowired
	JsonAgent jsonAgent;
	
	public AccountService() {
		LOG.info("loginService instantiated");
	}
	
	@POST
	@Path("/register")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String register(String request) throws UserException {
		UserAuth user = jsonAgent.deserialize(request, UserAuth.class);
		if (userData.registerUser(user)) {
			UserInfo userInfo = userData.signIn(user);
			String response = jsonAgent.serialize(userInfo);
			return response;
		} else {
			throw new UserException("incorrect username or password");
		}
	}

	@POST
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String login(String request) throws UserException {
		UserAuth user = jsonAgent.deserialize(request, UserAuth.class);
		UserInfo userInfo = userData.signIn(user);
		if (userInfo != null) {
			String response = jsonAgent.serialize(userInfo);
			return response;
		} else {
			throw new UserException("incorrect username or password");
		}
	}
	
	@POST
	@Path("/logout")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String logout(String request) throws UserException {
		UserInfo userInfo = jsonAgent.deserialize(request, UserInfo.class);
		userInfo.setOnline(false);
		userData.pushUser(userInfo);
		return "{}";
	}
} 
