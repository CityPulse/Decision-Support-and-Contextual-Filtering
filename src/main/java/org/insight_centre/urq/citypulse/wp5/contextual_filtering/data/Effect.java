package org.insight_centre.urq.citypulse.wp5.contextual_filtering.data;

/**
 *
 * @author Thu-Le Pham
 * @organization INSIGHT, NUIG, Galway, Ireland
 * @email thule.pham@insight-centre.org
 */

public class Effect {

	private final String eventName;
	private final String eventValue;
	private final Activity activity;

	public Effect(String name, String value, Activity a) {
		// TODO Auto-generated constructor stub
		this.eventName = name;
		this.eventValue = value;
		this.activity = a;
	}

	public String getEventName(){
		return this.eventName;
	}

	public String getEventValue(){
		return this.eventValue;
	}

	public Activity getActivity(){
		return this.activity;
	}

}
