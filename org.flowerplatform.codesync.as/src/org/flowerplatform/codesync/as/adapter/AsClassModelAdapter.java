package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_CLASS;
import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.parser.FunctionDefinitionNode;
import macromedia.asc.parser.LiteralStringNode;
import macromedia.asc.parser.MemberExpressionNode;
import macromedia.asc.parser.Node;
import macromedia.asc.parser.VariableDefinitionNode;
import macromedia.asc.util.Context;

import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.as.feature_provider.AsClassFeatureProvider;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link ClassDefinitionNode}. Children are {@link VariableDefinitionNode}s
 * and {@link FunctionDefinitionNode}s.
 * 
 * @see AsClassFeatureProvider
 * 
 * @author Mariana Gheorghe
 */
public class AsClassModelAdapter extends AsAbstractAstModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getClassDef(element).name.name;
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncAsConstants.STATEMENTS.equals(feature)) {
			return new FilteredIterable<Node, Node>(getClassDef(element).statements.items.iterator()) {

				@Override
				protected boolean isAccepted(Node node) {
					return node instanceof VariableDefinitionNode ||
							node instanceof FunctionDefinitionNode;
				}
			};
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getClassDef(element).name.name;
		} else if (SUPER_CLASS.equals(feature)) {
			Node baseClass = getClassDef(element).baseclass;
			if (baseClass == null) {
				return null;
			}
			if (baseClass instanceof MemberExpressionNode) {
				MemberExpressionNode member = (MemberExpressionNode) getClassDef(element).baseclass;
				return member.selector.getIdentifier().name;
			} else if (baseClass instanceof LiteralStringNode) {
				return ((LiteralStringNode) baseClass).value;
			}
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	protected ClassDefinitionNode getClassDef(Object element) {
		return (ClassDefinitionNode) element;
	}

	@Override
	protected Context getContext(Object element) {
		return getClassDef(element).cx;
	}
	
}
