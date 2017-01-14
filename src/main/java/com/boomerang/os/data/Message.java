package com.boomerang.os.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "author",
    "recipient",
    "content",
    "sent",
    "read",
    "timestamp"
})

@XmlRootElement(name = "Message")
public class Message {
	protected UserAuth author;
    protected String recipient;
	protected String content;
	@XmlElement(type = Boolean.class)
	protected Boolean sent;
	@XmlElement(type = Boolean.class)
	protected Boolean read;
	@XmlElement(type = Long.class)
	protected Long timestamp;
	
	public Message() {
		// Zero Args.
	}
	
	public UserAuth getAuthor() {
		if (this.author == null) {
			this.author = new UserAuth();
		}
		return this.author;
	}
	
	public void setAuthor(UserAuth author) {
		this.author = author;
	}
	
	public String getRecipient() {
		return this.recipient;
	}
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Boolean getSent() {
		return this.sent;
	}
	
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	public Boolean getRead() {
		return this.read;
	}
	
	public void setRead(boolean read) {
		this.read = read;
	}
	
	public Long getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
