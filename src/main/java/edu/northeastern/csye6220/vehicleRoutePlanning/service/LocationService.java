package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import java.util.List;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.LocationModel;

public interface LocationService {
	
	LocationModel save(LocationModel vehicleModel);
	
	List<LocationModel> saveList(List<LocationModel> vehicleModels);
	
	List<LocationModel> findByName(String query);
	
}
