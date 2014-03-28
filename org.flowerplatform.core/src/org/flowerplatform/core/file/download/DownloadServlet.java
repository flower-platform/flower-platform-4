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
import org.flowerplatform.util.servlet.ResourcesServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class DownloadServlet extends ResourcesServlet {

	private static final Logger logger = LoggerFactory.getLogger(DownloadServlet.class);

	private static final long serialVersionUID = 1L;
		   
	public static final String DOWNLOAD_SERVLET_NAME = "/servlet/download";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String info = req.getPathInfo();
		String downloadId = info.substring(1, info.lastIndexOf("/"));
	
		DownloadInfo downloadInfo = CorePlugin.getInstance().getDownloadService().getDownloadInfo(downloadId);
		if (downloadInfo == null) {
			// no data to download
			return;
		}
		File file = new File(downloadInfo.getPath());			
		if (!file.exists()) {
			return;
		}
		logger.trace("Downloading: {}", file.getAbsolutePath());
		
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
