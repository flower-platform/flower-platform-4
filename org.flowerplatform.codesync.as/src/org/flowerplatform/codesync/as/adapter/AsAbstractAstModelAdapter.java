package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.DOCUMENTATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import macromedia.asc.parser.AttributeListNode;
import macromedia.asc.parser.DefinitionNode;
import macromedia.asc.parser.DocCommentNode;
import macromedia.asc.parser.ListNode;
import macromedia.asc.parser.MetaDataEvaluator;
import macromedia.asc.parser.Node;
import macromedia.asc.parser.StatementListNode;
import macromedia.asc.util.Context;

import org.flowerplatform.codesync.code.adapter.AstModelElementAdapter;

/**
 * Mapped to {@link Node}.
 * 
 * @author Mariana Gheorghe
 */
public abstract class AsAbstractAstModelAdapter extends AstModelElementAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (DOCUMENTATION.equals(feature)) {
			if (element instanceof DefinitionNode) {
				DefinitionNode def = (DefinitionNode) element;
				StatementListNode metaData = def.metaData;
				if (metaData == null) {
					return null;
				}
				for (Node node : metaData.items) {
					if (node instanceof DocCommentNode) {
						DocCommentNode comment = (DocCommentNode) node;
						MetaDataEvaluator eval = new MetaDataEvaluator();
						comment.evaluate(getContext(element), eval);
						if (comment.getMetadata() == null) {
							return null;
						}
						return comment.getMetadata().id;
					}
				}
			}
		} 
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	protected abstract Context getContext(Object element);
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (MODIFIERS.equals(feature)) {
			if (element instanceof DefinitionNode) {
				AttributeListNode attrs = ((DefinitionNode) element).attrs;
				if (attrs == null) {
					return Collections.emptyList();
				}
				List<Node> modifiers = new ArrayList<Node>();
				for (Node node : attrs.items) {
					if (node instanceof ListNode) {
						for (Node modifier : ((ListNode) node).items) {
							modifiers.add(modifier);
						}
					}
				}
				return modifiers;
			}
			return Collections.emptyList();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}
	
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

	protected Node getNode(Object element) {
		return (Node) element;
	}
	
}
