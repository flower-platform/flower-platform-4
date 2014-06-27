package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.DOCUMENTATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;

import java.util.Collections;
import java.util.List;

import org.apache.flex.compiler.tree.as.IASNode;
import org.flowerplatform.codesync.code.adapter.AstModelElementAdapter;

/**
 * Mapped to {@link IASNode}.
 * 
 * @author Mariana Gheorghe
 */
public abstract class AsAbstractAstModelAdapter extends AstModelElementAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (DOCUMENTATION.equals(feature)) {
//			if (element instanceof DefinitionNode) {
//				DefinitionNode def = (DefinitionNode) element;
//				StatementListNode metaData = def.metaData;
//				if (metaData == null) {
//					return null;
//				}
//				for (Node node : metaData.items) {
//					if (node instanceof DocCommentNode) {
//						DocCommentNode comment = (DocCommentNode) node;
//						MetaDataEvaluator eval = new MetaDataEvaluator();
//						comment.evaluate(getContext(element), eval);
//						if (comment.getMetadata() == null) {
//							return null;
//						}
//						return comment.getMetadata().id;
//					}
//				}
//				return null;
//			}
			return null;
		} 
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (MODIFIERS.equals(feature)) {
//			if (element instanceof DefinitionNode) {
//				AttributeListNode attrs = ((DefinitionNode) element).attrs;
//				if (attrs == null) {
//					return Collections.emptyList();
//				}
//				List<Node> modifiers = new ArrayList<Node>();
//				getIdentifiers(modifiers, attrs);
//				return modifiers;
//			}
			return Collections.emptyList();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}
	
//	protected void getIdentifiers(List<Node> identifiers, Node node) {
//		if (node instanceof AttributeListNode) {
//			for (Node attr : ((AttributeListNode) node).items) {
//				getIdentifiers(identifiers, attr);
//			}
//		} else if (node instanceof ListNode) {
//			for (Node attr : ((ListNode) node).items) {
//				getIdentifiers(identifiers, attr);
//			}
//		} else if (node instanceof MemberExpressionNode) {
//			getIdentifiers(identifiers, ((MemberExpressionNode) node).selector.getIdentifier());
//		} else if (node instanceof IdentifierNode) {
//			identifiers.add(node);
//		} else if (node instanceof LiteralStringNode) {
//			identifiers.add(node);
//		}
//	}
	
	@Override
	public boolean hasChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// TODO Auto-generated method stub

	}

}
