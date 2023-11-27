package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/map")
public class MapController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapController.class);
	
	@GetMapping
    public ModelAndView showMap(Model model) {
		LOGGER.trace("will show the map");
		model.addAttribute("latitude", 42.361145);
		model.addAttribute("longitude", -71.057083);
        return new ModelAndView("map");
    }
	
}
