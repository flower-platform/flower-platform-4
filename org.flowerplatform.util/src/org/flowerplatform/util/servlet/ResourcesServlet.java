package org.flowerplatform.util.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResourcesServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(ResourcesServlet.class);
	
	protected void send404(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		} catch (IOException e) {
			// do nothing
		}
		logger.warn("Resource not found; sending 404: {}", request.getPathInfo());
	}
	
}
