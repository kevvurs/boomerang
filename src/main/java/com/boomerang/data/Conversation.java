package com.boomerang.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "user",
    "other"
})

@XmlRootElement(name = "Conversation")
public class Conversation {
	protected String user;
    protected String other;
    
    public Conversation() {
    	// Zero Args.
    }
    
    public String getUser() {
    	return this.user;
    }
    
    public void setUser(String user) {
    	this.user = user;
    }
    
    public String getOther() {
    	return this.other;
    }
    
    public void setOther(String other) {
    	this.other = other;
    }
}
