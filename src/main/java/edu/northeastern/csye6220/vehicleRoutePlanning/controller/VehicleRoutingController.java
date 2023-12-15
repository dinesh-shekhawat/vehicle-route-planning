package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.csye6220.vehicleRoutePlanning.constants.Constants;
import edu.northeastern.csye6220.vehicleRoutePlanning.properties.MapProperties;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/vehicle-routing")
public class VehicleRoutingController {

	private static final Logger LOGGER = LoggerFactory.getLogger(VehicleRoutingController.class);

	private final MapProperties mapProperties;

	@Autowired
	public VehicleRoutingController(MapProperties mapProperties) {
		this.mapProperties = mapProperties;
		LOGGER.info("loaded mapProperties: {}", mapProperties);
	}

	@GetMapping
	public ModelAndView showMap(Model model, HttpSession httpSession) {
		LOGGER.trace("will show the map on vehicle routing page");

		Object tokenObject = httpSession.getAttribute(Constants.JWT_TOKEN);
		if (tokenObject instanceof String) {
			String token = (String) tokenObject;
			LOGGER.trace("token found in request: {}", token);
			
			model.addAttribute("token", token);
			model.addAttribute("latitude", mapProperties.getInitialLatitude());
			model.addAttribute("longitude", mapProperties.getInitialLongitude());
			model.addAttribute("zoomLevel", mapProperties.getInitialZoomLevel());
		} else {
			LOGGER.error("token not found in session, this should never happen!!!!");
			return new ModelAndView("redirect:/login");
		}
		
		return new ModelAndView("vehicle-routing");	
	}

}
