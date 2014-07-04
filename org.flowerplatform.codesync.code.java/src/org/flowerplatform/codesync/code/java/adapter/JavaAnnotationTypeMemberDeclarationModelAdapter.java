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
import org.eclipse.jdt.core.dom.Modifier;
import org.flowerplatform.codesync.adapter.ModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationTypeMemberDeclarationFeatureProvider;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped by {@link AnnotationTypeMemberDeclaration}. Children are {@link Modifier}s.
 * 
 * @see JavaAnnotationTypeMemberDeclarationFeatureProvider
 * 
 * @author Mariana
 */
public class JavaAnnotationTypeMemberDeclarationModelAdapter extends JavaAbstractAstNodeModelAdapter {
	
	@Override
	public Object getMatchKey(Object element) {
		return getAnnotationMember(element).getName().getIdentifier();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CodeSyncCodeJavaConstants.ANNOTATION_MEMBER_DEFAULT_VALUE.equals(feature)) {
			return getStringFromExpression(getAnnotationMember(element).getDefault());
		} else if (CodeSyncCodeJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			return getStringFromType(getAnnotationMember(element).getType());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (CoreConstants.NAME.equals(feature)) {
			AnnotationTypeMemberDeclaration member = getAnnotationMember(element);
			member.setName(member.getAST().newSimpleName((String) value));
		} else if (CodeSyncCodeJavaConstants.ANNOTATION_MEMBER_DEFAULT_VALUE.equals(feature)) {
			AnnotationTypeMemberDeclaration member = getAnnotationMember(element);
			member.setDefault(getExpressionFromString(member.getAST(), (String) value));
		} else if (CodeSyncCodeJavaConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			AnnotationTypeMemberDeclaration member = getAnnotationMember(element);
			member.setType(getTypeFromString(member.getAST(), (String) value));
		}
		super.setValueFeatureValue(element, feature, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature,
			Object correspondingChild, ModelAdapterSet correspondingModelAdapterSet) {
		if (CodeSyncCodeJavaConstants.TYPE_MEMBERS.equals(feature)) {
			AST ast = ((ASTNode) parent).getAST();
			AnnotationTypeMemberDeclaration member = ast.newAnnotationTypeMemberDeclaration();
			((AbstractTypeDeclaration) parent).bodyDeclarations().add(member);
			return member;
		} 
		return super.createChildOnContainmentFeature(parent, feature,
				correspondingChild, correspondingModelAdapterSet);
	}

	private AnnotationTypeMemberDeclaration getAnnotationMember(Object element) {
		return (AnnotationTypeMemberDeclaration) element;
	}

}