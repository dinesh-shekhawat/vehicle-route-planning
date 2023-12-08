package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Delivery;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter.Print;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.DeliveryModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.LocationModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.Point;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.Route;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.ServiceModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.ShipmentModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleRoutingProblemModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleRoutingSolutionModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.VehicleRoutingProblemSolverService;

@org.springframework.stereotype.Service
public class VehicleRoutingProblemSolverServiceImpl implements VehicleRoutingProblemSolverService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);
	
	@Override
	public VehicleRoutingSolutionModel solve(VehicleRoutingProblemModel problemModel) {
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();

        final int WEIGHT_INDEX = 0;

        List<LocationModel> locations = new ArrayList<>();
        
		LOGGER.info("Solving the vehicle routing problem for the following constraints");
		
		List<VehicleModel> vehicles = problemModel.getVehicles();
		if (vehicles != null) {
		    for (VehicleModel vehicle : vehicles) {
		        LOGGER.info("vehicle: {}", vehicle);
		        
		        Point vehicleLocation = vehicle.getLocation();
		        
		        VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder
		        		.newInstance(vehicle.getRegistrationNumber())
		        		.addCapacityDimension(WEIGHT_INDEX, vehicle.getCapacity());
		        VehicleType vehicleType = vehicleTypeBuilder.build();
		        
		        VehicleImpl vehicleImpl = VehicleImpl.Builder
		        		.newInstance(vehicle.getRegistrationNumber())
	                    .setStartLocation(Location.newInstance(
	                    		vehicleLocation.getLongitude(),
	                    		vehicleLocation.getLatitude()))
	                    .setType(vehicleType)
	                    .build();
		        
		        LocationModel location = new LocationModel(
		        		vehicle.getRegistrationNumber(),
		        		vehicleLocation.getLatitude(),
		        		vehicleLocation.getLongitude());
		        
		        locations.add(location);
		        
	            vrpBuilder.addVehicle(vehicleImpl);
		    }
		}

		List<ServiceModel> services = problemModel.getServices();
		if (services != null) {
		    for (ServiceModel service : services) {
		        LOGGER.info("service: {}", service);
		        
		        Point serviceLocation = service.getLocation();
		        
		        Service serviceJob = Service.Builder
		        		.newInstance(service.getName())
		        		.setLocation(Location.newInstance(
		        				serviceLocation.getLongitude(), 
		        				serviceLocation.getLatitude()))
		        		.build();
		        
		        LocationModel location = new LocationModel(
		        		service.getName(),
		        		serviceLocation.getLatitude(),
		        		serviceLocation.getLongitude());
		        
		        locations.add(location);
		        
		        vrpBuilder.addJob(serviceJob);
		    }
		}
		
		List<DeliveryModel> deliveryModels = problemModel.getDeliveries();
		if (deliveryModels != null) {
			for (DeliveryModel deliveryModel : deliveryModels) {
				LOGGER.info("delivery: {}", deliveryModel);
				
				Point deliveryLocation = deliveryModel.getLocation();
				
				Delivery deliveryJob = Delivery.Builder
						.newInstance(deliveryModel.getName())
						.addSizeDimension(WEIGHT_INDEX, 1)
						.setLocation(Location.newInstance(
								deliveryLocation.getLongitude(),
								deliveryLocation.getLatitude()))
						.build();
				
				LocationModel location = new LocationModel(
						deliveryModel.getName(),
						deliveryLocation.getLatitude(),
						deliveryLocation.getLongitude());

				locations.add(location);

				vrpBuilder.addJob(deliveryJob);
			}
		}

		List<ShipmentModel> shipments = problemModel.getShipments();
		if (shipments != null) {
		    for (ShipmentModel shipment : shipments) {
		        LOGGER.info("shipment: {}", shipment);
		        
		        Point shipmentSourceLocation = shipment.getSourceLocation();
		        Point shipmentDestinationLocation = shipment.getDestinationLocation();
		        
		        LocationModel sourceLocation = new LocationModel(
		        		shipment.getName() + " - source",
		        		shipmentSourceLocation.getLatitude(),
		        		shipmentSourceLocation.getLongitude());
		        
		        locations.add(sourceLocation);
		        
		        LocationModel destinationLocation = new LocationModel(
		        		shipment.getName() + " - destination",
		        		shipmentDestinationLocation.getLatitude(),
		        		shipmentDestinationLocation.getLongitude());
		        
		        locations.add(destinationLocation);
		        
		        Shipment shipmentJob = Shipment.Builder
		        		.newInstance(shipment.getName())
		        		.addSizeDimension(WEIGHT_INDEX, 1)
		        		.setPickupLocation(Location.newInstance(
		        				shipmentSourceLocation.getLongitude(),
		        				shipmentDestinationLocation.getLatitude()))
		        		.setDeliveryLocation(Location.newInstance(
		        				shipmentDestinationLocation.getLongitude(),
		        				shipmentDestinationLocation.getLatitude()))
		        		.build();
		        
		        vrpBuilder.addJob(shipmentJob);
		    }
		}
	
        VehicleRoutingTransportCostsMatrix.Builder matrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);
        for (LocationModel location : locations) {
        	for (LocationModel otherLocation: locations) {
        		double distance = calculateEuclideanDistance(location, otherLocation);
                matrixBuilder.addTransportDistance(location.getName(), otherLocation.getName(), distance);
                matrixBuilder.addTransportTime(location.getName(), otherLocation.getName(), distance);
        	}
        }
		
		VehicleRoutingProblem problem = vrpBuilder.build();

		VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);
		
		Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
		
		VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
		
		SolutionPrinter.print(problem, bestSolution, Print.VERBOSE);
		
		Map<String, Route> solutionMap = new HashMap<>();
        for (VehicleRoute route : bestSolution.getRoutes()) {
            Collection<Job> routeJobs = route.getTourActivities().getJobs();
            Route yourRoute = new Route();
            // Extract information from routeJobs and populate yourRoute
            solutionMap.put(route.getVehicle().getId(), yourRoute);
        }
		
		// TODO Need to change following
		VehicleRoutingSolutionModel solutionModel = new VehicleRoutingSolutionModel();
		solutionModel.setSolution(new HashMap<>());
		return solutionModel;
	}

	private double calculateEuclideanDistance(LocationModel source, LocationModel destination) {
		double dx = source.getLongitude() - destination.getLongitude();
        double dy = source.getLatitude() - destination.getLatitude();
		return Math.sqrt(dx * dx + dy * dy);
	}

}
