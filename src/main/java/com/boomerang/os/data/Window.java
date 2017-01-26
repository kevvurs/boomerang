package com.boomerang.os.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "width",
    "height",
    "fullscreen"
})

@XmlRootElement(name = "Window")
public class Window {
	@XmlElement(type = Integer.class)
	protected Integer width;
	@XmlElement(type = Integer.class)
	protected Integer height;
	@XmlElement(type = Boolean.class)
	protected Boolean fullscreen;
	
	
	public Window() {
		// Zero Args.
	}
	
	public Window(Integer width, Integer height, Boolean fullscreen) {
	  this.width = width;
	  this.height = height;
	  this.fullscreen = fullscreen;
	}
	
	public Window(int width, int height, boolean fullscreen) {
	  this.width = width;
	  this.height = height;
	  this.fullscreen = fullscreen;
	}
	
	public Integer getWidth() {
		return this.width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public Integer getHeight() {
		return this.height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public Boolean getFullscreen() {
		return this.fullscreen;
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
}
