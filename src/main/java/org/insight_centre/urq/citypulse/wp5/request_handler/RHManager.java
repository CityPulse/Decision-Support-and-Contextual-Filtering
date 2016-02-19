/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Stefano Germano - Insight Centre for Data Analytics NUIG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package org.insight_centre.urq.citypulse.wp5.request_handler;

import java.util.logging.Logger;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;
import org.insight_centre.urq.citypulse.wp5.Configuration;

/**
 * The manager of the Request Handler server
 *
 * @author Stefano Germano
 *
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
				ReasoningRequestEndpoint.class,
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
