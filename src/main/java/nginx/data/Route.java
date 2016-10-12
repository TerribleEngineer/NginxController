package nginx.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Route {

	String target;
	String location;
	String uuid;

	public Route() {
		// Empty Constructor to appease the JAXB gods
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return getLocation() + " routing to " + getTarget();
	}

	public static boolean routeIsValid(Route route) {
		if (route == null) {
			return false;
		}

		String location = route.getLocation();

		if (location == null || location.isEmpty()) {
			return false;
		}

		String target = route.getTarget();

		if (target == null || target.isEmpty()) {
			return false;
		}

		return true;
	}

}
