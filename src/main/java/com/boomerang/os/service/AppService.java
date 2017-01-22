package com.boomerang.os.service;

import com.boomerang.os.util.JsonAgent;
import com.boomerang.os.data.Window;
import com.boomerang.os.data.App;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/app")
@Component
public class AppService {
	private static final Logger LOG = Logger.getLogger(AppService.class.getName());
	
	@Autowired
	JsonAgent jsonAgent;
	
	public AppService() {
		LOG.info("appService instantiated");
	}
	
	@GET
	@Path("/facebook")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getFacebook() {
		App app = new App();
		app.setWindow(mkWindow());
		app.setIcon("icons/fb.png");
		app.setTarget("www.facebook.com");
		return jsonAgent.serialize(app);
	}
	
	private Window mkWindow() {
		Window window = new Window();
		window.setWidth(400);
		window.setHeight(400);
		window.setFullscreen(false);
		return window;
	}
}