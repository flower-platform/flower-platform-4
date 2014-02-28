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
package org.flowerplatform.codesync.code.java.adapter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.flowerplatform.codesync.code.java.feature_provider.JavaFeaturesConstants;
import org.flowerplatform.codesync.code.java.feature_provider.JavaOperationFeatureProvider;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;

/**
 * Mapped to {@link MethodDeclaration}. Children are {@link Modifier}s and parameters (i.e. {@link SingleVariableDeclaration}).
 * 
 * @see JavaOperationFeatureProvider
 * 
 * @author Mariana
 */
public class JavaOperationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public static final String OPERATION = "javaOperation";
	
	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (JavaOperationFeatureProvider.OPERATION_PARAMETERS.equals(feature)) {
			return ((MethodDeclaration) element).parameters();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (FeatureProvider.NAME.equals(feature)) {
			return getLabel(element);
		} else if (JavaFeaturesConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			return getStringFromType(getMethodDeclaration(element).getReturnType2());
		} else if (JavaOperationFeatureProvider.HAS_BODY.equals(feature)) {
			return getMethodDeclaration(element).getBody() != null;
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (FeatureProvider.NAME.equals(feature)) {
			MethodDeclaration method = getMethodDeclaration(element);
			String name = (String) value;
			int index = name.indexOf("(");
			if (index == -1) {
				index = name.length();
			}
			name = name.substring(0, index);
			method.setName(method.getAST().newSimpleName(name));
		} else if (JavaFeaturesConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			MethodDeclaration method = getMethodDeclaration(element);
			Type type = getTypeFromString(method.getAST(), (String) value);
			method.setReturnType2(type);
		} else if (JavaOperationFeatureProvider.HAS_BODY.equals(feature)) {
			// needed to create methods with empty bodies
			MethodDeclaration method = getMethodDeclaration(element);
			if (value != null && (boolean) value) {
				if (method.getBody() == null) {
					method.setBody(method.getAST().newBlock());
				}
			} else {
				method.setBody(null);
			}
		}
		super.setValueFeatureValue(element, feature, value);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		if (JavaOperationFeatureProvider.OPERATION_PARAMETERS.equals(feature)) {
			MethodDeclaration method = (MethodDeclaration) element;
			AST ast = method.getAST();
			SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
			method.parameters().add(parameter);
			return parameter;
		}
		return super.createChildOnContainmentFeature(element, feature, correspondingChild, typeProvider);
	}
	
	private MethodDeclaration getMethodDeclaration(Object element) {
		return (MethodDeclaration) element;
	}
	
	@Override
	public Object getMatchKey(Object modelElement) {
		MethodDeclaration method = getMethodDeclaration(modelElement);
		String label = method.getName().getIdentifier();
		label += "(";
		for (Object param : method.parameters()) {
			label += ((SingleVariableDeclaration) param).getType().toString() + ",";
		}
		if (label.endsWith(",")) {
			label = label.substring(0, label.length() - 1);
		}
		label += ")";
		return label;
	}
	
}
