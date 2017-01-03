package com.boomerang.core;

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
import com.boomerang.data.Conversation;
import com.boomerang.data.Message;
import com.boomerang.data.UserInfo;
import com.boomerang.util.JsonAgent;

@Path("/conv")
@Component
public class ConversationService {
	private static final Logger LOG = Logger.getLogger(ConversationService.class.getName());
	
	@Autowired
	MessageData messageData;
	
	@Autowired
	UserData userData;
	
	@Autowired
	JsonAgent jsonAgent;
	
	public ConversationService() {
		LOG.info("conversationService instantiated");
	}
	
	@POST
	@Path("/make")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String make(String request) {
		UserInfo user = jsonAgent.deserialize(request, UserInfo.class);
		userData.pushUser(user);
		String response = jsonAgent.serialize(user);
		return response;
	}
	
	@POST
	@Path("/exists")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String exists(String request) {
		Conversation conversation = jsonAgent.deserialize(request, Conversation.class);
		boolean exists = userData.userExists(conversation.getOther());
		return new String("{ \"exists\":" + exists + " }");
	}
	
	@POST
	@Path("/online")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String online(String request) {
		Conversation conversation = jsonAgent.deserialize(request, Conversation.class);
		UserInfo user = userData.fetchUser(conversation.getOther());
		if (user == null) {
			return new String("{ \"online\":false }");
		} else {
			String online = String.valueOf(user.getOnline());
			return new String("{ \"online\":" + online + " }");
		}
	}
	
	@POST
	@Path("/recent")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_JSON)
	public String recent(String request) {
		Conversation conversation = jsonAgent.deserialize(request, Conversation.class);
		Message message = messageData.latestMessage(conversation.getUser(), conversation.getOther());
		if (message == null) {
			return new String("{}");
		} else {
			String response = jsonAgent.serialize(message);
			return response;
		}
	}
}