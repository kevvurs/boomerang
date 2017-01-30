package com.boomerang.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.boomerang.os.service.AccountService;
import com.boomerang.os.security.Encryption;
import com.boomerang.os.security.FirebaseConfigFactory;
import com.boomerang.os.dao.FirebaseDAO;
import com.boomerang.os.data.UserInfo;
import com.boomerang.os.util.JsonAgent;


@Test
@ContextConfiguration(locations = { "classpath:boomerang-test-context.xml" })
public class ApiTest extends AbstractTestNGSpringContextTests{
	private static final Logger LOG = Logger.getLogger(ApiTest.class.getName());
	
	@Autowired
	JsonAgent jsonAgent;
	
	@Autowired
	FirebaseDAO firebaseDAO;
	
	@Autowired
	FirebaseConfigFactory firebaseConfigFactory;
	
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
		StringBuilder cert = new StringBuilder();
		InputStream ins = firebaseConfigFactory.mkCertificate();
		int nx;
		char c;
		try {
			while ((nx = ins.read()) != -1) {
				c = (char)nx;
				cert.append(c);
			}
			ins.close();
		} catch (IOException e) {
			LOG.warning("cannot read certificate- " + e);
		}
		
		LOG.info(cert.toString());
		LOG.info("SUCCESS");
	}
}
