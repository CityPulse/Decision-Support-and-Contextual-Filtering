package org.insight_centre.urq.citypulse.wp5.test.DS.dummy_test;

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

import citypulse.commons.contextual_filtering.city_event_ontology.CityEvent;
import citypulse.commons.contextual_filtering.city_event_ontology.EventCategory;
import citypulse.commons.contextual_filtering.city_event_ontology.EventSource;
import citypulse.commons.contextual_filtering.city_event_ontology.EventType;
import citypulse.commons.contextual_filtering.contextual_event_request.Place;
import citypulse.commons.contextual_filtering.contextual_event_request.PlaceAdapter;
import citypulse.commons.contextual_filtering.contextual_event_request.Point;
import citypulse.commons.reasoning_request.Answer;
import citypulse.commons.reasoning_request.Answers;
import citypulse.commons.reasoning_request.ReRouteReasoningRequest;
import citypulse.commons.reasoning_request.ReasoningRequest;
import citypulse.commons.reasoning_request.concrete.AnswerAdapter;
import citypulse.commons.reasoning_request.concrete.FunctionalConstraintValueAdapter;
import citypulse.commons.reasoning_request.concrete.FunctionalParameterValueAdapter;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraintValue;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameterValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Test class for the Request Handler component
 *
 * @author Stefano Germano
 * @author Thu-Le Pham
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email stefano.germano@insight-centre.org
 * @email thule.pham@insight-centre.org
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

	private static final String SENT_MESSAGE_TRAVEL_PLANNER_SWEDEN = "{\"arType\":\"TRAVEL_PLANNER\","
			+ "\"functionalDetails\":{"
			+ "\"functionalConstraints\":{"
			+ "\"functionalConstraints\":[{\"name\":\"POLLUTION\",\"operator\":\"LESS_THAN\",\"value\":\"135\"}]},"
			+ "\"functionalParameters\":{"
			+ "\"functionalParameters\":["
			+ "{\"name\":\"STARTING_DATETIME\",\"value\":\"1434110314540\"},"
			+ "{\"name\":\"STARTING_POINT\",\"value\":\"18.024991 59.345464\"},"
			+ "{\"name\":\"ENDING_POINT\",\"value\":\"18.055983 59.344275\"},"
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

	private static final String SENT_MESSAGE_PARKING_SPACES1 = "{\"arType\":\"PARKING_SPACES\","
			+ "\"functionalDetails\":{"
			+ "\"functionalConstraints\":{"
			+ "\"functionalConstraints\":[{\"name\":\"COST\",\"operator\":\"LESS_THAN\",\"value\":\"100\"}]},"
			+ "\"functionalParameters\":{"
			+ "\"functionalParameters\":["
			+ "{\"name\":\"TIME_OF_STAY\",\"value\":\"100\"},"
			+ "{\"name\":\"DISTANCE_RANGE\",\"value\":\"1000\"},"
			+ "{\"name\":\"STARTING_DATETIME\",\"value\":\"1461750048469\"},"
			// +
			// "{\"name\":\"STARTING_POINT\",\"value\":\"10.16227 56.12568\"},"
			+ "{\"name\":\"POINT_OF_INTEREST\",\"value\":\"10.205143 56.154404\"}]},"
			+ "\"functionalPreferences\":{"
			+ "\"functionalPreferences\":[{\"order\":2,\"operation\":\"MINIMIZE\",\"value\":\"DISTANCE\"},"
			+ "{\"order\":1,\"operation\":\"MINIMIZE\",\"value\":\"COST\"}]}},"
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

								DummyClient.messageLatch.countDown();
							}
						});
						System.out.println("Sending at: "
								+ (new Date()).toString());
//						 session.getBasicRemote().sendText(
//						 DummyClient.SENT_MESSAGE_TRAVEL_PLANNER_SWEDEN);
//						session.getBasicRemote().sendText(
//								DummyClient.SENT_MESSAGE_TRAVEL_PLANNER);

						session.getBasicRemote().sendText(
								DummyClient.SENT_MESSAGE_PARKING_SPACES);

//						session.getBasicRemote().sendText(createReRoute());
						
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

			DummyClient.messageLatch.await(180, TimeUnit.SECONDS);
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
	
  public static String createReRoute(){
		
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
		event.setEventPlace(new Point(10.1983878, 56.1812222));
		event.setContextualCriticality(3);
		
		
		ReRouteReasoningRequest request = new ReRouteReasoningRequest(rRequest, event);
		
		String requestStr = gsonForReRoute().toJson(request);	
		System.out.println(requestStr);
		
		ReRouteReasoningRequest r = gsonForReRoute().fromJson(requestStr, ReRouteReasoningRequest.class);
		System.out.println(r.toString());
				
		return requestStr;
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
