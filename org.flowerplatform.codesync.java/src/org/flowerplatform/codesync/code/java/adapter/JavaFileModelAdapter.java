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
package org.flowerplatform.codesync.code.java.adapter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.file.AbstractFileModelAdapter;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * Mapped to files with the extension <code>java</code>. Children are {@link ASTNode}s.
 * 
 * @author Mariana
 */
public class JavaFileModelAdapter extends AbstractFileModelAdapter {

	/**
	 * @author see class
	 */
	public JavaFileModelAdapter() {
		containmentFeatures.add(CodeSyncJavaConstants.TYPE_MEMBERS);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.TYPE_MEMBERS.equals(feature)) {
			return getOrCreateCompilationUnit(element, codeSyncAlgorithm.getFileAccessController()).types();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}
	
	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child, CodeSyncAlgorithm codeSyncAlgorithm) {
		((ASTNode) child).delete();
	}

	private CompilationUnit getOrCreateCompilationUnit(Object file, IFileAccessController fileAccessController) {
		return (CompilationUnit) getOrCreateFileInfo(file, fileAccessController);
	}
	
	/**
	 * Creates a new compilation unit from the file's content.
	 */
	@Override
	protected Object createFileInfo(Object file, IFileAccessController fileAccessController) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setCompilerOptions(getOptions());
		char[] initialContent = getFileContent(file, fileAccessController);
		parser.setSource(initialContent);
		CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
		astRoot.recordModifications();
		return astRoot;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map getOptions() {
		Map options = new HashMap<>(JavaCore.getOptions());
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
		return options;
	}
	
	private char[] getFileContent(Object file, IFileAccessController fileAccessController) {
		boolean fileExists = fileAccessController.exists(file);
		if (!fileExists) {
			return new char[0];
		}
		return fileAccessController.readFileToString(file).toCharArray();
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo) {
		CompilationUnit cu = (CompilationUnit) fileInfo;
		return cu.rewrite(document, getOptions());
	}
	
}