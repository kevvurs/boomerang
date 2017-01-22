package com.boomerang.test;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.boomerang.os.service.AccountService;
import com.boomerang.os.security.Encryption;
import com.boomerang.os.dao.UserData;
import com.boomerang.os.data.UserInfo;
import com.boomerang.os.util.JsonAgent;

@Test
@ContextConfiguration(locations = { "classpath:boomerang-test-context.xml" })
public class ApiTest extends AbstractTestNGSpringContextTests{
	private static final Logger LOG = Logger.getLogger(ApiTest.class.getName());
	
	@Autowired
	JsonAgent jsonAgent;
	
	@Value( "${boomerang.app.version}" )
	private String appVersion;
	
	@Value( "${boomerang.app.profile}" )
	private String appProfile;
	
	// Test Constants //
	@Value( "${boomerang.test.ping}" )
	private String testPing;
	
	@Test()
	public void valueTest() {
		LOG.info("Testing values");
		Assert.assertEquals(appProfile, "test");
		
		LOG.info("Testing windows");
		
		
		LOG.info("SUCCESS");
	}
}
