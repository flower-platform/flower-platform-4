package org.flowerplatform.core.file;

import static org.flowerplatform.core.NodePropertiesConstants.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FilePropertiesProvider extends PropertiesProvider {
	private static IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();

	@Override
	public void populateWithProperties(Node node) {
		Object file;
		try {
			file = fileAccessController.getFile(node.getIdWithinResource());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		node.getProperties().put(TEXT, fileAccessController.getName(file));
		node.getProperties().put(HAS_CHILDREN, fileAccessController.hasChildren(file));
		
		Path path = Paths.get(fileAccessController.getAbsolutePath(file));
		Map<String, Object> atributes = null;
		
		try {
			 atributes = Files.readAttributes(path, "*");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (Map.Entry<String, Object> entry : atributes.entrySet()) {
			if (entry.getKey().equals(SIZE)) {
				node.getProperties().put(entry.getKey().toString(),	entry.getValue() +" bytes");
			} 
			if (entry.getKey().equals(LAST_ACCESS_TIME)||
				entry.getKey().equals(LAST_MODIFIED_TIME)||
				entry.getKey().equals(CREATION_TIME)) {
					node.getProperties().put(entry.getKey().toString(),FormatFileTime((FileTime)entry.getValue()));
			} else {
				node.getProperties().put(entry.getKey().toString(),	(entry.getValue() == null) ? "" : entry.getValue().toString());
			}
		}
	}
	
	private String FormatFileTime(FileTime filename) {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(filename.toMillis()));
	}

}
