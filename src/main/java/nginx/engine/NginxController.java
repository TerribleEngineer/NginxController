package nginx.engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import nginx.data.NginxConfiguration;

public class NginxController {

	private static Logger log = Logger.getLogger(NginxController.class);

	public static boolean reload(NginxConfiguration nginx, String configPath) {

		String configString = nginx.generateConfiguration();

		try {
			FileUtils.writeStringToFile(new File(configPath), configString, false);
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

			String input = IOUtils.toString(inputStream);
			String error = IOUtils.toString(errorStream);

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
