package org.flowerplatform.util.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.util.UtilConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResourcesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5438373882820622871L;

	protected static Map<String, String> tempFilesMap = new HashMap<String, String>();
	
	protected static int counter = 0;
	
	private static final Logger logger = LoggerFactory.getLogger(ResourcesServlet.class);
	
	//TODO replace with flower property
	protected static boolean useFilesFromTempProperty = false; 
	
	protected void send404(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		} catch (IOException e) {
			// do nothing
		}
		logger.warn("Resource not found; sending 404: {}", request.getPathInfo());
	}
	
	/**
	* @author Sebastian Solomon
	*/
	protected String getTempFilePath(String tempFileName) {
		return UtilConstants.TEMP_FOLDER + "\\" + tempFileName; 
	}
	
	/**
	* @author Sebastian Solomon
	*/
	protected File getTempFile(String tempFileName) {
		return new File(UtilConstants.TEMP_FOLDER, tempFileName); 
	}
}
