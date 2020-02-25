package com.terribleengineer.ngservice.models;

public class EndpointInfo {

	String description;
	String method;
	String path;

	public EndpointInfo(String path, String method, String description) {
		this.path = path;
		this.method = method;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
