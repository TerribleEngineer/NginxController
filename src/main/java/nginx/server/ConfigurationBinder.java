package nginx.server;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ConfigurationBinder extends AbstractBinder {

	ApiConfiguration config;

	public ConfigurationBinder(ApiConfiguration config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		bind(this.config).to(ApiConfiguration.class);

	}

}
