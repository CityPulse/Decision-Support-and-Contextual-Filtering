package org.insight_centre.urq.citypulse.wp5.request_handler;

import java.util.Date;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.insight_centre.urq.citypulse.wp5.decision_support_system.DSManager;
import org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.travel_planner.GraphUpdater;

import citypulse.commons.contextual_filtering.city_event_ontology.CityEvent;
import citypulse.commons.contextual_filtering.contextual_event_request.Place;
import citypulse.commons.contextual_filtering.contextual_event_request.PlaceAdapter;
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
 * Server Endpoint for the Reasoning Requests for the TravelPlanner scenario
 *
 * @author Stefano Germano, Thu-Le Pham
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email stefano.germano@insight-centre.org, thule.pham@insight-center.org
 */

@ServerEndpoint(value = "/reasoning_request")
public class ReRouteReasoningRequestEndpoint {
	
	GraphUpdater graphUpdater = new GraphUpdater();

	/**
	 * Default constructor
	 */
	public ReRouteReasoningRequestEndpoint() {
	}

	/**
	 * @param session
	 * @param closeReason
	 */
	@OnClose
	public void onClose(final Session session, final CloseReason closeReason) {
		RHManager.Log.info(String.format("Session %s closed because of %s",
				session.getId(), closeReason));
		graphUpdater.resetGraph();
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
		String result = "";
		System.out.println("Received Reasoning Request at: "
				+ new Date().toString());
		System.out.println(message);

		final long starting_time = System.currentTimeMillis();
		
		if(message.contains("ReRouteReasoningRequest")){
			ReRouteReasoningRequest reRouteRequest = parseReRouteReasoningRequest(message);
			
			graphUpdater.updateGraph(reRouteRequest.getEvent());
			result = reasoning(reRouteRequest.getrRequest());
			
		}else{
			ReasoningRequest reasoningRequest;
			try {
				reasoningRequest = parse(message);
			} catch (final Exception e) {
				e.printStackTrace();
				return "Error in ReasoningRequestEndpoint";
			}
			
			result =  reasoning(reasoningRequest);
			System.out.println("DS_TOTAL_PROCESSING_TIME (milisecond): "
					+ (System.currentTimeMillis() - starting_time));
		}
	
		return result; 

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
	
	/**
	 * Produces a ReRouteReasoningRequest from a JSON string
	 * @param message
	 * @return
	 */
	private ReRouteReasoningRequest parseReRouteReasoningRequest(final String message) {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(FunctionalParameterValue.class,
				new FunctionalParameterValueAdapter());
		builder.registerTypeAdapter(FunctionalConstraintValue.class,
				new FunctionalConstraintValueAdapter());
		builder.registerTypeAdapter(Place.class, new PlaceAdapter());
		final Gson gson = builder.create();
		return gson.fromJson(message, ReRouteReasoningRequest.class);
	}
	
	
	/**
	 * 
	 * @param reasoningRequest
	 * @return
	 */
	private String reasoning(ReasoningRequest reasoningRequest){
		try {

			Answers answers = new Answers();

			if (reasoningRequest != null) {
				answers = DSManager.startDSS(reasoningRequest);
			}

			final GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Answer.class, new AnswerAdapter());
			final Gson gson = builder.create();

			System.out.println("Sending Result at: " + new Date().toString());
			return gson.toJson(answers);

		} catch (final Exception e) {
			e.printStackTrace();
			return "Error in DSManager - ReasoningRequestEndpoint";
		}
	
	}
	
}
