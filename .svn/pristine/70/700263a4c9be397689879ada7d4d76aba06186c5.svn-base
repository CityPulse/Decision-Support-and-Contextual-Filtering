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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import citypulse.commons.contextual_events_request.ContextualEventRequest;
import citypulse.commons.data.Coordinate;
import citypulse.commons.data.CoordinateParseException;
import citypulse.commons.reasoning_request.ARType;
import citypulse.commons.reasoning_request.Answer;
import citypulse.commons.reasoning_request.Answers;
import citypulse.commons.reasoning_request.ReasoningRequest;
import citypulse.commons.reasoning_request.User;
import citypulse.commons.reasoning_request.concrete.AnswerAdapter;
import citypulse.commons.reasoning_request.concrete.AnswerParkingSpaces;
import citypulse.commons.reasoning_request.concrete.AnswerTravelPlanner;
import citypulse.commons.reasoning_request.concrete.FunctionalConstraintValueAdapter;
import citypulse.commons.reasoning_request.concrete.FunctionalParameterValueAdapter;
import citypulse.commons.reasoning_request.concrete.IntegerFunctionalConstraintValue;
import citypulse.commons.reasoning_request.concrete.StringFunctionalParameterValue;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraint;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraintName;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraintOperator;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraintValue;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraints;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalDetails;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameter;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameterName;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameterValue;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameters;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalPreference;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalPreferenceOperation;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Test class for the Request Handler component 
 *
 * @author Stefano Germano
 *
 */
public class DummyRequestsTest {

