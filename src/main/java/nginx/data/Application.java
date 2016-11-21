package nginx.data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Application {

	String name;

	Location location;
	List<UpstreamServer> upstreamServers;

	public Application(String name, String route, String backendUrl) throws URISyntaxException {

		URI backendUri = new URI(backendUrl);

		setName(name);
		setLocation(new Location(route, backendUri.getScheme(), backendUri.getPath()));

		UpstreamServer upstreamServer = new UpstreamServer(backendUri.getHost(), backendUri.getPort());
		getUpstreamServers().add(upstreamServer);
	}

	public void addServer(String name, String route, String backendUrl) throws URISyntaxException {
		URI backendUri = new URI(backendUrl);

		UpstreamServer upstreamServer = new UpstreamServer(backendUri.getHost(), backendUri.getPort());
		getUpstreamServers().add(upstreamServer);
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
