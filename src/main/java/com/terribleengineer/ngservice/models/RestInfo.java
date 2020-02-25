package com.terribleengineer.ngservice.models;

import java.util.ArrayList;
import java.util.List;

public class RestInfo {

	List<EndpointInfo> endpoints;
	String title;

	public RestInfo(String title, EndpointInfo... endpointList) {
		this.title = title;

		for (EndpointInfo endpoint : endpointList) {
			this.getEndpoints().add(endpoint);
		}
	}

	public List<EndpointInfo> getEndpoints() {
		if (endpoints == null) {
			endpoints = new ArrayList<>();
		}
		return endpoints;
	}

	public String getTitle() {
		return title;
	}

	public void setEndpoints(List<EndpointInfo> endpoints) {
		this.endpoints = endpoints;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
