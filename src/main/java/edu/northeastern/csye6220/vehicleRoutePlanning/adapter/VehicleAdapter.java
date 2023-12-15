package edu.northeastern.csye6220.vehicleRoutePlanning.adapter;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Vehicle;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleModel;

public class VehicleAdapter {

	public static Vehicle createRequest(VehicleModel vehicleModel) {
		Vehicle vehicle = new Vehicle();
        vehicle.setName(vehicleModel.getName());
        vehicle.setRegistrationNumber(vehicleModel.getRegistrationNumber());
        vehicle.setCapacity(vehicleModel.getCapacity());
        return vehicle;
	}
	
	public static VehicleModel createResponse(Vehicle vehicle) {
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setId(vehicle.getId());
        vehicleModel.setName(vehicle.getName());
        vehicleModel.setRegistrationNumber(vehicle.getRegistrationNumber());
        vehicleModel.setCapacity(vehicle.getCapacity());
        return vehicleModel;
    }

    public static List<Vehicle> createRequest(List<VehicleModel> vehicleModels) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (VehicleModel vehicleModel : vehicleModels) {
            vehicles.add(createRequest(vehicleModel));
        }
        return vehicles;
    }

    public static List<VehicleModel> createResponse(List<Vehicle> vehicles) {
        List<VehicleModel> vehicleModels = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            vehicleModels.add(createResponse(vehicle));
        }
        
        return vehicleModels;
    }
	
}
