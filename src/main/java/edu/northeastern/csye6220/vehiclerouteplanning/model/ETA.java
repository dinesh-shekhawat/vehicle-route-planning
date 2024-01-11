package edu.northeastern.csye6220.vehiclerouteplanning.model;

public class ETA {
	
	private double distance;
	private double time;
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "ETA [distance=" + distance + ", time=" + time + "]";
	}
	
}
