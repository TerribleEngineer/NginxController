package com.terribleengineer.ngservice.models;

public enum Status {

	DEGRADED("DEGRADED"), HEALTHY("HEALTHY"), UNHEALTHY("UNHEALTHY");

	public static Status getStatus(String statusString) {
		switch (statusString) {
		case "HEALTHY":
			return HEALTHY;
		case "UNHEALTHY":
			return UNHEALTHY;
		case "DEGRADED":
			return DEGRADED;
		default:
			throw new IllegalArgumentException(
					"Unsupported status string presented in impact environment variable for " + statusString);
		}
	}

	private String status;

	private Status(String status) {
		this.status = status;
	}

	public String value() {
		return this.status;
	}
}
