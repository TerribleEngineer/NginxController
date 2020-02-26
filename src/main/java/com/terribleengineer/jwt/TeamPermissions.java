package com.terribleengineer.jwt;

public enum TeamPermissions {

	WRITE("WRITE"), EDIT("EDIT"), DELETE("DELETE"), READ("READ");

	private String permission;

	private TeamPermissions(String permission) {
		this.permission = permission;
	}

	public String value() {
		return this.permission;
	}

}
