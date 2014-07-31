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
package org.flowerplatform.codesync.remote;

import static org.flowerplatform.codesync.CodeSyncConstants.NODE_ANCESTOR;
import static org.flowerplatform.codesync.CodeSyncConstants.NODE_LEFT;

import java.io.File;
import java.util.Collections;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.CodeSyncAlgorithm.Side;
import org.flowerplatform.codesync.adapter.file.CodeSyncFile;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncOperationsService {

	public Match synchronize(String nodeUri) throws Exception {
		return synchronize(nodeUri, CorePlugin.getInstance().getFileAccessController().getFile("user1/repo-1/my_files/modified_conflicts"), "java", true);
	}
		
	public Match synchronize(String nodeUri, Object file, String technology, boolean oneStepSync) {
		Match match = generateMatch(nodeUri, file, technology, oneStepSync);
		if (!oneStepSync) {
			performSync(match);
		}
		
		return match;
	}
	
	public Match generateMatch(String nodeUri, Object file, String technology, boolean oneStepSync) {
		// find containing SrcDir
		Node root = CodeSyncPlugin.getInstance().getCodeSyncMappingRoot(nodeUri);
		Node srcDir = null;
		File parent = (File) file;
		do {
			srcDir = CodeSyncPlugin.getInstance().getSrcDir(root, parent.getName());
			parent = parent.getParentFile();
		} while (srcDir == null && (parent != null));
		if (srcDir == null) {
			throw new RuntimeException("File " + file + " is not contained in a SrcDir!");
		}
		
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
		
	public Match performSync(Match match) {
		// STEP 3: sync
		match.getCodeSyncAlgorithm().synchronize(match);
		return match;
	}
	
}