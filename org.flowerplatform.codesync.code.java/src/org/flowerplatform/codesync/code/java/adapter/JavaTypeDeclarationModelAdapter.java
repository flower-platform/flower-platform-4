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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.flowerplatform.codesync.CodeSyncPropertiesConstants;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.code.java.JavaPropertiesConstants;
import org.flowerplatform.codesync.code.java.feature_provider.JavaTypeDeclarationFeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.NodePropertiesConstants;
import org.flowerplatform.core.node.remote.Node;

/**
 * Mapped to {@link AbstractTypeDeclaration}. Children are {@link BodyDeclaration}s.
 * 
 * @see JavaTypeDeclarationFeatureProvider
 * 
 * @author Mariana
 */
public class JavaTypeDeclarationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public static final String CLASS = "javaClass";
	public static final String INTERFACE = "javaInterface";
	public static final String ENUM = "javaEnum";
	public static final String ANNOTATION_TYPE = "javaAnnotationType";
	
	/**
	 * Returns only types, fields and methods.
	 */
	@Override
	public List<?> getChildren(Object modelElement) {
		List<ASTNode> children = new ArrayList<ASTNode>();
		AbstractTypeDeclaration type = getAbstractTypeDeclaration(modelElement);
		children.addAll(type.bodyDeclarations());
		if (type instanceof EnumDeclaration) {
			children.addAll(((EnumDeclaration) type).enumConstants());
		}
		
		Iterator it = new FilteredIterable(children.iterator()) {

			@Override
			protected boolean isAccepted(Object candidate) {
				if (candidate instanceof AbstractTypeDeclaration) {
					return true;
				}
				if (candidate instanceof FieldDeclaration) {
					return true;
				}
				if (candidate instanceof MethodDeclaration) {
					return true;
				}
				
				// children of EnumDeclaration
				if (candidate instanceof EnumConstantDeclaration) {
					return true;
				}
				
				// children of AnnotationTypeDeclaration
				if (candidate instanceof AnnotationTypeMemberDeclaration) {
					return true;
				}
				
				return false;
			}
		};
		List<ASTNode> rslt = new ArrayList<ASTNode>();
		while (it.hasNext()) {
			ASTNode node = (ASTNode) it.next();
			// TODO for field declarations, add the list of fragments
//			if (node instanceof FieldDeclaration) {
//				rslt.addAll(((FieldDeclaration) node).fragments());
//			} else {
				rslt.add(node); 
//			}
		}
		return rslt;
	}

	@Override
	public Object getMatchKey(Object modelElement) {
		return getAbstractTypeDeclaration(modelElement).getName().getIdentifier();
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (JavaPropertiesConstants.TYPE_MEMBERS.equals(feature)) {
			return getChildren(element);
		} else if (JavaPropertiesConstants.SUPER_INTERFACES.equals(feature)) {
			if (element instanceof TypeDeclaration) {
				return ((TypeDeclaration) element).superInterfaceTypes();
			}
			if (element instanceof EnumDeclaration) {
				return ((EnumDeclaration) element).superInterfaceTypes();
			}
			return Collections.emptyList();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CodeSyncPropertiesConstants.NAME.equals(feature)) {
			return getLabel(element);
		} else if (NodePropertiesConstants.TYPE.equals(feature)) {
			if (element instanceof TypeDeclaration) {
				if (((TypeDeclaration) element).isInterface()) {
					return INTERFACE;
				}
				return CLASS;
			}
			if (element instanceof EnumDeclaration) {
				return ENUM;
			}
			return ANNOTATION_TYPE;
		} else if (JavaPropertiesConstants.SUPER_CLASS.equals(feature)) {
			if (element instanceof TypeDeclaration) {
				TypeDeclaration type = (TypeDeclaration) element;
				if (type.getSuperclassType() != null) {
					return getStringFromType(type.getSuperclassType());
				}
			}
			return null;
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, final Object value) {
		if (CodeSyncPropertiesConstants.NAME.equals(feature)) {
			AbstractTypeDeclaration type = getAbstractTypeDeclaration(element);
			String name = (String) value;
			type.setName(type.getAST().newSimpleName(name));
		} else if (JavaPropertiesConstants.SUPER_CLASS.equals(feature)) {
			if (element instanceof TypeDeclaration) {
				String superClass = value.toString();
				TypeDeclaration cls = (TypeDeclaration) element;
				AST ast = cls.getAST();
				Type type = null;
				if (superClass != null) {
					type = getTypeFromString(ast, superClass);
				}
				cls.setSuperclassType(type);
			}
		}
		super.setValueFeatureValue(element, feature, value);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		// declared as containment by JavaFeatureProvider 
		if (JavaPropertiesConstants.SUPER_INTERFACES.equals(feature)) {
			if (element instanceof TypeDeclaration || element instanceof EnumDeclaration) {
				Node superInterface = (Node) correspondingChild;
				AbstractTypeDeclaration cls = (AbstractTypeDeclaration) element;
				AST ast = cls.getAST();
				Type type = getTypeFromString(ast, (String) superInterface.getOrPopulateProperties().get(CodeSyncPropertiesConstants.NAME));
				if (cls instanceof TypeDeclaration) {
					((TypeDeclaration) cls).superInterfaceTypes().add(type);
				} else if (cls instanceof EnumDeclaration) {
					((EnumDeclaration) cls).superInterfaceTypes().add(type);
				}
				return type;
			}
			return null;
		} else if (JavaPropertiesConstants.TYPE_MEMBERS.equals(feature)) {
			Node node = (Node) correspondingChild;
			AbstractTypeDeclaration parent = (AbstractTypeDeclaration) element;
			AST ast = parent.getAST();
			ASTNode child = (ASTNode) createCorrespondingModelElement(ast, node);
			
			if (parent instanceof EnumDeclaration) {
				((EnumDeclaration) parent).enumConstants().add(child);
			} else {
				parent.bodyDeclarations().add(child);
			}
			return child;
		}

		return super.createChildOnContainmentFeature(element, feature, correspondingChild, typeProvider);
	}
	
	public static Object createCorrespondingModelElement(AST ast, Node node) {
		ASTNode child = null;
		if (JavaAttributeModelAdapter.ATTRIBUTE.equals(node.getType())) {
			VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
			FieldDeclaration field = ast.newFieldDeclaration(fragment);
			child = field;
		} else if (JavaOperationModelAdapter.OPERATION.equals(node.getType())) {
			child = ast.newMethodDeclaration();
		} else if (JavaEnumConstantDeclarationModelAdapter.ENUM_CONSTANT.equals(node.getType())) {
			child = ast.newEnumConstantDeclaration();
		} else if (CLASS.equals(node.getType())) {
			child = ast.newTypeDeclaration();
		} else if (INTERFACE.equals(node.getType())) {
			TypeDeclaration type = ast.newTypeDeclaration();
			type.setInterface(true);
			child = type;
		} else if (ENUM.equals(node.getType())) {
			child = ast.newEnumDeclaration();
		} else if (ANNOTATION_TYPE.equals(node.getType())) {
			child = ast.newAnnotationTypeDeclaration();
		}
		return child;
	}
	
	private AbstractTypeDeclaration getAbstractTypeDeclaration(Object element) {
		return (AbstractTypeDeclaration) element;
	}
	
}