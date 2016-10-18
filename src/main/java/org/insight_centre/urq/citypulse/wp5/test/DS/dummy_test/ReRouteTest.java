package org.insight_centre.urq.citypulse.wp5.test.DS.dummy_test;

import citypulse.commons.contextual_filtering.city_event_ontology.CityEvent;
import citypulse.commons.contextual_filtering.city_event_ontology.EventCategory;
import citypulse.commons.contextual_filtering.city_event_ontology.EventSource;
import citypulse.commons.contextual_filtering.city_event_ontology.EventType;
import citypulse.commons.contextual_filtering.contextual_event_request.Place;
import citypulse.commons.contextual_filtering.contextual_event_request.PlaceAdapter;
import citypulse.commons.contextual_filtering.contextual_event_request.Point;
import citypulse.commons.reasoning_request.ReRouteReasoningRequest;
import citypulse.commons.reasoning_request.ReasoningRequest;
import citypulse.commons.reasoning_request.concrete.FunctionalConstraintValueAdapter;
import citypulse.commons.reasoning_request.concrete.FunctionalParameterValueAdapter;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraintValue;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameterValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReRouteTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		createReRoute();
	}
	
	/**
	 * mock up the ReRouteReasoningRequest
	 * @return
	 */
	static public ReRouteReasoningRequest createReRoute(){
		
		String REASONING_REQUEST = "{\"arType\":\"TRAVEL_PLANNER\","
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
		
		ReasoningRequest rRequest = parse(REASONING_REQUEST);
		
		CityEvent event = new CityEvent();
		event.setEventId("http://purl.oclc.org/NET/UNIS/sao/sao#6c10eb39-74f6-499e-9a14-0455a7b9cb2c");
		event.setEventType(new EventType("http://purl.oclc.org/NET/UNIS/sao/ec#TransportationEvent"));
		event.setEventCategory(new EventCategory("http://purl.oclc.org/NET/UNIS/sao/ec#TrafficJam"));
		event.setEventSource(EventSource.SENSOR);
		event.setEventLevel(1);
		event.setEventPlace(new Point(10.2049941,56.1650268));
		event.setContextualCriticality(3);
		
		
		ReRouteReasoningRequest request = new ReRouteReasoningRequest(rRequest, event);
		
		String requestStr = gsonForReRoute().toJson(request);	
		System.out.println(requestStr);
		
		ReRouteReasoningRequest r = gsonForReRoute().fromJson(requestStr, ReRouteReasoningRequest.class);
		System.out.println(r.toString());
				
		return null;
	}
	
	private static ReasoningRequest parse(final String message) {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(FunctionalParameterValue.class,
				new FunctionalParameterValueAdapter());
		builder.registerTypeAdapter(FunctionalConstraintValue.class,
				new FunctionalConstraintValueAdapter());
		final Gson gson = builder.create();
		return gson.fromJson(message, ReasoningRequest.class);
	}
	
	private static CityEvent parseEvent(String requestStr) {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Place.class, new PlaceAdapter());
		final Gson gson = builder.create();
		final CityEvent ceRequest = gson.fromJson(requestStr,
				CityEvent.class);

		return ceRequest;
	}
	
	private static Gson gsonForReRoute() {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(FunctionalParameterValue.class,
				new FunctionalParameterValueAdapter());
		builder.registerTypeAdapter(FunctionalConstraintValue.class,
				new FunctionalConstraintValueAdapter());
		builder.registerTypeAdapter(Place.class, new PlaceAdapter());
		
		final Gson gson = builder.create();
		return gson;
	}
	


}
