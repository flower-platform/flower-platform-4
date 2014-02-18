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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;

/**
 * Gets a composed image from the image URLs in the request string. 
 * The URLs are prefixed with the containing plugin, and separated with a |.
 * E.g. <tt>/image-composer/plugin1/image1|plugin2/image2|plugin3/image3</tt>
 * 
 * @author Mariana
 * @author Sebastian Solomon
 */
public class ImageComposerServlet extends ResourcesServlet {

	private static final long serialVersionUID = 1L;
	
	public static final String PATH_PREFIX = "/image-composer";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String requestedFile = request.getPathInfo();
		if (requestedFile.startsWith(PATH_PREFIX)) {
			requestedFile = requestedFile.substring(PATH_PREFIX.length());
		}
		
		String tempRequestedFile = requestedFile.replace(SEPARATOR, TEMP_SEPARATOR)
												.replace('/', TEMP_FOLDER_SEPARATOR);
		File tempFile = new File(TEMP_FOLDER, tempRequestedFile);
		
		if (tempFile.exists()) {
			InputStream result = new FileInputStream(tempFile);
			OutputStream output = response.getOutputStream();
			IOUtils.copy(result, output);
			return;
		}
		
		int indexOfSecondSlash = requestedFile.indexOf('/', 1); // 1, i.e. skip the first index
		if (indexOfSecondSlash < 0) {
			send404(request, response);
			return;
		}
		String separator = "\\" + SEPARATOR;
		String[] paths = requestedFile.split(separator);
		
		int width = 0;
		int height = 0;
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		for (String path : paths) {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			indexOfSecondSlash = path.indexOf('/', 1);
			String plugin = path.substring(0, indexOfSecondSlash);
			path = path.substring(indexOfSecondSlash);
			requestedFile = "platform:/plugin" + plugin + "/" + AbstractFlowerJavaPlugin.PUBLIC_RESOURCES_DIR + path;
			try {
				URL url = new URL(requestedFile);
				BufferedImage image = ImageIO.read(url);
				images.add(image);
				if (width < image.getWidth()) {
					width = image.getWidth();
				}
				if (height < image.getHeight()) {
					height = image.getHeight();
				}
			} catch (Exception e) {
				// one of the images was not found; skip it
			}
		}
		
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = result.createGraphics();
		for (BufferedImage image : images) {
			graphics.drawImage(image, null, 0, 0);
		}
		graphics.dispose();
		
		// write image into the Temp folder
    	if (!TEMP_FOLDER.exists()) {
    		TEMP_FOLDER.mkdir();
    	}
    	FileOutputStream tempOutput = new FileOutputStream(tempFile);
    	ImageIO.write(result, "png", tempOutput);
		
		OutputStream output = null;
		output = response.getOutputStream();
		ImageIO.write(result, "png", output);
	}

}