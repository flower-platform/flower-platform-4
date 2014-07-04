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
package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG_ATTRIBUTE_VALUE;

import org.apache.flex.compiler.definitions.metadata.IMetaTagAttribute;
import org.flowerplatform.codesync.code.adapter.AstModelElementAdapter;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link IMetaTagAttribute}.
 * 
 * @author Mariana Gheorghe
 */
public class AsMetaTagAttributeModelAdapter extends AstModelElementAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getMetaTagAttribute(element).getKey();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getMetaTagAttribute(element).getKey();
		} else if (META_TAG_ATTRIBUTE_VALUE.equals(feature)) {
			return getMetaTagAttribute(element).getValue();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	protected IMetaTagAttribute getMetaTagAttribute(Object element) {
		return (IMetaTagAttribute) element;
	}

	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// TODO Auto-generated method stub

	}

}