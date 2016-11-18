package nginx.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

public class ApiService {

	Logger log = Logger.getLogger(getClass());

	ApiConfiguration configuration = null;
	ApiServer server = null;
	Options options = null;
	boolean ready = false;

	public static void main(String[] args) {
		new ApiService(args);
	}

	public ApiService(String[] args) {
		configuration = initializeConfiguration(args);
		log.debug("Starting API Server w/ Configuration as: " + configuration.toString());
		server = new ApiServer(configuration);

		ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		newSingleThreadScheduledExecutor.schedule(new BootstrapRunner(), 4, TimeUnit.SECONDS);

		server.startService();
	}

	private ApiConfiguration initializeConfiguration(String[] arguments) {
		ApiConfiguration config = null;

		options = new Options();
		options.addOption("h", "hostname", true, "Hostname for the nginx api Service");
		options.addOption("p", "port", true, "Port for the nginx api Service");
		options.addOption("P", "proxy", true, "Port for the nginx proxy Service");
		options.addOption("c", "config", true, "path to nginx configuration file");
		options.addOption("d", "disable", false, "Disable nginx engine (debug mode)");

		try {

			CommandLine parsedArguments = new DefaultParser().parse(options, arguments);
			Option[] opts = parsedArguments.getOptions();

			if (config == null) {
				config = new ApiConfiguration();
			}

			for (Option option : opts) {
				switch (option.getOpt()) {
				case "h":
					config.setHostname(option.getValue());
					continue;
				case "p":
					config.setApiPort(Integer.parseInt(option.getValue()));
					continue;
				case "P":
					config.setProxyPort(Integer.parseInt(option.getValue()));
					continue;
				case "c":
					config.setNginxConfigLocation(option.getValue());
					continue;
				case "d":
					config.setDisableNginx(true);
					continue;
				default:
					continue;

				}

			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			log.error("Error parsing command line arguments", e);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(" ", options);
		}

		return config;
	}

}
