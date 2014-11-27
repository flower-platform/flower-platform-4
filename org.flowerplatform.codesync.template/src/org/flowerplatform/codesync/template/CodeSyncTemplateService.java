/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.codesync.template;

import static org.flowerplatform.core.CoreConstants.NAME;

import java.util.List;

import org.apache.velocity.VelocityContext;
import org.flowerplatform.codesync.template.config_loader.TemplatesEngineController;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncTemplateService {

	/**
	 * 
	 */
	public void generate(String nodeUri) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		String repo = CoreUtils.getRepoFromNode(node);

		// merge
		TemplatesEngineController engine = new TemplatesEngineController();
		engine.addTemplatesDirectory(repo + "/tpl/");
		engine.init();
		VelocityContext context = createVelocityContext(node);
		String output = engine.merge(context);
		
		// write output
		String outputPath = repo + "/gen/";
		try {
			Object outputFile = CorePlugin.getInstance().getFileAccessController().getFile(outputPath + node.getPropertyValue(NAME));
			CorePlugin.getInstance().getFileAccessController().writeStringToFile(outputFile, output.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a {@link VelocityContext} containing the full node hierarchy and the {@link Indenter} class.
	 */
	public VelocityContext createVelocityContext(Node node) {
		VelocityContext context = new VelocityContext();
		context.put("node", populateSubTree(node));
		context.put("Indenter", Indenter.class);
		return context;
	}
	
	/**
	 * Populate properties and children list for the entire tree rooted at node.
	 */
	private Node populateSubTree(Node node) {
		// populate properties
		node.getOrPopulateProperties(new ServiceContext<NodeService>());
		
		// populate children
		List<Node> children = CorePlugin.getInstance().getNodeService().getChildren(node, new ServiceContext<NodeService>());
		node.setChildren(children.size() == 0 ? null : children);
		for (Node child : children) {
			populateSubTree(child);
		}
		return node;
	}
	
}
