package edu.northeastern.csye6220.vehicleRoutePlanning.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.UserAccess;
import edu.northeastern.csye6220.vehicleRoutePlanning.properties.URLProperties;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.UserAccessService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

	private static final String X_FORWARED_FOR = "X-Forwarded-For";
	private static final String UNKNOWN = "unknown";

	private final URLProperties urlProperties;
	private final UserAccessService userAccessService;

	@Autowired
	public AuthenticationFilter(URLProperties urlProperties, UserAccessService userAccessService) {
		this.urlProperties = urlProperties;
		this.userAccessService = userAccessService;
		LOGGER.info("urlProperties: {}", urlProperties);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		LOGGER.trace("performing filter");

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

		UserAccess access = createUserAccess(httpRequest);
		userAccessService.add(access);

		long startTime = System.currentTimeMillis();
		filterChain.doFilter(servletRequest, servletResponse);
		long endTime = System.currentTimeMillis();
		long timeTaken = endTime - startTime;
		
		int responseCode = getResponseCode(httpResponse);
		access.setResponseCode(responseCode);
		access.setResponseTime(timeTaken);
		
		userAccessService.update(access);
		
		LOGGER.info("millseconds taken to complete the request: {}", timeTaken);
	}

	private UserAccess createUserAccess(HttpServletRequest httpRequest) {
		String url = buildRequestURL(httpRequest);
		String clientIpAddress = getClientIpAddress(httpRequest);
		String method = getRequestMethod(httpRequest);
		
		UserAccess userAccess = new UserAccess();
		userAccess.setUrl(url);
		userAccess.setClientIpAddress(clientIpAddress);
		userAccess.setMethodType(method);
		LOGGER.debug("created user userAccess: {}", userAccess);
		return userAccess;
	}

	private String buildRequestURL(HttpServletRequest request) {
		return request.getRequestURI();
	}
	
	private String getRequestMethod(HttpServletRequest request) {
		return request.getMethod();
	}

	private String getClientIpAddress(HttpServletRequest request) {
		String clientIpAddress = request.getHeader(X_FORWARED_FOR);
		if (clientIpAddress == null || clientIpAddress.isEmpty() || clientIpAddress.equalsIgnoreCase(UNKNOWN)) {
			clientIpAddress = request.getRemoteAddr();
		}
		return clientIpAddress;
	}

	private int getResponseCode(HttpServletResponse response) {
		return response.getStatus();
	}
}
