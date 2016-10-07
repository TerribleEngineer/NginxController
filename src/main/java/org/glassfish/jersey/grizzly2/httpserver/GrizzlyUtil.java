package org.glassfish.jersey.grizzly2.httpserver;

import org.glassfish.jersey.server.ResourceConfig;

public class GrizzlyUtil {

	public static GrizzlyHttpContainer getContainer(ResourceConfig rc) {
		return new GrizzlyHttpContainer(rc);
	}
}
