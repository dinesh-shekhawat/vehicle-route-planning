package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

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
import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingTransportCosts;
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
import com.graphhopper.jsprit.core.util.Coordinate;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.DeliveryModel;
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
		vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);

        final int WEIGHT_INDEX = 0;

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
		        
		        Location location = Location.newInstance(
                		vehicleLocation.getLongitude(),
                		vehicleLocation.getLatitude());
		        
		        VehicleImpl vehicleImpl = VehicleImpl.Builder
		        		.newInstance(vehicle.getRegistrationNumber())
	                    .setStartLocation(location)
//	                    .setEndLocation(location)
	                    .setType(vehicleType)
	                    .setReturnToDepot(true)
	                    .build();
		        
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
				
				vrpBuilder.addJob(deliveryJob);
			}
		}

		List<ShipmentModel> shipments = problemModel.getShipments();
		if (shipments != null) {
		    for (ShipmentModel shipment : shipments) {
		        LOGGER.info("shipment: {}", shipment);
		        
		        Point shipmentSourceLocation = shipment.getSourceLocation();
		        Point shipmentDestinationLocation = shipment.getDestinationLocation();
		        
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
	
        VehicleRoutingTransportCostsMatrix.Builder matrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(false);        
        LOGGER.trace("location map size: {}", vrpBuilder.getLocationMap().size());
        
        for (String from : vrpBuilder.getLocationMap().keySet()) {
            for (String to : vrpBuilder.getLocationMap().keySet()) {
              Coordinate fromCoord = vrpBuilder.getLocationMap().get(from);
              Coordinate toCoord = vrpBuilder.getLocationMap().get(to);
              
              double distance = calculateHaversineDistance(fromCoord, toCoord);

              matrixBuilder.addTransportDistance(from, to, distance);
              matrixBuilder.addTransportTime(from, to, (distance / 2.));
            }
          }
		
        VehicleRoutingTransportCosts transportCosts = matrixBuilder.build();
		LOGGER.trace("transportCosts: {}", transportCosts);
		
        vrpBuilder.setRoutingCost(transportCosts);
        
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

	private double calculateHaversineDistance(Coordinate source, Coordinate destination) {
        // Radius of the Earth in kilometers
        final double R = 6371.0;

        // Convert latitude and longitude from degrees to radians
        double lat1 = Math.toRadians(source.getY());
        double lon1 = Math.toRadians(source.getX());
        double lat2 = Math.toRadians(destination.getY());
        double lon2 = Math.toRadians(destination.getX());

        // Differences in coordinates
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // Haversine formula
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        double distance = R * c;

        return distance;
    }

}
