package com.terribleengineer.ngservice.nginxconfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NginxConfiguration {

	protected static final Logger log = LogManager.getLogger(NginxConfiguration.class);

	Integer port;
	String serverName;
	String serverIp;

	Map<String, Location> locations;

	public Map<String, Location> getLocations() {
		if (locations == null) {
			locations = new HashMap<>();
		}
		return locations;
	}

	public void addLocation(Location location) {
		if (getLocations().containsKey(location.getLocation())) {
			log.debug("Could not add location, as it already exists");
		} else {
			getLocations().put(location.getLocation(), location);
		}
	}

	public void removeLocation(Location location) {
		if (getLocations().containsKey(location.getLocation())) {
			getLocations().remove(location.getLocation());
		} else {
			log.debug("Could not remove the location as it doesn't currently exist");
		}
	}

	public void setLocations(Map<String, Location> locations) {
		this.locations = locations;
	}

	public NginxConfiguration(Integer port, String serverName) throws UnknownHostException {
		this.port = port;
		this.serverName = serverName;

		InetAddress address = InetAddress.getByName(serverName);
		this.serverIp = address.getHostAddress();
	}

	public static void main(String[] args) throws UnknownHostException {
		NginxConfiguration ng = new NginxConfiguration(9999, "localhost");

		System.out.println(ng.getServerIp());

	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
}
