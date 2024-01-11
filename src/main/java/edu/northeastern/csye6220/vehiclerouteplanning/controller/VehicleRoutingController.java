package edu.northeastern.csye6220.vehiclerouteplanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.csye6220.vehiclerouteplanning.properties.MapProperties;

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
	public ModelAndView showMap(Model model) {
		LOGGER.trace("will show the map on vehicle routing page");

		model.addAttribute("latitude", mapProperties.getInitialLatitude());
		model.addAttribute("longitude", mapProperties.getInitialLongitude());
		model.addAttribute("zoomLevel", mapProperties.getInitialZoomLevel());
		return new ModelAndView("vehicle-routing");
	}

}
