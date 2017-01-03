package com.boomerang.core;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boomerang.dao.MessageData;
import com.boomerang.dao.UserData;
import com.boomerang.data.Message;
import com.boomerang.data.UserAuth;
import com.boomerang.data.UserInfo;
import com.boomerang.util.JsonAgent;

@Path("/message")
@Component
public class MessagingService {
	private static final Logger LOG = Logger.getLogger(MessagingService.class.getName());
	//private static final Long ORIGIN = System.nanoTime();
	//private static final Long DAY = 86400000000000l;
	
	@Autowired
	MessageData messageData;
	
	@Autowired
	UserData userData;
	
	@Autowired
	JsonAgent jsonAgent;
	
	public MessagingService() {
		LOG.info("messagingService instantiated");
	}
	
	@POST
	@Path("/send")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String send(String request) {
		Message message = jsonAgent.deserialize(request, Message.class);
		message.setTimestamp(System.nanoTime());
		message.setRead(false);
		if (canSend(message.getAuthor(), message.getRecipient())) {
			message.getAuthor().setPassword("");
			messageData.sendMessage(message);
			message.setSent(true);
		} else {
			message.setSent(false);
		}
		String response = jsonAgent.serialize(message);
		return response;
	}
	
	@POST
	@Path("/reload")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String reload(String request) {
		UserInfo userInfo = jsonAgent.deserialize(request, UserInfo.class);
		if (canSend(userInfo.getUser(), userInfo.getConversation())) {
			List<Message> thread = messageData.fetchMessages(userInfo.getUser().getUsername(), userInfo.getConversation());
			String response = jsonAgent.serialize(thread);
			return response;
		} else {
			return new String("{}");
		}
	}
	
	@POST
	@Path("/refresh")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String refresh(String request) {
		UserInfo userInfo = jsonAgent.deserialize(request, UserInfo.class);
		List<Message> thread = messageData.refreshMessages(userInfo.getUser().getUsername(), userInfo.getConversation());
		String response = jsonAgent.serialize(thread);
		return response;
	}
	
	private boolean canSend(UserAuth user, String other) {
		return ((userData.signIn(user) != null) && userData.userExists(other));
	}
}