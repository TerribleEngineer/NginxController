package com.terribleengineer.ngservice.configuration;

public class Configuration {

	private boolean debug = true;
	private String hostname = "0.0.0.0";
	private Integer apiPort = 4343;
	private Integer proxyPort = 8080;
	private String ngConfigPath = "/etc/nginx/nginx.conf";
	private String staticContent = "/www";
	private String apiBase = "/api";

	public String getApiBase() {
		return apiBase;
	}

	public void setApiBase(String apiBase) {
		this.apiBase = apiBase;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Integer getApiPort() {
		return apiPort;
	}

	public void setApiPort(Integer apiPort) {
		this.apiPort = apiPort;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getNgConfigPath() {
		return ngConfigPath;
	}

	public void setNgConfigPath(String ngConfigPath) {
		this.ngConfigPath = ngConfigPath;
	}

	public String getStaticContent() {
		return staticContent;
	}

	public void setStaticContent(String staticContent) {
		this.staticContent = staticContent;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("API Configuration:\n");
		sb.append("\tHostname: " + getHostname() + "\n");
		sb.append("\tAPI Port: " + getApiPort() + "\n");
		sb.append("\tProxy Port: " + getProxyPort() + "\n");
		sb.append("\tConfig Location: " + getNgConfigPath() + "\n");
		sb.append("\tDebug mode: " + isDebug() + "\n");
		sb.append("\tBase Web Content Directory: " + getStaticContent() + "\n");

		return sb.toString();
	}

}
