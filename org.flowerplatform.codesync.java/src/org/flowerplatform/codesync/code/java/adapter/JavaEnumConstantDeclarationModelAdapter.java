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

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link EnumConstantDeclaration}. Children are enum constant arguments (i.e. {@link Expression}).
 * 
 * @author Mariana	
 */
public class JavaEnumConstantDeclarationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	/**
	 * @author see class
	 */
	public JavaEnumConstantDeclarationModelAdapter() {
		containmentFeatures.add(CodeSyncJavaConstants.ENUM_CONSTANT_ARGUMENTS);
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getEnumConstant(element).getName().getIdentifier();
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			EnumConstantDeclaration enumConstant = getEnumConstant(element);
			enumConstant.setName(enumConstant.getAST().newSimpleName((String) value));
		}
		super.setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.ENUM_CONSTANT_ARGUMENTS.equals(feature)) {
			return getEnumConstant(element).arguments();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, IModelAdapterSet modelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.TYPE_MEMBERS.equals(feature)) {
			AST ast = ((ASTNode) parent).getAST();
			EnumConstantDeclaration enumCt = ast.newEnumConstantDeclaration();
			((EnumDeclaration) parent).enumConstants().add(enumCt);
			return enumCt;
		} 
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, modelAdapterSet, codeSyncAlgorithm);
	}

	private EnumConstantDeclaration getEnumConstant(Object element) {
		return (EnumConstantDeclaration) element;
	}

}
