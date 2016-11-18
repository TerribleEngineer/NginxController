package nginx.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Route {

	String applicationName;
	String location;
	String backendUrl;

	Boolean proxySpecific;

	public Route() {
		proxySpecific = false;
	}

	public Boolean getProxySpecific() {
		return proxySpecific;
	}

	public void setProxySpecific(Boolean proxySpecific) {
		this.proxySpecific = proxySpecific;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return getLocation() + " routing to " + getBackendUrl();
	}

	public static boolean routeIsValid(Route route) {
		if (route == null) {
			return false;
		}

		String location = route.getLocation();

		if (location == null || location.isEmpty()) {
			return false;
		}

		String target = route.getBackendUrl();

		if (target == null || target.isEmpty()) {
			return false;
		}

		return true;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getBackendUrl() {
		return backendUrl;
	}

	public void setBackendUrl(String backendUrl) {
		this.backendUrl = backendUrl;
	}

}
