package org.flowerplatform.core.file;


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
public class FilePropertiesProvider extends PropertiesProvider<Object> {
	private static IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();

	@Override
	public void populateWithProperties(Node node, Object file) {
		node.getOrCreateProperties().put("body", fileAccessController.getName(file));
		node.getOrCreateProperties().put("hasChildren", fileAccessController.hasChildren(file));
		node.getOrCreateProperties().put("name", fileAccessController.getName(file));
		
		Path p = Paths.get(fileAccessController.getAbsolutePath(file));
		Map<String, Object> atributes = null;
		
		try {
			 atributes = Files.readAttributes(p, "*");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (Map.Entry<String, Object> entry : atributes.entrySet()) {
			if (entry.getKey().equals("size")) {
				node.getOrCreateProperties().put(entry.getKey().toString(),	entry.getValue() +" bytes");
			} 
			if (entry.getKey().equals("lastModifiedTime")||
				entry.getKey().equals("creationTime")||
				entry.getKey().equals("lastAccessTime")) {
					node.getOrCreateProperties().put(entry.getKey().toString(),FormatFileTime((FileTime)entry.getValue()));
			} else {
				node.getOrCreateProperties().put(entry.getKey().toString(),	(entry.getValue() == null) ? "" : entry.getValue().toString());
			}
		}
	}
	
	private String FormatFileTime(FileTime filename) {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(filename.toMillis()));
	}
	
}
