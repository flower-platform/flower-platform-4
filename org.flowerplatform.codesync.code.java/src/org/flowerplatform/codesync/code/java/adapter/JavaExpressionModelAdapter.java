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

import static org.flowerplatform.core.CoreConstants.NAME;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.ModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants;

/**
 * Mapped to {@link Expression} and {@link Type}.
 * 
 * @author Mariana Gheorghe
 */
public class JavaExpressionModelAdapter extends JavaAbstractAstNodeModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return element.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, 
			Object correspondingChild, ModelAdapterSet correspondingModelAdapterSet) {
		if (CodeSyncCodeJavaConstants.SUPER_INTERFACES.equals(feature)) {
			if (parent instanceof TypeDeclaration || parent instanceof EnumDeclaration) {
				AbstractTypeDeclaration cls = (AbstractTypeDeclaration) parent;
				AST ast = cls.getAST();
				IModelAdapter correspondingModelAdapter = correspondingModelAdapterSet.getModelAdapter(correspondingChild);
				String value = (String) correspondingModelAdapter.getValueFeatureValue(correspondingChild, NAME, null);
				Type type = getTypeFromString(ast, value);
				if (cls instanceof TypeDeclaration) {
					((TypeDeclaration) cls).superInterfaceTypes().add(type);
				} else if (cls instanceof EnumDeclaration) {
					((EnumDeclaration) cls).superInterfaceTypes().add(type);
				}
				return type;
			}
			throw new RuntimeException("Cannot create super interface for " + parent);
		} else if (CodeSyncCodeJavaConstants.ENUM_CONSTANT_ARGUMENTS.equals(feature)) {
			AST ast = ((ASTNode) parent).getAST();
			IModelAdapter correspondingModelAdapter = correspondingModelAdapterSet.getModelAdapter(correspondingChild);
			String value = (String) correspondingModelAdapter.getValueFeatureValue(correspondingChild, NAME, null);
			Expression arg = getExpressionFromString(ast, value);
			((EnumConstantDeclaration) parent).arguments().add(arg);
			return arg;
		}
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, correspondingModelAdapterSet);
	}
	
}