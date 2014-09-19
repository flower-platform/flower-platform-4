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

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.file.download.remote.DownloadService;

/**
 * @author Cristina Constantinescu
 */
public class DownloadServlet extends LoadCustomFileServlet {
		   	
	private static final long serialVersionUID = 1L;
	
	public static final String DOWNLOAD_SERVLET_NAME = "/servlet/download";
			
	@Override
	protected Object getFile(HttpServletRequest req) throws Exception {
		String info = req.getPathInfo();
		String downloadId = info.substring(1, info.lastIndexOf("/"));
	
		DownloadInfo downloadInfo = ((DownloadService) CorePlugin.getInstance().getServiceRegistry().getService("downloadService")).getDownloadInfo(downloadId);
		if (downloadInfo == null) {
			// no data to download
			return null;
		}
		
		return FileControllerUtils.getFileAccessController().getFileAsFile(new File(downloadInfo.getPath()));
	}
	
}