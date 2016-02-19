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
package org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.travel_planner;

import it.unical.mat.embasp.mapper.Predicate;
import it.unical.mat.embasp.mapper.Term;

/**
 * Object needed by the EmbASP framework to convert the textual Answer Set into Objects
 *
 * @author Stefano Germano
 *
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
