/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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

import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.MODIFIERS;

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
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link Annotation}. Children are annotation values, i.e. {@link MemberValuePair}.
 * 
 * @author Mariana
 */
public class JavaAnnotationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	/**
	 *@author Mariana Gheorghe
	 **/
	public JavaAnnotationModelAdapter() {
		containmentFeatures.add(CodeSyncJavaConstants.ANNOTATION_VALUES);
	}
	
	@Override
	public Object getMatchKey(Object modelElement, CodeSyncAlgorithm codeSyncAlgorithm) {
//		Annotation annotation = (Annotation) modelElement;
		String matchKey = getName(modelElement, codeSyncAlgorithm);
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
	protected String getName(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return ((Annotation) element).getTypeName().getFullyQualifiedName();
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CoreConstants.NAME.equals(feature)) {
			if (element instanceof Annotation) {
				Annotation annotation = (Annotation) element;
				String name = (String) value;
				annotation.setTypeName(annotation.getAST().newName(name));
			}
			return;
		}
		super.setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.ANNOTATION_VALUES.equals(feature)) {
			if (element instanceof NormalAnnotation) {
				return ((NormalAnnotation) element).values();
			} else if (element instanceof SingleMemberAnnotation) {
				AST ast = AST.newAST(AST.JLS4);
				MemberValuePair pair = ast.newMemberValuePair();
				Expression value = ((SingleMemberAnnotation) element).getValue();
				ASTNode newValue = ASTNode.copySubtree(ast, value);
				pair.setName(ast.newSimpleName(CodeSyncJavaConstants.SINGLE_MEMBER_ANNOTATION_VALUE_NAME));
				pair.setValue((Expression) newValue);
				return Collections.singletonList(pair);
			}
			return Collections.emptyList();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}
	
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, 
			IModelAdapterSet correspondingModelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.MODIFIERS.equals(feature)) {
			if (!(parent instanceof BodyDeclaration || parent instanceof SingleVariableDeclaration)) {
				throw new RuntimeException("Cannot create modifier for " + parent);
			}
			
			Annotation annotation;
			AST ast = ((ASTNode) parent).getAST();
			IModelAdapter correspondingModelAdapter = correspondingModelAdapterSet.getModelAdapter(correspondingChild, codeSyncAlgorithm);
			
			int valuesCount = getIterableSize(correspondingModelAdapter.getContainmentFeatureIterable(correspondingChild, MODIFIERS, null, codeSyncAlgorithm).iterator());
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
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, correspondingModelAdapterSet, codeSyncAlgorithm);
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
