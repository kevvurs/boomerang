package com.boomerang.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.boomerang.data.UserAuth;
import com.boomerang.data.UserInfo;

// Stateful psuedo-DB

@Repository
public class UserData {
	private static final Logger LOG = Logger.getLogger(UserData.class.getName());
	private Collection<UserInfo> userbase;
	
	public UserData() {
		LOG.info("userData instantiated");
		this.userbase = new HashSet<>();
	}
	
	public synchronized boolean pushUser(UserInfo user) {		
		return this.userbase.add(user);
	}
	
	public synchronized boolean registerUser(UserAuth user) {		
		if (!userExists(user.getUsername())) {
			UserInfo userInfo = new UserInfo();
			userInfo.setUser(user);
			userInfo.setConversation(user.getUsername());
			userInfo.getFriends().add(user.getUsername());
			userInfo.setOnline(false);
			this.userbase.add(userInfo);
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized UserInfo fetchUser(String username) {
		if (userExists(username)) {
			UserInfo userInfo = this.userbase.stream()
					.filter((user) -> (user.getUser().getUsername().equals(username)))
					.findAny().get();
			return userInfo;
		} else {
			return null;
		}
	}
	
	public UserInfo signIn(UserAuth user) {
		UserInfo userInfo = fetchUser(user.getUsername());
		if (userInfo != null && validateUser(user, userInfo)) {
			userInfo.setOnline(true);
			return userInfo;
		} else {
			return null;
		}
	}
	
	public synchronized boolean userExists(String username) {
		return this.userbase.parallelStream()
				.anyMatch(user -> user.getUser().getUsername().equals(username));
	}
	
	public boolean validateUser(UserAuth user, UserInfo userInfo) {
		return ((userInfo.getUser().getUsername().equals(user.getUsername())) 
				&& (userInfo.getUser().getPassword().equals(user.getPassword())));
	}
}