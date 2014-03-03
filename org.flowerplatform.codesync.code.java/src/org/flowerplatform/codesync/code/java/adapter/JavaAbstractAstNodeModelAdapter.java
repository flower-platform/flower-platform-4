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
package org.flowerplatform.codesync.code.java.adapter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.internal.core.dom.rewrite.NodeInfoStore;
import org.flowerplatform.codesync.CodeSyncPropertiesConstants;
import org.flowerplatform.codesync.code.adapter.AstModelElementAdapter;
import org.flowerplatform.codesync.code.java.JavaPropertiesConstants;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * Mapped to {@link ASTNode}.
 * 
 * @author Mariana
 */
public abstract class JavaAbstractAstNodeModelAdapter extends AstModelElementAdapter {

	@Override
	public boolean hasChildren(Object modelElement) {
		return getChildren(modelElement).size() > 0;
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		// handle modifiers here to avoid using the same code in multiple adapters
		if (JavaPropertiesConstants.MODIFIERS.equals(feature)) {
			if (element instanceof BodyDeclaration) {
				return ((BodyDeclaration) element).modifiers();
			}
			if (element instanceof SingleVariableDeclaration) {
				return ((SingleVariableDeclaration) element).modifiers();
			}
			return Collections.emptyList();
		}
		
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (JavaPropertiesConstants.DOCUMENTATION.equals(feature)) {
			return getJavaDoc(element);
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (JavaPropertiesConstants.DOCUMENTATION.equals(feature)) {
			setJavaDoc(element, (String) value);
		}
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		// handle modifiers here to avoid using the same code in multiple adapters
		if (JavaPropertiesConstants.MODIFIERS.equals(feature)) {
			if (!(element instanceof BodyDeclaration || element instanceof SingleVariableDeclaration)) {
				return null;
			} else {
				IExtendedModifier extendedModifier = null;
				
				Node node = (Node) correspondingChild;
				
				ASTNode parent = (ASTNode) element;
				AST ast = parent.getAST();
				
				if (JavaModifierModelAdapter.MODIFIER.equals(node.getType())) {
					String keyword = (String) node.getOrPopulateProperties().get(CodeSyncPropertiesConstants.NAME);
					extendedModifier = ast.newModifier(ModifierKeyword.toKeyword(keyword));
				} else if (JavaAnnotationModelAdapter.ANNOTATION.equals(node.getType())) {
					int valuesCount = CorePlugin.getInstance().getNodeService().getChildren(node, false).size();
					if (valuesCount == 0) {
						MarkerAnnotation markerAnnotation = ast.newMarkerAnnotation();
						extendedModifier = markerAnnotation;
					} else if (valuesCount == 1) {
						SingleMemberAnnotation singleMemberAnnotation = ast.newSingleMemberAnnotation();
						extendedModifier = singleMemberAnnotation;
					} else {
						NormalAnnotation normalAnnotation = ast.newNormalAnnotation();
						extendedModifier = normalAnnotation;
					}
				}
				
				addModifier(parent, extendedModifier);
				return extendedModifier;
			}
		}
		
		return super.createChildOnContainmentFeature(element, feature, correspondingChild, typeProvider);
	}
	
	protected void addModifier(ASTNode parent, IExtendedModifier modifier) {
		if (parent instanceof BodyDeclaration) {
			((BodyDeclaration) parent).modifiers().add(modifier);
		} else {
			((SingleVariableDeclaration) parent).modifiers().add(modifier);
		}
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		((ASTNode) child).delete();
	}

	@Override
	abstract public List<?> getChildren(Object modelElement);

	@Override
	public String getLabel(Object modelElement) {
		return (String) getMatchKey(modelElement);
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return null;
	}
	
	@Override
	public boolean save(Object element) {
		// nothing to do, the changes to the AST will be saved when the file is saved
		return false;
	}
	
	@Override
	public boolean discard(Object element) {
		// nothing to do, the changes to the AST will be discarded when the file is discarded
		return false;
	}

	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		if (element instanceof BodyDeclaration) {
			BodyDeclaration node = (BodyDeclaration) element;
			Javadoc javadoc = node.getJavadoc();
			// if it doesn't have any doc, create it
			if (javadoc == null) {
				javadoc = node.getAST().newJavadoc();
				node.setJavadoc(javadoc);
			}
			// first remove the existing flower tag, this way we also make sure that it's the last tag
			// note: if we only change the id, the rewriter won't format it correctly
			for (Object obj : javadoc.tags()) {
				if (FLOWER_UID.equals(((TagElement) obj).getTagName())) {
					javadoc.tags().remove(obj);
					break;
				}
			}
			// create new tag element for UID
			TagElement tag = javadoc.getAST().newTagElement();
			tag.setTagName(FLOWER_UID);
			javadoc.tags().add(tag);
			TextElement text = javadoc.getAST().newTextElement();
			tag.fragments().add(text);
			text.setText(((Node) correspondingElement).getIdWithinResource());
			System.out.println(javadoc);
		}
	}

	protected String getJavaDoc(Object element) {
		if (element instanceof BodyDeclaration) {
			BodyDeclaration node = (BodyDeclaration) element;
			if (node.getJavadoc() != null) {
				return node.getJavadoc().toString();
			}
		}
		return null;
	}
	
	protected void setJavaDoc(Object element, String docComment) {
		if (element instanceof BodyDeclaration) {
			BodyDeclaration node = (BodyDeclaration) element;
			try {
				Class ast = node.getAST().getClass();
				Field rewriterField = ast.getDeclaredField("rewriter");
				rewriterField.setAccessible(true);
				Object rewriter = rewriterField.get(node.getAST());
				Field storeField = rewriter.getClass().getDeclaredField("nodeStore");
				storeField.setAccessible(true);
				NodeInfoStore store = (NodeInfoStore) storeField.get(rewriter);
				ASTNode javadoc = store.newPlaceholderNode(ASTNode.JAVADOC);
				store.markAsStringPlaceholder(javadoc, docComment);
				node.setJavadoc((Javadoc) javadoc);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Creates an {@link Expression} from the given string, owned by the given AST. 
	 */
	protected Expression getExpressionFromString(AST ast, String expression) {
		if (expression == null) {
			return null;
		}
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_EXPRESSION);
		parser.setSource(expression.toCharArray());
		ASTNode node = parser.createAST(null);
		return (Expression) ASTNode.copySubtree(ast, node);
	}
	
	protected String getStringFromExpression(Expression expression) {
		if (expression == null) {
			return null;
		}
		return expression.toString();
	}
	
	/**
	 * Creates a {@link Type} from the given name, owned by the given AST.
	 */
	protected Type getTypeFromString(AST ast, String name) {
		if (name == null) {
			return ast.newPrimitiveType(PrimitiveType.VOID);
		}
		PrimitiveType.Code primitiveTypeCode = PrimitiveType.toCode(name);
		if (primitiveTypeCode != null) {
			return ast.newPrimitiveType(primitiveTypeCode);
		}
		
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource((name + " a;").toCharArray());
		
		Block block = (Block) parser.createAST(null);
		VariableDeclarationStatement declaration = (VariableDeclarationStatement) block.statements().get(0);
		return (Type) ASTNode.copySubtree(ast, declaration.getType());
	}
	
	protected String getStringFromType(Type type) {
		if (type == null) {
			return null;
		}
		return type.toString();
	}
	
}
