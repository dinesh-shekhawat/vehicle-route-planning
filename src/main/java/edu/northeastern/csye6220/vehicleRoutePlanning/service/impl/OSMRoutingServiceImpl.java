package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.ETA;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.LocationModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.Point;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.Route;
import edu.northeastern.csye6220.vehicleRoutePlanning.properties.RoutingProperties;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.RoutingFactoryService;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.RoutingService;

@Service
public class OSMRoutingServiceImpl implements RoutingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OSMRoutingServiceImpl.class);

	private final RoutingProperties routingProperties;

	@Autowired
	public OSMRoutingServiceImpl(RoutingProperties routingProperties) {
		this.routingProperties = routingProperties;
		LOGGER.info("loaded routingProperties: {}", routingProperties);
	}

	@Override
	public String getType() {
		return RoutingFactoryService.OSM;
	}

	@Override
	public String toString() {
		return "OSMRoutingServiceImpl []";
	}

	@Override
	public Route getRoute(List<LocationModel> locations) {
		LOGGER.trace("forming routes for locations size: {}", locations.size());

		Map<String, List<List<Double>>> requestPayload = new HashMap<>();
		List<List<Double>> coordinatesList = getCoordinatesList(locations);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Traversing coordinatesList of size: {}", coordinatesList.size());
			StringBuilder builder = new StringBuilder();
			
			builder.append(System.lineSeparator());
			builder.append("JSON LIST");
			builder.append("[");

			int lastIndex = coordinatesList.size() - 1;

            builder.append(System.lineSeparator());
			
		    for (int i = 0; i < coordinatesList.size(); i++) {
		    	List<Double> coordinates = coordinatesList.get(i);
		    	builder.append("[");
		    	builder.append(coordinates.get(0));
		    	builder.append(",");
		    	builder.append(coordinates.get(1));
		    	builder.append("]");
		    	
		    	if (i < lastIndex) {
		            builder.append(",");
		        }
		    }
		    
		    builder.append("]");
		    LOGGER.trace(builder.toString());
		    
		    builder.setLength(0);
		    builder.append("Plain LIST");
			builder.append(System.lineSeparator());

			lastIndex = coordinatesList.size() - 1;

            builder.append(System.lineSeparator());
			
		    for (int i = 0; i < coordinatesList.size(); i++) {
		    	List<Double> coordinates = coordinatesList.get(i);
		    	builder.append(coordinates.get(0));
		    	builder.append(",");
		    	builder.append(coordinates.get(1));
				builder.append(System.lineSeparator());
		    }
		    
		    LOGGER.trace(builder.toString());
		}
		
		requestPayload.put("coordinates", coordinatesList);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, List<List<Double>>>> requestEntity = new HttpEntity<>(requestPayload, headers);

		RestTemplate restTemplate = new RestTemplate();

		String osmURL = String.format("http://%s:%d/%s", routingProperties.getOsmHost(), routingProperties.getOsmPort(),
				routingProperties.getOsmDirectionsApi());
		LOGGER.trace("osmURL: {}", osmURL);

		ResponseEntity<String> responseBody = restTemplate.postForEntity(osmURL, requestEntity, String.class);

		Route route = parseResponse(responseBody.getBody());
		return route;
	}

	private Route parseResponse(String responseBody) {
		Route route = new Route();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(responseBody);

			JsonNode polylineNode = root.path("routes").path(0).path("geometry");
			String encodedPolyline = polylineNode.asText();

			List<Point> polyline = decodePolyline(encodedPolyline);
			route.setPolyline(polyline);
		} catch (Exception e) {
			LOGGER.error("Error parsing response: {}", e.getMessage(), e);
			// Handle the exception or return a default Route object
		}

		return route;
	}

	private List<Point> decodePolyline(String encodedPolyline) {
		LOGGER.trace("decode encodedPolyline: {}", encodedPolyline);
		
		List<LatLng> decodedLatLngs = PolylineEncoding.decode(encodedPolyline);

		List<Point> points = decodedLatLngs.stream().map(latLng -> new Point(latLng.lat, latLng.lng))
				.collect(Collectors.toList());
		LOGGER.trace("points size: {}", points.size());
		return points;
	}

	private List<List<Double>> getCoordinatesList(List<LocationModel> locations) {
		return locations.stream().map(location -> List.of(location.getLongitude(), location.getLatitude()))
				.collect(Collectors.toList());
	}

	@Override
	public Map<String, ?> getInformation() {
		Map<String, Object> information = new HashMap<>();
		information.put("type", getType());
		information.put("routingProperties", routingProperties);
		return information;
	}

	@Override
	public ETA getDistance(
			double sourceLatitude, 
			double sourceLongitude, 
			double destinationLatitude,
			double destinationLongitude) {
		LOGGER.trace("osm is getting distance between sourceLatitude: {}, sourceLongitude: {}, destinationLatitude: {}, destinationLongitude: {}", 
				sourceLatitude, 
				sourceLongitude,
				destinationLatitude,
				destinationLongitude);

		String osmURL = String.format(
        		"http://%s:%d/%s?start=%f,%f&end=%f,%f", 
        		routingProperties.getOsmHost(),
        		routingProperties.getOsmPort(),
        		routingProperties.getOsmDirectionsApi(),
        		sourceLongitude, 
        		sourceLatitude,
                destinationLongitude, 
                destinationLatitude);
        LOGGER.trace("osmURL: {}", osmURL);
        
        RestTemplate restTemplate = new RestTemplate();

        String jsonResponse = restTemplate.getForObject(osmURL, String.class);

        ETA eta = extractETAFromJson(jsonResponse);
        return eta;
	}

	private ETA extractETAFromJson(String jsonResponse) {
		ETA eta = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonResponse);

			JsonNode summaryNode = jsonNode.path("features").get(0).path("properties").path("summary");
			double distance = summaryNode.path("distance").asDouble();
			double time = summaryNode.path("duration").asDouble();

			eta = new ETA();
			eta.setDistance(distance);
			eta.setTime(time);
		} catch (Exception e) {
			LOGGER.error("exception in extractETAFromJson: {}", e.getMessage(), e);
		}
		
		return eta;
	}

}
