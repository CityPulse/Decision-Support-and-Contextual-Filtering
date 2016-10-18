package org.insight_centre.urq.citypulse.wp5.request_handler;

import java.util.logging.Logger;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;
import org.insight_centre.urq.citypulse.wp5.Configuration;

/**
 * The manager of the Request Handler server
 *
 * @author Stefano Germano
 * @author Thu-Le Pham
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email stefano.germano@insight-centre.org
 * @email thule.pham@insight-centre.org
 */
public class RHManager {

	/**
	 * Reference to the Logger class
	 */
	public static Logger Log = Logger.getLogger(RHManager.class.getPackage()
			.getName());

	/**
	 * The starting function of the Request Handler component
	 *
	 * @param args - not used
	 */
	public static void main(final String[] args) {
		new RHManager().startRH(Configuration.getInstance().getHostName(),
				Configuration.getInstance().getPort(), Configuration
						.getInstance().getRootPath());
	}

	/**
	 * Starts the Request Handler server
	 *
	 * @param hostName the hostname of the Request Handler server
	 * @param port the port of the Request Handler server
	 * @param rootPath the base path of the Request Handler server
	 */
	private void startRH(final String hostName, final int port,
			final String rootPath) {

		RHManager.Log.info("started " + this.getClass().getName());

		final Server rhServer = new Server(hostName, port, rootPath, null,
				ReRouteReasoningRequestEndpoint.class,
				ContextualEventsRequestEndpoint.class);

		// try {
		try {
			rhServer.start();
			while (true) {
				Thread.sleep(1000);
			}
		} catch (final DeploymentException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			rhServer.stop();
		}
		// final BufferedReader reader = new BufferedReader(
		// new InputStreamReader(System.in));
		// System.out.println("Please press a key to stop the server.");
		// reader.readLine();
		// } catch (final Exception e) {
		// e.printStackTrace();
		// } finally {
		// rhServer.stop();
		// }

	}

}
