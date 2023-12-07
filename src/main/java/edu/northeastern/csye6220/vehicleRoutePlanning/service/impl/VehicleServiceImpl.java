package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService {
	private static final Logger LOGGER = LoggerFactory.getLogger(VehicleServiceImpl.class);

	// TODO Keeping as temporary placeholder till hiberante connection is not done
	private List<VehicleModel> data = new ArrayList<>();
	
	@Override
	public VehicleModel save(VehicleModel vehicleModel) {
		// TODO LOGIC WIll change when hiberate is integrated
		LOGGER.trace("saving vehicle: {}", vehicleModel);
		data.add(vehicleModel);
		return vehicleModel;
	}

	@Override
	public List<VehicleModel> findByNameOrRegistration(String query) {
	    // TODO: Replace this logic with Hibernate-based implementation
		
		if (query == null) {
			LOGGER.warn("null query passed, it should not be called like that");
			return data;
		}
		
		LOGGER.trace("querying vehicles for: {}", query);
		
		List<VehicleModel> result = new ArrayList<>();

	    for (VehicleModel vehicle : data) {
	        if (vehicle.getName().toLowerCase().contains(query.toLowerCase().trim()) ||
	            vehicle.getRegistration().toLowerCase().contains(query.toLowerCase().trim())) {
	            result.add(vehicle);
	        }
	    }

	    return result;
	}

	@Override
	public List<VehicleModel> saveList(List<VehicleModel> vehicleModels) {
		LOGGER.debug("saving vehicles of size: {}", vehicleModels != null ? vehicleModels.size() : "NULL");
		
		if (vehicleModels == null) {
			LOGGER.warn("null request body passed, it should not be called like that");
			return Collections.emptyList();
		}
		
		if (LOGGER.isTraceEnabled()) {
			for (VehicleModel vehicleModel : vehicleModels) {
				LOGGER.trace("vehicle: {}", vehicleModel);
			}
		}
		
		// TODO LOGIC WIll change when hiberate is integrated
		data.addAll(vehicleModels);
		return vehicleModels;
	}

}
