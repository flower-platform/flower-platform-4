package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.CoreConstants.FILE_SIZE;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.IS_OPENABLE_IN_NEW_EDITOR;
import static org.flowerplatform.core.CoreConstants.SUBSCRIBABLE_RESOURCES;
import static org.flowerplatform.core.CoreConstants.TEXT_CONTENT_TYPE;
import static org.flowerplatform.core.file.FileControllerUtils.getFileAccessController;
import static org.flowerplatform.core.file.FileControllerUtils.getFilePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public class FilePropertiesController extends AbstractController implements IPropertiesProvider, IPropertySetter {
	
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		Object file;
		try {
			file = getFileAccessController().getFile(getFilePath(node));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		node.getProperties().put(CoreConstants.NAME, getFileAccessController().getName(file));
		node.getProperties().put(CoreConstants.HAS_CHILDREN, getFileAccessController().hasChildren(file));
		
		Path path = Paths.get(getFileAccessController().getAbsolutePath(file));
		Map<String, Object> atributes = null;
		
		try {
			 atributes = Files.readAttributes(path, "lastModifiedTime,lastAccessTime,creationTime,isDirectory");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (Map.Entry<String, Object> entry : atributes.entrySet()) {
			switch (entry.getKey()) {
				case CoreConstants.FILE_LAST_ACCESS_TIME:
				case CoreConstants.FILE_LAST_MODIFIED_TIME:
				case CoreConstants.FILE_CREATION_TIME:
					node.getProperties().put(entry.getKey().toString(), new Date(((FileTime)entry.getValue()).toMillis()));
					break;
				case CoreConstants.FILE_IS_DIRECTORY: 
					node.getProperties().put(FILE_IS_DIRECTORY, entry.getValue());
					break;
			}
		}

		if ((boolean)node.getProperties().get(FILE_IS_DIRECTORY)) {
			long folderSize = getFolderSize(file);
			node.getProperties().put(FILE_SIZE, folderSize);
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/core/folder.gif"));
		} else {
			node.getProperties().put(IS_OPENABLE_IN_NEW_EDITOR, true);
			node.getProperties().put(FILE_SIZE, getFileAccessController().length(file));
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/core/file.gif"));
			
			@SuppressWarnings("unchecked")
			List<Pair<String, String>> subscribableResources = (List<Pair<String, String>>) 
					node.getProperties().get(SUBSCRIBABLE_RESOURCES);
			if (subscribableResources == null) {
				subscribableResources = new ArrayList<Pair<String, String>>();
				node.getProperties().put(SUBSCRIBABLE_RESOURCES, subscribableResources);
			}
			
			String resourceUri = Utils.getUri("txt", node.getSchemeSpecificPart(), null);
			Pair<String, String> subscribableResource = new Pair<String, String>(resourceUri, TEXT_CONTENT_TYPE);
			subscribableResources.add(subscribableResource);
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
	
	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper value, ServiceContext<NodeService> context) {
		if (CoreConstants.NAME.equals(property)) {
			Object file;
			if (!node.getOrPopulateProperties().get(CoreConstants.NAME).equals(value.getPropertyValue())) {
				try {
					throw new UnsupportedOperationException();
//					file = fileAccessController.getFile(node.getIdWithinResource());
//					String parentPath = fileAccessController.getParent(file);
//					Object parent = fileAccessController.getFile(parentPath);
//					Object dest = fileAccessController.getFile(parent, value.getPropertyValue().toString());
//					if (fileAccessController.exists(dest)) {
//						throw new RuntimeException("There is already a file with the same name in this location.");
//					}
//					if (!fileAccessController.rename(file, dest)) {
//						throw new RuntimeException("The filename, directory name, or volume label syntax is incorrect");
//					}
//					node.getProperties().put(NAME, value.getPropertyValue());
//					node.setIdWithinResource(fileAccessController.getAbsolutePath(dest));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		node.getOrPopulateProperties().remove(property);
	}

}
