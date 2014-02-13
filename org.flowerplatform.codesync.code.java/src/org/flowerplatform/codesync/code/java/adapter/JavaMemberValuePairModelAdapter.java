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

import org.eclipse.jdt.core.dom.MemberValuePair;
import org.flowerplatform.codesync.code.java.feature_provider.JavaMemberValuePairFeatureProvider;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;

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
		if (FeatureProvider.NAME.equals(feature)) {
			return ((MemberValuePair) element).getName().getIdentifier();
		} else if (FeatureProvider.TYPE.equals(feature)) {
			return MEMBER_VALUE_PAIR;
		} else if (JavaMemberValuePairFeatureProvider.ANNOTATION_VALUE_VALUE.equals(feature)) {
			return getStringFromExpression(((MemberValuePair) element).getValue());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (FeatureProvider.NAME.equals(feature)) {
			MemberValuePair pair = (MemberValuePair) element;
			String name = (String) value;
			pair.setName(pair.getAST().newSimpleName(name));
		} else if (JavaMemberValuePairFeatureProvider.ANNOTATION_VALUE_VALUE.equals(feature)) {
			MemberValuePair pair = (MemberValuePair) element;
			String expression = (String) value;
			pair.setValue(getExpressionFromString(pair.getAST(), expression));
		}
		super.setValueFeatureValue(element, feature, value);
	}

}