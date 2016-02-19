package org.insight_centre.urq.citypulse.wp5.contextual_filter.data;


public class Effect {
	
	private String eventName;
	private String eventValue;
	private Activity activity;

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
