package nginx.server;

public class ApiConfiguration {
	String hostname = "0.0.0.0";
	String proxyname = "localhost";

	Integer apiPort = 7777;
	Integer proxyPort = 80;
	String nginxConfigLocation = "/etc/nginx/nginx.conf";
	Boolean disableNginx = false;

	public String getProxyname() {
		return proxyname;
	}

	public void setProxyname(String proxyname) {
		this.proxyname = proxyname;
	}

	public String getNginxConfigLocation() {
		return nginxConfigLocation;
	}

	public void setNginxConfigLocation(String nginxConfigLocation) {
		this.nginxConfigLocation = nginxConfigLocation;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Integer getApiPort() {
		return apiPort;
	}

	public void setApiPort(Integer port) {
		this.apiPort = port;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer port) {
		this.proxyPort = port;
	}

	public Boolean getDisableNginx() {
		return disableNginx;
	}

	public void setDisableNginx(Boolean disableNginx) {
		this.disableNginx = disableNginx;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("API Configuration:\n");
		sb.append("\tHostname: " + getHostname() + "\n");
		sb.append("\tProxyname: " + getProxyname() + "\n");
		sb.append("\tAPI Port: " + getApiPort() + "\n");
		sb.append("\tProxy Port: " + getProxyPort() + "\n");
		sb.append("\tConfig Location: " + getNginxConfigLocation() + "\n");
		sb.append("\tDisable Nginx updates: " + getDisableNginx().toString() + "\n");

		return sb.toString();
	}

}
