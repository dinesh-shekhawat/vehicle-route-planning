package edu.northeastern.csye6220.vehiclerouteplanning.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import com.graphhopper.jsprit.core.problem.job.Delivery;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter.Print;
import com.graphhopper.jsprit.core.util.Coordinate;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;

import edu.northeastern.csye6220.vehiclerouteplanning.model.DeliveryModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.ETA;
import edu.northeastern.csye6220.vehiclerouteplanning.model.LocationModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.Point;
import edu.northeastern.csye6220.vehiclerouteplanning.model.Route;
import edu.northeastern.csye6220.vehiclerouteplanning.model.ServiceModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.ShipmentModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.VehicleModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.VehicleRoutingProblemModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.VehicleRoutingSolutionModel;
import edu.northeastern.csye6220.vehiclerouteplanning.service.RoutingFactoryService;
import edu.northeastern.csye6220.vehiclerouteplanning.service.RoutingService;
import edu.northeastern.csye6220.vehiclerouteplanning.service.VehicleRoutingProblemSolverService;

@org.springframework.stereotype.Service
public class VehicleRoutingProblemSolverServiceImpl implements VehicleRoutingProblemSolverService {

	private static final String DASH = "-";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);
	
	private final RoutingFactoryService routingFactoryService;
	
	@Autowired
	public VehicleRoutingProblemSolverServiceImpl(RoutingFactoryService routingFactoryService) {
		this.routingFactoryService = routingFactoryService;
	}
	
	@Override
	public VehicleRoutingSolutionModel solve(VehicleRoutingProblemModel problemModel) {
		RoutingService routingService = routingFactoryService.getDefaultRoutingService();
		
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
		        				shipmentSourceLocation.getLatitude()))
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
              
              ETA eta = routingService.getDistance(
            		  fromCoord.getY(), 
            		  fromCoord.getX(),
            		  toCoord.getY(),
            		  toCoord.getX());
              
              matrixBuilder.addTransportDistance(from, to, eta.getDistance());
              matrixBuilder.addTransportTime(from, to, eta.getTime());
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
		
		Map<String, List<LocationModel>> pointsMap = new HashMap<>();
		
        int counter = 1;
		for (VehicleRoute route : bestSolution.getRoutes()) {
        	LOGGER.trace("route: {}", counter);

        	Vehicle vehicle = route.getVehicle();
        	String vehicleId = vehicle.getId();
        	LOGGER.trace("vehicleId: {}", vehicleId);
        	List<LocationModel> pointList = pointsMap.getOrDefault(vehicleId, new ArrayList<>());
        	
        	TourActivity start = route.getStart();
        	Location startLocation = start.getLocation();
            LOGGER.trace("start location: {}", startLocation);

            LocationModel startPoint = new LocationModel(
            		startLocation.getName(),
            		startLocation.getCoordinate().getY(), 
            		startLocation.getCoordinate().getX());
            
            pointList.add(startPoint);
            
            List<TourActivity> tourActivities = route.getActivities();
        	for (TourActivity activity : tourActivities) {
        		String jobId;
                if (activity instanceof TourActivity.JobActivity) {
                    jobId = ((TourActivity.JobActivity) activity).getJob().getId();
                } else {
                    jobId = DASH;
                }
                
                Location location = activity.getLocation();
                LOGGER.trace("jobId: {}, location: {}", jobId, location);
                
                LocationModel point = new LocationModel(
                		location.getName(),
                		location.getCoordinate().getY(), 
                		location.getCoordinate().getX());
                pointList.add(point);
        	}
        	
        	TourActivity end = route.getEnd();
        	Location endLocation = end.getLocation();
        	LOGGER.trace("end location: {}", startLocation);
        	
        	LocationModel lastPoint = new LocationModel(
        			endLocation.getName(),
        			endLocation.getCoordinate().getY(), 
        			endLocation.getCoordinate().getX());
            
            pointList.add(lastPoint);
        	
        	pointsMap.put(vehicleId, pointList);
        	
            counter++;
        }
		
		Map<String, Route> solution = new HashMap<>();
		for (Map.Entry<String, List<LocationModel>> entry : pointsMap.entrySet()) {
			String key = entry.getKey();
			List<LocationModel> locations = entry.getValue();
			
			Route route = routingService.getRoute(locations);
			
			solution.put(key, route);
		}
		
		VehicleRoutingSolutionModel solutionModel = new VehicleRoutingSolutionModel();
		solutionModel.setSolution(solution);
		return solutionModel;
	}

}
