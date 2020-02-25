package com.terribleengineer.ngservice.models;

public class HealthInfo {

	Status status;

	public HealthInfo(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}