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
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.flowerplatform.codesync.adapter.ModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAttributeFeatureProvider;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link FieldDeclaration}. Children are {@link Modifier}s.
 * 
 * @see JavaAttributeFeatureProvider
 * 
 * @author Mariana
 */
public class JavaAttributeModelAdapter extends JavaAbstractAstNodeModelAdapter {

	@Override
	public Object getMatchKey(Object modelElement) {
		VariableDeclaration var = (VariableDeclaration) getFieldDeclaration(modelElement).fragments().get(0);
		return var.getName().getIdentifier();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CodeSyncCodeJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			return getStringFromType(getFieldDeclaration(element).getType());
		} else if (CodeSyncCodeJavaConstants.ATTRIBUTE_INITIALIZER.equals(feature)) {
			VariableDeclaration var = (VariableDeclaration) getFieldDeclaration(element).fragments().get(0);
			return getStringFromExpression(var.getInitializer());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (CoreConstants.NAME.equals(feature)) {
			FieldDeclaration field = getFieldDeclaration(element);
			String name = (String) value;
			VariableDeclaration var = (VariableDeclaration) field.fragments().get(0);
			var.setName(field.getAST().newSimpleName(name));
		} else if (CodeSyncCodeJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			FieldDeclaration field = getFieldDeclaration(element);
			Type type = getTypeFromString(field.getAST(), (String) value);
			field.setType(type);
		} else if (CodeSyncCodeJavaConstants.ATTRIBUTE_INITIALIZER.equals(feature)) {
			VariableDeclaration var = (VariableDeclaration) getFieldDeclaration(element).fragments().get(0);
			var.setInitializer(getExpressionFromString(var.getAST(), (String) value));
		}
		super.setValueFeatureValue(element, feature, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, 
			Object correspondingChild, ModelAdapterSet correspondingModelAdapterSet) {
		if (CodeSyncCodeJavaConstants.TYPE_MEMBERS.equals(feature)) {
			AST ast = ((ASTNode) parent).getAST();
			VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
			FieldDeclaration field = ast.newFieldDeclaration(fragment);
			((AbstractTypeDeclaration) parent).bodyDeclarations().add(field);
			return field;
		} 
		return super.createChildOnContainmentFeature(parent, feature,
				correspondingChild, correspondingModelAdapterSet);
	}

	private FieldDeclaration getFieldDeclaration(Object element) {
		return (FieldDeclaration) element;
	}
	
}