/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.team.git.controller;

import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.team.git.GitConstants.FETCH_REF_SPECS;
import static org.flowerplatform.team.git.GitConstants.NAME;
import static org.flowerplatform.team.git.GitConstants.PUSH_REF_SPECS;
import static org.flowerplatform.team.git.GitConstants.REMOTE_URIS;

import java.util.ArrayList;
import java.util.List;

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
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		try {
			String name = (String) node.getRawNodeData();
			List<String> fetch = new ArrayList<String>();
			List<String> push = new ArrayList<String>();
			List<String> uris = new ArrayList<String>();

			RemoteConfig config = new RemoteConfig(GitUtils.getRepository(FileControllerUtils.getFileAccessController().getFile(Utils.getRepo(node.getNodeUri()))).getConfig(), name);

			for (RefSpec spec : config.getFetchRefSpecs()) {
				fetch.add(spec.toString());
			}
			for (RefSpec spec : config.getPushRefSpecs()) {
				push.add(spec.toString());
			}
			for (URIish uri : config.getURIs()) {
				uris.add(uri.toString());
			}
				
			node.getProperties().put(NAME, name);
			node.getProperties().put(FETCH_REF_SPECS, fetch);
			node.getProperties().put(PUSH_REF_SPECS, push);
			node.getProperties().put(REMOTE_URIS, uris);
			node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl("/images/team.git/remoteSpec.gif"));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}