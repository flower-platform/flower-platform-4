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

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.flowerplatform.codesync.code.adapter.AstModelElementAdapter;

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

	/**
	 * Must handle all the containment features provided by <code>common</code>.
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
//		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
//			return getChildren(element);
//		}
//		
//		// handle modifiers here to avoid using the same code in multiple adapters
//		if (AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers().equals(feature)) {
//			if (element instanceof BodyDeclaration) {
//				return ((BodyDeclaration) element).modifiers();
//			}
//			if (element instanceof SingleVariableDeclaration) {
//				return ((SingleVariableDeclaration) element).modifiers();
//			}
//			return Collections.emptyList();
//		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Must handle all the features provided by <code>common</code>, except for containment features.
	 */
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
//		if (AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation().equals(feature)) {
//			return getJavaDoc(element);
//		}
		return null;
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
//		if (AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation().equals(feature)) {
//			setJavaDoc(element, value);
//		}
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
//		if (AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers().equals(feature)) {
//			if (!(element instanceof BodyDeclaration || element instanceof SingleVariableDeclaration)) {
//				return null;
//			} else {
//				IExtendedModifier extendedModifier = null;
//				
//				if (correspondingChild instanceof com.crispico.flower.mp.model.astcache.code.Modifier) {
//					ASTNode parent = (ASTNode) element;
//					AST ast = parent.getAST();
//					com.crispico.flower.mp.model.astcache.code.Modifier modifier = 
//							(com.crispico.flower.mp.model.astcache.code.Modifier) correspondingChild;
//					
//					extendedModifier = ast.newModifier(Modifier.ModifierKeyword.fromFlagValue(modifier.getType()));
//					if (parent instanceof BodyDeclaration) {
//						((BodyDeclaration) parent).modifiers().add(extendedModifier);
//					} else {
//						((SingleVariableDeclaration) parent).modifiers().add(extendedModifier);
//					}
//				}
//				
//				if (correspondingChild instanceof com.crispico.flower.mp.model.astcache.code.Annotation) {
//					ASTNode parent = (ASTNode) element;
//					AST ast = parent.getAST();
//					com.crispico.flower.mp.model.astcache.code.Annotation annotation = 
//							(com.crispico.flower.mp.model.astcache.code.Annotation) correspondingChild;
//					if (annotation.getValues().size() == 0) {
//						MarkerAnnotation markerAnnotation = ast.newMarkerAnnotation();
//						extendedModifier = markerAnnotation;
//					}
//					if (annotation.getValues().size() == 1) {
//						SingleMemberAnnotation singleMemberAnnotation = ast.newSingleMemberAnnotation();
//						extendedModifier = singleMemberAnnotation;
//					} else {
//						NormalAnnotation normalAnnotation = ast.newNormalAnnotation();
//						extendedModifier = normalAnnotation;
//					}
//					if (parent instanceof BodyDeclaration) {
//						((BodyDeclaration) parent).modifiers().add(extendedModifier);
//					} else {
//						((SingleVariableDeclaration) parent).modifiers().add(extendedModifier);
//					}
//				}
//				return extendedModifier;
//			}
//		}
		
		return null;
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
//		if (element instanceof BodyDeclaration) {
//			BodyDeclaration node = (BodyDeclaration) element;
//			Javadoc javadoc = node.getJavadoc();
//			// if it doesn't have any doc, create it
//			if (javadoc == null) {
//				javadoc = node.getAST().newJavadoc();
//				node.setJavadoc(javadoc);
//			}
//			// first remove the existing flower tag, this way we also make sure that it's the last tag
//			// note: if we only change the id, the rewriter won't format it correctly
//			for (Object obj : javadoc.tags()) {
//				if (FLOWER_UID.equals(((TagElement) obj).getTagName())) {
//					javadoc.tags().remove(obj);
//					break;
//				}
//			}
//			// create new tag element for UID
//			TagElement tag = javadoc.getAST().newTagElement();
//			tag.setTagName(FLOWER_UID);
//			javadoc.tags().add(tag);
//			TextElement text = javadoc.getAST().newTextElement();
//			tag.fragments().add(text);
//			EObject eObject = (EObject) correspondingElement;
//			text.setText(eObject.eResource().getURIFragment(eObject));
//			System.out.println(javadoc);
//		}
	}

	protected Object getJavaDoc(Object element) {
		if (element instanceof BodyDeclaration) {
			BodyDeclaration node = (BodyDeclaration) element;
			if (node.getJavadoc() != null) {
				String docComment = null;
				for (Object o : node.getJavadoc().tags()) {
					TagElement tag = (TagElement) o;
					String tagName = tag.getTagName();
					if (getModelAdapterFactorySet().useUIDs() && FLOWER_UID.equals(tagName)) {
						continue;
					}
					if (docComment == null) {
						docComment = new String();
					}
					if (tagName != null) {
						docComment += tag.getTagName() + " ";
					}
					for (Object o2 : tag.fragments()) {
						docComment += getTextFromDocElement(o2);
					}
					docComment += "\n";
				}
				return docComment;
			}
		}
		return null;
	}
	
	/**
	 * @param node an IDocElement
	 */
	private String getTextFromDocElement(Object node) {
		if (node instanceof MemberRef) {
			return ((MemberRef) node).getName().getIdentifier();
		}
		if (node instanceof MethodRef) {
			return ((MethodRef) node).getName().getIdentifier();
		}
		if (node instanceof SimpleName) {
			return ((SimpleName) node).getIdentifier();
		}
		if (node instanceof QualifiedName) {
			return ((QualifiedName) node).getFullyQualifiedName();
		}
		if (node instanceof TagElement) {
			return ((TagElement) node).getTagName();
		}
		if (node instanceof TextElement) {
			return ((TextElement) node).getText();
		}
		return "";
	}
	
	protected void setJavaDoc(Object element, Object docComment) {
		if (element instanceof BodyDeclaration) {
			BodyDeclaration node = (BodyDeclaration) element;
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
			parser.setSource(("/** " + docComment + "*/ int x;").toCharArray());
			TypeDeclaration type = (TypeDeclaration) parser.createAST(null);
			BodyDeclaration x = (BodyDeclaration) type.bodyDeclarations().get(0);
			Javadoc javadoc = x.getJavadoc();
			node.setJavadoc((Javadoc) ASTNode.copySubtree(node.getAST(), javadoc));
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