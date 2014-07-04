/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.adapter.ModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants;
import org.flowerplatform.codesync.code.java.feature_provider.JavaTypeDeclarationFeatureProvider;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link AbstractTypeDeclaration}. Children are {@link BodyDeclaration}s.
 * 
 * @see JavaTypeDeclarationFeatureProvider
 * 
 * @author Mariana
 */
public class JavaTypeDeclarationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	/**
	 * Returns only types, fields and methods.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<?> getChildren(Object modelElement) {
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
		if (CodeSyncCodeJavaConstants.TYPE_MEMBERS.equals(feature)) {
			return getChildren(element);
		} else if (CodeSyncCodeJavaConstants.SUPER_INTERFACES.equals(feature)) {
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
		if (CodeSyncCodeJavaConstants.SUPER_CLASS.equals(feature)) {
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
		if (CoreConstants.NAME.equals(feature)) {
			AbstractTypeDeclaration type = getAbstractTypeDeclaration(element);
			String name = (String) value;
			type.setName(type.getAST().newSimpleName(name));
		} else if (CodeSyncCodeJavaConstants.SUPER_CLASS.equals(feature)) {
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

	@SuppressWarnings("unchecked")
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, ModelAdapterSet modelAdapterSet) {
		// declared as containment by JavaFeatureProvider 
		if (CodeSyncCodeJavaConstants.TYPE_MEMBERS.equals(feature)) {
			AbstractTypeDeclaration type = (AbstractTypeDeclaration) parent;
			AST ast = type.getAST();
			ASTNode child = createCorrespondingModelElement(ast, modelAdapterSet.getType(correspondingChild));
			type.bodyDeclarations().add(child);
			return child;
		}

		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, modelAdapterSet);
	}
	
	protected ASTNode createCorrespondingModelElement(AST ast, String type) {
		ASTNode child = null;
		if (CodeSyncCodeJavaConstants.CLASS.equals(type)) {
			child = ast.newTypeDeclaration();
		} else if (CodeSyncCodeJavaConstants.INTERFACE.equals(type)) {
			TypeDeclaration i = ast.newTypeDeclaration();
			i.setInterface(true);
			child = i;
		} else if (CodeSyncCodeJavaConstants.ENUM.equals(type)) {
			child = ast.newEnumDeclaration();
		} else if (CodeSyncCodeJavaConstants.ANNOTATION_TYPE.equals(type)) {
			child = ast.newAnnotationTypeDeclaration();
		}
		return child;
	}
	
	private AbstractTypeDeclaration getAbstractTypeDeclaration(Object element) {
		return (AbstractTypeDeclaration) element;
	}
	
}
