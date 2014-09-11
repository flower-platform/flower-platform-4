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
package org.flowerplatform.core.file.upload;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.file.upload.remote.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet used to process upload requests from client.
 * 
 * Gets the {@link UploadInfo uploadInfo} from the uploadId stored in the URL.
 * If the file to upload is an archive that must be unzipped ({@link UploadData#unzipFile()}) 
 * before setting the content to original location (path stored in {@link UploadData#getLocation()},
 * then it is uploaded in the upload's temporary directory (path stored in {@link UploadData#getTempLocation()})
 * and at the end it is unzipped to original location.
 * <p>
 * At the end delete the archive created in temporary location if necessary.
 * 
 * @author Cristina Constantinescu
 */
public class UploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public static final String UPLOAD_SERVLET_NAME = "/servlet/upload";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadServlet.class);
	
	/**
	 *@author see class
	 **/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			return;
		}
		// entire URL displayed after servlet name ("servlet/upload") -> /uploadId/file_to_upload_name			
		String uploadId = request.getPathInfo().substring(1, request.getPathInfo().lastIndexOf("/"));
		UploadService uploadService = ((UploadService) CorePlugin.getInstance().getServiceRegistry().getService("uploadService"));
		
		UploadInfo uploadInfo = uploadService.getUploadInfo(uploadId);
		if (uploadInfo.getTmpLocation() == null) {
			return;
		}
		
		LOGGER.trace("Uploading {}", uploadInfo);
		
		// create temporary upload location file for archive that needs to be unzipped after
		File file = new File(uploadInfo.getTmpLocation());
		if (!file.exists() && uploadInfo.unzipFile()) {
			file.createNewFile();
		}
		
		// Create a factory for disk-based file items

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(uploadService.getTemporaryUploadDirectory());
		factory.setFileCleaningTracker(FileCleanerCleanup.getFileCleaningTracker(request.getServletContext()));
		
		// Create a new file upload handler
		ServletFileUpload uploadHandler = new ServletFileUpload(factory);
		
		File uploadedFile = null;
		try {			
			// Parse the request
			List<FileItem> items = uploadHandler.parseRequest(request);	
			// Process the uploaded items
			Iterator<FileItem> it = items.iterator();
			while (it.hasNext()) {
			    FileItem item = it.next();
			    if (!item.isFormField()) { // uploaded file
			    	uploadedFile = new File(uploadInfo.unzipFile() ? uploadInfo.getTmpLocation() : (uploadInfo.getTmpLocation() + "/" + item.getName()));
			    	item.write(uploadedFile);
			    }
			}
			if (uploadInfo.unzipFile()) { // unzip file if requested
				CoreUtils.unzipArchive(uploadedFile, new File(uploadInfo.getLocation()));
			}
		} catch (Exception e) { // something happened or user cancelled the upload while in progress
			if (uploadedFile != null) { 
				CoreUtils.delete(uploadedFile);
			}			
		}
		
	}

}