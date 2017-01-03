package com.boomerang.util;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class JsonAgent {
	private static final Logger LOG = Logger.getLogger(JsonAgent.class.getName());
	
	public <T> T deserialize(String jstr, Class<T> jcls) {
        try{
            Gson gson = new Gson();
            return gson.fromJson(jstr, jcls);
        } catch (Exception e) {
            LOG.warning("Deserialize fails for "+jstr);
            LOG.warning("JSON Exception "+e.toString());
            return null;
        }
    }
    
    public <T> String serialize(T t) {
        try {
            Gson gson = new Gson();
            return gson.toJson(t);
        } catch (Exception e) {
        	LOG.warning("JSON Failure"+e.toString());
            return null;
        }
    }
}
