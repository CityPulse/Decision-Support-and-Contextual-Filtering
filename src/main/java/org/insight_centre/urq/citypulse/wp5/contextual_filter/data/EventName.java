package org.insight_centre.urq.citypulse.wp5.contextual_filter.data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class EventName {
	
	private String eventName; 
	private List<String> eventValue = new LinkedList<String>();

	public EventName(String name, List<String> value) {
		// TODO Auto-generated constructor stub
		this.eventName = name;
		this.eventValue = value;
	}
	
	public EventName(String name, String[] value) {
		// TODO Auto-generated constructor stub
		this.eventName = name;
		this.eventValue =  Arrays.asList(value);
	}
	public String getEventName(){
		return eventName;
	}
	
	public List<String> getEventValue(){
		return eventValue;
	}
	
	public String getValue(){
		String temp = eventValue.get(0);
		for(int i = 1 ; i<this.eventValue.size(); i++){
			temp = temp + ";" + this.eventValue.get(i);
		}
		return temp;
	}

}
