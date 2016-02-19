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
package org.insight_centre.urq.citypulse.wp5;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Allows to get the data from the configuration file
 * It's a singleton
 *
 * @author Stefano Germano
 *
 */
public class Configuration {

	/**
	 * The only instance of the class (Singleton pattern)
	 */
	private static final Configuration INSTANCE = new Configuration();

	/**
	 * @return the only instance of the class (Singleton pattern)
	 */
	public static Configuration getInstance() {
		return Configuration.INSTANCE;
	}

	/**
	 * Reference to the Properties object
	 */
	private Properties prop = null;

	/**
	 * Reference to the root folder of the resouces
	 */
	private String resourceFolder = null;

	/**
	 * Private Constructor (Singleton pattern)
	 */
	private Configuration() {
		initialize();
	}

	/**
	 * @return the path to the folder containing the resources needed by the Contextual Filter
	 */
	public String getCFResourceFolderPath() {
		return resourceFolder + "cf" + File.separator;
	}


	/**
	 * @return the path to the folder containing the resources needed by the Decision Support System
	 */
	public String getDSSResourceFolderPath() {
		return resourceFolder + "dss" + File.separator;
	}

	/**
	 * @ Thu-Le Pham
	 *
	 * @return
	 */
	public String getEventRabbitURI() {
		if (prop == null) {
			return "amqp://guest:guest@140.203.155.76:8009";
		}
		return prop.getProperty("eventRabbitURI");
	}

	/**
	 * @return the hostname of the Request Handler server
	 */
	public String getHostName() {
		if (prop == null) {
			return "localhost";
		}
		return prop.getProperty("hostname");
	}

	/**
	 * @return the port of the Request Handler server
	 */
	public int getPort() {
		if (prop == null) {
			return 8005;
		}
		int parseInt;
		try {
			parseInt = Integer.parseInt(prop.getProperty("port"));
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			return 8005;
		}
		return parseInt;
	}

	/**
	 * @return the root folder of the resouces
	 */
	public String getResourceFolderPath() {
		return resourceFolder;
	}

	/**
	 * @ Thu-Le Pham
	 *
	 * @return
	 */
	public String getRestResource() {
		if (prop == null) {
			return "http://131.227.92.55:8008/citypulse_eventprocessing-0.0.1-SNAPSHOT/";
		}
		return prop.getProperty("restResource");
	}

	/**
	 * @return the base path of the Request Handler server
	 */
	public String getRootPath() {
		if (prop == null) {
			return "/websockets";
		}
		return prop.getProperty("rootpath");
	}

	/**
	 * Initializes the resourceFolder and the prop objects
	 */
	private void initialize() {

		try {
			// resourceFolder = new
			// File(Configuration.class.getProtectionDomain()
			// .getCodeSource().getLocation().getPath()).getParentFile()
			// .getAbsolutePath()
			// + File.separator
			// + "res"
			// + File.separator;
			resourceFolder = System.getProperty("user.dir") + File.separator
					+ "res" + File.separator;

		} catch (final Exception e2) {
			System.err.println("Could find the res folder path.");
			System.err.println(e2);
			System.err.println("Falling back to defaults.");
			resourceFolder = null;
		}

		// try {
		// final ClassLoader loader = Thread.currentThread()
		// .getContextClassLoader();
		// Configuration.prop = new Properties();
		// final InputStream resourceStream = loader
		// .getResourceAsStream(resourceName);
		// Configuration.prop.load(resourceStream);
		// } catch (final Exception e) {
		try {
			prop = new Properties();
			prop.load(new FileInputStream(getResourceFolderPath()
					+ "config.properties"));
		} catch (final Exception e2) {
			System.err.println("Could not open configuration file.");
			System.err.println(e2);
			System.err.println("Falling back to defaults.");
			prop = null;
		}

	}

	/**
	 * @return <code>true</code> if the debug mode is active
	 */
	public boolean isDebugMode() {
		if (prop == null) {
			return false;
		}
		final String b = prop.getProperty("debug");
		final boolean retVal = !b.equals("false") && !b.equals("no")
				&& !b.equals("0");
		return retVal;
	}

}
