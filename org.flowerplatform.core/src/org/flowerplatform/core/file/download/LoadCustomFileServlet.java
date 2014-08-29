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

	protected static final Logger logger = LoggerFactory.getLogger(LoadCustomFileServlet.class);

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
			if (logger.isTraceEnabled()) {
				logger.trace("Resource requested: {}", FileControllerUtils.getFileAccessController().getPath(file));
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
