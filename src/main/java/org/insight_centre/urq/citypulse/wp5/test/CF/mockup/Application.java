package org.insight_centre.urq.citypulse.wp5.test.CF.mockup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.insight_centre.urq.citypulse.wp5.Configuration;

import citypulse.commons.contextual_filtering.city_event_ontology.EventSource;
import citypulse.commons.contextual_filtering.contextual_event_request.ContextualEventRequest;
import citypulse.commons.contextual_filtering.contextual_event_request.FilteringFactor;
import citypulse.commons.contextual_filtering.contextual_event_request.FilteringFactorName;
import citypulse.commons.contextual_filtering.contextual_event_request.FilteringFactorValue;
import citypulse.commons.contextual_filtering.contextual_event_request.Place;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingElement;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingElementName;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingElementValue;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingFactor;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingFactorType;
import citypulse.commons.contextual_filtering.contextual_event_request.Route;
import citypulse.commons.reasoning_request.Answer;
import citypulse.commons.reasoning_request.ReasoningRequest;
import citypulse.commons.reasoning_request.concrete.AnswerAdapter;
import citypulse.commons.reasoning_request.concrete.AnswerTravelPlanner;
import citypulse.commons.reasoning_request.concrete.FunctionalConstraintValueAdapter;
import citypulse.commons.reasoning_request.concrete.FunctionalParameterValueAdapter;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraintValue;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameterValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * @author Thu-Le Pham
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email thule.pham@insight-centre.org
 */
public class Application {

	public static void main(String[] args) {

		final ContextualEventRequest request = generateContextualEventRequestRoute();
		// final String request = generateOldCERequest();
		final ApplicationClient client1 = new ApplicationClient(request);
		new Thread(client1).start();
		try {
			Thread.sleep(5000);

		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		/*
		 * generate user's coordinates on route
		 *
		 */

	}

	private static String generateOldCERequest() {

		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(FunctionalParameterValue.class,
				new FunctionalParameterValueAdapter());
		builder.registerTypeAdapter(FunctionalConstraintValue.class,
				new FunctionalConstraintValueAdapter());
		builder.registerTypeAdapter(Answer.class, new AnswerAdapter());

		final Gson gson = builder.create();

		final String SENT_MESSAGE_TRAVEL_PLANNER = "{\"arType\":\"TRAVEL_PLANNER\","
				+ "\"functionalDetails\":{"
				+ "\"functionalConstraints\":{"
				+ "\"functionalConstraints\":[{\"name\":\"POLLUTION\",\"operator\":\"LESS_THAN\",\"value\":\"135\"}]},"
				+ "\"functionalParameters\":{"
				+ "\"functionalParameters\":["
				+ "{\"name\":\"STARTING_DATETIME\",\"value\":\"1434110314540\"},"
				+ "{\"name\":\"STARTING_POINT\",\"value\":\"10.16227 56.12568\"},"
				+ "{\"name\":\"ENDING_POINT\",\"value\":\"10.1442012 56.2136502\"},"
				+ "{\"name\":\"TRANSPORTATION_TYPE\",\"value\":\"car\"}]},"
				+ "\"functionalPreferences\":{"
				+ "\"functionalPreferences\":[{\"operation\":\"MINIMIZE\",\"value\":\"TIME\",\"order\":1}]}},"
				+ "\"user\":{}}";
		final ReasoningRequest rrequest = gson.fromJson(SENT_MESSAGE_TRAVEL_PLANNER, ReasoningRequest.class);

		final Route route = loadRouteExample(Configuration.getInstance()
				.getCFResourceFolderPath() + "RouteExample.txt");
		final AnswerTravelPlanner answer = new AnswerTravelPlanner(
				route.getRoute(), route.getLength(), 20);
		final citypulse.commons.contextual_events_request.ContextualEventRequest oldRequest = new citypulse.commons.contextual_events_request.ContextualEventRequest();
		oldRequest.setAnswer(answer);
		oldRequest.setReasoningRequest(rrequest);

		return gson.toJson(oldRequest);
	}

	private static ContextualEventRequest generateContextualEventRequestRoute() {
		/**
		 * ContextualEventRequest example
		 */

		/*
		 * Place of interest
		 */
		final Place place = loadRouteExample(Configuration.getInstance()
				.getCFResourceFolderPath() + "RouteExample.txt");

		/*
		 * Filtering factors
		 */
		final Set<FilteringFactor> filteringFactors = new HashSet<FilteringFactor>();

		final Set<FilteringFactorValue> filteringFactorValueEventSource = new HashSet<FilteringFactorValue>();
		filteringFactorValueEventSource.add(new FilteringFactorValue(
				EventSource.SENSOR.toString()));
		filteringFactorValueEventSource.add(new FilteringFactorValue(
				EventSource.SOCIAL_NETWORK.toString()));
		FilteringFactor filteringFactor = new FilteringFactor(
				FilteringFactorName.EVENT_SOURCE,
				filteringFactorValueEventSource);
		filteringFactors.add(filteringFactor);

		final Set<FilteringFactorValue> filteringFactorValueCategory = new HashSet<FilteringFactorValue>();
		filteringFactorValueCategory.add(new FilteringFactorValue(
				"http://purl.oclc.org/NET/UNIS/sao/ec#" + "TrafficJam"));
		filteringFactorValueCategory.add(new FilteringFactorValue(
				"http://purl.oclc.org/NET/UNIS/sao/ec#" + "PublicParking"));
		filteringFactor = new FilteringFactor(
				FilteringFactorName.EVENT_CATEGORY,
				filteringFactorValueCategory);
		filteringFactors.add(filteringFactor);

		final Set<FilteringFactorValue> filteringFactorValueActivity = new HashSet<FilteringFactorValue>();
		filteringFactorValueActivity
				.add(new FilteringFactorValue("CarCommute"));
		filteringFactorValueActivity
				.add(new FilteringFactorValue("BikeCommute"));
		filteringFactor = new FilteringFactor(FilteringFactorName.ACTIVITY,
				filteringFactorValueActivity);
		filteringFactors.add(filteringFactor);

		/*
		 * Ranking factor
		 */
		final Set<RankingElement> rankingElements = new HashSet<RankingElement>();
		rankingElements.add(new RankingElement(RankingElementName.DISTANCE,
				new RankingElementValue(70)));
		rankingElements.add(new RankingElement(RankingElementName.EVENT_LEVEL,
				new RankingElementValue(30)));

		final RankingFactor rankingFactor = new RankingFactor(
				RankingFactorType.LINEAR, rankingElements);

		/*
		 * create a ContextualEventRequest
		 */
		final ContextualEventRequest request = new ContextualEventRequest(
				place, filteringFactors, rankingFactor);

		return request;
	}

	public static Route loadRouteExample(String routeExampleFile) {
		final Route route = new Route();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(routeExampleFile));
			final StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			final GsonBuilder builder = new GsonBuilder();

			builder.registerTypeAdapter(Answer.class, new AnswerAdapter());

			final Gson gson = builder.create();

			final AnswerTravelPlanner answer = gson.fromJson(sb.toString(),
					AnswerTravelPlanner.class);
			route.setPlaceId("" + answer.getId());
			route.setRoute(answer.getRoute());
			route.setLength(answer.getLength());

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Example route = " + route);
		return route;
	}


}

