package nginx.data;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.http.client.utils.URIBuilder;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteList {

	private int proxyPort = 80;
	List<Route> routes;

	public RouteList(List<Application> applications) throws URISyntaxException {
		List<Route> routeList = new ArrayList<>();
		for (Application application : applications) {
			String name = application.getName();
			String location = application.getLocation().getRoute();
			String scheme = application.getLocation().getScheme();
			String proxyPath = application.getLocation().getProxyPath();
			for (UpstreamServer upstreamServer : application.getUpstreamServers()) {
				String host = upstreamServer.getHost();
				Integer port = upstreamServer.getPort();

				URIBuilder uriBuilder = new URIBuilder();
				uriBuilder.setScheme(scheme);
				uriBuilder.setHost(host);

				if (port != 80) {
					uriBuilder.setPort(port);
				}
				if (proxyPath != null && !proxyPath.isEmpty()) {
					uriBuilder.setPath(proxyPath);
				}
				String uri = uriBuilder.build().toString();
				routeList.add(new Route(name, location, uri));

			}
		}
		setRoutes(routeList);
	}

	public List<Route> getRoutes() {
		if (routes == null) {
			routes = new ArrayList<>();
		}
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

}
