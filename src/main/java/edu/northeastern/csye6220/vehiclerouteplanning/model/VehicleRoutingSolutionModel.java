package edu.northeastern.csye6220.vehiclerouteplanning.model;

import java.util.Map;

public class VehicleRoutingSolutionModel {
	
	private Map<String, Route> solution;

	public Map<String, Route> getSolution() {
		return solution;
	}

	public void setSolution(Map<String, Route> solution) {
		this.solution = solution;
	}
	
	// Don't feel there is any point of toString() override, payload will be huge
}
