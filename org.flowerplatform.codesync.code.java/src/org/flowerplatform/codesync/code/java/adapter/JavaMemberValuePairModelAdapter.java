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

import javax.lang.model.element.AnnotationValue;

import org.eclipse.jdt.core.dom.MemberValuePair;
import org.flowerplatform.codesync.code.java.feature_provider.JavaMemberValuePairFeatureProvider;

/**
 * Mapped to {@link MemberValuePair}.
 * 
 * @see JavaMemberValuePairFeatureProvider
 * 
 * @author Mariana
 */
public class JavaMemberValuePairModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public static final String MEMBER_VALUE_PAIR = "javaMemberValuePair";
	
	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}

	@Override
	public Object getMatchKey(Object modelElement) {
		return ((MemberValuePair) modelElement).getName().getIdentifier();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
//		if (AstCacheCodePackage.eINSTANCE.getAnnotationValue_Name().equals(feature)) {
//			return ((MemberValuePair) element).getName().getIdentifier();
//		}
//		if (AstCacheCodePackage.eINSTANCE.getAnnotationValue_Value().equals(feature)) {
//			return getStringFromExpression(((MemberValuePair) element).getValue());
//		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
//		if (AstCacheCodePackage.eINSTANCE.getAnnotationValue_Name().equals(feature)) {
//			MemberValuePair pair = (MemberValuePair) element;
//			String name = (String) value;
//			pair.setName(pair.getAST().newSimpleName(name));
//		}
//		if (AstCacheCodePackage.eINSTANCE.getAnnotationValue_Value().equals(feature)) {
//			MemberValuePair pair = (MemberValuePair) element;
//			String expression = (String) value;
//			pair.setValue(getExpressionFromString(pair.getAST(), expression));
//		}
		super.setValueFeatureValue(element, feature, value);
	}

	/**
	 * Creates a {@link AnnotationValue} instance. Also set the name, in case the AST cache was deleted.
	 */
	@Override
	public Object createCorrespondingModelElement(Object element) {
//		AnnotationValue value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName(((MemberValuePair) element).getName().getIdentifier());
//		return value;
		return null;
	}

}