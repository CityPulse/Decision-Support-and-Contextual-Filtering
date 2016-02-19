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
package org.insight_centre.urq.citypulse.wp5.contextual_filter.mockup;

import java.util.logging.Logger;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;
import org.insight_centre.urq.citypulse.wp5.Configuration;

import citypulse.commons.data.CoordinateParseException;

/**
 * @author Stefano Germano
 *
 */
public class RHManager {

	/**
	 *
	 */
	public static Logger Log = Logger.getLogger(RHManager.class.getPackage()
			.getName());

	/**
	 * @param args
	 * @throws CoordinateParseException
	 */
	public static void main(final String[] args)
			throws CoordinateParseException {
		// subscribeEvents();
		new RHManager().startRH(Configuration.getInstance().getHostName(),
				Configuration.getInstance().getPort(), Configuration
						.getInstance().getRootPath());
	}

	/**
	 * @param hostName
	 * @param port
	 * @param rootPath
	 */
	private void startRH(final String hostName, final int port,
			final String rootPath) {

		RHManager.Log.info("started " + this.getClass().getName());

		final Server rhServer = new Server(hostName, port, rootPath, null,
				ContextualEventsRequestEndpointMockup.class);

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

	}

}
