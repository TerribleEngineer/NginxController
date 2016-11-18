package nginx.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import nginx.data.Route;

@Path("register")
public class SelfRegisterEndpoint {

	@GET
	@Path("ui")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUiRegistration(@Context UriInfo info) throws Exception {
		String scheme = info.getBaseUri().getScheme();
		int port = info.getBaseUri().getPort();
		String host = info.getBaseUri().getHost();

		String baseString = scheme + "://" + host + ":" + port;
		String targetString = baseString + "/ui/";

		Route route = new Route();
		route.setApplicationName("ReverseProxyUI");
		route.setLocation("/proxy-ui/");
		route.setBackendUrl(targetString);
		route.setProxySpecific(true);

		return Response.ok(route).build();
	}

	@GET
	@Path("api")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApiRegistration(@Context UriInfo info) throws Exception {
		String scheme = info.getBaseUri().getScheme();
		int port = info.getBaseUri().getPort();
		String host = info.getBaseUri().getHost();

		String baseString = scheme + "://" + host + ":" + port;
		String targetString = baseString + "/nginx/";

		Route route = new Route();
		route.setApplicationName("ReverseProxyApi");
		route.setLocation("/proxy-api/");
		route.setBackendUrl(targetString);
		route.setProxySpecific(true);

		return Response.ok(route).build();
	}
}
