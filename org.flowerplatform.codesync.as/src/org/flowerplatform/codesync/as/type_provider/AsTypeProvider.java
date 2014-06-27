package org.flowerplatform.codesync.as.type_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.FUNCTION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VARIABLE;

import org.apache.flex.compiler.definitions.IClassDefinition;
import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.definitions.IInterfaceDefinition;
import org.apache.flex.compiler.definitions.IVariableDefinition;
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
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		if (fileAccessController.isFile(object)) {
			if (fileAccessController.isDirectory(object)) {
				return CodeSyncCodeConstants.FOLDER;
			} else {
				return CodeSyncCodeConstants.FILE;
			}
		} else if (object instanceof IClassDefinition) {
			return CLASS;
		} else if (object instanceof IInterfaceDefinition) {
			return INTERFACE;
		} else if (object instanceof IFunctionDefinition) {
			return FUNCTION;
		} else if (object instanceof IVariableDefinition) {
			return VARIABLE;
		}
		return null;
	}

}
