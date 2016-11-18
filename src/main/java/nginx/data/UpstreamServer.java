package nginx.data;

public class UpstreamServer {

	String host;
	Integer port;
	Long ttl;
	Boolean requireRefresh = false;
	String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public UpstreamServer() {
	}

	public Boolean getRequireRefresh() {
		return requireRefresh;
	}

	public void setRequireRefresh(Boolean requireRefresh) {
		this.requireRefresh = requireRefresh;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Long getTtl() {
		return ttl;
	}

	public void setTtl(Long ttl) {
		this.ttl = ttl;
	}

	@Override
	public String toString() {
		return getHost() + ":" + getPort();
	}
}