	public static void main(final String[] args) {

		final FunctionalParameters functionalParameters = new FunctionalParameters();
		functionalParameters.addFunctionalParameter(new FunctionalParameter(
				FunctionalParameterName.STARTING_POINT,
				new StringFunctionalParameterValue("10.116919 56.226144")));
		functionalParameters.addFunctionalParameter(new FunctionalParameter(
				FunctionalParameterName.ENDING_POINT,
				new StringFunctionalParameterValue("10.1591864 56.1481156")));

		final FunctionalConstraints functionalConstraints = new FunctionalConstraints();
		functionalConstraints.addFunctionalConstraint(new FunctionalConstraint(
				FunctionalConstraintName.POLLUTION,
				FunctionalConstraintOperator.LESS_THAN,
				new IntegerFunctionalConstraintValue(135)));

		final FunctionalPreferences functionalPreferences = new FunctionalPreferences();
		functionalPreferences.addFunctionalPreference(new FunctionalPreference(
				1, FunctionalPreferenceOperation.MINIMIZE,
				FunctionalConstraintName.TIME));
		functionalPreferences.addFunctionalPreference(new FunctionalPreference(
				2, FunctionalPreferenceOperation.MINIMIZE,
				FunctionalConstraintName.DISTANCE));

		final ReasoningRequest reasoningRequest = new ReasoningRequest(
				new User(), ARType.TRAVEL_PLANNER, new FunctionalDetails(
						functionalParameters, functionalConstraints,
						functionalPreferences));

		final List<Coordinate> route = new ArrayList<Coordinate>();
		try {
			route.add(new Coordinate("10.1442042 56.2136402"));
			route.add(new Coordinate("10.2345567 56.2345678"));
		} catch (final CoordinateParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final Answer answer = new AnswerTravelPlanner(route, 10, 20);

		final ContextualEventRequest contextualEventRequest = new ContextualEventRequest(
				reasoningRequest, answer);

		final Answers answers = new Answers();
		try {
			// answers.addAnswer(new AnswerParkingSpaces(new Coordinate(
			// "10.1171296 56.2261545"), 13, 150));
			// answers.addAnswer(new AnswerParkingSpaces(new Coordinate(
			// "10.1442042 56.2136402"), 17, 100));
			final List<Coordinate> list1 = new LinkedList<>();
			list1.add(new Coordinate("10.1542546 56.2105575"));
			list1.add(new Coordinate("10.1544869 56.2104016"));
			list1.add(new Coordinate("10.1550011 56.2102371"));
			list1.add(new Coordinate("10.1550785 56.2101647"));
			list1.add(new Coordinate("10.1550606 56.2100706"));
			final AnswerTravelPlanner answerTravelPlanner1 = new AnswerTravelPlanner(
					list1, 1200, 15000);
			answers.addAnswer(answerTravelPlanner1);
			final List<Coordinate> list2 = new LinkedList<>();
			list2.add(new Coordinate("10.1543473 56.2077227"));
			list2.add(new Coordinate("10.1543388 56.2076823"));
			list2.add(new Coordinate("10.1543326 56.2076354"));
			list2.add(new Coordinate("10.1543366 56.2075876"));
			list2.add(new Coordinate("10.1543478 56.2075505"));
			list2.add(new Coordinate("10.1543629 56.2075121"));
			final AnswerTravelPlanner answerTravelPlanner2 = new AnswerTravelPlanner(
					list2, 1700, 12000);
			answers.addAnswer(answerTravelPlanner2);
		} catch (final CoordinateParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final GsonBuilder builder = new GsonBuilder();

		builder.registerTypeAdapter(FunctionalParameterValue.class,
				new FunctionalParameterValueAdapter());
		builder.registerTypeAdapter(FunctionalConstraintValue.class,
				new FunctionalConstraintValueAdapter());
		builder.registerTypeAdapter(Answer.class, new AnswerAdapter());

		final Gson gson = builder.create();

		final String rrString = gson.toJson(reasoningRequest);
		final String aString = gson.toJson(answers);
		final String testString = gson.toJson(contextualEventRequest);
		System.out.println(rrString);
		System.out.println(aString);
		System.out.println(testString);

		final Answer answerParkingSpaces1 = new AnswerParkingSpaces(
				route.get(0), 12, 150);
		final Answer answerParkingSpaces2 = new AnswerParkingSpaces(
				route.get(0), 12, 150);
		final Answers answers2 = new Answers();
		answers2.addAnswer(answerParkingSpaces1);
		answers2.addAnswer(answerParkingSpaces2);
		final String a2String = gson.toJson(answers2);
		System.out.println(a2String);

		// final String testString =
		// "{\"reasoningRequest\":{\"user\":{},\"arType\":\"TRAVEL_PLANNER\",\"functionalDetails\":{\"functionalParameters\":{\"functionalParameters\":[{\"name\":\"ENDING_POINT\",\"value\":\"10.1591864 56.1481156\"},{\"name\":\"STARTING_POINT\",\"value\":\"10.116919 56.226144\"}]},\"functionalConstraints\":{\"functionalConstraints\":[{\"name\":\"POLLUTION\",\"operator\":\"LESS_THAN\",\"value\":\"135\"}]},\"functionalPreferences\":{\"functionalPreferences\":[{\"order\":2,\"operation\":\"MINIMIZE\",\"value\":\"DISTANCE\"},{\"order\":1,\"operation\":\"MINIMIZE\",\"value\":\"TRAVEL_TIME\"}]}}},\"answer\":{\"route\":[{\"latitude\":10.1442042,\"longitude\":56.2136402},{\"latitude\":10.2345567,\"longitude\":56.2345678}],\"length\":10,\"number_of_seconds\":20,\"arType\":\"TRAVEL_PLANNER\"}}";
		// final String testString2 =
		// "{\"arType\":\"PARKING_SPACES\",\"functionalDetails\":{\"functionalConstraints\":{\"functionalConstraints\":[{\"name\":\"COST\",\"operator\":\"LESS_THAN\",\"value\":\"100\"}]},\"functionalParameters\":{\"functionalParameters\":[{\"name\":\"DISTANCE_RANGE\",\"value\":\"1000\"},{\"name\":\"STARTING_DATETIME\",\"value\":\"1433742151765\"},{\"name\":\"TIME_OF_STAY\",\"value\":\"100\"},{\"name\":\"POINT_OF_INTEREST\",\"value\":\"0.0 0.0\"}]},\"functionalPreferences\":{\"functionalPreferences\":[{\"operation\":\"MINIMIZE\",\"value\":\"COST\",\"order\":1},{\"operation\":\"MINIMIZE\",\"value\":\"DISTANCE\",\"order\":2}]}},\"user\":{}}";
		// System.out.println(gson.fromJson(testString2,
		// ReasoningRequest.class));

		final String testString3 = "{\"answers\":[\"{\\\"position\\\":{\\\"longitude\\\":10.1442042,\\\"latitude\\\":56.2136402},\\\"availablePS\\\":12,\\\"walking_distance\\\":150,\\\"arType\\\":\\\"PARKING_SPACES\\\"}\",\"{\\\"position\\\":{\\\"longitude\\\":10.1442042,\\\"latitude\\\":56.2136402},\\\"availablePS\\\":12,\\\"walking_distance\\\":150,\\\"arType\\\":\\\"PARKING_SPACES\\\"}\"]}";
		System.out.println(gson.fromJson(testString3, Answers.class));

		System.out.println(gson.fromJson(rrString, ReasoningRequest.class));
		System.out.println(gson.fromJson(aString, Answers.class));

		System.out.println(gson.fromJson(testString,
				ContextualEventRequest.class));

	}
}
