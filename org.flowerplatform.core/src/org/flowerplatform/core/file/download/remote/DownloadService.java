package org.flowerplatform.core.file.download.remote;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.file.download.DownloadInfo;
import org.flowerplatform.core.file.download.DownloadServlet;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.session.ISessionListener;
import org.flowerplatform.util.UtilConstants;

/**
 * Manager that handles the download requests from client.
 * 
 * <p>
 * Holds a map between the downloadId used as a download URL request and the {@link DownloadInfo downloadInfo} 
 * available for that download (path to download, type, ...).
 * <p>
 * A scheduler is executed each {@link #downloadCleanSchedulerTimestamp} seconds to clean the old entries.
 * 
 * <p>
 * Manages the creation/modification/deletion of a download temporary directory ({@link #DOWNLOAD_TEMP_FOLDER_NAME})
 * where data is stored in the process:
 * <ul>
 * 	<li> at server startup, the directory is deleted
 * 	<li> it is created when needed
 * 	<li> the ZIP archive created to be downloaded is stored in this directory 
 * 		(it is removed when the download is executed successfully or when the clean scheduler is called)
 * </ul>
 * 
 * @see DownloadServlet 
 * @author Cristina Constantinescu
 */
public class DownloadService implements ISessionListener {

	private static final String DOWNLOAD_TEMP_FOLDER_NAME = "download";
	private static final String ARCHIVE_EXTENSION = ".zip";
	
	// TODO CC: make properties
	private final long downloadCleanSchedulerTimestamp = 3600;
	private final boolean deleteFilesAfterSessionRemoved = true;
	
	private Map<String, DownloadInfo> downloadIdToDownloadInfo = new ConcurrentHashMap<String, DownloadInfo>();
	
	private ScheduledExecutorService scheduler = CorePlugin.getInstance().getScheduledExecutorServiceFactory().createScheduledExecutorService();

	class ClearDownloadInfoRunnable implements Runnable {
		
		private ScheduledExecutorService parentScheduler;
		
		public ClearDownloadInfoRunnable(ScheduledExecutorService parentScheduler) {			
			this.parentScheduler = parentScheduler;
		}

		@Override
		public void run() {
			for (Map.Entry<String, DownloadInfo> entry : downloadIdToDownloadInfo.entrySet()) {	
				if (entry.getValue().getTimestamp()/100 < System.currentTimeMillis()/100 - downloadCleanSchedulerTimestamp) {
					removeDownloadInfo(entry.getKey());
				}
			}
			parentScheduler.schedule(this, downloadCleanSchedulerTimestamp, TimeUnit.SECONDS);
		}
	}
	
	public DownloadService() {
		super();
		CorePlugin.getInstance().addSessionListener(this);
		
		deleteTemporaryDownloadFolder();
		scheduler.schedule(new ClearDownloadInfoRunnable(scheduler), downloadCleanSchedulerTimestamp, TimeUnit.SECONDS);
	}
	
	public DownloadInfo getDownloadInfo(String downloadId) {
		return downloadIdToDownloadInfo.get(downloadId);
	}
	
	private File getTemporaryDownloadFolder() {		
		File tempFolder = new File(UtilConstants.TEMP_FOLDER, DOWNLOAD_TEMP_FOLDER_NAME);
		if (!tempFolder.exists()) {
			tempFolder.mkdirs();
		}
		return tempFolder;
	}
	
	private void deleteTemporaryDownloadFolder() {		
		File tempFolder = new File(UtilConstants.TEMP_FOLDER + "/" + DOWNLOAD_TEMP_FOLDER_NAME);
		if (tempFolder.exists()) {
			CoreUtils.delete(tempFolder);
		}
	}
	
	private void removeDownloadInfo(String downloadId) {
		DownloadInfo data = downloadIdToDownloadInfo.get(downloadId);
		if (data.getType() == DownloadInfo.ARCHIVE_TYPE) {
			File file = new File(data.getPath());
			if (file.exists()) {
				CoreUtils.delete(file);
			}
		}
		downloadIdToDownloadInfo.remove(downloadId);
	}
		
	@Override
	public void sessionCreated(String sessionId) {
	}

	@Override
	public void sessionRemoved(String sessionId) {
		if (deleteFilesAfterSessionRemoved) {			
			for (Map.Entry<String, DownloadInfo> entry : downloadIdToDownloadInfo.entrySet()) {
				if (entry.getValue().getSessionId().equals(sessionId)) {
					removeDownloadInfo(entry.getKey());
				}	
			}
		}
	}
		
	/**
	 * Prepares the download context for given resources before starting the download process. <br>
	 * Stores in a {@link DownloadInfo} information about this download: path where to download, file type, etc.
	 * 
	 * <p>
	 * Creates a downloadId with the following format:
	 * <code>sessionId.timestamp</code>
	 * Registers the downloadId and download info. This data will be used later, when getting
	 * contents in servlet.
	 *  
	 * <p>
	 * If we have a container resource or multiple resources, then an archive is created and stored in:
	 * <code>download/sessionId.timestamp.zip</code>
	 * 
	 * <p>
	 * At the end, invokes download method on client side, the URL used has the following format:
	 * 	<code>servlet/download/downloadId/file_name</code>
	 */
	public String prepareDownload(List<String> fullNodeIds) throws Throwable {
		// get files from fullNodeIds
		List<Object> files = new ArrayList<>();
		boolean isSingle = fullNodeIds.size() == 1; // true if single file, not directory
		for (String fullNodeId : fullNodeIds) {
			try {
				Object file = CorePlugin.getInstance().getFileAccessController().getFile(new Node(fullNodeId).getIdWithinResource());
				files.add(file);
				if (CorePlugin.getInstance().getFileAccessController().isDirectory(file)) {
					isSingle = false;
				}
			} catch (Exception e) {
				throw e;
			}
		}
		String sessionId = CorePlugin.getInstance().getRequestThreadLocal().get().getSession().getId();		
		long timestamp = System.currentTimeMillis();
		String downloadId = String.format("%s.%s", sessionId, timestamp);	
				
		DownloadInfo downloadInfo = new DownloadInfo().setTimestamp(timestamp).setSessionId(sessionId);
		String fileName;
			
		if (isSingle) { // single file -> no need to create ZIP or store it in temporary directory
			Object file = files.get(0);
			fileName = CorePlugin.getInstance().getFileAccessController().getName(file);
			downloadInfo.setPath(CorePlugin.getInstance().getFileAccessController().getPath(file)).setType(DownloadInfo.FILE_TYPE);
		} else { // directory or multiple files selected	
			fileName = timestamp + ARCHIVE_EXTENSION;
			String zipPath = String.format("%s/%s.%s", getTemporaryDownloadFolder(), downloadId, fileName);			
			downloadInfo.setPath(zipPath).setType(DownloadInfo.ARCHIVE_TYPE);
								
			// create ZIP archive
			List<String> paths = new ArrayList<String>();
			for (Object file : files) {
				paths.add(CorePlugin.getInstance().getFileAccessController().getPath(file));					
			}			
			try {
				CoreUtils.zipFiles(paths, zipPath, String.valueOf(timestamp));
			} catch (Exception e) {
				throw (e.getCause() != null ? e.getCause() : e);
			}
		}
		downloadIdToDownloadInfo.put(downloadId, downloadInfo);
		
		return String.format("%s/%s/%s", DownloadServlet.DOWNLOAD_SERVLET_NAME, downloadId, fileName); // download link
	}	

}