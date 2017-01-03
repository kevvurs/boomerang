package com.boomerang.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.boomerang.data.Message;
import com.boomerang.data.MessageChronology;

@Repository
public class MessageData {
	private static final Logger LOG = Logger.getLogger(MessageData.class.getName());
	private Collection<Message> messages;
	
	public MessageData() {
		LOG.info("messageData instantiated");
		this.messages = new ArrayList<>();
	}
	
	public synchronized void sendMessage(Message message) {
		this.messages.add(message);
	}
	
	public synchronized List<Message> fetchMessages(String user, String other) {
		this.messages.stream().filter((message) -> filterThread(user, other, message))
			.forEach( message -> message.setRead(true) );
		List<Message> allMessages = this.messages.parallelStream()
				.filter((message) -> filterThread(user, other, message))
				.sorted(new MessageChronology())
				.collect(Collectors.toList());
		return allMessages;
	}
	
	public synchronized List<Message> refreshMessages(String user, String other) {
		List<Message> freshMessages = this.messages.parallelStream()
				.filter((message) -> filterThread(user, other, message))
				.filter((message) -> (!message.getRead()))
				.sorted(new MessageChronology())
				.collect(Collectors.toList());
		this.messages.stream().filter((message) -> filterThread(user, other, message))
			.filter((message) -> (!message.getRead()))
			.forEach( message -> message.setRead(true) );
		return freshMessages;
	}
	
	public Message latestMessage(String user, String other) {
		Optional<Message> lastMessage = this.messages.stream()
				.filter((message) -> filterThread(user, other, message))
				.max(new MessageChronology());
		if (lastMessage.isPresent()) {
			return lastMessage.get();
		} else return null;
	}
	
	private boolean filterThread(String user1, String user2, Message message) {
		if (user1 != null && user2 != null && message != null) {
			boolean sentFrom = (message.getAuthor().getUsername().equals(user1) && message.getRecipient().equals(user2));
			boolean sentTo = (message.getAuthor().getUsername().equals(user2) && message.getRecipient().equals(user1));
			return (sentTo || sentFrom);
		} else {
			return false;
		}
	}
}