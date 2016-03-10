package org.insight_centre.urq.citypulse.wp5.decision_support_system;

import it.unical.mat.embasp.base.ASPHandler;
import it.unical.mat.embasp.base.AnswerSet;
import it.unical.mat.embasp.base.AnswerSets;
import it.unical.mat.embasp.clingo.ClingoHandler;
import it.unical.mat.embasp.mapper.IllegalTermException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.insight_centre.urq.citypulse.wp5.Configuration;
import org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.parking_spaces.ParkingSpaceSelected;
import org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.travel_planner.RouteSelected;
import org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.travel_planner.RouteSelectedData;
import org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.travel_planner.RouteSelectedId;
import org.insight_centre.urq.citypulse.wp5.decision_support_system.interfaces.CoreEngineInterface;

import citypulse.commons.data.Coordinate;
import citypulse.commons.data.CoordinateParseException;
import citypulse.commons.reasoning_request.Answer;
import citypulse.commons.reasoning_request.Answers;
import citypulse.commons.reasoning_request.ReasoningRequest;
import citypulse.commons.reasoning_request.concrete.AnswerParkingSpaces;
import citypulse.commons.reasoning_request.concrete.AnswerTravelPlanner;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalParameter;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalPreference;

/**
 * Manages the communication to the ASP solver using the EmbASP framework
 *
 * @author Stefano Germano
 * @author Thu-Le Pham
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email stefano.germano@insight-centre.org
 * @email thule.pham@insight-centre.org
 */
public class CoreEngine implements CoreEngineInterface {

	/**
	 * @param answerSet an AnswerSet for the Parking Space problem
	 * @return a translation of the answerSet into an Answer
	 */
	private Answer getAnswerParkingSpace(final AnswerSet answerSet) {

		final AnswerParkingSpaces answerParkingSpaces = new AnswerParkingSpaces();

		try {

			final Set<Object> answerObjects = answerSet.getAnswerObjects();
			for (final Object object : answerObjects) {
				if (object instanceof ParkingSpaceSelected) {
					final ParkingSpaceSelected parkingSpaceSelected = (ParkingSpaceSelected) object;
					DSManager.Log.info(parkingSpaceSelected.toString());
					answerParkingSpaces.setPosition(new Coordinate(
							parkingSpaceSelected.getPosition()));
					answerParkingSpaces.setAvailablePS(parkingSpaceSelected
							.getAvailablePS());
					answerParkingSpaces
							.setWalking_distance(parkingSpaceSelected
									.getDistance());
				}
			}

		} catch (InvocationTargetException | NoSuchMethodException
				| InstantiationException | IllegalAccessException
				| CoordinateParseException e) {
			DSManager.Log.severe(e.toString());
		}

		return answerParkingSpaces;
	}

