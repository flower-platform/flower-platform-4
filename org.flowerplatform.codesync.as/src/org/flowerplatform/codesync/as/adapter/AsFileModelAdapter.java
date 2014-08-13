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
package org.flowerplatform.codesync.as.adapter;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.internal.definitions.ClassDefinition;
import org.apache.flex.compiler.internal.projects.DefinitionPriority;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.internal.scopes.MXMLFileScope;
import org.apache.flex.compiler.internal.tree.as.FileNode;
import org.apache.flex.compiler.internal.tree.mxml.MXMLFileNode;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.scopes.IASScope;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.tree.as.IScopedNode;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.compiler.units.requests.IRequest;
import org.apache.flex.compiler.units.requests.ISyntaxTreeRequestResult;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.file.AbstractFileModelAdapter;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.as.DelegatingFileSpecification;
import org.flowerplatform.codesync.as.asdoc.AsDocDelegate;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.util.Pair;

/**
 * @author Mariana Gheorghe
 */
public class AsFileModelAdapter extends AbstractFileModelAdapter {

	public AsFileModelAdapter() {
		containmentFeatures.add(CodeSyncAsConstants.STATEMENTS);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncAsConstants.STATEMENTS.equals(feature)) {
			@SuppressWarnings("unchecked")
			Pair<ICompilationUnit, IASNode> pair = (Pair<ICompilationUnit, IASNode>) 
					getOrCreateFileInfo(element, codeSyncAlgorithm.getFileAccessController());
			if (pair.a == null) {
				return Collections.emptyList();
			}

			IASNode ast = pair.b;
			if (ast == null) {
				return Collections.emptyList();
			}
			if (ast instanceof FileNode) {
				FileNode fileNode = (FileNode) ast;
				return Arrays.asList(fileNode.getTopLevelDefinitions(true, true));
			} else if (ast instanceof MXMLFileNode) {
				MXMLFileNode fileNode = (MXMLFileNode) ast;
				return Arrays.asList(((MXMLFileScope) fileNode.getScope()).getMainClassDefinition());
			} else {
				if (ast instanceof IScopedNode) {
					IASScope scope = ((IScopedNode) ast).getScope();
					return Arrays.asList(scope.getDefinition());
				}
			}
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}

	/**
	 * The file info must be a pair of {@link ICompilationUnit} and AST root, because the 
	 * they are referred through {@link WeakReference}s, and will be garbage collected during sync.
	 */
	@Override
	protected Object createFileInfo(Object file, IFileAccessController fileAccessController) {
		// prepare the workspace and project
		Workspace ws = new Workspace();
		ws.setASDocDelegate(new AsDocDelegate());
		FlexProject project = new FlexProject(ws);
		
		// add the file spec that will be used during AST build
		ws.fileAdded(new DelegatingFileSpecification(file, fileAccessController));
		
		// create compilation unit
		ICompilationUnit cu = project.getSourceCompilationUnitFactory().createCompilationUnit(
				new File(fileAccessController.getAbsolutePath(file)),  
				DefinitionPriority.BasePriority.SOURCE_PATH, 0, null, null);
		IRequest<ISyntaxTreeRequestResult, ICompilationUnit> req = cu.getSyntaxTreeRequest();
		
		// create AST rooted at IFileNode
		IASNode ast = null;
		try {
			ast = (IFileNode) req.get().getAST();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		if (ast instanceof MXMLFileNode) {
			MXMLFileNode fileNode = (MXMLFileNode) ast;
			ClassDefinition classDef = ((MXMLFileScope) fileNode.getScope()).getMainClassDefinition();
			if (classDef == null) {
				// empty file; was added/deleted
				ast = null; 
			} else {
				Collection<IDefinition> definitions = classDef.getContainedScope().getAllLocalDefinitions();
				if (!definitions.isEmpty()) {
					IDefinitionNode node = definitions.iterator().next().getNode();
					if (node != null) {
						ast = node.getContainingScope();
					}
				}
			}
		}
		return new Pair<ICompilationUnit, IASNode>(cu, ast);
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}