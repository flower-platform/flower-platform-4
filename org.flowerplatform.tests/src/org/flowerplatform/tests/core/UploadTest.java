package org.flowerplatform.tests.core;

import org.flowerplatform.core.file.download.remote.DownloadService;
import org.flowerplatform.tests.TestUtil;

/**
 * @author Cristina Brinza
 *
 */
public class UploadTest {
	
	public static final String UPLOAD = "upload/";
	
	public static final String DIR = TestUtil.getResourcesDir(DownloadTest.class) + UPLOAD;
	
	public static final int NO_TESTS = 3;
	
	public static final String[] files = {"mindmap1.mm", "mindmap2.mm", "file.txt"};
	
	public DownloadService uploadService;
	
		
}
