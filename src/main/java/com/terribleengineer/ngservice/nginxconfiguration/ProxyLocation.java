package com.terribleengineer.ngservice.nginxconfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProxyLocation extends Location {

	protected static final Logger log = LogManager.getLogger(ProxyLocation.class);

	String proxypass;

	public ProxyLocation(String location, String proxypass) {
		super(location);

		log.debug("Creating proxy for Location: " + location + " , proxypass: " + proxypass);

		if (!proxypass.endsWith("/")) {
			proxypass = proxypass + "/";
		}
		this.proxypass = proxypass;
	}

	public ProxyLocation(String location, String proxypass, String description) {
		super(location, description);

		log.debug("Creating proxy for Location: " + location + " , proxypass: " + proxypass);

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
