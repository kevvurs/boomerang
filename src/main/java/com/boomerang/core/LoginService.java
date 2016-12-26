package com.boomerang.core;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.DateTime;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

@Path("/login")
@Component
public class LoginService {
	private static final Logger LOG = Logger.getLogger(LoginService.class.getName());
	private static final String PING = "OK";
	
	@Value( "${rush.app.version}" )
	private String appVersion;
	
	@Value( "${rush.app.profile}" )
	private String appProfile;
	
	@GET
	@Produces( {MediaType.TEXT_PLAIN} )
    public String ping() {
        LOG.info("Ping invoked");
        Key key = touchDatastore();
        String string = touchDatastore(key);
        return string;
    }
	
	private Key touchDatastore() {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		KeyFactory keyFactory = datastore.newKeyFactory().setKind("Anything");
		Key key = datastore.allocateId(keyFactory.newKey());
	    Entity task = Entity.newBuilder(key)
	        .set("description", StringValue.newBuilder("literally, anything... LITERALLY! FUCKING OMG.").setExcludeFromIndexes(true).build())
	        .set("created", DateTime.now())
	        .set("done", true)
	        .build();
	    datastore.put(task);
	    return key;
	}
	
	private String touchDatastore(Key key) {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		Entity entity = datastore.get(key);
	    if (entity != null) {
	    	LOG.info("retrieved!");
	    	return entity.getString("description");
	    } else {
	    	LOG.warning("not retrieved...");
	    	return "Null";
	    }
	}
}
