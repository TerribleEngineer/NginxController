package com.terribleengineer.ngservice.nginxconfiguration;

public class StaticContentLocation extends Location {

	String rootPath;

	public StaticContentLocation(String location, String rootPath) {
		super(location);
		this.rootPath = rootPath;
	}

	public StaticContentLocation(String location, String rootPath, String description) {
		super(location, description);
		this.rootPath = rootPath;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	@Override
	public boolean isStaticContentLocation() {
		return true;
	}

}
