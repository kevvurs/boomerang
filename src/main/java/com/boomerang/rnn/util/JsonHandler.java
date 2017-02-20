package com.boomerang.rnn.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class JsonHandler {
	private static final Logger LOG = Logger.getLogger(JsonHandler.class.getName());
	
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
    
    public String flattenData(INDArray array) {
    	Collection<Collection> collection = new ArrayList<Collection>();
    	int days = array.shape()[array.shape().length - 1];
    	for (int i = 0 ; i < days; i++) {
    		
    	}
    	return null;
    }
}
