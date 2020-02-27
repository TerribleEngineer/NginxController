package com.terribleengineer.ngservice.nginxconfiguration;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NginxConfigurationBuilder {

	protected static final Logger log = LogManager.getLogger(NginxConfigurationBuilder.class);

	public static String buildConfig(NginxConfiguration config) {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\n");
		sb.append("worker_processes 2;\n");

		sb.append("\n\n");
		sb.append("events {\n");
		sb.append("\tworker_connections 1000;\n");
		sb.append("}\n");

		sb.append("\n\n");
		sb.append("http {\n");
		sb.append("\tinclude /etc/nginx/mime.types;\n");
		sb.append("server {\n");

		sb.append("\tserver_name " + config.getServerName() + ";\n");
		sb.append("\tlisten " + config.getPort() + ";\n");

		Map<String, Location> locations = config.getLocations();

		for (String locationKey : locations.keySet()) {
			Location location = locations.get(locationKey);
			if (location.isStaticContentLocation()) {
				StaticContentLocation scl = (StaticContentLocation) location;
				sb.append("\tlocation " + scl.getLocation() + " {\n");
				sb.append("\t\tindex index.html index.htm;\n");
				sb.append("\t\tautoindex on;\n");
				sb.append("\t\troot " + scl.getRootPath() + ";\n");
				sb.append("\t\tadd_header Authentication 'Bearer " + config.getJwt() + "';\n");
				sb.append("\t}\n\n");
			} else {
				ProxyLocation pl = (ProxyLocation) location;
				sb.append("\tlocation " + pl.getLocation() + " {\n");
				sb.append("\t\tproxy_pass " + pl.getProxypass() + ";\n");
				sb.append("\t\tproxy_set_header Host $host;\n");
				sb.append("\t\tproxy_set_header X-Real-IP $remote_addr;\n");
				sb.append("\t\tadd_header Authentication 'Bearer " + config.getJwt() + "';\n");
				sb.append("\t}\n\n");
			}
		}

		sb.append("}\n");
		sb.append("}\n");

		String cfg = sb.toString();
		log.debug(cfg);
		return cfg;
	}

}
