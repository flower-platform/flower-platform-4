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
package org.flowerplatform.core.file.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.download.remote.DownloadService;
import org.flowerplatform.util.servlet.ResourcesServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class DownloadServlet extends ResourcesServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadServlet.class);

	private static final long serialVersionUID = 1L;
		   
	public static final String DOWNLOAD_SERVLET_NAME = "/servlet/download";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String info = req.getPathInfo();
		String downloadId = info.substring(1, info.lastIndexOf("/"));
	
		DownloadInfo downloadInfo = ((DownloadService) CorePlugin.getInstance().getServiceRegistry().getService("downloadService")).getDownloadInfo(downloadId);
		if (downloadInfo == null) {
			// no data to download
			return;
		}
		
		File file = null;
		try {
			file = new File(downloadInfo.getPath());			
			if (!file.exists()) {
				return;
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
		LOGGER.trace("Downloading: {}", file.getAbsolutePath());
		
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment");
		resp.setHeader("Cache-Control", "no cache");
		
		InputStream in = new FileInputStream(file);
		OutputStream out = resp.getOutputStream();
		try {
			IOUtils.copy(in, out);
		} catch (IOException e) {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}			
	
}