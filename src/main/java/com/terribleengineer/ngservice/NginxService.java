package com.terribleengineer.ngservice;

import static spark.Spark.awaitInitialization;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.IOException;
import java.util.Map;

import javax.naming.ConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.terribleengineer.jwt.JwtMaker;
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

	protected static final Logger log = LogManager.getLogger(NginxService.class);

	public static void main(String[] args) throws ConfigurationException, InterruptedException, IOException {

		Configuration config = new ConfigLoader().loadConfiguration();
		NginxConfiguration ngConfig = new NginxConfiguration(config.getProxyPort(), config.getHostname());

		log.debug("starting service with configuration below:");
		log.debug(config.toString());

		port(config.getApiPort());

		get("/", (req, res) -> {
			log.debug("Retrieving API description");

			res.type("application/json");
			return new RestInfo("Available Endpoints:",
					new EndpointInfo("/version", "GET", "Provides version information"),
					new EndpointInfo("/health", "GET", "Provides a healthcheck endpoint"),
					new EndpointInfo("/config", "GET", "Retrieve a copy of the Nginx config file"),
					new EndpointInfo("/endpoint", "POST",
							"Adds a proxy location; payload={ 'location' : 'nginx path', 'detail':'proxied location, 'description': 'Human readable detailing of the location'}"),
					new EndpointInfo("/endpoint", "DELETE", "Removes a proxy location {'location':'nginx path'}"));

		}, new JsonTransformer());

		get("/version", (req, res) -> {
			log.debug("Retrieving API version");

			res.type("application/json");
			return new VersionInfo("0.1");
		}, new JsonTransformer());

		get("/health", (req, res) -> {
			log.debug("Retrieving API health");

			// TODO add mechanism to check status of nginx server here

			res.type("application/json");
			return new HealthInfo(Status.HEALTHY);
		}, new JsonTransformer());

		// TODO add mechanism for setting specific JWT subjects

		get("/resetjwt", (req, res) -> {
			log.debug("Resetting static jwt");

			JwtMaker jwtMaker = new JwtMaker();
			String jwt = jwtMaker.getDevJwt();
			ngConfig.setJwt(jwt);
			res.type("text/plain");
			return jwt;
		});

		get("/jwt", (req, res) -> {
			log.debug("Resetting static jwt");

			res.type("text/plain");
			return ngConfig.getJwt();
		});

		get("/config", (req, res) -> {
			log.debug("Retrieving Nginx Configuration file");
			return NginxConfigurationBuilder.buildConfig(ngConfig);
		});

		get("/locations", (req, res) -> {
			log.debug("Retrieving Nginx Locations");
			LocationListing list = new LocationListing();
			Map<String, Location> locations = ngConfig.getLocations();
			for (String location : locations.keySet()) {
				list.getLocations().add(location);
			}
			res.type("application/json");
			return list;
		}, new JsonTransformer());

		post("/endpoint", (req, res) -> {
			log.debug("Attempting to add Nginx proxy location...");
			LocationInfo info = new Gson().fromJson(req.body(), LocationInfo.class);
			ProxyLocation pl = new ProxyLocation(config.getApiBase() + info.getLocation(), info.getProxy(),
					info.getTrailingSlash());
			ngConfig.addLocation(pl);

			if (NginxReloader.reload(config, ngConfig)) {
				log.debug("Nginx proxy location added.");
				res.status(200);
				return NginxConfigurationBuilder.buildConfig(ngConfig);
			} else {
				ngConfig.removeLocation(pl);
			}

			log.debug("Unable to add new Nginx proxy location, rolling back.");
			res.status(406);
			return NginxConfigurationBuilder.buildConfig(ngConfig);

		});

		delete("/endpoint", (req, res) -> {
			log.debug("Attempting to remove Nginx proxy location...");
			LocationInfo info = new Gson().fromJson(req.body(), LocationInfo.class);
			Location removed = null;

			if (ngConfig.getLocations().containsKey(config.getApiBase() + info.getLocation())) {
				removed = ngConfig.getLocations().remove(config.getApiBase() + info.getLocation());
				if (NginxReloader.reload(config, ngConfig)) {
					log.debug("location removed");
					for (String s : ngConfig.getLocations().keySet()) {
						log.debug("test: " + s);
					}

					log.debug("Nginx proxy location removed.");
					res.status(200);
					return NginxConfigurationBuilder.buildConfig(ngConfig);
				} else {
					log.debug("Unable to remove Nginx proxy location.");
					ngConfig.getLocations().put(removed.getLocation(), removed);
				}
			} else {
				log.debug("Presented location for deletion not found in Nginx configuration.");
			}

			res.status(406);
			return NginxConfigurationBuilder.buildConfig(ngConfig);
		});

		awaitInitialization();

		log.debug("Initializing system...");
		StaticContentLocation baseStatic = new StaticContentLocation("/", "/www", "Static Storage Root");
		ProxyLocation proxy = new ProxyLocation("/_ng", "http://" + config.getHostname() + ":" + config.getApiPort(),
				"Nginx Gateway Control API", true);
		ngConfig.addLocation(baseStatic);
		ngConfig.addLocation(proxy);

		NginxReloader.reload(config, ngConfig);
		log.debug("System now ready.");

	}

}
