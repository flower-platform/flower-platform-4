/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.core.file.download;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.util.servlet.ResourcesServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * @author Alina Bratu
 * @author Cristina Constantinescu
 */
public class LoadCustomFileServlet extends ResourcesServlet {

	protected static final Logger LOGGER = LoggerFactory.getLogger(LoadCustomFileServlet.class);

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		InputStream in = null;
		OutputStream out = null;
		
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment");
		resp.setHeader("Cache-Control", "no cache");
		
		try {
			Object file = getFile(req);
			if (file == null || !FileControllerUtils.getFileAccessController().exists(file)) {
				return;
			}			
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Resource requested: {}", FileControllerUtils.getFileAccessController().getPath(file));
			}
			
			out = resp.getOutputStream();
			in = new FileInputStream(FileControllerUtils.getFileAccessController().getFileAsFile(file));
			
			IOUtils.copy(in, out); 
		} catch (Exception e) {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}	
	
	/**
	 * @param req The <code>HttpServletRequest</code> passed as parameter in <code>doGet</code> method
	 * @return file at path found in the request
	 * @throws Exception
	 */
	protected Object getFile(HttpServletRequest req) throws Exception {
		return FileControllerUtils.getFileAccessController().getFile(req.getPathInfo());
	}
	
}
