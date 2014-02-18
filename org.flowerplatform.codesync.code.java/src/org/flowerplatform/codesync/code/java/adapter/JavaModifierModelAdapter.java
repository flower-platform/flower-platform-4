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

import org.eclipse.jdt.core.dom.Modifier;
import org.flowerplatform.codesync.code.java.feature_provider.JavaModifierFeatureProvider;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.core.NodePropertiesConstants;

/**
 * Mapped to {@link Modifier}.
 * 
 * @see JavaModifierFeatureProvider
 * 
 * @author Mariana
 */
public class JavaModifierModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public static final String MODIFIER = "javaModifier";
	
	@Override
	public Object getMatchKey(Object element) {
		return ((Modifier) element).getKeyword().toString();
	}

	@Override
	public String getLabel(Object modelElement) {
		return (String) getMatchKey(modelElement);
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (FeatureProvider.NAME.equals(feature)) {
			return getLabel(element);
		} else if (NodePropertiesConstants.TYPE.equals(feature)) {
			return MODIFIER;
		} else if (JavaModifierFeatureProvider.MODIFIER_TYPE.equals(feature)) {
			return getModifierType(element);
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (JavaModifierFeatureProvider.MODIFIER_TYPE.equals(feature)) {
			if (element instanceof Modifier) {
				Modifier modifier = (Modifier) element;
				int flag = (int) value;
				modifier.setKeyword(Modifier.ModifierKeyword.fromFlagValue(flag));
			}
			return;
		}
		super.setValueFeatureValue(element, feature, value);
	}

	private Integer getModifierType(Object element) {
		if (element instanceof Modifier) {
			return ((Modifier) element).getKeyword().toFlagValue();
		}
		return null;
	}
	
}