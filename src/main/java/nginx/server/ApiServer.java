package nginx.server;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyUtil;
import org.glassfish.jersey.server.ResourceConfig;

public class ApiServer {

	HttpServer server;
	Logger log = Logger.getLogger(getClass());

	public ApiServer(ApiConfiguration configuration) {
		String host = configuration.getHostname();
		String port = configuration.getApiPort().toString();

		NetworkListener listener = new NetworkListener("api-listener", host, Integer.parseInt(port));

		server = new HttpServer();
		server.addListener(listener);

		ServerConfiguration config = server.getServerConfiguration();

		CLStaticHttpHandler fileContainer = new CLStaticHttpHandler(ApiServer.class.getClassLoader());
		config.addHttpHandler(fileContainer, "/ui");

		ResourceConfig rc = new ResourceConfig().packages("nginx")
				.register(new ConfigurationBinder(configuration));
		GrizzlyHttpContainer restContainer = GrizzlyUtil.getContainer(rc);
		config.addHttpHandler(restContainer, "/nginx");

	}

	public void startService() {
		try {
			if (server != null) {
				server.start();
				Thread.currentThread().join();
			} else {
				log.warn("Cannot start an uninitialized server");
			}
		} catch (final Exception ioe) {
			log.error(ioe);
		} finally {
			this.stopService();
		}
	}

	public void stopService() {
		if (server != null) {
			server.shutdownNow();
		}
	}

}
