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
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.core.mindmap.remote.Node;

/**
 * Mapped to {@link AbstractTypeDeclaration}. Children are {@link BodyDeclaration}s.
 * 
 * @author Mariana
 */
public class JavaTypeModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public static final String CLASS = "javaClass";
	public static final String INTERFACE = "javaInterface";
	public static final String ENUM = "javaEnum";
	public static final String ANNOTATION = "javaAnnotation";
	
	/**
	 * Returns only types, fields and methods.
	 */
	@Override
	public List<?> getChildren(Object modelElement) {
		List children = new ArrayList<>();
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
		List rslt = new ArrayList();
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
		// declared as containment by JavaFeatureProvider
//		if (AstCacheCodePackage.eINSTANCE.getClass_SuperInterfaces().equals(feature)) {
//			if (element instanceof TypeDeclaration) {
//				return getTypeNames(((TypeDeclaration) element).superInterfaceTypes());
//			}
//			if (element instanceof EnumDeclaration) {
//				return getTypeNames(((EnumDeclaration) element).superInterfaceTypes());
//			}
//			return Collections.emptyList();
//		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
//		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
//			return getLabel(element);
//		}
//		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type().equals(feature)) {
//			if (element instanceof TypeDeclaration) {
//				if (((TypeDeclaration) element).isInterface()) {
//					return INTERFACE;
//				}
//				return CLASS;
//			}
//			if (element instanceof EnumDeclaration) {
//				return ENUM;
//			}
//			return ANNOTATION;
//		}
//		if (AstCacheCodePackage.eINSTANCE.getClass_SuperClasses().equals(feature)) {
//			if (element instanceof TypeDeclaration) {
//				TypeDeclaration type = (TypeDeclaration) element;
//				if (type.getSuperclassType() != null) {
//					return Collections.singletonList(getStringFromType(type.getSuperclassType()));
//				} else {
//					return Collections.emptyList();
//				}
//			}
//			return Collections.emptyList();
//		}
//		if (AstCacheCodePackage.eINSTANCE.getClass_SuperInterfaces().equals(feature)) { 
//			return Collections.emptyList();
//		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, final Object value) {
//		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
//			AbstractTypeDeclaration type = getAbstractTypeDeclaration(element);
//			String name = (String) value;
//			type.setName(type.getAST().newSimpleName(name));
//		}
//		if (AstCacheCodePackage.eINSTANCE.getClass_SuperClasses().equals(feature)) {
//			if (element instanceof TypeDeclaration) {
//				List<String> superClasses = (List<String>) value;
//				TypeDeclaration cls = (TypeDeclaration) element;
//				AST ast = cls.getAST();
//				Type type = null;
//				if (superClasses != null && superClasses.size() > 0) {
//					type = getTypeFromString(ast, superClasses.get(0));
//				}
//				cls.setSuperclassType(type);
//			}
//		}
		super.setValueFeatureValue(element, feature, value);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		// declared as containment by JavaFeatureProvider 
//		if (AstCacheCodePackage.eINSTANCE.getClass_SuperInterfaces().equals(feature)) {
//			if (element instanceof TypeDeclaration || element instanceof EnumDeclaration) {
//				String superInterface = (String) correspondingChild;
//				AbstractTypeDeclaration cls = (AbstractTypeDeclaration) element;
//				AST ast = cls.getAST();
//				Type type = getTypeFromString(ast, superInterface);
//				if (cls instanceof TypeDeclaration) {
//					((TypeDeclaration) cls).superInterfaceTypes().add(type);
//				}
//				if (cls instanceof EnumDeclaration) {
//					((EnumDeclaration) cls).superInterfaceTypes().add(type);
//				}
//				return getStringFromType(type);
//			}
//			return null;
//		}
//		
//		if (AstCacheCodePackage.eINSTANCE.getClass_SuperClasses().equals(feature)) {
//			if (element instanceof TypeDeclaration) {
//				String superClass = (String) correspondingChild;
//				TypeDeclaration cls = (TypeDeclaration) element;
//				AST ast = cls.getAST();
//				Type type = getTypeFromString(ast, superClass);
//				cls.setSuperclassType(type);
//				return getStringFromType(type);
//			}
//			return null;
//		}
//		
//		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
//			CodeSyncElement cse = (CodeSyncElement) correspondingChild;
//			AstCacheElement ace = cse.getAstCacheElement();
//			AbstractTypeDeclaration parent = (AbstractTypeDeclaration) element;
//			AST ast = parent.getAST();
//			ASTNode child = (ASTNode) createCorrespondingModelElement(ast, cse);
//			parent.bodyDeclarations().add(child);
//			return child;
//		}

		return super.createChildOnContainmentFeature(element, feature, correspondingChild);
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
//		return AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createClass();
		return null;
	}
	
	public static Object createCorrespondingModelElement(AST ast, Node cse) {
		ASTNode child = null;
		if (JavaAttributeModelAdapter.ATTRIBUTE.equals(cse.getType())) {
			VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
			FieldDeclaration field = ast.newFieldDeclaration(fragment);
			child = field;
		}
		if (JavaOperationModelAdapter.OPERATION.equals(cse.getType())) {
			child = ast.newMethodDeclaration();
		}
		if (CLASS.equals(cse.getType())) {
			child = ast.newTypeDeclaration();
		}
		if (INTERFACE.equals(cse.getType())) {
			TypeDeclaration type = ast.newTypeDeclaration();
			type.setInterface(true);
			child = type;
		}
		if (ENUM.equals(cse.getType())) {
			child = ast.newEnumDeclaration();
		}
		if (ANNOTATION.equals(cse.getType())) {
			child = ast.newAnnotationTypeDeclaration();
		}
		return child;
	}
	
	private AbstractTypeDeclaration getAbstractTypeDeclaration(Object element) {
		return (AbstractTypeDeclaration) element;
	}
	
	private List<String> getTypeNames(List types) {
		List<String> rslt = new ArrayList<String>();
		for (Object type : types) {
			rslt.add(getStringFromType((Type) type));
		}
		return rslt;
	}

}