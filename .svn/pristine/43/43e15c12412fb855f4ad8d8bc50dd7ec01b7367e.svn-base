/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Stefano Germano - Insight Centre for Data Analytics NUIG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.parking_spaces;

import it.unical.mat.embasp.mapper.Predicate;
import it.unical.mat.embasp.mapper.Term;

/**
 * Object needed by the EmbASP framework to convert the textual Answer Set into Objects
 *
 * @author Stefano Germano
 *
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
