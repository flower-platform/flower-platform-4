/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.util.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.util.UtilConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *@author Mariana Gheorghe
 **/
public abstract class ResourcesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5438373882820622871L;

	protected static Map<String, String> tempFilesMap = new HashMap<String, String>();
	
	protected static int counter = 0;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesServlet.class);
		
	protected boolean useFilesFromTemporaryDirectory = false; 
		
	/**
	 * @author Cristina Constantinescu
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {		
		super.init(config);
		ServletUtils.addAllAdditionalAttributesToServletContext(getServletContext());	
		
		useFilesFromTemporaryDirectory = Boolean.valueOf((String) getServletContext().getAttribute(ServletUtils.PROP_USE_FILES_FROM_TEMPORARY_DIRECTORY));
	}
	/**
	 *@author Mariana Gheorghe
	 **/
	protected void send404(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
			//CHECKSTYLE:OFF
		} catch (IOException e) {
			// do nothing
			//CHECKSTYLE:ON
		}
		LOGGER.warn("Resource not found; sending 404: {}", request.getPathInfo());
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