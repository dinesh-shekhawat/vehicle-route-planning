package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.DeliveryModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.Route;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.ServiceModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.ShipmentModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleRoutingProblemModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleRoutingSolutionModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.VehicleRoutingProblemSolverService;

@Service
public class VehicleRoutingProblemSolverServiceImpl implements VehicleRoutingProblemSolverService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);
	
	@Override
	public VehicleRoutingSolutionModel solve(VehicleRoutingProblemModel problemModel) {
		Map<String, Route> map = new HashMap<>();
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Solving the vehicle routing problem for the following constraints");
			
			List<VehicleModel> vehicles = problemModel.getVehicles();
			if (vehicles != null) {
			    for (VehicleModel vehicle : vehicles) {
			        LOGGER.info("vehicle: {}", vehicle);
			        map.put(vehicle.getRegistrationNumber(), new Route());
			    }
			}

			List<ServiceModel> services = problemModel.getServices();
			if (services != null) {
			    for (ServiceModel service : services) {
			        LOGGER.info("service: {}", service);
			    }
			}
			
			List<DeliveryModel> deliveryModels = problemModel.getDeliveries();
			if (deliveryModels != null) {
				for (DeliveryModel deliveryModel : deliveryModels) {
					LOGGER.info("delivery: {}", deliveryModel);
				}
			}

			List<ShipmentModel> shipments = problemModel.getShipments();
			if (shipments != null) {
			    for (ShipmentModel shipment : shipments) {
			        LOGGER.info("shipment: {}", shipment);
			    }
			}
		}

		VehicleRoutingSolutionModel solutionModel = new VehicleRoutingSolutionModel();
		solutionModel.setSolution(map);
		return solutionModel;
	}

}
