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
@Predicate("route_selected_id")
public class RouteSelectedId {
	/**
	 *
	 */
	@Term(1)
	private int id;

	/**
	 *
	 */

	public RouteSelectedId() {
		super();
	}

	public RouteSelectedId(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "RouteSelectedId [id=" + id + "]";
	}

}
