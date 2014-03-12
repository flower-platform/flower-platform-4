package org.flowerplatform.util.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResourcesServlet extends HttpServlet {
	/**
	 * @see ImageComposerServlet 
	 */
	public static final char SEPARATOR = '|';

	public static final String FLOWER_PLATFORM = "flower-platform";
	
	protected static Map<String, String> tempFilesMap = new HashMap<String, String>();
	
	protected static int counter = 0;
	
	public static final File TEMP_FOLDER =  new File (System.getProperty("java.io.tmpdir"), FLOWER_PLATFORM);

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
		return TEMP_FOLDER + "\\" + tempFileName; 
	}
	
	/**
	* @author Sebastian Solomon
	*/
	protected File getTempFile(String tempFileName) {
		return new File(TEMP_FOLDER, tempFileName); 
	}
}
