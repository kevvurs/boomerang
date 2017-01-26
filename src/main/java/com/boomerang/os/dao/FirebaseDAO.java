package com.boomerang.os.dao;

import javax.annotation.PostConstruct;
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
  
  @Autowired
  FirebaseConfigFactory firebaseConfigFactory;

  public FirebaseDAO() {
	LOG.info("firebaseDAO instantiated");
	database = null;
  }

  @PostConstruct
  public void init() {
	LOG.info("Attempting to connect to Firebase...");
	try {
	  FirebaseOptions options = firebaseConfigFactory.mkOptions();
	  FirebaseApp.initializeApp(options);
	  database = FirebaseDatabase.getInstance();
	} catch (Exception e) {
	  LOG.warning("Firebase not connected- " + e);
	}
	appRef = database.getReference("boomerang-686/app");
	appRef.addValueEventListener(new ValueEventListener() {
	  @Override
	  public void onDataChange(DataSnapshot dataSnapshot) {
        App app = dataSnapshot.getValue(App.class);
        if (app == null) {
		  LOG.info("null");
        } else {
		  LOG.info(app.getTarget());
        }
	  }

	  @Override
	  public void onCancelled(DatabaseError databaseError) {
		LOG.info("firebase cancel invoked");
	  }
	});
  }
  
  public String test() {
	StringBuilder builder = new StringBuilder();
	builder.append('>');
	DatabaseReference ref = database.getReference("boomerang-686/app");
	ref.addValueEventListener(new ValueEventListener() {
	  @Override
	  public void onDataChange(DataSnapshot dataSnapshot) {
        App app = dataSnapshot.getValue(App.class);
        if (app == null) {
		  builder.append("null");
        } else {
		  builder.append(app.getTarget());
        }
	  }

	  @Override
	  public void onCancelled(DatabaseError databaseError) {
		builder.append("firebase cancel invoked");
	  }
	});
	return builder.toString();
  }
}