package nginx.endpoints;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import nginx.data.NginxConfiguration;
import nginx.data.Route;
import nginx.data.RouteList;
import nginx.engine.NginxController;
import nginx.server.ApiConfiguration;

@Path("config")
public class ConfigurationEndpoint {

	Logger log = Logger.getLogger(getClass());

	@Inject
	ApiConfiguration configuration;

	NginxConfiguration nginx = null;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRoute(@Context UriInfo uriInfo, Route route) {

		log.debug("POST accepted: " + uriInfo.getBaseUri().toString());

		if (!Route.routeIsValid(route)) {
			log.error("Invalid Route object received");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (nginx == null) {
			nginx = NginxConfiguration.getInstance(configuration.getProxyname(), configuration.getProxyPort());
		}

		nginx.addApplication(route.getApplicationName(), route.getLocation(), route.getBackendUrl());
		log.debug("Route added to configuration: " + route.toString());

		if (!configuration.getDisableNginx()) {
			if (NginxController.reload(nginx, configuration.getNginxConfigLocation())) {
				log.debug("Successfully reloaded nginx with new configuration");
				return Response.ok(route).build();
			} else {
				log.error(
						"Error reloading with new configuration; fallback to running with last successful configuration");
				return Response.notModified().build();
			}
		}

		return Response.ok(route).build();

	}

	@GET
	@Path("routes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApplications(@Context UriInfo uriInfo) {
		log.debug("GET recieved from " + uriInfo.getBaseUri().toString());

		if (nginx == null) {
			nginx = NginxConfiguration.getInstance(configuration.getProxyname(), configuration.getProxyPort());
		}

		RouteList appList;
		try {
			appList = new RouteList(nginx.getApplications());
			if (configuration.getProxyPort() != 80) {
				appList.setProxyPort(configuration.getProxyPort());
			}
		} catch (URISyntaxException e) {
			log.error("Error generating route list", e);
			return Response.serverError().build();
		}

		return Response.ok(appList).build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getConfiguration(@Context UriInfo uriInfo) {

		log.debug("GET recieved from " + uriInfo.getBaseUri().toString());

		if (nginx == null) {
			nginx = NginxConfiguration.getInstance(configuration.getProxyname(), configuration.getProxyPort());
		}

		String config;

		try {
			config = nginx.generateConfiguration();
			return Response.ok(config).build();
		} catch (Exception e) {
			log.error("Error generating nginx config file string", e);
			return Response.serverError().build();
		}

	}

	@DELETE
	@Path("{appName}/{backendServer}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeRoute(@Context UriInfo uriInfo, @PathParam("appName") String appName,
			@PathParam("backendServer") String encodedBackendServer)
			throws URISyntaxException, UnsupportedEncodingException {

		String backendServer = URLDecoder.decode(encodedBackendServer, "UTF-8");

		log.debug("DELETE received for application " + appName + "'s backend = " + backendServer);

		if (nginx == null) {
			nginx = NginxConfiguration.getInstance(configuration.getProxyname(), configuration.getProxyPort());
		}

		nginx.removeBackend(appName, backendServer);

		if (!configuration.getDisableNginx()) {
			if (NginxController.reload(nginx, configuration.getNginxConfigLocation())) {
				log.debug("Successfully reloaded nginx with new configuration");
				return Response.ok().build();
			} else {
				log.error(
						"Error reloading with new configuration; fallback to running with last successful configuration");
				return Response.notModified().build();
			}
		}

		return Response.ok().build();
	}

}
