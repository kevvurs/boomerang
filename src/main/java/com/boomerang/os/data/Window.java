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
	@XmlElement(type = Double.class)
	protected Double width;
	@XmlElement(type = Double.class)
	protected Double height;
	@XmlElement(type = Boolean.class)
	protected Boolean fullscreen;
	
	
	public Window() {
		// Zero Args.
	}
	
	public Double getWidth() {
		return this.width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public Double getHeight() {
		return this.height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}

	public Boolean getFullscreen() {
		return this.fullscreen;
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
}
