package org.flowerplatform.util.servlet;

import java.io.File;
import java.io.IOException;

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
	
	/**
	 * Used to replace {@value #SEPARATOR} 
	 */
	public static final char TEMP_SEPARATOR = '#';
	
	/**
	 * Used to replace '/' 
	 */
	public static final char TEMP_FOLDER_SEPARATOR = '$';
	
	public static final String FLOWER_PLATFORM = "flower-platform";
	
	public static final File TEMP_FOLDER =  new File (System.getProperty("java.io.tmpdir"), FLOWER_PLATFORM);

	private static final Logger logger = LoggerFactory.getLogger(ResourcesServlet.class);
	
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
	protected String searchInTemp(String requestedFile) {
		return null;
//		File file = new File(TEMP_FOLDER , requestedFile);
//		return file.exists() ? file.getAbsolutePath() : null;
	}
	
}
