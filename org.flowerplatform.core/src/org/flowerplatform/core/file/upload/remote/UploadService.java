package org.flowerplatform.core.file.upload.remote;


import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.file.upload.UploadInfo;
import org.flowerplatform.core.file.upload.UploadServlet;
import org.flowerplatform.core.session.ISessionListener;
import org.flowerplatform.util.UtilConstants;

/**
 * Manager that handles the upload requests from client.
 * 
 * <p>
 * Holds a map between the UID used as an upload URL request and the {@link UploadData data} 
 * available for that upload.
 * 
 * <p>
 * Manages the creation/modification/deletion of a temporary directory ({@link #UNLOAD_TEMP_FOLDER_NAME})
 * where data is stored in the process:
 * <ul>
 * 	<li> at server startup, the directory is deleted
 * 	<li> it is created when needed
 * 	<li> the ZIP archive created to be unzipped after uploading is stored in this directory 
 * 		(it is removed when the upload operation finishes)
 * </ul>
 * 
 * @see UploadServlet
 * 
 * @author Cristina Constantinescu
 */
public class UploadService implements ISessionListener {

	private static final String UPLOAD_TEMP_FOLDER_NAME = "upload";
	
	private Map<String, UploadInfo> uploadIdToUploadInfo = new ConcurrentHashMap<String, UploadInfo>();
	
	public UploadService() {
		CorePlugin.getInstance().addSessionListener(this);
		
		deleteTemporaryUploadDirectory();
	}
	
	public UploadInfo getUploadInfo(String uploadId) {
		return uploadIdToUploadInfo.get(uploadId);
	}
	
	public File getTemporaryUploadDirectory() {		
		File tempFolder = new File(UtilConstants.TEMP_FOLDER + "/" + UPLOAD_TEMP_FOLDER_NAME);
		if (!tempFolder.exists()) {
			tempFolder.mkdirs();
		}
		return tempFolder;
	}
	
	private void deleteTemporaryUploadDirectory() {		
		File tempFolder = new File(UtilConstants.TEMP_FOLDER + "/" + UPLOAD_TEMP_FOLDER_NAME);
		if (tempFolder.exists()) {
			CoreUtils.delete(tempFolder);
		}
	}	
		
	/**
	 * Prepares the upload context for given resource before starting the upload process. <br>
	 * Stores in an {@link UploadInfo} information about this upload: path/temporary path where to upload, unzip file, etc.
	 * 
	 * <p>
	 * Creates an uploadId with the following format:
	 * <code>sessionId.timestamp</code> <br>
	 * Registers the uploadId and upload info. This data will be used later, when getting
	 * contents in servlet.
	 * 
	 * <p>
	 * If the user wants to unzip the file before setting it to given location, then the upload will be done
	 * in a temporary location having the following format:
	 * <code>upload/sessionId.timestamp.zip</code> <br>
	 * This is stored in {@link UploadInfo#setTmpLocation(String)}.
	 *  
	 * The URL used to execute the {@link UploadServlet} has the following format:
	 * <code>servlet/upload/uploadId/file_name</code>
	 * @throws Exception 
	 */	
	public String prepareUpload(String fullNodeId, String fileName, boolean unzipfile) throws Exception {
		String path = FileControllerUtils.getFilePath(fullNodeId);
		Object file = CorePlugin.getInstance().getFileAccessController().getFile(path);
		
		String sessionId = CorePlugin.getInstance().getRequestThreadLocal().get().getSession().getId();		
		long timestamp = System.currentTimeMillis();
		String uploadId = String.format("%s.%s", sessionId, timestamp);	
				
		String location = CorePlugin.getInstance().getFileAccessController().getAbsolutePath(file);		
		UploadInfo uploadInfo = new UploadInfo().setLocation(location).setTimestamp(timestamp).setSessionId(sessionId).setUnzipFile(unzipfile);		
		if (!unzipfile) {
			uploadInfo.setTmpLocation(location);			
		} else {
			uploadInfo.setTmpLocation(String.format("%s/%s.%s", getTemporaryUploadDirectory().getAbsolutePath(), uploadId, timestamp + ".zip"));			
		}		
		uploadIdToUploadInfo.put(uploadId, uploadInfo);
		
		return String.format("%s/%s/%s", UploadServlet.UPLOAD_SERVLET_NAME, uploadId, fileName);	
	}

	@Override
	public void sessionCreated(String sessionId) {
	}

	@Override
	public void sessionRemoved(String sessionId) {
		for (Map.Entry<String, UploadInfo> entry : uploadIdToUploadInfo.entrySet()) {
			if (sessionId.equals(entry.getValue().getSessionId())) {
				if (entry.getValue().unzipFile()) {
					File file = new File(entry.getValue().getTmpLocation());
					if (file.exists()) {
						CoreUtils.delete(file);
					}
				}				
				uploadIdToUploadInfo.remove(entry.getKey());
			}
		}		
	}
	
}