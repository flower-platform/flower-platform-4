package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.team.git.GitConstants.FETCH_REF_SPECS;
import static org.flowerplatform.team.git.GitConstants.NAME;
import static org.flowerplatform.team.git.GitConstants.PUSH_REF_SPECS;
import static org.flowerplatform.team.git.GitConstants.REMOTE_URIS;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.team.git.GitUtils;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cojocea Marius Eduard
 */
public class GitRemotePropertiesProvider extends AbstractController implements IPropertiesProvider  {

	@Override
	public void populateWithProperties(Node node,ServiceContext<NodeService> context) {
		try {
			Repository repo = null;
			String repoPath = Utils.getRepo(node.getNodeUri());
					
			repo = GitUtils.getRepository((File) FileControllerUtils.getFileAccessController().getFile(repoPath));
		
			String name = (String) node.getRawNodeData();
			List<String> fetch = new ArrayList<String>();
			List<String> push = new ArrayList<String>();
			List<String> uris = new ArrayList<String>();
			
			List<RefSpec> fetchRefSpecs = new ArrayList<RefSpec>();
			List<RefSpec> pushRefSpecs = new ArrayList<RefSpec>();
			List<URIish> URIs = new ArrayList<URIish>();
			
			RemoteConfig config = new RemoteConfig(repo.getConfig(),name);
			fetchRefSpecs = config.getFetchRefSpecs();
			pushRefSpecs = config.getPushRefSpecs();
			URIs = config.getURIs();
				
			for (RefSpec spec : fetchRefSpecs) {
				fetch.add(spec.toString());
			}
			for (RefSpec spec : pushRefSpecs) {
				push.add(spec.toString());
			}
			for (URIish uri : URIs) {
				uris.add(uri.toString());
			}
				
			node.getProperties().put(NAME, name);
			node.getProperties().put(FETCH_REF_SPECS, fetch);
			node.getProperties().put(PUSH_REF_SPECS, push);
			node.getProperties().put(REMOTE_URIS, uris);
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/" + "remoteSpec.gif"));
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

}
