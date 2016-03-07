package org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.parking_spaces;

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
@Predicate("parking_space_selected")
public class ParkingSpaceSelected {

	@Term(1)
	public String position;

	@Term(2)
	public int availablePS;

	@Term(3)
	public int distance;

	public ParkingSpaceSelected() {
		super();
	}

	public ParkingSpaceSelected(final String position, final int availablePS,
			final int distance) {
		super();
		this.position = position;
		this.availablePS = availablePS;
		this.distance = distance;
	}

	public int getAvailablePS() {
		return availablePS;
	}

	public int getDistance() {
		return distance;
	}

	public String getPosition() {
		return position;
	}

	public void setAvailablePS(final int availablePS) {
		this.availablePS = availablePS;
	}

	public void setDistance(final int distance) {
		this.distance = distance;
	}

	public void setPosition(final String position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "PakingSpaceSelected [position=" + position + ", availablePS="
				+ availablePS + ", distance=" + distance + "]";
	}

}
