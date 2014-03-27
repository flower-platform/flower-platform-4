package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.CONTENT_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.TEXT_CONTENT_TYPE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FilePropertiesProvider extends PropertiesProvider {
	
	@Override
	public void populateWithProperties(Node node, ServiceContext context) {
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		Object file;
		try {
			file = fileAccessController.getFile(node.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		node.getProperties().put(CoreConstants.NAME, fileAccessController.getName(file));
		node.getProperties().put(CoreConstants.HAS_CHILDREN, fileAccessController.hasChildren(file));
		
		Path path = Paths.get(fileAccessController.getAbsolutePath(file));
		Map<String, Object> atributes = null;
		
		try {
			 atributes = Files.readAttributes(path, "size,lastModifiedTime,lastAccessTime,creationTime,isDirectory");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (Map.Entry<String, Object> entry : atributes.entrySet()) {
			switch (entry.getKey()) {
				case CoreConstants.FILE_SIZE:
					if (node.getProperties().get(FILE_IS_DIRECTORY).equals("true")) {
						long folderSize = getFolderSize(file);
						node.getProperties().put(entry.getKey().toString(), folderSize);
					} else {
						node.getProperties().put(entry.getKey().toString(), fileAccessController.length(file));
					}
					
					break;
				case CoreConstants.FILE_LAST_ACCESS_TIME:
				case CoreConstants.FILE_LAST_MODIFIED_TIME:
				case CoreConstants.FILE_CREATION_TIME:
					node.getProperties().put(entry.getKey().toString(),  new Date(((FileTime)entry.getValue()).toMillis()));
					break;
				case "isDirectory": 
					node.getProperties().put(FILE_IS_DIRECTORY, entry.getValue().toString());
					break;
			}
		}
		
		if (node.getProperties().get(FILE_IS_DIRECTORY).equals("true")) {
			node.getProperties().put(ICONS, CorePlugin.getInstance().getResourceUrl("images/folder.gif"));
		} else {
			node.getProperties().put(ICONS, CorePlugin.getInstance().getResourceUrl("images/file.gif"));
			node.getProperties().put(CONTENT_TYPE, TEXT_CONTENT_TYPE);
		}
	}

	private long getFolderSize(Object folder) {
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		long length = 0;
	    for (Object child : fileAccessController.listFiles(folder)) {
	        if (fileAccessController.isFile(folder))
	            length += fileAccessController.length(child);
	        else
	            length += getFolderSize(child);
	    }
	    return length;
	}
	

}
