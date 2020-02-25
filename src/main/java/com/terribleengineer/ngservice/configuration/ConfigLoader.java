package com.terribleengineer.ngservice.configuration;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

public class ConfigLoader {

	Logger log = Logger.getLogger(ConfigLoader.class);

	private Env env;

	private final String HOSTNAME = "HOSTNAME";
	private final String API = "CONTROL_PORT";
	private final String PROXY = "PROXY_PORT";
	private final String NGCONF = "CONF_PATH";
	private final String DEBUG = "DEBUG";
	private final String BASE_CONTENT = "CONTENT";

	public Env getEnv() {
		if (env == null) {
			env = new Env();
		}
		return env;
	}

	public Configuration loadConfiguration() throws ConfigurationException {

		log.debug("Loading configuration");

		Configuration config = new Configuration();

		try {

			String debugEvn = getEnv().getEnvVariable(DEBUG);
			if (!Strings.isNullOrEmpty(debugEvn)) {
				if (debugEvn.toLowerCase().equals("false")) {
					log.debug("\tdebug: overriding default");
					config.setDebug(false);
				} else {
					log.debug("\tdebug: using default");
				}
			}

			String hostnameEnv = getEnv().getEnvVariable(HOSTNAME);
			if (!Strings.isNullOrEmpty(hostnameEnv)) {
				log.debug("hostname: overriding default with " + hostnameEnv);
				config.setHostname(hostnameEnv);
			} else {
				log.debug("hostname: using default");
			}

			String apiPortEnv = getEnv().getEnvVariable(API);
			if (!Strings.isNullOrEmpty(apiPortEnv)) {
				log.debug("api port: overriding default with " + apiPortEnv);
				config.setApiPort(Integer.parseInt(apiPortEnv));
			} else {
				log.debug("api port: using default");
			}

			String proxyPortEnv = getEnv().getEnvVariable(PROXY);
			if (!Strings.isNullOrEmpty(proxyPortEnv)) {
				log.debug("proxy port: overriding default with " + proxyPortEnv);
				config.setProxyPort(Integer.parseInt(proxyPortEnv));
			} else {
				log.debug("proxy port: using default");
			}

			String nginxConfigEnv = getEnv().getEnvVariable(NGCONF);
			if (!Strings.isNullOrEmpty(nginxConfigEnv)) {
				log.debug("nginx config: overriding default with " + nginxConfigEnv);
				config.setNgConfigPath(nginxConfigEnv);
			} else {
				log.debug("nginx config: using default");
			}

			String baseContentEnv = getEnv().getEnvVariable(BASE_CONTENT);
			if (!Strings.isNullOrEmpty(baseContentEnv)) {
				log.debug("base web content: overriding default with " + baseContentEnv);
				config.setNgConfigPath(baseContentEnv);
			} else {
				log.debug("base web content: using default");
			}

		} catch (Exception e) {
			throw new ConfigurationException(e.getMessage());
		}

		return config;
	}

	public void setEnv(Env env) {
		this.env = env;
	}
}
