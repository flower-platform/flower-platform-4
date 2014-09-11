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
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link SingleVariableDeclaration}. Children are {@link Modifier}s.
 * 
 * @author Mariana
 */
public class JavaParameterModelAdapter extends JavaAbstractAstNodeModelAdapter {

	/**
	 *@author Mariana Gheorghe
	 **/
	public JavaParameterModelAdapter() {
		valueFeatures.add(CodeSyncJavaConstants.TYPED_ELEMENT_TYPE);
		containmentFeatures.add(CodeSyncJavaConstants.MODIFIERS);
	}
	
	@Override
	public Object getMatchKey(Object modelElement, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getVariableDeclaration(modelElement).getName().getIdentifier();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			return getStringFromType(getVariableDeclaration(element).getType());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			SingleVariableDeclaration parameter = getVariableDeclaration(element);
			String name = (String) value;
			parameter.setName(parameter.getAST().newSimpleName(name));
		} else if (CodeSyncJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			SingleVariableDeclaration parameter = getVariableDeclaration(element);
			Type type = getTypeFromString(parameter.getAST(), (String) value);
			parameter.setType(type);
		}
		super.setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, 
			IModelAdapterSet correspondingModelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.OPERATION_PARAMETERS.equals(feature)) {
			MethodDeclaration method = (MethodDeclaration) parent;
			AST ast = method.getAST();
			SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
			method.parameters().add(parameter);
			return parameter;
		}
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, correspondingModelAdapterSet, codeSyncAlgorithm);
	}

	private SingleVariableDeclaration getVariableDeclaration(Object element) {
		return (SingleVariableDeclaration) element;
	}

}