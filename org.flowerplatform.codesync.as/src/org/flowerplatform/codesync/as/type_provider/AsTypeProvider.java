package org.flowerplatform.codesync.as.type_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.FUNCTION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VARIABLE;

import java.util.HashMap;
import java.util.Map;

import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.parser.FunctionDefinitionNode;
import macromedia.asc.parser.MemberExpressionNode;
import macromedia.asc.parser.ParameterNode;
import macromedia.asc.parser.VariableDefinitionNode;

import org.flowerplatform.codesync.code.CodeSyncCodeConstants;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * @author Mariana Gheorghe
 */
public class AsTypeProvider implements ITypeProvider {

	private Map<Class<?>, String> directMap = new HashMap<Class<?>, String>();

	public AsTypeProvider() {
		directMap.put(ClassDefinitionNode.class, CLASS);
		directMap.put(VariableDefinitionNode.class, VARIABLE);
		directMap.put(FunctionDefinitionNode.class, FUNCTION);
		directMap.put(MemberExpressionNode.class, MODIFIER);
		directMap.put(ParameterNode.class, PARAMETER);
	}
	
	@Override
	public String getType(Object object) {
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		if (fileAccessController.isFile(object)) {
			if (fileAccessController.isDirectory(object)) {
				return CodeSyncCodeConstants.FOLDER;
			} else {
				return CodeSyncCodeConstants.FILE;
			}
		}
		return directMap.get(object.getClass());
	}

}
