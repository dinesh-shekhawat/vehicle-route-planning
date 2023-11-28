package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.northeastern.csye6220.vehicleRoutePlanning.properties.MapProperties;

@Controller
@RequestMapping("/map")
public class MapController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapController.class);
	
	private MapProperties mapProperties;
	
	@Autowired
	public MapController(MapProperties mapProperties) {
		this.mapProperties = mapProperties;
		LOGGER.info("loaded mapProperties: {}", mapProperties);
	}
	
	@GetMapping
    public ModelAndView showMap(Model model) {
		LOGGER.trace("will show the map");
		model.addAttribute("latitude", mapProperties.getInitialLatitude());
		model.addAttribute("longitude", mapProperties.getInitialLongitude());
		model.addAttribute("zoomLevel", mapProperties.getInitialZoomLevel());
        return new ModelAndView("map");
    }
	
}