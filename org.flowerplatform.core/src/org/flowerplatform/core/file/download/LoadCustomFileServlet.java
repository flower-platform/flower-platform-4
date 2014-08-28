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
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.util.servlet.ResourcesServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * Servlet for loading user files. Usage: set the object url with value "servlet/load/[path-to-file]"
 * @author Alina Bratu
 *
 */

public class LoadCustomFileServlet extends ResourcesServlet {

	private static final Logger logger = LoggerFactory.getLogger(DownloadServlet.class);

	private static final long serialVersionUID = 1L;
	
	protected static File file = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			file = getFile(req);
			if (!file.exists()) {
				return;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		OutputStream out = resp.getOutputStream();
		InputStream in = new FileInputStream(file);
		
		try {
			IOUtils.copy(in, out); 
		} catch (IOException e) {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}	
	
	protected void writeToLog(HttpServletResponse resp) {
		if (logger.isTraceEnabled()) {
			logger.trace("Resource requested: {}", file);
		}
	}
	
	/**
	 * @param req The <code>HttpServletRequest</code> passed as parameter in <code>doGet</code> method
	 * @return file at path found in the request
	 * @throws Exception
	 */
	protected File getFile( HttpServletRequest req) throws Exception {
		return FileControllerUtils.getFileAccessController().getFileAsFile(FileControllerUtils.getFileAccessController().getFile(req.getPathInfo()));
	}
}
