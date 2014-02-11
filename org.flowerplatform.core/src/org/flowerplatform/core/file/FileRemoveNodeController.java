package org.flowerplatform.core.file;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileRemoveNodeController extends RemoveNodeController {
	private IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
	@Override
	public void removeNode(Node node, Node child) {
		
		try {
			deleteFolder(fileAccessController.getFile(child.getId()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void deleteFolder(Object folder) {
		Object[] files = fileAccessController.listFiles(folder);
	    if(files != null) { 
	        for(Object f: files) {
	            if(fileAccessController.isDirectory(f)) {
	                deleteFolder(f);
	            } else {
	                fileAccessController.delete(f);
	            }
	        }
	    }
	    fileAccessController.delete(folder);
	}

}
