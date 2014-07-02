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
import org.flowerplatform.codesync.code.CodeSyncCodeConstants;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;

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
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		if (fileAccessController.isFile(object)) {
			if (fileAccessController.isDirectory(object)) {
				return CodeSyncCodeConstants.FOLDER;
			} else {
				return CodeSyncCodeConstants.FILE;
			}
		}
		return null;
	}
}
