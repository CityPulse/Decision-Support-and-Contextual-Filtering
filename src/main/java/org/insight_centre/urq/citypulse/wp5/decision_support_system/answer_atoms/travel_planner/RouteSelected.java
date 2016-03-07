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
@Predicate("route_selected")
public class RouteSelected {

	@Term(1)
	private int step;

	@Term(2)
	private String point;

	public RouteSelected() {
		super();
	}

	public RouteSelected(final int step, final String point) {
		super();
		this.step = step;
		this.point = point;
	}

	public String getPoint() {
		return point;
	}

	public int getStep() {
		return step;
	}

	public void setPoint(final String point) {
		this.point = point;
	}

	public void setStep(final int step) {
		this.step = step;
	}

	@Override
	public String toString() {
		return "RouteSelected [step=" + step + ", point=" + point + "]";
	}

}
