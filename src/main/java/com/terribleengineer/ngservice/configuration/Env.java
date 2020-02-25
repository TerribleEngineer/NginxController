package com.terribleengineer.ngservice.configuration;

import java.util.HashMap;
import java.util.Map;

public class Env {

	Map<String, String> environmentVars;

	public String getEnvVariable(String key) {
		return this.getEnvVariables().get(key);
	}

	public Map<String, String> getEnvVariables() {
		if (environmentVars == null) {
			environmentVars = new HashMap<>();

			Map<String, String> envMap = System.getenv();
			for (String key : envMap.keySet()) {
				environmentVars.put(key, envMap.get(key));
			}
		}
		return environmentVars;
	}

}
