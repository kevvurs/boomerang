package com.boomerang.os.security;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.FirebaseCredential;
import com.google.firebase.FirebaseOptions;

import com.boomerang.os.data.FirebaseConfig;

@Component
public class FirebaseConfigFactory {
	//LOG
  private static final String RES = "src/main/resources/";
  
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

  @Value( "${boomerang.firebase.keyname}" )
  String key;

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
  
  public FirebaseOptions mkOptions() {
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	InputStream cert = classLoader.getResourceAsStream(RES + key);
	FirebaseCredential credentials = FirebaseCredentials.fromCertificate(cert);
	// TODO: null check
	FirebaseOptions options = new FirebaseOptions.Builder()
	.setCredential(credentials)
	.setDatabaseUrl("https://boomerang-686.firebaseio.com")
	.build();
	
	return options;
  }
  
  public InputStream mkCertificate() {
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	InputStream cert = classLoader.getResourceAsStream(key);
	return cert;
  }
}