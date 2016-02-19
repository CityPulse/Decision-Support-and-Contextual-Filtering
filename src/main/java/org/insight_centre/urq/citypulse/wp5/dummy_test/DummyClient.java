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
package org.insight_centre.urq.citypulse.wp5.dummy_test;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;
import org.insight_centre.urq.citypulse.wp5.Configuration;

import citypulse.commons.reasoning_request.Answer;
import citypulse.commons.reasoning_request.Answers;
import citypulse.commons.reasoning_request.concrete.AnswerAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Test class for the Request Handler component
 *
 * @author Stefano Germano
 *
 */
// 10.16227 56.12568
// 10.1442012 56.2136502
public class DummyClient {

	private static CountDownLatch messageLatch;

	private static final String SENT_MESSAGE_TRAVEL_PLANNER = "{\"arType\":\"TRAVEL_PLANNER\","
			+ "\"functionalDetails\":{"
			+ "\"functionalConstraints\":{"
			+ "\"functionalConstraints\":[{\"name\":\"POLLUTION\",\"operator\":\"LESS_THAN\",\"value\":\"135\"}]},"
			+ "\"functionalParameters\":{"
			+ "\"functionalParameters\":["
			+ "{\"name\":\"STARTING_DATETIME\",\"value\":\"1434110314540\"},"
			+ "{\"name\":\"STARTING_POINT\",\"value\":\"10.103644989430904 56.232567308059835\"},"
			+ "{\"name\":\"ENDING_POINT\",\"value\":\"10.203921 56.162939\"},"
			+ "{\"name\":\"TRANSPORTATION_TYPE\",\"value\":\"car\"}]},"
			+ "\"functionalPreferences\":{"
			+ "\"functionalPreferences\":[{\"operation\":\"MINIMIZE\",\"value\":\"TIME\",\"order\":1}]}},"
			+ "\"user\":{}}";
	private static final String SENT_MESSAGE_PARKING_SPACES = "{\"arType\":\"PARKING_SPACES\","
			+ "\"functionalDetails\":{"
			+ "\"functionalConstraints\":{"
			+ "\"functionalConstraints\":[{\"name\":\"COST\",\"operator\":\"LESS_THAN\",\"value\":\"100\"}]},"
			+ "\"functionalParameters\":{"
			+ "\"functionalParameters\":["
			+ "{\"name\":\"TIME_OF_STAY\",\"value\":\"100\"},"
			+ "{\"name\":\"DISTANCE_RANGE\",\"value\":\"1000\"},"
			+ "{\"name\":\"STARTING_DATETIME\",\"value\":\"1434106063700\"},"
			+ "{\"name\":\"STARTING_POINT\",\"value\":\"10.16227 56.12568\"},"
			+ "{\"name\":\"POINT_OF_INTEREST\",\"value\":\"10.203921 56.162939\"}]},"
			+ "\"functionalPreferences\":{"
			+ "\"functionalPreferences\":[{\"operation\":\"MINIMIZE\",\"value\":\"WALK\",\"order\":1}]}},"
			+ "\"user\":{}}";

	public static void main(final String[] args) {
		System.out.println("Connecting at: " + (new Date()).toString());
		try {
			DummyClient.messageLatch = new CountDownLatch(2);

			final ClientEndpointConfig cec = ClientEndpointConfig.Builder
					.create().build();

			final ClientManager client = ClientManager.createClient();
			client.connectToServer(new Endpoint() {

				@Override
				public void onOpen(final Session session,
						final EndpointConfig config) {
					try {
						session.addMessageHandler(new MessageHandler.Whole<String>() {
							@Override
							public void onMessage(final String message) {

								System.out.println("Received message: "
										+ message);
								System.out.println("Received messaged at: "
										+ (new Date()).toString());

								final GsonBuilder builder = new GsonBuilder();

								builder.registerTypeAdapter(Answer.class,
										new AnswerAdapter());

								final Gson gson = builder.create();

								System.out.println(gson.fromJson(message,
										Answers.class));

								/*
								 * Get one route for testing CF component
								 */
								// final AnswerTravelPlanner answer =
								// (AnswerTravelPlanner) gson
								// .fromJson(message, Answers.class)
								// .getAnswers().get(0);
								// System.out
								// .println("\nRoute Example: " + answer);
								// System.out
								// .println("Successfully Copied JSON Object to File...");
								// System.out.println("\nJSON Object: "
								// + gson.toJson(answer));
								//
								// try (FileWriter file = new FileWriter(
								// Configuration.getInstance()
								// .getCFResourceFolderPath()
								// + "RouteExample.txt")) {
								// try {
								// file.write(gson.toJson(answer));
								// file.flush();
								// file.close();
								// } catch (final IOException e) {
								// // TODO Auto-generated catch block
								// e.printStackTrace();
								// }
								// } catch (final IOException e1) {
								// // TODO Auto-generated catch block
								// e1.printStackTrace();
								// }

								// ---------------------------

								DummyClient.messageLatch.countDown();
							}
						});
						System.out.println("Sending at: "
								+ (new Date()).toString());
						session.getBasicRemote().sendText(
								DummyClient.SENT_MESSAGE_TRAVEL_PLANNER);
						session.getBasicRemote().sendText(
								DummyClient.SENT_MESSAGE_PARKING_SPACES);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}, cec, new URI("ws://"
					+ "131.227.92.55"
					+ ":"
					// }, cec, new URI("ws://" + "localhost"
					// + ":"
					// }, cec, new URI("ws://" + "localhost" + ":" + "2353"
					+ Configuration.getInstance().getPort()
					+ Configuration.getInstance().getRootPath()
					+ "/reasoning_request"));

			DummyClient.messageLatch.await(100, TimeUnit.SECONDS);
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

}
