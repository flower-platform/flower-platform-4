package org.flowerplatform.team.git.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.team.git.GitUtils;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitBranchesAndTagsChildrenProvider extends AbstractController implements IChildrenProvider {

	private String refType;
	private String scheme;
	private String type;
	
	
	public GitBranchesAndTagsChildrenProvider(String refType, String scheme, String type){
		this.refType = refType;
		this.scheme = scheme;
		this.type = type;
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		Repository repo = null;
		String repoPath = Utils.getRepo(node.getNodeUri());
		try {
			repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Map<String, org.eclipse.jgit.lib.Ref> local = repo.getRefDatabase().getRefs(refType);
			
			for(Entry<String, org.eclipse.jgit.lib.Ref> entry : local.entrySet()){
				String path = new String();
				path = repoPath + "|" + type + "$" + entry.getValue().getName();
				children.add(CorePlugin.getInstance().getResourceService().getResourceHandler(scheme)
						.createNodeFromRawNodeData(Utils.getUri(scheme, path), entry.getValue()));
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
