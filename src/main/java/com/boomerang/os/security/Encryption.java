package com.boomerang.os.security;

import java.util.logging.Logger;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class Encryption {
  private static final Logger LOG = Logger.getLogger(Encryption.class.getName());
  private static final int lowerBound = 33;
  private static final int upperBound = 127;

  public Encryption() {
	// Zero Args.
  }

  public String encode(String info) {
	StringBuilder obfuscate = new StringBuilder();
	for (int i = 0; i < info.length(); i++) {
	  obfuscate.append(generateASCII());
	  obfuscate.append(generateASCII());
	  obfuscate.append(info.charAt(i));
	}
	obfuscate.append(generateASCII());
	obfuscate.append(generateASCII());
	return obfuscate.toString();
  }

  public String decode(String info) {
	StringBuilder concentrate = new StringBuilder();
	for (int i = 2; i < (info.length() - 2); i += 3) {
	  concentrate.append(info.charAt(i));
	}
	return concentrate.toString();
  }

  private char generateASCII() {
	int ASCIIcode = ThreadLocalRandom.current().nextInt(lowerBound, upperBound);
	return (char)ASCIIcode;
  }
}