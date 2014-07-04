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

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Type;

/**
 * Mapped to {@link Expression} and {@link Type}.
 * 
 * @author Mariana Gheorghe
 */
public class JavaExpressionModelAdapter extends JavaAbstractAstNodeModelAdapter {

	private String type;
	
	public JavaExpressionModelAdapter(String type) {
		this.type = type;
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return element.toString();
	}

	@Override
	public String toString() {
		return String.format("JavaExpressionModelAdapter [type = %s, orderIndex = %d]", type, getOrderIndex());
	}
	
}