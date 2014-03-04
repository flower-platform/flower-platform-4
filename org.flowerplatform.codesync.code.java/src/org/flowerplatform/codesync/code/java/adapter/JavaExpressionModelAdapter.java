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

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Type;
import org.flowerplatform.codesync.CodeSyncPropertiesConstants;

/**
 * Mapped to {@link Expression} and {@link Type}.
 * 
 * @author Mariana Gheorghe
 */
public class JavaExpressionModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public static final String SUPER_INTERFACE = "superInterface";
	public static final String ENUM_CONSTANT_ARGUMENT = "javaEnumConstantArgument";
	
	private String type;
	
	public JavaExpressionModelAdapter(String type) {
		this.type = type;
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return element.toString();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CodeSyncPropertiesConstants.NAME.equals(feature)) {
			return element.toString();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}
	
}
