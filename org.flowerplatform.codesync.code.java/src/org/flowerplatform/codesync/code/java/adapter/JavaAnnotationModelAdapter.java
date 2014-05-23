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
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationFeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.remote.Node;

/**
 * Mapped to {@link Annotation}. Children are annotation values, i.e. {@link MemberValuePair}.
 * 
 * @see JavaAnnotationFeatureProvider
 * 
 * @author Mariana
 */
public class JavaAnnotationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	@Override
	public Object getMatchKey(Object modelElement) {
		Annotation annotation = (Annotation) modelElement;
		String matchKey = (String) getAnnotationName(modelElement);
		// TODO fix this
//		if (annotation instanceof MarkerAnnotation) {
//			matchKey += CodeSyncCodePlugin.MARKER_ANNOTATION;
//		} else {
//			if (annotation instanceof SingleMemberAnnotation) {
//				matchKey += CodeSyncCodePlugin.SINGLE_MEMBER_ANNOTATION;
//			} else {
//				matchKey += CodeSyncCodePlugin.NORMAL_ANNOTATION;
//			}
//		}
		return matchKey;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getAnnotationName(element);
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (CoreConstants.NAME.equals(feature)) {
			if (element instanceof Annotation) {
				Annotation annotation = (Annotation) element;
				String name = (String) value;
				annotation.setTypeName(annotation.getAST().newName(name));
			}
			return;
		}
		super.setValueFeatureValue(element, feature, value);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncCodeJavaConstants.ANNOTATION_VALUES.equals(feature)) {
			if (element instanceof NormalAnnotation) {
				return ((NormalAnnotation) element).values();
			} else if (element instanceof SingleMemberAnnotation) {
				AST ast = AST.newAST(AST.JLS4);
				MemberValuePair pair = ast.newMemberValuePair();
				Expression value = ((SingleMemberAnnotation) element).getValue();
				ASTNode newValue = ASTNode.copySubtree(ast, value);
				pair.setName(ast.newSimpleName(CodeSyncCodeJavaConstants.SINGLE_MEMBER_ANNOTATION_VALUE_NAME));
				pair.setValue((Expression) newValue);
				return Collections.singletonList(pair);
			}
			return Collections.emptyList();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}
	
	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		if (CodeSyncCodeJavaConstants.ANNOTATION_VALUES.equals(feature)) {
			ASTNode child = null;
			ASTNode parent = (ASTNode) element;
			AST ast = parent.getAST();
			
			// for an existing NormalAnnotation, just add the new value
			if (parent instanceof NormalAnnotation) {
				MemberValuePair pair = ast.newMemberValuePair();
				((NormalAnnotation) parent).values().add(pair);
				child = pair;
			} else {
				Node value = (Node) correspondingChild;
				// if the existing annotation is a SingleMemberAnnotation, then set its value
				if (parent instanceof SingleMemberAnnotation) {
					ASTNode expression = getExpressionFromString(parent.getAST(), 
							(String) value.getOrPopulateProperties().get(CodeSyncCodeJavaConstants.ANNOTATION_VALUE_VALUE));
					((SingleMemberAnnotation) parent).setValue((Expression) expression);
					child = ast.newMemberValuePair(); // avoid NPE later
				}
			}
			
			return child;
		}
		
		return super.createChildOnContainmentFeature(element, feature, correspondingChild, typeProvider);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		if (CodeSyncCodeJavaConstants.ANNOTATION_VALUES.equals(feature) && !(parent instanceof NormalAnnotation)) {
			return;
		}
		super.removeChildrenOnContainmentFeature(parent, feature, child);
	}

	private Object getAnnotationName(Object element) {
		return ((Annotation) element).getTypeName().getFullyQualifiedName();
	}
	
}
