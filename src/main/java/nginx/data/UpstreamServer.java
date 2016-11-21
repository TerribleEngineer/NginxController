package nginx.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class UpstreamServer {

	Map<String, String> serverMap;

	public UpstreamServer(String host, Integer port, String uuid) {
		serverMap = createUpstreamServer(host, port, uuid);
	}

	public UpstreamServer(String host, Integer port) {
		serverMap = createUpstreamServer(host, port, UUID.randomUUID().toString());
	}

	private Map<String, String> createUpstreamServer(String host, Integer port, String uuid) {
		Map<String, String> map = new HashMap<>();
		map.put(HOST, host);
		map.put(PORT, port.toString());
		map.put(ID, uuid);
		return map;
	}

	public String getUuid() {
		return serverMap.get(ID);
	}

	public void setUuid(String uuid) {
		serverMap.put(ID, uuid);
	}

	public String getHost() {
		return serverMap.get(HOST);
	}

	public void setHost(String host) {
		serverMap.put(HOST, host);
	}

	public Integer getPort() {
		return Integer.parseInt(serverMap.get(PORT));
	}

	public void setPort(Integer port) {
		serverMap.put(PORT, port.toString());
	}

	@Override
	public String toString() {
		return getHost() + ":" + getPort();
	}

	public Map<String, String> getMap() {
		Map<String, String> clone = new HashMap<>();
		for (String key : serverMap.keySet()) {
			String value = serverMap.get(key);
			clone.put(key, value);
		}
		return clone;
	}

	private final static String PORT = "port";
	private final static String HOST = "host";
	private final static String ID = "uuid";
}
