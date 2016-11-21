package nginx.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class NginxConfiguration {

	Logger log = Logger.getLogger(getClass());

	List<Application> applications;

	String name;
	Integer port;

	private NginxConfiguration(String name, int port) {
		setPort(port);
		setName(name);
	}

	private static NginxConfiguration instance = null;

	public static NginxConfiguration getInstance(String name, Integer port) {
		if (instance == null) {
			instance = new NginxConfiguration(name, port);
		}
		return instance;
	}

	public String getName() {
		return name;
	}

	private String getBackend(Location location, UpstreamServer upstreamServer) {
		UriBuilder builder = UriBuilder.fromPath(upstreamServer.getHost()).scheme(location.getScheme());

		if (upstreamServer.getPort() != null) {
			builder.port(upstreamServer.getPort());
		}

		if (location.getProxyPath() != null && !location.getProxyPath().isEmpty()) {
			builder.path(location.getProxyPath());
		}

		return builder.build().toString();
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void addApplication(String name, String route, String backendUrl) {
		for (Application application : getApplications()) {
			if (application.getName().equals(name) && application.getLocation().getRoute().equals(route)) {
				log.debug("Application already exists within proxy.  Adding additional upstream server");
				try {
					application.addServer(name, route, backendUrl);
				} catch (URISyntaxException e) {
					log.error("Error adding upstream server to application: " + name, e);
				}
				return;
			} else if (application.getName().equals(name) && !application.getLocation().getRoute().equals(route)) {
				log.error("Cannot create application; Name already associated with the provided route");
				return;
			} else if (!application.getName().equals(name) && application.getLocation().getRoute().equals(route)) {
				log.error("Cannot create application; Route already associated with the provided Name");
				return;
			}
		}

		Application app;
		try {
			app = new Application(name, route, backendUrl);
			applications.add(app);
		} catch (URISyntaxException e) {
			log.error("Error creating new application", e);
		}

	}

	public List<Application> getApplications() {
		if (applications == null) {
			applications = new ArrayList<>();
		}
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public String generateConfiguration() throws IOException {
		StringBuilder sb = new StringBuilder();

		InputStream topStream = getClass().getClassLoader().getResourceAsStream("templates/nginx-top");
		InputStream upperMiddleStream = getClass().getClassLoader().getResourceAsStream("templates/nginx-upper-middle");
		InputStream lowerMiddleStream = getClass().getClassLoader().getResourceAsStream("templates/nginx-lower-middle");
		InputStream bottomStream = getClass().getClassLoader().getResourceAsStream("templates/nginx-bottom");

		sb.append(IOUtils.toString(topStream));
		generateServerDetails(sb);
		sb.append(IOUtils.toString(upperMiddleStream));
		generateLocations(getApplications(), sb);
		sb.append(IOUtils.toString(lowerMiddleStream));
		generateUpstreamServers(getApplications(), sb);
		sb.append(IOUtils.toString(bottomStream));

		String cfg = sb.toString();
		log.debug(cfg);
		return cfg;
	}

	private void generateLocations(List<Application> applications, StringBuilder sb) {
		for (Application application : applications) {
			Location location = application.getLocation();
			sb.append("\n        location " + location.getRoute() + " {");
			String proxy = "\n            proxy_pass " + location.getScheme() + "://" + application.getName();
			if (location.getProxyPath() != null && !location.getProxyPath().isEmpty()) {
				proxy += location.getProxyPath();
			}
			proxy += ";";
			sb.append(proxy);
			sb.append("\n    }\n");
		}
	}

	private void generateUpstreamServers(List<Application> applications, StringBuilder sb) {
		for (Application application : applications) {
			if (application.getUpstreamServers().isEmpty()) {
				continue;
			}

			sb.append("\n    upstream " + application.getName() + " {");
			for (UpstreamServer server : application.getUpstreamServers()) {
				sb.append("\n        server " + server.getHost() + ":" + server.getPort() + ";");
			}
			sb.append("\n    }");
		}
	}

	public void removeBackend(String name, String backendServer) throws URISyntaxException {
		Application app = null;
		for (Application application : getApplications()) {
			if (application.getName().equals(name)) {
				app = application;
				break;
			}
		}

		URI uri = new URI(backendServer);
		String backendKey = uri.getHost() + ":" + uri.getPort();

		if (app != null) {

			int deleteIndex = -1;

			for (int i = 0; i < app.getUpstreamServers().size(); i++) {
				UpstreamServer upstreamServer = app.getUpstreamServers().get(i);
				if (upstreamServer.toString().equals(backendKey)) {
					deleteIndex = i;
					break;
				}
			}

			if (deleteIndex >= 0) {
				app.getUpstreamServers().remove(deleteIndex);
			}

			if (app.getUpstreamServers().size() == 0) {
				removeApp(app);
			}
		}
	}

	public void removeApp(Application app) {
		int deleteIndex = -1;
		for (int i = 0; i < getApplications().size(); i++) {
			Application application = getApplications().get(i);
			if (application.getName().equals(app.getName())) {
				deleteIndex = i;
				break;
			}
		}

		if (deleteIndex >= 0) {
			getApplications().remove(deleteIndex);
		}
	}

	private void generateServerDetails(StringBuilder sb) {
		sb.append("\n        listen " + getPort() + ";");
		sb.append("\n        listen [::]:" + getPort() + ";");
		sb.append("\n        server_name " + getName() + ";\n");
	}
}
