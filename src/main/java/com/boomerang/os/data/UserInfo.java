package com.boomerang.os.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "user",
    "friends",
    "conversation"
})

@XmlRootElement(name = "UserInfo")
public class UserInfo {
	protected UserAuth user;
	protected List<String> friends;
	protected String conversation;
	@XmlElement( type = Boolean.class)
	protected Boolean online;
	
	public UserInfo() {
		// Zero Args.
	}
	
	public UserAuth getUser() {
		if (this.user == null) {
			this.user = new UserAuth();
		}
		return this.user;
	}
	
	public void setUser(UserAuth user) {
		this.user = user;
	}
	
	public List<String> getFriends() {
		if (this.friends == null) {
			this.friends = new ArrayList<>();
		}
		return this.friends;
	}
	
	public String getConversation() {
		return this.conversation;
	}
	
	public void setConversation(String conversation) {
		this.conversation = conversation;
	}
	
	public Boolean getOnline() {
		return this.online;
	}
	
	public void setOnline(Boolean online) {
		this.online = online;
	}
	
	@Override
	public int hashCode() {
		return this.user.getUsername().hashCode();
	}
}
