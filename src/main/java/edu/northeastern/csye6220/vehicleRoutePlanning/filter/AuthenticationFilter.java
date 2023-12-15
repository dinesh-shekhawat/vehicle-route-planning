package edu.northeastern.csye6220.vehicleRoutePlanning.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import edu.northeastern.csye6220.vehicleRoutePlanning.properties.URLProperties;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

	private final URLProperties urlProperties;
	
	@Autowired
	public AuthenticationFilter(URLProperties urlProperties) {
		this.urlProperties = urlProperties;
		LOGGER.info("urlProperties: {}", urlProperties);
	}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	LOGGER.trace("performing filter");

    	
    	// TODO Add logic
    	
        
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
