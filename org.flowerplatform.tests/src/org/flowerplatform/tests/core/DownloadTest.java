package org.flowerplatform.tests.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.Utils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.flowerplatform.core.file.download.DownloadServlet;
import org.flowerplatform.core.file.download.remote.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Cristina Brinza
 */
public class DownloadTest {

	public static final String DOWNLOAD = "download/";
		
	public static final String DIR = TestUtil.getResourcesDir(DownloadTest.class) + DOWNLOAD;
	
	public static final int NO_TESTS = 3;
	
	public static final String[] files = {"mindmap1.mm", "mindmap2.mm", "file.txt"};
	
	public DownloadService downloadService;
	
	public static List<String> testNodeUris;

	@BeforeClass
	public static void beforeClass() {
		TestUtil.copyFiles(DIR, DOWNLOAD);
		
		testNodeUris = new ArrayList<String>();
		testNodeUris.add(Utils.getUri("fpm", DOWNLOAD + files[0]));
		testNodeUris.add(Utils.getUri("fpm", DOWNLOAD + files[1]));
		testNodeUris.add(Utils.getUri("txt", DOWNLOAD + files[2]));
	}
	
	@Before
	public void before() {
		downloadService = new DownloadService();
		when(downloadService.getSessionId()).thenReturn("dummy-session");
	}
	
	@Test
	public void testPrepareDownloadFunctionSingleFile() throws Throwable{
		
		//doReturn("dummy-session").when(downloadService).getSessionId();
		for (int i = 0; i < NO_TESTS; i++) {
			String downloadLink = downloadService.prepareDownload(Arrays.asList(testNodeUris.get(i)));
			int first = DownloadServlet.DOWNLOAD_SERVLET_NAME.length();
			int last = downloadLink.lastIndexOf("/");
			String downloadServletName = downloadLink.substring(0, first);
			String downloadId = downloadLink.substring(first + 1, last);
				int dot = downloadId.indexOf(".");
				String sessionId = downloadId.substring(0, dot);
			String fileName = downloadLink.substring(last + 1);
			
			assertEquals("DownloadServletName doesn't match!", DownloadServlet.DOWNLOAD_SERVLET_NAME, downloadServletName);
			assertEquals("sessionId doesn't match!", sessionId, "dummy-session");
			assertEquals("fileName doesn't match!", fileName, files[i]);
		}
	}
	
	@Test
	public void testPrepareDownloadFunctionZipArchive() throws Throwable{
		String downloadLink = downloadService.prepareDownload(testNodeUris);
		
		int first = DownloadServlet.DOWNLOAD_SERVLET_NAME.length();
		int last = downloadLink.lastIndexOf("/");
		String downloadServletName = downloadLink.substring(0, first);
		String downloadId = downloadLink.substring(first + 1, last);
			int dot = downloadId.indexOf(".");
			String sessionId = downloadId.substring(0, dot);
			String timestamp = downloadId.substring(dot + 1);
		String fileName = downloadLink.substring(last + 1);
			dot = fileName.indexOf(".");
			String fileNameTimestamp = fileName.substring(0, dot);
			String fileNameExtension = fileName.substring(dot);
		
		assertEquals("DownloadServletName doesn't match!", DownloadServlet.DOWNLOAD_SERVLET_NAME, downloadServletName);
		assertEquals("sessionId doesn't match!", sessionId, "dummy-session");
		assertEquals("Timestamp doesn't match!", timestamp, fileNameTimestamp);
		assertEquals("Wrong extension!", fileNameExtension, ".zip");
	}
	
}
