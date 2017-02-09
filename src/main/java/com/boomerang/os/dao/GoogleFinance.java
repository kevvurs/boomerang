package com.boomerang.os.dao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GoogleFinance {
	private static final Logger LOG = Logger.getLogger(GoogleFinance.class.getName());
	
	@Value( "${boomerang.google.link}" )
	String datasource;
	
	public GoogleFinance() {
		LOG.info("googleFinance instantiated");
	}
	
	@Transactional
	public List<String> fetchData(String symbol) {
		LOG.info("Connecting to Google Finance...");
		List<String> data = new ArrayList<>();
		URL ref;
		try {
			ref = mkURL(symbol);
		} catch (MalformedURLException e) {
			LOG.severe("URL is malformed- " + datasource + symbol);
			return data;
		}
		
		try {
			HttpURLConnection portal = (HttpURLConnection) ref.openConnection();
			portal.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(portal.getInputStream()));
			@SuppressWarnings("unused")
			String header = rd.readLine();
			data = rd.lines().collect(Collectors.toList());
			rd.close();
		} catch (IOException e) {
			LOG.severe("Error fetching data- " + e.getMessage());
		}
		return data;
	}
	
	private URL mkURL(String symbol) throws MalformedURLException {
		String link = new StringBuilder()
				.append(datasource)
				.append(symbol)
				.toString();
		URL url = new URL(link);
		return url;
	}
	
}
