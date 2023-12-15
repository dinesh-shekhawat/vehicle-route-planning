package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleRoutingProblemModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleRoutingSolutionModel;

public interface VehicleRoutingProblemSolverService {

	VehicleRoutingSolutionModel solve(VehicleRoutingProblemModel problemModel); 
	
}
