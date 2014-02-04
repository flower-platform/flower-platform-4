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

import static org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationTypeMemberDeclarationFeatureProvider.ANNOTATION_MEMBER_DEFAULT_VALUE;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationTypeMemberDeclarationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaFeaturesConstants;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * Mapped by {@link AnnotationTypeMemberDeclaration}. Children are {@link Modifier}s.
 * 
 * @see JavaAnnotationTypeMemberDeclarationFeatureProvider
 * 
 * @author Mariana
 */
public class JavaAnnotationTypeMemberDeclarationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public static final String ANNOTATION_MEMBER = "javaAnnotationMember";
	
	@Override
	public Object getMatchKey(Object element) {
		return getAnnotationMember(element).getName().getIdentifier();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (NodeFeatureProvider.NAME.equals(feature)) {
			return getMatchKey(element);
		} else if (NodeFeatureProvider.TYPE.equals(feature)) {
			return ANNOTATION_MEMBER;
		} else if (ANNOTATION_MEMBER_DEFAULT_VALUE.equals(feature)) {
			return getStringFromExpression(getAnnotationMember(element).getDefault());
		} else if (JavaFeaturesConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			return getStringFromType(getAnnotationMember(element).getType());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (NodeFeatureProvider.NAME.equals(feature)) {
			AnnotationTypeMemberDeclaration member = getAnnotationMember(element);
			member.setName(member.getAST().newSimpleName((String) value));
		} else if (ANNOTATION_MEMBER_DEFAULT_VALUE.equals(feature)) {
			AnnotationTypeMemberDeclaration member = getAnnotationMember(element);
			member.setDefault(getExpressionFromString(member.getAST(), (String) value));
		} else if (JavaFeaturesConstants.TYPED_ELEMENT_TYPE.equals(feature)) {
			AnnotationTypeMemberDeclaration member = getAnnotationMember(element);
			member.setType(getTypeFromString(member.getAST(), (String) value));
		}
		super.setValueFeatureValue(element, feature, value);
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}
	
	private AnnotationTypeMemberDeclaration getAnnotationMember(Object element) {
		return (AnnotationTypeMemberDeclaration) element;
	}

}