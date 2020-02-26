package com.terribleengineer.ngservice.nginxconfiguration;

public abstract class Location {

	String description;
	String location;

	public Location(String location) {
		this.location = location;
		this.description = "";
	}

	public Location(String location, String description) {
		this.location = location;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getLocation() {
		return location;
	}

	public abstract boolean isStaticContentLocation();

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
