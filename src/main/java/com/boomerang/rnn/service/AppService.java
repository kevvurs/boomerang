package com.boomerang.rnn.service;

import com.boomerang.rnn.dao.GoogleFinance;
import com.boomerang.rnn.mind.StockMarketRNN;
import com.boomerang.rnn.util.JsonHandler;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Path("/market")
@Component
public class AppService {
	private static final Logger LOG = Logger.getLogger(AppService.class.getName());
	
	@Autowired
	JsonHandler jsonHandler;
	
	@Autowired
	GoogleFinance googleFinance;
	
	@Autowired
	StockMarketRNN stockMarketRNN;
	
	@Value( "${boomerang.app.ping}" )
	String pingStatus;
	
	public AppService() {
		LOG.info("appService instantiated");
	}
	
	@GET
	@Produces({ MediaType.TEXT_HTML })
	public String ping() {
		return new StringBuilder()
				.append(AppService.class.getName())
				.append(':')
				.append(pingStatus)
				.toString();
	}
	
	@GET
	@Path("{symbol}")
	public String stockMarketAnalysis(@PathParam("symbol") String symbol) {
		List<String> dataRaws = googleFinance.fetchData(symbol);
		INDArray forecast = stockMarketRNN.learn(dataRaws);
		
		return symbol;
	}
}