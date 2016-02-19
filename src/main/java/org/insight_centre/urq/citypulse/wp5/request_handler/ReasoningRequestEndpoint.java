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

import java.util.Date;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.insight_centre.urq.citypulse.wp5.decision_support_system.DSManager;

import citypulse.commons.reasoning_request.Answer;
import citypulse.commons.reasoning_request.Answers;
import citypulse.commons.reasoning_request.ReasoningRequest;
import citypulse.commons.reasoning_request.concrete.AnswerAdapter;
import citypulse.commons.reasoning_request.concrete.FunctionalConstraintValueAdapter;
import citypulse.commons.reasoning_request.concrete.FunctionalParameterValueAdapter;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraintValue;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameterValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Server Endpoint for the Reasoning Requests
 *
 * @author Stefano Germano
 *
 */
@ServerEndpoint(value = "/reasoning_request")
public class ReasoningRequestEndpoint {

	/**
	 * Default constructor
	 */
	public ReasoningRequestEndpoint() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param session
	 * @param closeReason
	 */
	@OnClose
	public void onClose(final Session session, final CloseReason closeReason) {
		RHManager.Log.info(String.format("Session %s closed because of %s",
				session.getId(), closeReason));
	}

	/**
	 * Produces a Reasoning Request, calls the Decision Support System, gets the Answers, parse the Answers into JSON and return them
	 *
	 * @param message
	 * @param session
	 * @return the answers obtained by the Decision Support System
	 */
	@OnMessage
	public String onMessage(final String message, final Session session) {

		System.out.println("Received Reasoning Request at: "
				+ new Date().toString());
		System.out.println(message);

		final long starting_time = System.currentTimeMillis();

		ReasoningRequest reasoningRequest;
		try {
			reasoningRequest = parse(message);
		} catch (final Exception e) {
			e.printStackTrace();
			return "Error in ReasoningRequestEndpoint";
		}

		// FIXME for testing
		try {

			Answers answers = new Answers();

			if (reasoningRequest != null) {
				answers = DSManager.startDSS(reasoningRequest);
			}

			// final List<Answer> answers = new LinkedList<>();
			// answers.add(new AnswerParkingSpaces(new Coordinate(
			// "10.1171296 56.2261545"), 13, 150));
			// answers.add(new AnswerParkingSpaces(new Coordinate(
			// "10.1442042 56.2136402"), 17, 100));

			// final Answers answers = new Answers();
			//
			// if (reasoningRequest != null)
			// switch (reasoningRequest.getArType()) {
			// case TRAVEL_PLANNER:
			// final List<Coordinate> list1 = new LinkedList<>();
			// list1.add(new Coordinate("10.1542546 56.2105575"));
			// list1.add(new Coordinate("10.1544869 56.2104016"));
			// list1.add(new Coordinate("10.1550011 56.2102371"));
			// list1.add(new Coordinate("10.1550785 56.2101647"));
			// list1.add(new Coordinate("10.1550606 56.2100706"));
			// final AnswerTravelPlanner answerTravelPlanner1 = new
			// AnswerTravelPlanner(
			// list1, 1200, 15000);
			// answers.addAnswer(answerTravelPlanner1);
			// final List<Coordinate> list2 = new LinkedList<>();
			// list2.add(new Coordinate("10.1543473 56.2077227"));
			// list2.add(new Coordinate("10.1543388 56.2076823"));
			// list2.add(new Coordinate("10.1543326 56.2076354"));
			// list2.add(new Coordinate("10.1543366 56.2075876"));
			// list2.add(new Coordinate("10.1543478 56.2075505"));
			// list2.add(new Coordinate("10.1543629 56.2075121"));
			// final AnswerTravelPlanner answerTravelPlanner2 = new
			// AnswerTravelPlanner(
			// list2, 1700, 12000);
			// answers.addAnswer(answerTravelPlanner2);
			// break;
			// case PARKING_SPACES:
			// answers.addAnswer(new AnswerParkingSpaces(new Coordinate(
			// "10.1171296 56.2261545"), 13, 150));
			// answers.addAnswer(new AnswerParkingSpaces(new Coordinate(
			// "10.1442042 56.2136402"), 17, 100));
			// break;
			// default:
			// break;
			// }

			final GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Answer.class, new AnswerAdapter());
			final Gson gson = builder.create();

			System.out.println("DS_TOTAL_PROCESSING_TIME (milisecond): "
					+ (System.currentTimeMillis() - starting_time));

			System.out.println("Sending Result at: " + new Date().toString());
			return gson.toJson(answers);

		} catch (final Exception e) {
			e.printStackTrace();
			return "Error in DSManager - ReasoningRequestEndpoint";
		}

		// FIXME at the moment only echo
		// return message + "\n(echo by ReasoningRequestEndpoint)";

	}

	/**
	 * @param session
	 */
	@OnOpen
	public void onOpen(final Session session) {
		RHManager.Log.info("Connected ... " + session.getId());
	}

	/**
	 * Produces a Reasoning Request from a JSON string
	 *
	 * @param message a string containing the JSON representation of a Reasoning Request
	 * @return the Reasoning Request
	 */
	private ReasoningRequest parse(final String message) {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(FunctionalParameterValue.class,
				new FunctionalParameterValueAdapter());
		builder.registerTypeAdapter(FunctionalConstraintValue.class,
				new FunctionalConstraintValueAdapter());
		final Gson gson = builder.create();
		return gson.fromJson(message, ReasoningRequest.class);
	}

}
