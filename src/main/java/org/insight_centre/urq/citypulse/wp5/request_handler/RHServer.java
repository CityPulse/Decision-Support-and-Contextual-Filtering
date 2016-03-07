package org.insight_centre.urq.citypulse.wp5.request_handler;

import java.util.Map;
import java.util.Set;

import org.glassfish.tyrus.server.Server;

/**
 * The Request Handler server implementation
 *
 * @author Stefano Germano
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email stefano.germano@insight-centre.org
 */
public class RHServer extends Server {

	public RHServer(final Map<String, Object> properties,
			final Class<?>... configuration) {
		super(properties, configuration);
		// TODO Auto-generated constructor stub
	}

	public RHServer(final String hostName, final int port,
			final String contextPath, final Map<String, Object> properties,
			final Class<?>... configuration) {
		super(hostName, port, contextPath, properties, configuration);
		// TODO Auto-generated constructor stub
	}

	public RHServer(final String hostName, final int port,
			final String contextPath, final Map<String, Object> properties,
			final Set<Class<?>> configuration) {
		super(hostName, port, contextPath, properties, configuration);
		// TODO Auto-generated constructor stub
	}

}
