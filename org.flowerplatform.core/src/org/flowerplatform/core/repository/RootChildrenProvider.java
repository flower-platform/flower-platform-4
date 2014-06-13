package org.flowerplatform.core.repository;

import static org.flowerplatform.core.CoreConstants.METADATA;
import static org.flowerplatform.core.CoreConstants.REPOSITORY_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Scans the workspace to create repository nodes.
 * 
 * Workspace structure:
 * <ul>
 * 	<li>user-1
 * 		<ul>
 * 		<li>repo-1
 * 		<li>repo-2
 * 		</ul>
 * 	<li>user-2
 * 		<ul>
 * 		<li>repo-1
 * 		<li>repo-2
 * 		</ul>
 * </ul>
 * 
 * For this structure, the repositories will be: <tt>user-1/repo-1</tt>, 
 * <tt>user-1/repo-2</tt>, <tt>user-2/repo-1</tt>, <tt>user-2/repo-2</tt>.
 * 
 * @author Sebastian Solomon
 * @author Mariana Gheorghe
 */
public class RootChildrenProvider extends AbstractController implements IChildrenProvider {

	public RootChildrenProvider() {
		setOrderIndex(200);
	}

	/**
	 * 
	 */
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		Object root = null;
		try {
			root = fileAccessController.getFile(null);
		} catch (Exception e) {
			throw new RuntimeException("Error accessing the workspace", e);
		}
		for (Object user : fileAccessController.listFiles(root)) {
			if (METADATA.equals(fileAccessController.getName(user))) {
				// skip the .metadata directory from the workspace
				continue;
			}
			for (Object repository : fileAccessController.listFiles(user)) {
				String path = fileAccessController.getPath(repository);
				children.add(new Node(Utils.getUri(REPOSITORY_TYPE, path), REPOSITORY_TYPE));
			}
		}
		return children;
	}
	
	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}
}
