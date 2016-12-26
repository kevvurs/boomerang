package com.rush.core;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Path("/service")
@Component
public class WebService {
	private static final Logger LOG = Logger.getLogger(WebService.class.getName());
	private static final String PING = "OK";
	private static final String RUNTIME_VERION = "com.google.appengine.runtime.version";
	
	@Value( "${rush.app.version}" )
	private String appVersion;
	
	@Value( "${rush.app.profile}" )
	private String appProfile;
	
	@GET
    public String ping() {
        LOG.info("Ping invoked");
        touchSQLServer();
        return PING;
    }
	
	private void touchSQLServer() {
		// From https://cloud.google.com/appengine/docs/java/cloud-sql/
		String url;
		if (System.getProperty(RUNTIME_VERION).startsWith("Google App Engine/")) {
			url = System.getProperty("ae-cloudsql.cloudsql-database-url");
			try {
		        // Load the class that provides the new "jdbc:google:mysql://" prefix.
		        Class.forName("com.mysql.jdbc.GoogleDriver");
		      } catch (ClassNotFoundException e) {
		        LOG.warning("Error loading Google JDBC Driver- "+ e);
		        url = null;
		      }
		} else {
			// Set the url with the local MySQL database connection url when running locally
		      url = System.getProperty("ae-cloudsql.local-database-url");
		}
		if (url == null) {
			LOG.warning("Driver/Environment failed");
			return;
		}
		LOG.info("Connecting to " + url);
		String tables;
		try {
			StringBuilder stringBuilder = new StringBuilder();
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("show tables");
			stringBuilder.append("Tables: ");
			while(resultSet.next()) {
				stringBuilder.append(resultSet.getString(1));
				stringBuilder.append(',');
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			tables = stringBuilder.toString();
		} catch (SQLException e) {
			tables = ("Error- " + e);
		}
		LOG.info(tables);
	}
}
