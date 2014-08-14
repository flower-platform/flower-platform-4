package org.flowerplatform.core.file.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreService;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.file.download.remote.DownloadService;
import org.flowerplatform.util.servlet.ResourcesServlet;
 
/**
 * 
 * @author Alina Bratu
 *
 */

public class LoadCustomIconServlet extends ResourcesServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		CoreService service = (CoreService) CorePlugin.getInstance().getServiceRegistry().getService("coreService");
//		List<String> iconPaths = service.getCustomIconsPaths();
		File file = null;
		InputStream in;

		String iconPath = req.toString().substring(req.toString().lastIndexOf('/'));
//		for (int i=0; i < iconPaths.size(); i++) {
//			try {
//				file = (File) FileControllerUtils.getFileAccessController().getFile(iconPaths.get(i));
//			} catch (Exception e) {
//				throw new IOException(e);
//			}	
//			
//			OutputStream out = resp.getOutputStream();
//			in = new FileInputStream(file);
//			try {
//				IOUtils.copy(in, out); 
//			} catch (IOException e) {
//				IOUtils.closeQuietly(in);
//				IOUtils.closeQuietly(out);
//			}
//		}
		try {
			file = (File) FileControllerUtils.getFileAccessController().getFile(iconPath);
		} catch (Exception e) {
			throw new IOException(e);
		}	
		
		OutputStream out = resp.getOutputStream();
		in = new FileInputStream(file);
		try {
			IOUtils.copy(in, out); 
		} catch (IOException e) {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		
	}	
}
