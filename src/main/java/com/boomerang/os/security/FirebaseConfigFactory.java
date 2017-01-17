package com.boomerang.os.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boomerang.os.data.FirebaseConfig;

@Component
public class FirebaseConfigFactory {

  @Value( "${boomerang.firebase.apikey}" )
  String apikey;

  @Value( "${boomerang.firebase.authdomain}" )
  String authdomain;

  @Value( "${boomerang.firebase.databaseurl}" )
  String databaseurl;

  @Value( "${boomerang.firebase.storagebucket}" )
  String storagebucket;

  @Value( "${boomerang.firebase.messagingsenderid}" )
  String messagingsenderid;

  public FirebaseConfigFactory() {
	// Zero args.
  }

  public FirebaseConfig mkConfig() {
	FirebaseConfig config = new FirebaseConfig();
	config.setApiKey(apikey);
	config.setAuthDomain(authdomain);
	config.setDatabaseURL(databaseurl);
	config.setStorageBucket(storagebucket);
	config.setMessagingSenderId(messagingsenderid);
	return config;
  }
}