	/**
	 * @param answerSet an AnswerSet for the Travel Planner problem
	 * @return a translation of the answerSet into an Answer
	 */
	private Answer getAnswerTravelPlanner(final AnswerSet answerSet) {

		final HashMap<Integer, RouteSelected> routeMap = new HashMap<>();
		final AnswerTravelPlanner answerTravelPlanner = new AnswerTravelPlanner();
		RouteSelectedData routeData = null;
		RouteSelectedId routeSelectedId = null;

		try {

			final Set<Object> answerObjects = answerSet.getAnswerObjects();
			// System.out.println("Thu");
			for (final Object object : answerObjects) {
				if (object instanceof RouteSelectedId) {
					routeSelectedId = (RouteSelectedId) object;
					// System.out.println("Thu_route id");
					DSManager.Log.info(routeSelectedId.toString());
				}
				if (object instanceof RouteSelected) {
					// System.out.println("Thu_route selected");
					final RouteSelected routeSelected = (RouteSelected) object;
					// DSManager.Log.info(routeSelected.toString());
					routeMap.put(routeSelected.getStep(), routeSelected);
				}
				if (object instanceof RouteSelectedData) {
					// System.out.println("Thu_route selected data");
					routeData = (RouteSelectedData) object;
					DSManager.Log.info(routeData.toString());
				}
			}

			// add route id
			if (routeSelectedId != null) {
				answerTravelPlanner.setId(routeSelectedId.getId());
			}
			// FIXME Dangerous code! Just for testing
			routeMap.forEach((key, value) -> {
				try {
					// System.out.println("C: " + value.getPoint());
					answerTravelPlanner.addCoordinate(new Coordinate(value
							.getPoint()));
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			if (routeData != null) {
				answerTravelPlanner.setLength(routeData.getDistance());
				answerTravelPlanner.setNumber_of_seconds(routeData
						.getTravel_time());
				// TODO: get routId
			}

		} catch (InvocationTargetException | NoSuchMethodException
				| InstantiationException | IllegalAccessException e) {
			DSManager.Log.severe(e.toString());
		}

		return answerTravelPlanner;
	}

	/**
	 * @see
	 * org.insight_centre.urq.citypulse.wp5.decision_support_system.interfaces
	 * .CoreEngineInterface
	 * #performReasoning(org.insight_centre.urq.citypulse.commons
	 * .reasoning_request.ReasoningRequest, java.util.List)
	 */
	@Override
	public Answers performReasoning(final ReasoningRequest reasoningRequest,
			final List<String> rules) {

		long starting_time = System.currentTimeMillis();

		final Answers answers = new Answers();

		final String clingoUri = Configuration.getInstance().getClingoPath();
		System.out.println("ClingoURI = " + clingoUri);

		final ASPHandler handler = new ClingoHandler(clingoUri);

		/*
		 * dlvhex specific
		 */
		// handler.addOption("--strongsafety");
		// handler.addOption("--python-plugin="
		// + Configuration.getInstance().getDSSResourceFolderPath()
		// + "external_atoms.py");
		handler.addOption("-n 0");
		handler.addOption("--opt-mode=optN");

		try {

			// String filename = null;
			//
			// switch (reasoningRequest.getArType()) {
			// case TRAVEL_PLANNER:
			// filename = "travel_planner.lp";
			// break;
			// case PARKING_SPACES:
			// filename = "parking_spaces.lp";
			// break;
			// default:
			// break;
			// }
			//
			// if (filename != null)
			// handler.addFileInput(System.getProperty("user.dir")
			// + System.getProperty("file.separator") + "dlvhex"
			// + System.getProperty("file.separator") + filename);

			if (reasoningRequest.getArType() != null) {
				handler.addFileInput(Configuration.getInstance()
						.getDSSResourceFolderPath()
						+ reasoningRequest.getArType().toString().toLowerCase()
						+ ".lp");
			}

			/*
			 * Clingo specific
			 */
			if (reasoningRequest.getArType() != null) {
				handler.addFileInput(Configuration.getInstance()
						.getDSSResourceFolderPath()
						+ reasoningRequest.getArType().toString().toLowerCase()
						+ ".py");
			}

		} catch (final FileNotFoundException e) {
			DSManager.Log.severe(e.toString());
		}

		for (final String rule : rules) {
			handler.addRawInput(rule);
		}

		try {
			for (final FunctionalParameter functionalParameter : reasoningRequest
					.getFunctionalDetails().getFunctionalParameters()
					.getFunctionalParameters()) {
				handler.addInput(functionalParameter);
			}
			for (final FunctionalPreference functionalPreference : reasoningRequest
					.getFunctionalDetails().getFunctionalPreferences()
					.getFunctionalPreferences()) {
				handler.addInput(functionalPreference);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException | IllegalTermException e1) {
			DSManager.Log.severe(e1.toString());
		}


		switch (reasoningRequest.getArType()) {
		case TRAVEL_PLANNER:
			handler.addFilter(RouteSelectedId.class);
			handler.addFilter(RouteSelected.class);
			handler.addFilter(RouteSelectedData.class);
			break;
		case PARKING_SPACES:
			handler.addFilter(ParkingSpaceSelected.class);
			break;
		default:
			break;
		}

		// Not needed for Clingo
		// switch (reasoningRequest.getArType()) {
		// case TRAVEL_PLANNER:
		// handler.addFilter(RouteSelected.class);
		// handler.addFilter(RouteSelectedData.class);
		// break;
		// case PARKING_SPACES:
		// handler.addFilter(ParkingSpaceSelected.class);
		// break;
		// default:
		// break;
		// }
		long ending_time = System.currentTimeMillis();
		DSManager.Log
				.info("DS_DummyCoreEngine_ADD_RULESfromREWRITER_TIME(milisecond): "
						+ (ending_time - starting_time));
		starting_time = ending_time;

		AnswerSets answerSets = null;

		try {
			answerSets = handler.reason();
		} catch (final IOException | InterruptedException e) {
			DSManager.Log.severe(e.toString());
		}

		// ASPMapper.getInstance().registerClass(PathTest.class);
		// ASPMapper.getInstance().registerClass(RouteData.class);
		ending_time = System.currentTimeMillis();
		DSManager.Log
				.info("DS_DummyCoreEngine_REASONING_CLINGO_TIME(Milisecond): "
						+ (ending_time - starting_time));
		starting_time = ending_time;

		if (answerSets != null) {

			final List<AnswerSet> answerSetList = answerSets
					.getAnswerSetsList();

			DSManager.Log
			.info("Number of Answer Sets: " + answerSetList.size());

			if (!answerSetList.isEmpty()) {
				for (final AnswerSet answerSet : answerSetList) {

					DSManager.Log.info(answerSet.getAnswerSet().toString());

					// final Map<Integer, Integer> weightMap =
					// answerSet
					// .getWeightMap();
					// for (final Integer level :
					// weightMap.keySet())
					// System.out.println("[" + level + ":"
					// + weightMap.get(level) + "]");

					switch (reasoningRequest.getArType()) {
					case TRAVEL_PLANNER:
						answers.addAnswer(getAnswerTravelPlanner(answerSet));
						break;
					case PARKING_SPACES:
						answers.addAnswer(getAnswerParkingSpace(answerSet));
						break;
					default:
						break;
					}

				}
			}
		}

		DSManager.Log.info("DS_DummyCoreEngine_GET_RESUTLS_TIME(Milisecond): "
				+ (System.currentTimeMillis() - starting_time));
		return answers;

	}
}
