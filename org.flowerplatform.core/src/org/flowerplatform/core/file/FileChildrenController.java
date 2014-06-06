package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public class FileChildrenController extends AbstractController 
		implements IChildrenProvider, IAddNodeController, IRemoveNodeController {

	private static IFileAccessController fileAccessController = CorePlugin
			.getInstance().getFileAccessController();

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		String ssp = node.getSchemeSpecificPart();
		String repo = ssp;
		String path = null;
		int index = ssp.indexOf(":");
		if (index > 0) {
			repo = ssp.substring(0, index);
			path = ssp.substring(index + 1);
		}
		
		Object file = null;
		try {
			file = fileAccessController.getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Object[] files = fileAccessController.listFiles(file);
		List<Node> children = new ArrayList<Node>();
		for (Object object : files) {
			if (CoreConstants.METADATA.equals(fileAccessController.getName(object)) && path == null) {
				// don't show metadata directory from workspace
				continue;
			}
			Node child = new Node(CoreConstants.FILE_NODE_TYPE, repo + ":" + fileAccessController.getPath(object), null);
			children.add(child);
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		Object file = null;
		String ssp = node.getSchemeSpecificPart();
		String path = null;
		int index = ssp.indexOf(":");
		if (index > 0) {
			path = ssp.substring(index + 1);
		}
		try {
			file = fileAccessController.getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Object[] files = fileAccessController.listFiles(file);
		if (files == null) {
			return false;
		}
		if (files.length == 1 && CoreConstants.METADATA.equals(fileAccessController.getName(files[0]))) {
			// calculate hasChildren without metadata directory
			return false;
		
		}
		return files.length > 0;
	}
	
	@Override
	public void addNode(Node parentNode, Node child, ServiceContext<NodeService> context) {
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		Object parentFile;

		String ssp = parentNode.getSchemeSpecificPart();
		String repo = ssp;
		String path = null;
		int index = ssp.indexOf(":");
		if (index > 0) {
			repo = ssp.substring(0, index);
			path = ssp.substring(index + 1);
		}
		try {
			parentFile = fileAccessController.getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!fileAccessController.isDirectory(parentFile)) {
			parentFile = fileAccessController.getParentFile(parentFile);
			path = fileAccessController.getPath(parentFile);
			Node fileParentNode;
			
			if (path.length() != 0) { // parent File is not the FileSystem node
//				fileParentNode = new Node(FILE_NODE_TYPE, parentNode.getResource(), path, null);
			} else {
//				fileParentNode = new Node(FILE_SYSTEM_NODE_TYPE, CoreConstants.SELF_RESOURCE, null, null);
			}
//			context.getService().addChild(fileParentNode, child, context);
			context.add(DONT_PROCESS_OTHER_CONTROLLERS, true);
			return;
		}
		
		String name = (String) context.get(NAME);
		Object fileToCreate = fileAccessController.getFile(parentFile, name);
		child.setNodeUri(Utils.getUri(FILE_NODE_TYPE, repo + ":" + fileAccessController.getPath(fileToCreate), null));
		boolean isDir = (boolean) context.get(FILE_IS_DIRECTORY);
		
		if (fileAccessController.exists(fileToCreate)) {
			throw new RuntimeException("There is already a file with the same name in this location.");
		} else if (!fileAccessController.createFile(fileToCreate, isDir)) {
			throw new RuntimeException("The filename, directory name, or volume label syntax is incorrect");
		}
		child.getOrPopulateProperties();
	}
	
	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		IFileAccessController fileAccessController = CorePlugin
				.getInstance().getFileAccessController();	
		try {
			fileAccessController.delete(fileAccessController
					.getFile(child.getFragment()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
