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
package org.flowerplatform.codesync.code.java;

import org.eclipse.jdt.internal.core.CompilationUnit;
import org.flowerplatform.codesync.code.FileFullyQualifiedNameProvider;

/**
 * @author Mariana
 */
public class JavaFullyQualifiedNameProvider extends FileFullyQualifiedNameProvider {

	@Override
	public String getFullyQualifiedName(Object object) {
		String fqName = super.getFullyQualifiedName(object);
		if (fqName != null) {
			return fqName;
		}
		// TODO Mariana : add support for JE
		if (object instanceof CompilationUnit) {
			return String.copyValueOf((((CompilationUnit) object).getFileName()));
		}
		return null;
	}

}