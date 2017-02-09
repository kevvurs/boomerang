package com.boomerang.test;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.boomerang.os.service.AppService;
import com.boomerang.os.dao.GoogleFinance;
import com.boomerang.os.mind.StockMarketRNN;
import com.boomerang.os.util.JsonHandler;


@Test
@ContextConfiguration(locations = { "classpath:boomerang-test-context.xml" })
public class ApiTest extends AbstractTestNGSpringContextTests{
	private static final Logger LOG = Logger.getLogger(ApiTest.class.getName());
	private static final String PING = "OK";
	private static final String SYMBOL = "SHLD";
	
	@Autowired
	JsonHandler jsonAgent;
	
	@Autowired
	GoogleFinance googleFinance;
	
	@Autowired
	StockMarketRNN stockMarketRNN;
	
	@Autowired
	AppService appService;
	
	@Test()
	public void valueTest() {
		LOG.info("Testing API");
		String testPing = appService.ping();
		Assert.assertEquals(testPing,PING);
		
		List<String> stockData = googleFinance.fetchData(SYMBOL);
		LOG.info("Data units imported: " + stockData.size());
		stockMarketRNN.learn(stockData);

		LOG.info("SUCCESS");
	}
}
