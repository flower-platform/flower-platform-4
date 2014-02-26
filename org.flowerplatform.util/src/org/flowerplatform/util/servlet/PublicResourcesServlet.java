/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.util.servlet;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inspired from FileServlet proposed here: http://balusc.blogspot.de/2007/07/fileservlet.html.
 * 
 * <p>
 * Serves files from <plugin>/public-resources/*.
 * 
 * @author Cristian Spiescu
 */
public class PublicResourcesServlet extends ResourcesServlet {

	private static final Logger logger = LoggerFactory.getLogger(PublicResourcesServlet.class);

	private static final long serialVersionUID = 1L;
	
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	protected static PublicResourcesServlet INSTANCE;

	public static final String PATH_PREFIX = "/public-resources";

	public static PublicResourcesServlet getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PublicResourcesServlet();
		}
		return INSTANCE;
	}

	private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
            	// Do nothing.
            }
        }
    }
    
	protected Pair<InputStream, Closeable> getInputStreamForFileWithinZip(final InputStream fileInputStream, String fileWithinZip) throws IOException {
		final BufferedInputStream bis = new BufferedInputStream(fileInputStream, DEFAULT_BUFFER_SIZE);
		final ZipInputStream zis = new ZipInputStream(bis);
		
		Closeable closeable = new Closeable() {
			
			@Override
			public void close() throws IOException {
				zis.close();
				bis.close();
				fileInputStream.close();
			}
		};
		
		for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
			if (fileWithinZip.equals(ze.getName())) {
				return new Pair<InputStream, Closeable>(zis, closeable); 
			}
		}

		closeable.close();
		return null;
	}
	
	/**
	* @author Cristian Spiescu
	* @author Sebastian Solomon
	*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestedFile = request.getPathInfo();
		if (logger.isTraceEnabled()) {
			logger.trace("Resource requested: {}", requestedFile);
		}

		// Check if file is actually supplied to the request URI.
		if (requestedFile == null) {
			send404(request, response);
			return;
		}

		// Decode the file name (might contain spaces and on) and prepare file
		// object.
		requestedFile = URLDecoder.decode(requestedFile, "UTF-8");
		if (requestedFile.startsWith(PATH_PREFIX)) {
			requestedFile = requestedFile.substring(PATH_PREFIX.length());
		}
		
		// this may be an attempt to see files that are not public, i.e. to go to the
		// parent. From my tests, when you put in the browser (or even telnet) something like
		// parent1/parent2/../bla, it seems to be automatically translated to parent1/bla. However,
		// I wanted to make sure that we are all right
		if (requestedFile.contains("..")) {
			send404(request, response);
			return;
		}

		// we need something like /plugin/file....
		int indexOfSecondSlash = requestedFile.indexOf('/', 1); // 1, i.e. skip the first index
		if (indexOfSecondSlash < 0) {
			send404(request, response);
			return;
		}
		
		// both variables are prefixed with /
		String plugin = requestedFile.substring(0, indexOfSecondSlash);
		String file = requestedFile.substring(indexOfSecondSlash);

		// if | is supplied => the file is a zip, and we want what's in it
		int indexOfZipSeparator = file.indexOf(SEPARATOR);
		String fileInsideZipArchive = null; 
		if (indexOfZipSeparator >= 0 && indexOfZipSeparator < file.length() - 1) { // has | and | is not the last char in the string
			fileInsideZipArchive = file.substring(indexOfZipSeparator + 1);
			file = file.substring(0, indexOfZipSeparator);
		}
		
		String mapValue = null;
		String mapKey = null;
		
		// if the file is in a zip, search first in the Temp folder
		if (fileInsideZipArchive != null) {
			mapKey = createMapKey(file, fileInsideZipArchive).intern();
			if (useFilesFromTempProperty) {
				mapValue = tempFilesMap.get(mapKey);
			}
		} else {
			// we don't need synchronization if the file is not inside archive (so we don't use .intern)
			mapKey = createMapKey(file, fileInsideZipArchive);
		}
		
		synchronized (mapKey) {
			if (useFilesFromTempProperty) {
				if (fileInsideZipArchive != null) {
					if (mapValue != null) { // file exists in 'tempFilesMap'
						if (getTempFile(mapValue).exists()) {
							 response.reset();
						     response.setBufferSize(DEFAULT_BUFFER_SIZE);
						     response.setContentType(fileInsideZipArchive);
						     InputStream input = new FileInputStream(getTempFilePath(mapValue));
						     OutputStream output = response.getOutputStream();
						     IOUtils.copy(input, output);
						     input.close();
						     output.close();
						     logger.debug("File {} served from temp",  mapValue);
						     return;
						} else { // temporary file was deleted from disk
							logger.debug("File {} found to be missing from temp",  mapValue);
						}
					} else {
						synchronized(this) {
							counter++;
							mapValue = counter + "";
							tempFilesMap.put(mapKey, mapValue);
							logger.debug("mapValue '{}' added", mapValue );
						}
					}
				}
			}
				
			requestedFile = "platform:/plugin" + plugin + "/" + AbstractFlowerJavaPlugin.PUBLIC_RESOURCES_DIR + file;
			
			// Get content type by filename from the file or file inside zip
			String contentType = getServletContext().getMimeType(fileInsideZipArchive != null ? fileInsideZipArchive : file);
	
			// If content type is unknown, then set the default value.
			// For all content types, see:
			// http://www.w3schools.com/media/media_mimeref.asp
			// To add new content types, add new mime-mapping entry in web.xml.
			if (contentType == null) {
				contentType = "application/octet-stream";
			}
	
	        // Init servlet response.
	        response.reset();
	        response.setBufferSize(DEFAULT_BUFFER_SIZE);
	        response.setContentType(contentType);
	//        response.setHeader("Content-Length", String.valueOf(file.length()));
	//        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
	
	        // Prepare streams.
	        URL url;
	        InputStream input = null;
	        Closeable inputCloseable = null;
	        OutputStream output = null;
	
	        try {
        		url = new URL(requestedFile);
	            try {
					input = url.openConnection().getInputStream();
					inputCloseable = input;
	            } catch (IOException e) {
					// may fail if the resource is not available
	            	send404(request, response);
					return;
				}
	
				if (fileInsideZipArchive != null) {
					// we need to look for a file in the archive
	            	Pair<InputStream, Closeable> pair = getInputStreamForFileWithinZip(input, fileInsideZipArchive);
	            	if (pair == null) {
	            		// the file was not found; the input streams are closed in this case
	            		send404(request, response);
	            		return;
	            	}
	            	
	            	input = pair.a;
	            	inputCloseable = pair.b;
	            	
					if (useFilesFromTempProperty) {
						// write the file from archive in Temp folder
						if (!TEMP_FOLDER.exists()) {
							TEMP_FOLDER.mkdir();
						}
						
						Files.copy(input, getTempFile(mapValue).toPath(), StandardCopyOption.REPLACE_EXISTING);
						logger.debug("file '{}' was writen in temp", mapValue);
						
						input.close();
						input = new FileInputStream(getTempFilePath(mapValue));
					}
	            }
	
	            output = response.getOutputStream();
	            
	            // according to the doc, no need to use Buffered..., because the method buffers internally
	            IOUtils.copy(input, output);
	            
	         } finally {
	            // Gently close streams.
	            close(output);
	            close(inputCloseable);
	            close(input);
	        }
		}
	}
	
	/**
	* @author Sebastian Solomon
	*/
	private String createMapKey(String fileName, String fileInsideZipArchive) {
    	return fileName + SEPARATOR + fileInsideZipArchive;
    }
	
}
