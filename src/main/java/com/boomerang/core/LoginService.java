package com.boomerang.core;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boomerang.dao.UserData;
import com.boomerang.data.UserAuth;
import com.boomerang.data.UserInfo;
import com.boomerang.util.JsonAgent;
import com.boomerang.util.UserException;

@Path("/user")
@Component
public class LoginService {
	private static final Logger LOG = Logger.getLogger(LoginService.class.getName());
	
	@Autowired
	UserData userData;
	
	@Autowired
	JsonAgent jsonAgent;
	
	public LoginService() {
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
}
