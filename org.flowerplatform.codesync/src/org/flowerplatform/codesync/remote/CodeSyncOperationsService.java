/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.remote;

import java.io.File;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.type_provider.ComposedTypeProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncOperationsService {

	public void synchronize(String path) {
//		File diagram;
//		try {
//			diagram = (File) EditorPlugin.getInstance().getFileAccessController().getFile(path);
//		} catch (Exception e) {
//			throw new RuntimeException(path);
//		}
//		File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile(diagram);
//		File srcDir = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(project, "js");
//		CodeSyncPlugin.getInstance().getCodeSyncAlgorithmRunner().runCodeSyncAlgorithm(project, srcDir, technology, context.getCommunicationChannel(), true);
	
		// TODO test
		path = "D:/temp/";
		File project = new File(path);
		File file = new File(path, "sync_test");
		
		// find model file
		Node root = CodeSyncPlugin.getInstance().getCodeSyncMappingRoot(project);
		
		// find containing SrcDir
		Node srcDir = null;
		File parent = file;
		do {
			srcDir = CodeSyncPlugin.getInstance().getSrcDir(root, parent.getName());
			parent = parent.getParentFile();
		} while (srcDir == null && !parent.equals(project));
		if (srcDir == null) {
			throw new RuntimeException("File " + file + " is not contained in a SrcDir!");
		}
		
		String technology = "java";
		
		// START THE ALGORITHM
		
		// STEP 1: create a match
		Match match = new Match();
		
		// ancestor + left: model (Node structure)
		match.setAncestor(srcDir);
		match.setLeft(srcDir);
	
		// right: source code (file system)
		Object ast = file;
		match.setRight(ast);
		
		// initialize the algorithm
		ITypeProvider typeProvider = new ComposedTypeProvider()
				.addTypeProvider(CodeSyncPlugin.getInstance().getTypeProvider("node"))
				.addTypeProvider(CodeSyncPlugin.getInstance().getTypeProvider(technology));
		TypeDescriptorRegistry typeDescriptorRegistry = CorePlugin.getInstance().getNodeTypeDescriptorRegistry();
		
		CodeSyncAlgorithm algorithm = new CodeSyncAlgorithm(typeDescriptorRegistry, typeProvider);
		match.setCodeSyncAlgorithm(algorithm);
		
		// STEP 2: generate the diff, i.e. 3-way compare
		algorithm.generateDiff(match, true);
		
		// STEP 3: sync
//		algorithm.synchronize(match);
		
		save(match, true);
		save(match, false);
	}
	
	private void save(Match match, boolean isLeft) {
		Object lateral = isLeft ? match.getLeft() : match.getRight();
		AbstractModelAdapter adapter = isLeft ? match.getCodeSyncAlgorithm().getLeftModelAdapter(lateral)
				: match.getCodeSyncAlgorithm().getRightModelAdapter(lateral);
		if (lateral != null) {
			if (adapter.save(lateral)) {
				for (Match subMatch : match.getSubMatches()) {
					save(subMatch, isLeft);
				}
			}
		} 
	}
}
