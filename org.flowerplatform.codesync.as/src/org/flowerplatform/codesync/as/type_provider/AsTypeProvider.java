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
package org.flowerplatform.codesync.as.type_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CONST;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.FUNCTION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.GETTER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG_ATTRIBUTE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SETTER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VARIABLE;

import org.apache.flex.compiler.common.ASModifier;
import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IConstantDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IGetterDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.IParameterDefinition;
import org.apache.flex.compiler.definitions.ISetterDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
import org.apache.flex.compiler.definitions.metadata.IMetaTag;
import org.apache.flex.compiler.definitions.metadata.IMetaTagAttribute;
import org.apache.flex.compiler.definitions.references.IReference;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.util.file.StringHolder;

/**
 * @author Mariana Gheorghe
 */
public class AsTypeProvider implements ITypeProvider {

	@Override
	public String getType(Object object) {
		if (object instanceof IParameterDefinition) {
			return PARAMETER;
		} else if (object instanceof IGetterDefinition) {
			return GETTER;
		} else if (object instanceof ISetterDefinition) {
			return SETTER;
		} else if (object instanceof IFunctionDefinition) {
			return FUNCTION;
		} else if (object instanceof IConstantDefinition) {
			return CONST;
		} else if (object instanceof IVariableDefinition) {
			return VARIABLE;
		} else if (object instanceof IClassDefinition) {
			return CLASS;
		} else if (object instanceof ASModifier) {
			return MODIFIER;
		} else if (object instanceof IInterfaceDefinition) {
			return INTERFACE;
		} else if (object instanceof IReference) {
			return SUPER_INTERFACE;
		} else if (object instanceof IMetaTag) {
			return META_TAG;
		} else if (object instanceof IMetaTagAttribute) {
			return META_TAG_ATTRIBUTE;
		}
		IFileAccessController fileAccessController = CodeSyncAlgorithm.fileAccessController;
		if (fileAccessController.isFile(object)) {
			if (fileAccessController.isDirectory(object)) {
				return CodeSyncConstants.FOLDER;
			} else {
				return CodeSyncConstants.FILE;
			}
		}
		return null;
	}
}