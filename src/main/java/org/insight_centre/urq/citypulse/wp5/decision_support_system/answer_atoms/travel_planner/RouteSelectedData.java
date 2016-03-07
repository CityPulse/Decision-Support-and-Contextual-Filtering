package org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.travel_planner;

import it.unical.mat.embasp.mapper.Predicate;
import it.unical.mat.embasp.mapper.Term;

/**
 * Object needed by the EmbASP framework to convert the textual Answer Set into
 * Objects
 *
 * @author Stefano Germano
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email stefano.germano@insight-centre.org
 */
@Predicate("route_selected_data")
public class RouteSelectedData {

	@Term(1)
	private int travel_time;

	@Term(2)
	private int distance;

	@Term(3)
	private int pollution;

	public RouteSelectedData() {
		super();
	}

	public RouteSelectedData(final int travel_time, final int distance,
			final int pollution) {
		super();
		this.travel_time = travel_time;
		this.distance = distance;
		this.pollution = pollution;
	}

	public int getDistance() {
		return distance;
	}

	public int getPollution() {
		return pollution;
	}

	public int getTravel_time() {
		return travel_time;
	}

	public void setDistance(final int distance) {
		this.distance = distance;
	}

	public void setPollution(final int pollution) {
		this.pollution = pollution;
	}

	public void setTravel_time(final int travel_time) {
		this.travel_time = travel_time;
	}

	@Override
	public String toString() {
		return "RouteSelectedData [travel_time=" + travel_time + ", distance="
				+ distance + ", pollution=" + pollution + "]";
	}

}
