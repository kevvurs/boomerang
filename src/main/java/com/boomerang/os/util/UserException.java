package com.boomerang.os.util;

public class UserException extends Exception {
	private static final long serialVersionUID = -5325136878718862278L;
	public UserException() { super(); }
	public UserException(String message) { super(message); }
	public UserException(String message, Throwable cause) { super(message, cause); }
	public UserException(Throwable cause) { super(cause); }
}
