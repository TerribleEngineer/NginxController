package nginx.server;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

public class BootstrapRunner implements Runnable {

	Logger log = Logger.getLogger(getClass());

	public BootstrapRunner() {

	}

	@Override
	public void run() {
		Client client = ClientBuilder.newClient();
		WebTarget apiTarget = client.target("http://127.0.0.1:7777/nginx/register/api");
		WebTarget uiTarget = client.target("http://127.0.0.1:7777/nginx/register/ui");

		String apiJson = apiTarget.request().get(String.class);
		String uiJson = uiTarget.request().get(String.class);

		WebTarget configTarget = client.target("http://127.0.0.1:7777/nginx/config");

		configTarget.request().post(Entity.entity(apiJson, MediaType.APPLICATION_JSON_TYPE));
		configTarget.request().post(Entity.entity(uiJson, MediaType.APPLICATION_JSON_TYPE));

	}

}
