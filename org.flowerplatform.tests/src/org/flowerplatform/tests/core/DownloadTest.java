package org.flowerplatform.tests.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.Utils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.flowerplatform.core.file.download.DownloadServlet;
import org.flowerplatform.core.file.download.remote.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author Cristina Brinza
 */
public class DownloadTest {

	public static final String DOWNLOAD = "download/";
	
	public static final String DIR = TestUtil.getResourcesDir(DownloadTest.class) + DOWNLOAD;
	
	public static final int NO_TESTS = 3;
	
	public static final String[] files = {"mindmap1.mm", "mindmap2.mm", "file.txt"};
	
	public static DownloadService downloadService;
	
	public List<String> testNodeUris;

	public DownloadTest() {
		downloadService = new DownloadService();
		
		testNodeUris = new ArrayList<String>();
		testNodeUris.add(Utils.getUri("fpm", DIR + files[0]));
		testNodeUris.add(Utils.getUri("fpm", DIR + files[1]));
		testNodeUris.add(Utils.getUri("txt", DIR + files[2]));
	}
	
	@Test
	public void testPrepareDownloadFunctionSingleFile() throws Throwable{
		
		//doReturn("dummy-session").when(downloadService).getSessionId();
		when(downloadService.getSessionId()).thenReturn("dummy-session");
		
		for (int i = 0; i < NO_TESTS; i++) {
			String downloadLink = downloadService.prepareDownload(Arrays.asList(testNodeUris.get(i)));
			int first = DownloadServlet.DOWNLOAD_SERVLET_NAME.length();
			int last = downloadLink.lastIndexOf("/");
			String downloadServletName = downloadLink.substring(0, first);
			String downloadId = downloadLink.substring(first + 1, last);
				int dot = downloadId.indexOf(".");
				String sessionId = downloadId.substring(0, dot);
			String fileName = downloadLink.substring(last + 1);
			
			assertTrue("DownloadServletName matches", DownloadServlet.DOWNLOAD_SERVLET_NAME.equals(downloadServletName));
			assertTrue("sessionId matches", sessionId.equals("dummy-session"));
			assertTrue("fileName matches", fileName.equals(files[i]));
		}
	}
	
	@Test
	public void testPrepareDownloadFunctionZipArchive() throws Throwable{
		String downloadLink = downloadService.prepareDownload(testNodeUris);
	}
}
