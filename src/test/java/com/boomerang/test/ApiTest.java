package com.boomerang.test;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.boomerang.core.LoginService;
import com.boomerang.dao.UserData;
import com.boomerang.data.UserInfo;
import com.boomerang.util.JsonAgent;

@Test
@ContextConfiguration(locations = { "classpath:boomerang-test-context.xml" })
public class ApiTest extends AbstractTestNGSpringContextTests{
	private static final Logger LOG = Logger.getLogger(ApiTest.class.getName());
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	JsonAgent jsonAgent;
	
	@Autowired
	UserData userData;
	
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

		LOG.info("SUCCESS");
	}
}
