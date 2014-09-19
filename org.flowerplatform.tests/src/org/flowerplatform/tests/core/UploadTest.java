package org.flowerplatform.tests.core;

import org.flowerplatform.core.file.upload.UploadServlet;
import org.flowerplatform.core.file.upload.remote.UploadService;
import org.flowerplatform.tests.EclipseIndependentTestSuite;
import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.Utils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

/**
 * @author Cristina Brinza
 *
 */
public class UploadTest {
	
	public static final String UPLOAD = "upload";
	
	public static final String DIR = TestUtil.getResourcesDir(DownloadTest.class) + UPLOAD;
	
	public static UploadService uploadService;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		uploadService = new UploadService();
		when(uploadService.getSessionId()).thenReturn("dummy-session");
	}
	
	@Test
	public void testPrepareUpload() throws Throwable {
		String fileToUploadName = "file.txt";
		String nodeUri = Utils.getUri("file", UPLOAD + "|");
		
		String uploadLink = uploadService.prepareUpload(nodeUri, fileToUploadName, false);
		int first = UploadServlet.UPLOAD_SERVLET_NAME.length();
		int last = uploadLink.lastIndexOf("/");
		String uploadServletName = uploadLink.substring(0, first);
		String uploadId = uploadLink.substring(first + 1, last);
			int dot = uploadId.indexOf(".");
			String sessionId = uploadId.substring(0, dot);
		String fileName = uploadLink.substring(last + 1);
	
		assertEquals("UploadServletName doesn't match!", uploadServletName, UploadServlet.UPLOAD_SERVLET_NAME);
		assertEquals("sessionId doesn't match!", sessionId, "dummy-session");
		assertEquals("fileName doesn't match!", fileName, fileToUploadName);
	}
	
	@Test
	public void testPrepareUploadUnzipArchive() throws Throwable {
		String fileToUploadName = "archive.zip";
		String nodeUri = Utils.getUri("file", UPLOAD + "|");
		
		String uploadLink = uploadService.prepareUpload(nodeUri, fileToUploadName, true);
		int first = UploadServlet.UPLOAD_SERVLET_NAME.length();
		int last = uploadLink.lastIndexOf("/");
		String uploadServletName = uploadLink.substring(0, first);
		String uploadId = uploadLink.substring(first + 1, last);
			int dot = uploadId.indexOf(".");
			String sessionId = uploadId.substring(0, dot);
		String fileName = uploadLink.substring(last + 1);
	
		assertEquals("UploadServletName doesn't match!", uploadServletName, UploadServlet.UPLOAD_SERVLET_NAME);
		assertEquals("sessionId doesn't match!", sessionId, "dummy-session");
		assertEquals("fileName doesn't match!", fileName, fileToUploadName);
	}
	
	@AfterClass
	public static void afterClass() {
		EclipseIndependentTestSuite.deleteFiles(UPLOAD);
	}
}
