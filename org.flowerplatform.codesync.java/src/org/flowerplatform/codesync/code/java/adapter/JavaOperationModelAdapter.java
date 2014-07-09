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
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link MethodDeclaration}. Children are {@link Modifier}s and parameters (i.e. {@link SingleVariableDeclaration}).
 * 
 * @author Mariana
 */
public class JavaOperationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public JavaOperationModelAdapter() {
		valueFeatures.add(CodeSyncJavaConstants.DOCUMENTATION);
		valueFeatures.add(CodeSyncJavaConstants.TYPED_ELEMENT_TYPE);
		valueFeatures.add(CodeSyncJavaConstants.OPERATION_HAS_BODY);
		
		containmentFeatures.add(CodeSyncJavaConstants.MODIFIERS);
		containmentFeatures.add(CodeSyncJavaConstants.OPERATION_PARAMETERS);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.OPERATION_PARAMETERS.equals(feature)) {
			return ((MethodDeclaration) element).parameters();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			return getStringFromType(getMethodDeclaration(element).getReturnType2());
		} else if (CodeSyncJavaConstants.OPERATION_HAS_BODY.equals(feature)) {
			return getMethodDeclaration(element).getBody() != null;
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			MethodDeclaration method = getMethodDeclaration(element);
			String name = (String) value;
			int index = name.indexOf("(");
			if (index == -1) {
				index = name.length();
			}
			name = name.substring(0, index);
			method.setName(method.getAST().newSimpleName(name));
		} else if (CodeSyncJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			MethodDeclaration method = getMethodDeclaration(element);
			Type type = getTypeFromString(method.getAST(), (String) value);
			method.setReturnType2(type);
		} else if (CodeSyncJavaConstants.OPERATION_HAS_BODY.equals(feature)) {
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
		super.setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, 
			Object correspondingChild, IModelAdapterSet correspondingModelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.TYPE_MEMBERS.equals(feature)) {
			AST ast = ((ASTNode) parent).getAST();
			MethodDeclaration method = ast.newMethodDeclaration();
			((AbstractTypeDeclaration) parent).bodyDeclarations().add(method);
			return method;
		} 
		return super.createChildOnContainmentFeature(parent, feature,
				correspondingChild, correspondingModelAdapterSet, codeSyncAlgorithm);
	}
	
	private MethodDeclaration getMethodDeclaration(Object element) {
		return (MethodDeclaration) element;
	}
	
	@Override
	public Object getMatchKey(Object modelElement, CodeSyncAlgorithm codeSyncAlgorithm) {
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