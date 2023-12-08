package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleRoutingProblemModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleRoutingSolutionModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.VehicleRoutingProblemSolverService;

@RestController
@RequestMapping("/vehicle-routing-problem-solver")
public class VehicleRoutingProblemSolverController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VehicleRoutingProblemSolverController.class);
	
	private final VehicleRoutingProblemSolverService problemSolverService;
	
	@Autowired
	public VehicleRoutingProblemSolverController(VehicleRoutingProblemSolverService problemSolverService) {
		this.problemSolverService = problemSolverService;
		LOGGER.info("loaded problemSolverService: {}", problemSolverService);
	}
	
	// TODO Should return a solution instead of the problem statement itself
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public VehicleRoutingSolutionModel solve(@RequestBody(required = false) VehicleRoutingProblemModel model) {
		VehicleRoutingSolutionModel solution = problemSolverService.solve(model);
		return solution;
	}
	
}
