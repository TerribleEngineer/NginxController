package com.terribleengineer.ngservice.models;

public class LocationInfo {
	String location;
	String proxy;
	String description;
	Boolean trailingSlash;

	public Boolean getTrailingSlash() {
		if (trailingSlash == null) {
			trailingSlash = true;
		}
		return trailingSlash;
	}

	public void setTrailingSlash(Boolean trailingSlash) {
		this.trailingSlash = trailingSlash;
	}

	public LocationInfo() {

	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
