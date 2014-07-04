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

import static org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants.MODIFIERS;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.ModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationFeatureProvider;
import org.flowerplatform.core.CoreConstants;

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
//		Annotation annotation = (Annotation) modelElement;
		String matchKey = getName(modelElement);
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
	protected String getName(Object element) {
		return ((Annotation) element).getTypeName().getFullyQualifiedName();
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
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, ModelAdapterSet correspondingModelAdapterSet) {
		if (CodeSyncCodeJavaConstants.MODIFIERS.equals(feature)) {
			if (!(parent instanceof BodyDeclaration || parent instanceof SingleVariableDeclaration)) {
				throw new RuntimeException("Cannot create modifier for " + parent);
			}
			
			Annotation annotation;
			AST ast = ((ASTNode) parent).getAST();
			IModelAdapter correspondingModelAdapter = correspondingModelAdapterSet.getModelAdapter(correspondingChild);
			
			int valuesCount = getIterableSize(correspondingModelAdapter.getContainmentFeatureIterable(correspondingChild, MODIFIERS, null).iterator());
			if (valuesCount == 0) {
				annotation = ast.newMarkerAnnotation();
			} else if (valuesCount == 1) {
				annotation = ast.newSingleMemberAnnotation();
			} else {
				annotation = ast.newNormalAnnotation();
			}
			
			addModifier((ASTNode) parent, annotation);
			return annotation;
		}
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, correspondingModelAdapterSet);
	}

	private int getIterableSize(Iterator<?> it) {
		int size = 0;
		while (it.hasNext()) {
			it.next();
			size++;
		}
		return size;
	}

}
