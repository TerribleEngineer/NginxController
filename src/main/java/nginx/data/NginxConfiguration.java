package nginx.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NginxConfiguration {

	private static NginxConfiguration instance = null;

	public static NginxConfiguration getInstance(String name, int port) {
		if (instance == null) {
			instance = new NginxConfiguration(name, port);
		}
		return instance;
	}

	int port;
	String name;
	List<Route> routes;

	private NginxConfiguration(String name, int port) {
		setPort(port);
		setName(name);
	}

	public String generateConfiguration() {
		StringBuilder sb = new StringBuilder();
		sb.append("server {\n");
		sb.append("\tlisten ");
		sb.append(getPort());
		sb.append(";\n\tserver_name ");
		sb.append(getName());
		sb.append(";\n\n");

		for (Route route : getRoutes()) {
			sb.append("\tlocation ");
			sb.append(route.getLocation());
			sb.append(" {\n\t\t");
			sb.append("proxy_pass ");
			sb.append(route.getTarget());
			sb.append(";\n\t}\n");

		}

		sb.append("\n}\n");
		return sb.toString();
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Route> getRoutes() {
		if (routes == null) {
			routes = new ArrayList<>();
		}
		return this.routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public Route addRoute(Route route) {
		route.setUuid(UUID.randomUUID().toString());
		getRoutes().add(route);
		return route;
	}

}
