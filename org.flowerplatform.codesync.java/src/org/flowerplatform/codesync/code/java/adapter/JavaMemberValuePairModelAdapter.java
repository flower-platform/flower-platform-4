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

import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ANNOTATION_VALUE_VALUE;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link MemberValuePair}.
 * 
 * @author Mariana
 */
public class JavaMemberValuePairModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public JavaMemberValuePairModelAdapter() {
		valueFeatures.add(CodeSyncJavaConstants.ANNOTATION_VALUE_VALUE);
	}
	
	@Override
	public Object getMatchKey(Object modelElement, CodeSyncAlgorithm codeSyncAlgorithm) {
		return ((MemberValuePair) modelElement).getName().getIdentifier();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.ANNOTATION_VALUE_VALUE.equals(feature)) {
			return getStringFromExpression(((MemberValuePair) element).getValue());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			MemberValuePair pair = (MemberValuePair) element;
			String name = (String) value;
			pair.setName(pair.getAST().newSimpleName(name));
		} else if (CodeSyncJavaConstants.ANNOTATION_VALUE_VALUE.equals(feature)) {
			MemberValuePair pair = (MemberValuePair) element;
			String expression = (String) value;
			pair.setValue(getExpressionFromString(pair.getAST(), expression));
		}
		super.setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, 
			Object correspondingChild, IModelAdapterSet correspondingModelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.ANNOTATION_VALUES.equals(feature)) {
			ASTNode child = null;
			AST ast = ((ASTNode) parent).getAST();
			
			// for an existing NormalAnnotation, just add the new value
			if (parent instanceof NormalAnnotation) {
				MemberValuePair pair = ast.newMemberValuePair();
				((NormalAnnotation) parent).values().add(pair);
				child = pair;
			} else {
				// if the existing annotation is a SingleMemberAnnotation, then set its value
				if (parent instanceof SingleMemberAnnotation) {
					IModelAdapter correspondingModelAdapter = correspondingModelAdapterSet.getModelAdapter(correspondingChild, codeSyncAlgorithm);
					ASTNode expression = getExpressionFromString(((ASTNode) parent).getAST(), 
							(String) correspondingModelAdapter.getValueFeatureValue(correspondingChild, ANNOTATION_VALUE_VALUE, null, codeSyncAlgorithm));
					((SingleMemberAnnotation) parent).setValue((Expression) expression);
					child = ast.newMemberValuePair(); // avoid NPE later
				}
			}
		
			return child;
		}
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, correspondingModelAdapterSet, codeSyncAlgorithm);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.ANNOTATION_VALUES.equals(feature) && !(parent instanceof NormalAnnotation)) {
			return;
		}
		super.removeChildrenOnContainmentFeature(parent, feature, child, codeSyncAlgorithm);
	}

}