package com.boomerang.os.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "apiKey",
    "authDomain",
    "databaseURL",
    "storageBucket",
    "messagingSenderId"
})

@XmlRootElement(name = "FirebaseConfig")
public class FirebaseConfig {
    protected String apiKey;
	protected String authDomain;
	protected String databaseURL;
	protected String storageBucket;
	protected String messagingSenderId;

	public FirebaseConfig() {
		// Zero Args.
	}
	
	public String getApiKey() {
	  return this.apiKey;
	}

	public void setApiKey(String apiKey) {
	  this.apiKey = apiKey;
	}

	public String getAuthDomain() {
	  return this.authDomain;
	}

	public void setAuthDomain(String authDomain) {
	  this.authDomain = authDomain;
	}

	public String getDatabaseURL() {
	  return this.databaseURL;
	}

	public void setDatabaseURL(String databaseURL) {
	  this.databaseURL = databaseURL;
	}

	public String getStorageBucket() {
	  return this.storageBucket;
	}

	public void setStorageBucket(String storageBucket) {
	  this.storageBucket = storageBucket;
	}

	public String getMessagingSenderId() {
	  return this.messagingSenderId;
	}

	public void setMessagingSenderId(String messagingSenderId) {
	  this.messagingSenderId = messagingSenderId;
	}
}
