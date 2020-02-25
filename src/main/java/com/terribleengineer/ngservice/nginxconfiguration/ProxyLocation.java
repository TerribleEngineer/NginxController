package com.terribleengineer.ngservice.nginxconfiguration;

public class ProxyLocation extends Location {

	String proxypass;

	public ProxyLocation(String location, String proxypass) {
		super(location);
		if (!proxypass.endsWith("/")) {
			proxypass = proxypass + "/";
		}
		this.proxypass = proxypass;
	}

	public ProxyLocation(String location, String proxypass, String description) {
		super(location, description);
		if (!proxypass.endsWith("/")) {
			proxypass = proxypass + "/";
		}
		this.proxypass = proxypass;
	}

	public String getProxypass() {
		return proxypass;
	}

	public void setProxypass(String proxypass) {
		this.proxypass = proxypass;
	}

	@Override
	public boolean isStaticContentLocation() {
		return false;
	}
}
