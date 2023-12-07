package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.LocationModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);

	 // File to store data
    private static final String DATA_FILE_PATH = "/home/ubuntu/NEU/semester-4/CSYE_6220-Enterprise_Software_Design/vrp-storage/locations.json";
	
	@Override
	public LocationModel save(LocationModel vehicleModel) {
		// TODO LOGIC WIll change when hiberate is integrated
		LOGGER.trace("saving vehicle: {}", vehicleModel);
		List<LocationModel> data = loadDataFromFile();
		data.add(vehicleModel);
		saveDataToFile(data);
		return vehicleModel;
	}

	@Override
	public List<LocationModel> findByName(String query) {
	    // TODO: Replace this logic with Hibernate-based implementation
		
		List<LocationModel> data = loadDataFromFile();
		
		if (query == null) {
			LOGGER.warn("null query passed, it should not be called like that");
			return data;
		}
		
		LOGGER.trace("querying vehicles for: {}", query);
		
		List<LocationModel> result = new ArrayList<>();

	    for (LocationModel location : data) {
	        if (location.getName().toLowerCase().contains(query.toLowerCase().trim()) ) {
	            result.add(location);
	        }
	    }

	    return result;
	}

	@Override
	public List<LocationModel> saveList(List<LocationModel> vehicleModels) {
		LOGGER.debug("saving vehicles of size: {}", vehicleModels != null ? vehicleModels.size() : "NULL");
		
		if (vehicleModels == null) {
			LOGGER.warn("null request body passed, it should not be called like that");
			return Collections.emptyList();
		}
		
		if (LOGGER.isTraceEnabled()) {
			for (LocationModel vehicleModel : vehicleModels) {
				LOGGER.trace("vehicle: {}", vehicleModel);
			}
		}
		
		// TODO LOGIC WIll change when hiberate is integrated
		List<LocationModel> data = loadDataFromFile();
		data.addAll(vehicleModels);
		saveDataToFile(data);
		return vehicleModels;
	}
	
	// Helper method to load data from file
    private List<LocationModel> loadDataFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(DATA_FILE_PATH), new TypeReference<List<LocationModel>>() {});
        } catch (Exception e) {
            LOGGER.warn("Error loading data from file", e);
            return new ArrayList<>();
        }
    }
    
    // Helper method to save data to file
    private void saveDataToFile(List<LocationModel> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE_PATH), data);
            LOGGER.info("Data saved to {}", DATA_FILE_PATH);
        } catch (Exception e) {
            LOGGER.error("Error saving data to file", e);
        }
    }

}