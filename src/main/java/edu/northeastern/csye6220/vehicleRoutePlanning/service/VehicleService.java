package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import java.util.List;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleModel;

public interface VehicleService {
	
	VehicleModel save(VehicleModel vehicleModel);
	
	List<VehicleModel> saveList(List<VehicleModel> vehicleModels);
	
	List<VehicleModel> findByNameOrRegistration(String query);
	
}
