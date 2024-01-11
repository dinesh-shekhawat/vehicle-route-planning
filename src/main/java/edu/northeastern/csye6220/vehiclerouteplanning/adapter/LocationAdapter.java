package edu.northeastern.csye6220.vehiclerouteplanning.adapter;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.Location;
import edu.northeastern.csye6220.vehiclerouteplanning.model.LocationModel;

public class LocationAdapter {

    public static Location createRequest(LocationModel locationModel) {
        Location location = new Location();
        location.setName(locationModel.getName());
        location.setLatitude(locationModel.getLatitude());
        location.setLongitude(locationModel.getLongitude());
        return location;
    }

    public static LocationModel createResponse(Location location) {
        LocationModel locationModel = new LocationModel();
        locationModel.setId(location.getId());
        locationModel.setName(location.getName());
        locationModel.setLatitude(location.getLatitude());
        locationModel.setLongitude(location.getLongitude());
        return locationModel;
    }

    public static List<Location> createRequest(List<LocationModel> locationModels) {
        List<Location> locations = new ArrayList<>();
        for (LocationModel locationModel : locationModels) {
            locations.add(createRequest(locationModel));
        }
        return locations;
    }

    public static List<LocationModel> createResponse(List<Location> locations) {
        List<LocationModel> locationModels = new ArrayList<>();
        for (Location location : locations) {
            locationModels.add(createResponse(location));
        }
        
        return locationModels;
    }
}
