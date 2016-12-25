package com.rush.test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.rush.core.WebService;

@Test
@ContextConfiguration(locations = { "classpath:rush-test-context.xml" })
public class ApiTest extends AbstractTestNGSpringContextTests{
	private static final Logger LOG = Logger.getLogger(ApiTest.class);
	
	@Autowired
	WebService webService;
	
	@Value( "${rush.version}" )
	private String appVersion;
	
	
	@Test()
	public void valueTest() {
		LOG.info("Testing values");
		Assert.assertEquals(webService.ping(), "OK");
	}
}
