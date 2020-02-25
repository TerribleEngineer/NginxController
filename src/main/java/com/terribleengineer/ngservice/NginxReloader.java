package com.terribleengineer.ngservice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.terribleengineer.ngservice.configuration.Configuration;
import com.terribleengineer.ngservice.nginxconfiguration.NginxConfiguration;
import com.terribleengineer.ngservice.nginxconfiguration.NginxConfigurationBuilder;

public class NginxReloader {

	private static Logger log = Logger.getLogger(NginxReloader.class);

	public static boolean reload(Configuration conf, NginxConfiguration nconf) {

		try {
			String configString = NginxConfigurationBuilder.buildConfig(nconf);
			FileUtils.writeStringToFile(new File(conf.getNgConfigPath()), configString, StandardCharsets.UTF_8, false);
		} catch (IOException e) {
			log.error("Error writing to nginx configuration file", e);
			return false;
		}

		ProcessBuilder pb = new ProcessBuilder("/usr/sbin/nginx", "-s", "reload");
		Process start;
		try {
			start = pb.start();
			start.waitFor();

			InputStream inputStream = start.getInputStream();
			InputStream errorStream = start.getErrorStream();

			String input = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			String error = IOUtils.toString(errorStream, StandardCharsets.UTF_8);

			log.debug("input: " + input);

			if (error != null && error.length() > 0) {
				log.error("error: " + error);
			}

		} catch (IOException | InterruptedException e) {
			log.error("Error reloading nginx configuration", e);
			return false;
		}

		log.debug("nginx configuration reload complete");
		return true;
	}

}
