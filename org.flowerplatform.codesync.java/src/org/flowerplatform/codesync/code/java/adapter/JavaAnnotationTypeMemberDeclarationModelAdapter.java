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
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped by {@link AnnotationTypeMemberDeclaration}. Children are {@link Modifier}s.
 * 
 * @author Mariana
 */
public class JavaAnnotationTypeMemberDeclarationModelAdapter extends JavaAbstractAstNodeModelAdapter {
	
	/**
	 * @author see class
	 */
	public JavaAnnotationTypeMemberDeclarationModelAdapter() {
		valueFeatures.add(CodeSyncJavaConstants.DOCUMENTATION);
		valueFeatures.add(CodeSyncJavaConstants.TYPED_ELEMENT_TYPE);
		valueFeatures.add(CodeSyncJavaConstants.ANNOTATION_MEMBER_DEFAULT_VALUE);
		
		containmentFeatures.add(CodeSyncJavaConstants.MODIFIERS);
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getAnnotationMember(element).getName().getIdentifier();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.ANNOTATION_MEMBER_DEFAULT_VALUE.equals(feature)) {
			return getStringFromExpression(getAnnotationMember(element).getDefault());
		} else if (CodeSyncJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			return getStringFromType(getAnnotationMember(element).getType());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			AnnotationTypeMemberDeclaration member = getAnnotationMember(element);
			member.setName(member.getAST().newSimpleName((String) value));
		} else if (CodeSyncJavaConstants.ANNOTATION_MEMBER_DEFAULT_VALUE.equals(feature)) {
			AnnotationTypeMemberDeclaration member = getAnnotationMember(element);
			member.setDefault(getExpressionFromString(member.getAST(), (String) value));
		} else if (CodeSyncJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			AnnotationTypeMemberDeclaration member = getAnnotationMember(element);
			member.setType(getTypeFromString(member.getAST(), (String) value));
		}
		super.setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature,
			Object correspondingChild, IModelAdapterSet correspondingModelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.TYPE_MEMBERS.equals(feature)) {
			AST ast = ((ASTNode) parent).getAST();
			AnnotationTypeMemberDeclaration member = ast.newAnnotationTypeMemberDeclaration();
			((AbstractTypeDeclaration) parent).bodyDeclarations().add(member);
			return member;
		} 
		return super.createChildOnContainmentFeature(parent, feature,
				correspondingChild, correspondingModelAdapterSet, codeSyncAlgorithm);
	}

	private AnnotationTypeMemberDeclaration getAnnotationMember(Object element) {
		return (AnnotationTypeMemberDeclaration) element;
	}

}