package org.insight_centre.urq.citypulse.wp5.request_handler;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.insight_centre.urq.citypulse.wp5.contextual_filtering.contextual_event_system.ContextualEventFilter;

import citypulse.commons.contextual_filtering.contextual_event_request.ContextualEventRequest;
import citypulse.commons.contextual_filtering.contextual_event_request.FilteringFactor;
import citypulse.commons.contextual_filtering.contextual_event_request.FilteringFactorName;
import citypulse.commons.contextual_filtering.contextual_event_request.FilteringFactorValue;
import citypulse.commons.contextual_filtering.contextual_event_request.Place;
import citypulse.commons.contextual_filtering.contextual_event_request.PlaceAdapter;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingElement;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingElementName;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingElementValue;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingFactor;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingFactorType;
import citypulse.commons.contextual_filtering.contextual_event_request.Route;
import citypulse.commons.contextual_filtering.user_context_ontology.UserGPSCoordinate;
import citypulse.commons.data.Coordinate;
import citypulse.commons.data.CoordinateParseException;
import citypulse.commons.reasoning_request.Answer;
import citypulse.commons.reasoning_request.concrete.AnswerAdapter;
import citypulse.commons.reasoning_request.concrete.AnswerTravelPlanner;
import citypulse.commons.reasoning_request.concrete.FunctionalConstraintValueAdapter;
import citypulse.commons.reasoning_request.concrete.FunctionalParameterValueAdapter;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraintValue;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameterValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class is the server endpoint of the Contextual Filtering component
 * 
 * @author Thu-Le Pham
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email thule.pham@insight-centre.org
 */

@ServerEndpoint(value = "/contextual_events_request")
public class ContextualEventsRequestEndpoint {

	public static Logger Log = Logger
			.getLogger(ContextualEventsRequestEndpoint.class.getPackage()
					.getName());
	/*
	 *
	 */
	private ContextualEventFilter cf;

	/**
	 *
	 */
	public ContextualEventsRequestEndpoint() {
	}

	/**
	 * @param session
	 * @param closeReason
	 */
	@OnClose
	public void onClose(final Session session, final CloseReason closeReason) {
		Log.info(String.format("Session %s closed because of %s",
				session.getId(), closeReason));
		cf.stopCFF();
		cf = null;
	}

	/**
	 * @param session
	 * @param t
	 */
	@OnError
	public void onError(final Session session, final Throwable t) {
		Log.severe(t.getMessage());
	}

	/**
	 * @param message
	 * @param session
	 * @return
	 * @throws CoordinateParseException
	 */
	@OnMessage
	public synchronized void onMessage(final String message,
			final Session session) throws CoordinateParseException {
		Log.info("Received message: " + message);

		// 1 user has only 1 connection to ContextualEventsRequestEndpointMockup
		// 1 user can send different types of message such as:
		// ContextualEventRequest, (GPS)Coordinate
		if (message.contains("ContextualEventRequest")) {
			// received contextual_event_request
			final ContextualEventRequest ceRequest = parse(message);
			if (cf != null) {
				// final ContextualEvent event = new ContextualEvent();
				// event.setCeName("....");
				// event.setCeType("TrafficJam");
				// event.setCeLevel(1);
				// event.setCeWeight(1);
				// session.getAsyncRemote().sendText(new Gson().toJson(event));

				cf.startCEF(ceRequest);
			} else {
				Log.info("The Contextual Filter is null!");
			}
		} else if (message.contains("UserGPSCoordinate")) {
			// received user's coordinate
			final Coordinate userCoor = (new Gson().fromJson(message,
					UserGPSCoordinate.class)).getUserCoordinate();
			if (cf != null) {
				cf.receiveUserGPSCoordinate(userCoor);
			} else {
				Log.info("The Contextual Filter is null!");
			}

		} else if (message.contains("reasoningRequest")) {
			final ContextualEventRequest ceRequest = parseOldCERequest(message);
			if (cf != null) {
				cf.startCEF(ceRequest);
			} else {
				Log.info("The Contextual Filter is null!");
			}
		}

	}

	/**
	 * @param session
	 */
	@OnOpen
	public void onOpen(final Session session) {
		Log.info("Connected ... " + session.getId());
		cf = new ContextualEventFilter(session);
	}

	/**
	 * This function parses message from String to ContextualEventRequest
	 *
	 * @param message
	 * @return ContextualEventRequest
	 */
	private ContextualEventRequest parse(final String message) {

		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Place.class, new PlaceAdapter());
		final Gson gson = builder.create();
		final ContextualEventRequest ceRequest =  gson.fromJson(message, ContextualEventRequest.class);
		Log.info("After parsing = " + ceRequest.toString());
		return ceRequest;
	}

	/**
	 * This is temporal function if Application send old ContextualEventRequest
	 *
	 * @param message
	 */
	private ContextualEventRequest parseOldCERequest(final String message) {
		// parse abstract class in ReasoningRequest
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(FunctionalParameterValue.class,
				new FunctionalParameterValueAdapter());
		builder.registerTypeAdapter(FunctionalConstraintValue.class,
				new FunctionalConstraintValueAdapter());
		builder.registerTypeAdapter(Answer.class, new AnswerAdapter());

		final Gson gson = builder.create();
		Log.info("Done parse contextual event request....");
		final citypulse.commons.contextual_events_request.ContextualEventRequest oldRequest = gson
				.fromJson(
						message,
						citypulse.commons.contextual_events_request.ContextualEventRequest.class);

		// transform Old ContextualEventRequest to New ContextualEventRequest


		/*
		 * Place of interest
		 */
		final AnswerTravelPlanner answerTP = (AnswerTravelPlanner) oldRequest
				.getAnswer();
		final Route place = new Route();
		place.setPlaceId(answerTP.getId() + "");
		place.setRoute(answerTP.getRoute());
		place.setLength(answerTP.getLength());

		/*
		 * Filtering factors
		 */
		final Set<FilteringFactor> filteringFactors = new HashSet<FilteringFactor>();

		// final Set<FilteringFactorValue> filteringFactorValueEventSource = new
		// HashSet<FilteringFactorValue>();
		// filteringFactorValueEventSource.add(new FilteringFactorValue(
		// EventSource.SENSOR.toString()));
		// filteringFactorValueEventSource.add(new FilteringFactorValue(
		// EventSource.SOCIAL_NETWORK.toString()));
		// FilteringFactor filteringFactor = new FilteringFactor(
		// FilteringFactorName.EVENT_SOURCE,
		// filteringFactorValueEventSource);
		// filteringFactors.add(filteringFactor);

		// final Set<FilteringFactorValue> filteringFactorValueCategory = new
		// HashSet<FilteringFactorValue>();
		// filteringFactorValueCategory
		// .add(new FilteringFactorValue("TrafficJam"));
		// filteringFactorValueCategory.add(new FilteringFactorValue(
		// "PublicParking"));
		// filteringFactor = new FilteringFactor(
		// FilteringFactorName.EVENT_CATEGORY,
		// filteringFactorValueCategory);
		// filteringFactors.add(filteringFactor);

		final Set<FilteringFactorValue> filteringFactorValueActivity = new HashSet<FilteringFactorValue>();
		filteringFactorValueActivity
				.add(new FilteringFactorValue("CarCommute"));
		// filteringFactorValueActivity
		// .add(new FilteringFactorValue("BikeCommute"));
		final FilteringFactor filteringFactor = new FilteringFactor(
				FilteringFactorName.ACTIVITY,
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
}
