package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;
import macromedia.asc.parser.VariableDefinitionNode;
import macromedia.asc.util.Context;

import org.flowerplatform.codesync.as.feature_provider.AsVariableFeatureProvider;
import org.flowerplatform.core.CoreConstants;

import flex2.compiler.as3.reflect.NodeMagic;

/**
 * Mapped to {@link VariableDefinitionNode}.
 * 
 * @see AsVariableFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsVariableModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return NodeMagic.getVariableName(getVariable(element));
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return NodeMagic.getVariableName(getVariable(element));
		} else if (TYPED_ELEMENT_TYPE.equals(feature)) {
			return NodeMagic.getVariableTypeName(getVariable(element));
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	protected VariableDefinitionNode getVariable(Object element) {
		return (VariableDefinitionNode) element;
	}

	@Override
	protected Context getContext(Object element) {
		return getVariable(element).cx;
	}

}
