package nginx.endpoints;

import java.util.List;
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

		nginx.addRoute(route);
		log.debug("Route added to configuration: " + route.toString());

		if (!configuration.getDisableNginx()) {
			if (NginxController.reload(nginx, configuration.getNginxConfigLocation())) {
				log.debug("Successfully reloaded nginx with new configuration");
				return Response.ok(route).build();
			} else {
				log.error("Error reloading with new configuration; fallback to running with last successful configuration");
				return Response.notModified().build();
			}
		}

		return Response.ok(route).build();

	}

	@GET
	@Path("routes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoutes(@Context UriInfo uriInfo) {
		log.debug("GET recieved from " + uriInfo.getBaseUri().toString());

		if (nginx == null) {
			nginx = NginxConfiguration.getInstance(configuration.getProxyname(), configuration.getProxyPort());
		}

		RouteList routeList = new RouteList(nginx.getRoutes());
		if (configuration.getProxyPort() != 80) {
			routeList.setProxyPort(configuration.getProxyPort());
		}

		return Response.ok(routeList).build();
	}

	@GET
	public Response getConfiguration(@Context UriInfo uriInfo) {

		log.debug("GET recieved from " + uriInfo.getBaseUri().toString());

		if (nginx == null) {
			nginx = NginxConfiguration.getInstance(configuration.getProxyname(), configuration.getProxyPort());
		}

		return Response.ok(nginx.generateConfiguration()).build();
	}

	@DELETE
	@Path("{uuid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeRoute(@Context UriInfo uriInfo, @PathParam("uuid") String uuid) {

		log.debug("DELETE received for location with uuid: " + uuid);

		if (nginx == null) {
			nginx = NginxConfiguration.getInstance(configuration.getProxyname(), configuration.getProxyPort());
		}

		List<Route> routes = nginx.getRoutes();
		Integer removeIndex = null;

		for (int i = 0; i < routes.size(); i++) {
			Route route = routes.get(i);
			if (route.getUuid().equals(uuid)) {
				removeIndex = i;
				break;
			}
		}

		if (removeIndex != null) {
			if (routes.remove(removeIndex.intValue()) == null) {
				log.debug("Error removing location from nginx configuration object");
				return Response.serverError().build();
			}

			if (!configuration.getDisableNginx()) {
				if (!NginxController.reload(nginx, configuration.getNginxConfigLocation())) {
					return Response.serverError().build();
				}
			}

			log.debug("Successfully removed location");
			return Response.ok().build();

		} else {
			log.debug("Requested Location for removal not found in configured locations");
			return Response.ok().build();
		}

	}

}
