/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
import org.apache.flex.compiler.internal.definitions.SyntheticBindableGetterDefinition;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.type_provider.FileTypeProvider;

/**
 * @author Mariana Gheorghe
 */
public class AsTypeProvider extends FileTypeProvider {

	@Override
	public String getType(Object object, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (object instanceof IParameterDefinition) {
			return PARAMETER;
		} else if (object instanceof SyntheticBindableGetterDefinition) {
			// treat the getter as a variable; the setter is ignored
			return VARIABLE;
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
		return super.getType(object, codeSyncAlgorithm);
	}
}