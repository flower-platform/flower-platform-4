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
package org.flowerplatform.codesync.code.java.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.code.adapter.AbstractFileModelAdapter;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.file.FileHolder;

/**
 * Mapped to files with the extension <code>java</code>. Chidren are {@link ASTNode}s.
 * 
 * @author Mariana
 */
public class JavaFileModelAdapter extends AbstractFileModelAdapter {

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncCodeJavaConstants.TYPE_MEMBERS.equals(feature)) {
			return getChildren(element);
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}
	
	@Override
	public Object createChildOnContainmentFeature(Object file, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		if (CodeSyncCodeJavaConstants.TYPE_MEMBERS.equals(feature)) {
			CompilationUnit cu = getOrCreateCompilationUnit(file);
			ASTNode node = (ASTNode) JavaTypeDeclarationModelAdapter.createCorrespondingModelElement(cu.getAST(), (Node) correspondingChild);
			cu.types().add(node);
			return node;
		}
		return super.createChildOnContainmentFeature(file, feature, correspondingChild, typeProvider);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		((ASTNode) child).delete();
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return getOrCreateCompilationUnit(modelElement).types();
	}
	
	private CompilationUnit getOrCreateCompilationUnit(Object file) {
		return (CompilationUnit) getOrCreateFileInfo(file);
	}
	
	/**
	 * Creates a new compilation unit from the file's content.
	 */
	@Override
	protected Object createFileInfo(Object file) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setCompilerOptions(getOptions());
		char[] initialContent = getFileContent(file);
		parser.setSource(initialContent);
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		return astRoot;
	}

	private Map getOptions() {
		Map options = new HashMap<>(JavaCore.getOptions());
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
		return options;
	}
	
	private char[] getFileContent(Object file) {
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
//		boolean fileExists = ((FileHolder) file).exists();
		boolean fileExists = fileAccessController.exists(file);
		if (!fileExists) {
			return new char[0];
		}
//		return ((FileHolder) file).getContent().toCharArray();
		return fileAccessController.readFileToString(file).toCharArray();
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo) {
		CompilationUnit cu = (CompilationUnit) fileInfo;
		return cu.rewrite(document, getOptions());
	}
	
}