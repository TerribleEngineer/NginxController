package nginx.data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Application {

	String name;

	Location location;
	List<UpstreamServer> upstreamServers;

	public Application(String name, String route, String backendUrl, Boolean temporary) throws URISyntaxException {
		upstreamServers = new ArrayList<>();
		this.name = name;

		URI backendUri = new URI(backendUrl);
		String scheme = backendUri.getScheme();
		Integer port = backendUri.getPort();
		String path = backendUri.getPath();

		location = new Location();
		location.setRoute(route);
		location.setScheme(scheme);
		location.setProxyPath(path);

		UpstreamServer upstreamServer = new UpstreamServer();
		upstreamServer.setHost(backendUri.getHost());
		upstreamServer.setPort(port);
		upstreamServer.setTtl(new Date().getTime() + 1000 * 60);
		if (temporary) {
			upstreamServer.setRequireRefresh(true);
		}

		upstreamServers.add(upstreamServer);
	}

	public void addServer(String name, String route, String backendUrl, Boolean temporary) throws URISyntaxException {
		URI backendUri = new URI(backendUrl);
		Integer port = backendUri.getPort();

		UpstreamServer upstreamServer = new UpstreamServer();
		upstreamServer.setHost(backendUri.getHost());
		upstreamServer.setPort(port);
		upstreamServer.setTtl(new Date().getTime() + 1000 * 60);
		if (temporary) {
			upstreamServer.setRequireRefresh(true);
		}

		upstreamServers.add(upstreamServer);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<UpstreamServer> getUpstreamServers() {
		if (upstreamServers == null) {
			upstreamServers = new ArrayList<>();
		}
		return upstreamServers;
	}

	public void setUpstreamServers(List<UpstreamServer> upstreamServers) {
		this.upstreamServers = upstreamServers;
	}

}
