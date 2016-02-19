package org.insight_centre.urq.citypulse.wp5.contextual_filter.data;

import java.util.UUID;

import citypulse.commons.data.Coordinate;

public class UserStatus {

	private String userID = "user_" + UUID.randomUUID().toString();
	private Coordinate userCoordinate;
	private Activity userActivity;

	public UserStatus(){
		super();
	}

	public UserStatus(String id, Activity activity, Coordinate coordinate) {
		this.userID = id;
		this.userCoordinate = coordinate;
		this.userActivity = activity;
	}

	public UserStatus (UserStatus us){
		this.userID = us.userID;
		this.userCoordinate = us.userCoordinate;
		this.userActivity = us.userActivity;
	}

	public String getUserID(){
		return this.userID;
	}

	public void setUserID(String id){
		this.userID = id;
	}

	public Coordinate getUserCoordinate() {
		return userCoordinate;
	}

	public void setUserCoordinate(Coordinate userCoordinate) {
		this.userCoordinate = userCoordinate;
	}

	public Activity getUserActivity(){
		return this.userActivity;
	}

	public void setUserActivity(Activity a){
		this.userActivity = a;
	}

	@Override
	public String toString(){
		return "User[ id = " + this.userID + "," + "currentLocation = " + this.userCoordinate.toString() + ", currentActivity = " + this.userActivity+ "]\n ";
	}

	public String parseToAspFacts(){
		final StringBuilder asp = new StringBuilder();
		asp.append("user_status(").append("\"").append(this.userID)
				.append("\"").append(",").append("\"")
				.append(this.userActivity).append("\"").append(",")
				.append("\"").append(this.userCoordinate.toString())
				.append("\"").append(").");
		return asp.toString();
	}

}
