package com.terribleengineer.ngservice;

import static spark.Spark.awaitInitialization;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.IOException;
import java.util.Map;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.terribleengineer.ngservice.configuration.ConfigLoader;
import com.terribleengineer.ngservice.configuration.Configuration;
import com.terribleengineer.ngservice.models.EndpointInfo;
import com.terribleengineer.ngservice.models.HealthInfo;
import com.terribleengineer.ngservice.models.LocationInfo;
import com.terribleengineer.ngservice.models.LocationListing;
import com.terribleengineer.ngservice.models.RestInfo;
import com.terribleengineer.ngservice.models.Status;
import com.terribleengineer.ngservice.models.VersionInfo;
import com.terribleengineer.ngservice.nginxconfiguration.Location;
import com.terribleengineer.ngservice.nginxconfiguration.NginxConfiguration;
import com.terribleengineer.ngservice.nginxconfiguration.NginxConfigurationBuilder;
import com.terribleengineer.ngservice.nginxconfiguration.ProxyLocation;
import com.terribleengineer.ngservice.nginxconfiguration.StaticContentLocation;

public class NginxService {

	static Logger log = Logger.getLogger(NginxService.class);

	public static void main(String[] args) throws ConfigurationException, InterruptedException, IOException {

		Configuration config = new ConfigLoader().loadConfiguration();
		NginxConfiguration ngConfig = new NginxConfiguration(config.getProxyPort(), config.getHostname());

		log.debug("starting service with configuration below:");
		log.debug(config.toString());

		port(config.getApiPort());

		get("/", (req, res) -> {
			res.type("application/json");
			return new RestInfo("Available Endpoints:",
					new EndpointInfo("/version", "GET", "Provides version information"),
					new EndpointInfo("/health", "GET", "Provides a healthcheck endpoint"),
					new EndpointInfo("/config", "GET", "Retrieve a copy of the Nginx config file"),
					new EndpointInfo("/endpoint", "POST",
							"Adds a proxy location; payload={ 'location' : 'nginx path', 'detail':'proxied location, 'description': 'Human readable detailing of the location'}"),
					new EndpointInfo("/enpoint", "DELETE", "Removes a proxy location {'location':'nginx path'}"));

		}, new JsonTransformer());

		get("/version", (req, res) -> {
			res.type("application/json");
			return new VersionInfo("0.1");
		}, new JsonTransformer());

		get("/health", (req, res) -> {
			res.type("application/json");
			return new HealthInfo(Status.HEALTHY);
		}, new JsonTransformer());

		get("/config", (req, res) -> {
			return NginxConfigurationBuilder.buildConfig(ngConfig);
		});

		get("/locations", (req, res) -> {
			LocationListing list = new LocationListing();
			Map<String, Location> locations = ngConfig.getLocations();
			for (String location : locations.keySet()) {
				list.getLocations().add(location);
			}
			res.type("application/json");
			return list;
		}, new JsonTransformer());

		post("/endpoint", (req, res) -> {
			LocationInfo info = new Gson().fromJson(req.body(), LocationInfo.class);
			ProxyLocation pl = new ProxyLocation(config.getApiBase() + info.getLocation(), info.getProxy());
			ngConfig.addLocation(pl);

			if (NginxReloader.reload(config, ngConfig)) {
				res.status(200);
				return NginxConfigurationBuilder.buildConfig(ngConfig);
			} else {
				ngConfig.removeLocation(pl);
			}

			res.status(406);
			return NginxConfigurationBuilder.buildConfig(ngConfig);

		}, new JsonTransformer());

		delete("/endpoint", (req, res) -> {
			LocationInfo info = new Gson().fromJson(req.body(), LocationInfo.class);
			log.debug("Attempting to remove " + info.getLocation());
			Location removed = null;

			if (ngConfig.getLocations().containsKey(config.getApiBase() + info.getLocation())) {
				removed = ngConfig.getLocations().remove(config.getApiBase() + info.getLocation());
				if (NginxReloader.reload(config, ngConfig)) {
					log.debug("location removed");
					for (String s : ngConfig.getLocations().keySet()) {
						log.debug("test: " + s);
					}
					res.status(200);
					return NginxConfigurationBuilder.buildConfig(ngConfig);
				} else {
					log.debug("Could not successfully remove requested entry");
					ngConfig.getLocations().put(removed.getLocation(), removed);
				}
			}

			res.status(406);
			return NginxConfigurationBuilder.buildConfig(ngConfig);
		}, new JsonTransformer());

		awaitInitialization();

		log.debug("running bootstrap script...");

		StaticContentLocation baseStatic = new StaticContentLocation("/ui", "/",
				"User Interface supporting the Nginx Gateway");
		ProxyLocation proxy = new ProxyLocation("/ngapi", "http://" + config.getHostname() + ":" + config.getApiPort(),
				"Nginx Gateway Control API");
		ngConfig.addLocation(baseStatic);
		ngConfig.addLocation(proxy);

		NginxReloader.reload(config, ngConfig);

		log.debug("system ready.");

	}

}
