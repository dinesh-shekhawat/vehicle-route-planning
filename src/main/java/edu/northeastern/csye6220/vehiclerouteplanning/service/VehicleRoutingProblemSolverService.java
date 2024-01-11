package edu.northeastern.csye6220.vehiclerouteplanning.service;

import edu.northeastern.csye6220.vehiclerouteplanning.model.VehicleRoutingProblemModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.VehicleRoutingSolutionModel;

public interface VehicleRoutingProblemSolverService {

	VehicleRoutingSolutionModel solve(VehicleRoutingProblemModel problemModel); 
	
}
