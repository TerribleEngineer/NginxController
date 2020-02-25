package com.terribleengineer.ngservice.models;

import java.util.ArrayList;
import java.util.List;

public class LocationListing {

	List<String> locations;

	public LocationListing() {

	}

	public List<String> getLocations() {
		if (locations == null) {
			locations = new ArrayList<>();
		}
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

}
