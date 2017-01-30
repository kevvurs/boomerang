package com.boomerang.os.dao;

import javax.annotation.PostConstruct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.boomerang.os.data.App;
import com.boomerang.os.security.FirebaseConfigFactory;

@Repository
public class FirebaseDAO {
  private static final Logger LOG = Logger.getLogger(FirebaseDAO.class.getName());
  private FirebaseDatabase database;
  DatabaseReference appRef;
  Map<String, App> appMap;
  
  @Autowired
  FirebaseConfigFactory firebaseConfigFactory;

  public FirebaseDAO() {
	LOG.info("firebaseDAO instantiated");
	database = null;
	appMap = new HashMap<>();
  }

  @PostConstruct
  public void init() {
	LOG.info("connecting to Firebase...");
	try {
	  FirebaseOptions options = firebaseConfigFactory.mkOptions();
	  FirebaseApp.initializeApp(options);
	  database = FirebaseDatabase.getInstance();
	} catch (Exception e) {
	  LOG.warning("Firebase not connected- " + e);
	}
	appRef = database.getReference("app");
	appRef.addValueEventListener(new ValueEventListener() {
	  @Override
	  public void onDataChange(DataSnapshot dataSnapshot) {
		LOG.info("onDataChange");
        App app = dataSnapshot.getValue(App.class);
        if (app == null) {
		  LOG.info("app:null");
        } else {
		  LOG.info("app:" + dataSnapshot.getKey());
		  appMap.put(dataSnapshot.getKey(), app);
        }
	  }

	  @Override
	  public void onCancelled(DatabaseError databaseError) {
		LOG.info("app:cancel invoked");
	  }
	});
  }
  
  public App getApp(String appName) {
	  if (appMap.containsKey(appName)) {
		  return appMap.get(appName);  
	  } else {
		  return null;
	  }
  }
  
  public Collection<String> getAppKeys() {
	  return appMap.keySet();
  }
}