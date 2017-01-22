package com.boomerang.os.data;

import java.util.Map;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "window",
    "target",
    "icon"
})

@XmlRootElement(name = "App")
public class App {
	protected Window window;
	protected String target;
	protected String icon;
	protected transient Map<String, String> data;
	
	public App() {
	  // Zero Args.
	}
	
	public Window getWindow() {
	  if (this.window == null) {
		  this.window = new Window();
	  }
	  return this.window;
	}
	
	public void setWindow(Window window) {
	  this.window = window;
	}
	
	public String getTarget() {
	  return this.target;
	}
	
	public void setTarget(String target) {
	  this.target = target;
	}
	
	public String getIcon() {
	  return this.icon;
	}
	
	public void setIcon(String icon) {
	  this.icon = icon;
	}
	
	public Map<String, String> getData() {
	  if (this.data == null) {
		this.data = new HashMap<>(); 
	  }
	  return this.data;
	}
}
