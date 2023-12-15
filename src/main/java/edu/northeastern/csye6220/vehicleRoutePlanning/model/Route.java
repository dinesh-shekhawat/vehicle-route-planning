package edu.northeastern.csye6220.vehicleRoutePlanning.model;

import java.util.List;

public class Route {
	private List<LocationModel> stops;
	private List<Point> polyline;

	public List<LocationModel> getStops() {
		return stops;
	}

	public void setStops(List<LocationModel> stops) {
		this.stops = stops;
	}

	public List<Point> getPolyline() {
		return polyline;
	}

	public void setPolyline(List<Point> polyline) {
		this.polyline = polyline;
	}

	@Override
	public String toString() {
		return "Route [stops=" + stops + ", polyline=" + polyline + "]";
	}

}
