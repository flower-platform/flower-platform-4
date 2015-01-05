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
package org.flowerplatform.codesync.remote;

import static org.flowerplatform.codesync.CodeSyncConstants.NODE_ANCESTOR;
import static org.flowerplatform.codesync.CodeSyncConstants.NODE_LEFT;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.util.Collections;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncAlgorithm.Side;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.file.CodeSyncFile;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncOperationsService {

	/**
	 *@author Valentina Bojan
	 **/
	public Match synchronize(String nodeUri) throws Exception {
		Node srcDir = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();

		while (true) {
			Node parent = CorePlugin.getInstance().getNodeService().getParent(srcDir, context);
			Node grandParent = CorePlugin.getInstance().getNodeService().getParent(parent, context);

			if (grandParent == null) {
				break;
			}
			srcDir = parent;
		}

		Object file = CorePlugin.getInstance().getFileAccessController().getFile(CoreUtils.getRepoFromNode(srcDir) + "/" + srcDir.getPropertyValue(NAME));

		return synchronize(srcDir.getNodeUri(), file, "java", true);
	}
	
	/**
	 * @param nodeUri The nodeUri corresponding to the given file
	 */
	public Match synchronize(String nodeUri, Object file, String technology, boolean oneStepSync) {
		Match match = generateMatch(nodeUri, file, technology, oneStepSync);
		if (!oneStepSync) {
			performSync(match);
		}
		
		return match;
	}
	
	/**
	 *@author Valentina Bojan
	 **/
	public Match generateMatch(String nodeUri, Object file, String technology, boolean oneStepSync) {
		Node srcDir = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		
		// START THE ALGORITHM
		
		// STEP 1: create a match
		Match match = new Match();
		
		// ancestor + left: model (Node structure)
		match.setAncestor(srcDir);
		match.setLeft(srcDir);
	
		// right: source code (file system)
		Object ast = file;
		match.setRight(new CodeSyncFile(ast));
		
		// initialize the algorithm
		CodeSyncAlgorithm algorithm = new CodeSyncAlgorithm();
		algorithm.initializeModelAdapterSets(
				Collections.singletonList(NODE_LEFT), 
				Collections.singletonList(technology),
				Collections.singletonList(NODE_ANCESTOR));
		algorithm.initializeFeatureProvider(Side.RIGHT);
		algorithm.setFileAccessController(CorePlugin.getInstance().getFileAccessController()); 
		match.setCodeSyncAlgorithm(algorithm);
		match.setMatchKey(algorithm.getAncestorModelAdapter(srcDir).getMatchKey(srcDir, algorithm));
		
		// STEP 2: generate the diff, i.e. 3-way compare
		algorithm.generateDiff(match, oneStepSync);
		
		return match;
	}
		
	/**
	 *@author Mariana Gheorghe
	 **/
	public Match performSync(Match match) {
		// STEP 3: sync
		match.getCodeSyncAlgorithm().synchronize(match);
		return match;
	}
	
}