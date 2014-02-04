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

import static org.flowerplatform.codesync.code.java.adapter.JavaAnnotationModelAdapter.ANNOTATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaAnnotationTypeMemberDeclarationModelAdapter.ANNOTATION_MEMBER;
import static org.flowerplatform.codesync.code.java.adapter.JavaAttributeModelAdapter.ATTRIBUTE;
import static org.flowerplatform.codesync.code.java.adapter.JavaEnumConstantDeclarationModelAdapter.ENUM_CONSTANT;
import static org.flowerplatform.codesync.code.java.adapter.JavaMemberValuePairModelAdapter.MEMBER_VALUE_PAIR;
import static org.flowerplatform.codesync.code.java.adapter.JavaModifierModelAdapter.MODIFIER;
import static org.flowerplatform.codesync.code.java.adapter.JavaOperationModelAdapter.OPERATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaParameterModelAdapter.PARAMETER;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.ANNOTATION_TYPE;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.CLASS;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.ENUM;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.INTERFACE;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.ModelAdapterFactory;
import org.flowerplatform.codesync.adapter.ModelAdapterFactorySet;
import org.flowerplatform.codesync.code.adapter.CodeSyncModelAdapterFactory;
import org.flowerplatform.codesync.code.adapter.FolderModelAdapter;
import org.flowerplatform.codesync.code.adapter.NodeModelAdapterAncestor;
import org.flowerplatform.codesync.code.adapter.NodeModelAdapterLeft;
import org.flowerplatform.codesync.code.feature_provider.FileFeatureProvider;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaPlugin;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationTypeMemberDeclarationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAttributeFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaEnumConstantDeclarationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaMemberValuePairFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaModifierFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaOperationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaParameterFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaTypeFeatureProvider;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana
 */
public class JavaModelAdapterFactorySet extends ModelAdapterFactorySet {

	@Override
	public void initialize(String limitedPath, boolean useUIDs) {
		super.initialize(limitedPath, useUIDs);
		
		// right - AST
		rightFactory = new ModelAdapterFactory();
		
		// folder adapter
		FolderModelAdapter folderModelAdapter = (FolderModelAdapter) createAstModelAdapter(new FolderModelAdapter());
		folderModelAdapter.setLimitedPath(limitedPath);
		Class<?> fileClass = CorePlugin.getInstance().getFileAccessController().getFileClass();
		rightFactory.addModelAdapter(fileClass, folderModelAdapter, "", CodeSyncPlugin.FOLDER);
		
		// java specific adapters
		JavaFileModelAdapter fileModelAdapter = (JavaFileModelAdapter) createAstModelAdapter(new JavaFileModelAdapter());
		rightFactory.addModelAdapter(fileClass, fileModelAdapter, CodeSyncCodeJavaPlugin.TECHNOLOGY, CodeSyncPlugin.FILE);
		rightFactory.addModelAdapter(AbstractTypeDeclaration.class, createAstModelAdapter(new JavaTypeModelAdapter()), CLASS);
		rightFactory.addModelAdapter(FieldDeclaration.class, createAstModelAdapter(new JavaAttributeModelAdapter()), ATTRIBUTE);
		rightFactory.addModelAdapter(MethodDeclaration.class, createAstModelAdapter(new JavaOperationModelAdapter()), OPERATION);
		rightFactory.addModelAdapter(SingleVariableDeclaration.class, createAstModelAdapter(new JavaParameterModelAdapter()), PARAMETER);
		rightFactory.addModelAdapter(Annotation.class, createAstModelAdapter(new JavaAnnotationModelAdapter()), ANNOTATION);
		rightFactory.addModelAdapter(Modifier.class, createAstModelAdapter(new JavaModifierModelAdapter()), MODIFIER);
		rightFactory.addModelAdapter(MemberValuePair.class, createAstModelAdapter(new JavaMemberValuePairModelAdapter()), MEMBER_VALUE_PAIR);
		rightFactory.addModelAdapter(EnumConstantDeclaration.class, createAstModelAdapter(new JavaEnumConstantDeclarationModelAdapter()), ENUM_CONSTANT);
		rightFactory.addModelAdapter(AnnotationTypeMemberDeclaration.class, createAstModelAdapter(new JavaAnnotationTypeMemberDeclarationModelAdapter()), ANNOTATION_MEMBER);
		
		// ancestor - CodeSyncElements
		this.ancestorFactory = createCodeSyncModelAdapterFactory(false);
		
		// left - CodeSyncElements
		leftFactory = createCodeSyncModelAdapterFactory(true);
		
		// feature providers
		NodeFeatureProvider featureProvider = new FileFeatureProvider();
		addFeatureProvider(fileClass, featureProvider);
		addFeatureProvider(CodeSyncPlugin.FOLDER, featureProvider);
		addFeatureProvider(CodeSyncPlugin.FILE, featureProvider);
		
		JavaTypeFeatureProvider typeFeatureProvider = new JavaTypeFeatureProvider();
		addFeatureProvider(AbstractTypeDeclaration.class, typeFeatureProvider);
		addFeatureProvider(CLASS, typeFeatureProvider);
		addFeatureProvider(INTERFACE, typeFeatureProvider);
		addFeatureProvider(ENUM, typeFeatureProvider);
		addFeatureProvider(ANNOTATION_TYPE, typeFeatureProvider);
		
		JavaAttributeFeatureProvider attributeFeatureProvider = new JavaAttributeFeatureProvider();
		addFeatureProvider(FieldDeclaration.class, attributeFeatureProvider);
		addFeatureProvider(ATTRIBUTE, attributeFeatureProvider);
		
		JavaOperationFeatureProvider operationFeatureProvider = new JavaOperationFeatureProvider();
		addFeatureProvider(MethodDeclaration.class, operationFeatureProvider);
		addFeatureProvider(OPERATION, operationFeatureProvider);
		
		JavaEnumConstantDeclarationFeatureProvider enumCtFeatureProvider = new JavaEnumConstantDeclarationFeatureProvider();
		addFeatureProvider(EnumConstantDeclaration.class, enumCtFeatureProvider);
		addFeatureProvider(ENUM_CONSTANT, enumCtFeatureProvider);
		
		JavaAnnotationTypeMemberDeclarationFeatureProvider annotationMemberFeatureProvider = new JavaAnnotationTypeMemberDeclarationFeatureProvider();
		addFeatureProvider(AnnotationTypeMemberDeclaration.class, annotationMemberFeatureProvider);
		addFeatureProvider(ANNOTATION_MEMBER, annotationMemberFeatureProvider);
		
		JavaAnnotationFeatureProvider annotationFeatureProvider = new JavaAnnotationFeatureProvider();
		addFeatureProvider(Annotation.class, annotationFeatureProvider);
		addFeatureProvider(ANNOTATION, annotationFeatureProvider);
		
		JavaMemberValuePairFeatureProvider annotationValueFeatureProvider = new JavaMemberValuePairFeatureProvider();
		addFeatureProvider(MemberValuePair.class, annotationValueFeatureProvider);
		addFeatureProvider(MEMBER_VALUE_PAIR, annotationValueFeatureProvider);
		
		JavaModifierFeatureProvider modifierFeatureProvider = new JavaModifierFeatureProvider();
		addFeatureProvider(Modifier.class, modifierFeatureProvider);
		addFeatureProvider(MODIFIER, modifierFeatureProvider);
		
		JavaParameterFeatureProvider parameterFeatureProvider = new JavaParameterFeatureProvider();
		addFeatureProvider(SingleVariableDeclaration.class, parameterFeatureProvider);
		addFeatureProvider(PARAMETER, parameterFeatureProvider);
	}
	
	private CodeSyncModelAdapterFactory createCodeSyncModelAdapterFactory(boolean isLeft) {
		CodeSyncModelAdapterFactory factory = new CodeSyncModelAdapterFactory(this, rightFactory, isLeft);
		
		factory.addModelAdapter(CLASS, createNodeModelAdapter(factory, isLeft), CLASS);
		factory.addModelAdapter(INTERFACE, createNodeModelAdapter(factory, isLeft), INTERFACE);
		factory.addModelAdapter(ENUM, createNodeModelAdapter(factory, isLeft), ENUM);
		factory.addModelAdapter(ANNOTATION_TYPE, createNodeModelAdapter(factory, isLeft), ANNOTATION_TYPE);
		
		factory.addModelAdapter(ATTRIBUTE, createNodeModelAdapter(factory, isLeft), ATTRIBUTE);
		factory.addModelAdapter(OPERATION, createNodeModelAdapter(factory, isLeft), OPERATION);
		factory.addModelAdapter(ENUM_CONSTANT, createNodeModelAdapter(factory, isLeft), ENUM_CONSTANT);
		factory.addModelAdapter(ANNOTATION_MEMBER, createNodeModelAdapter(factory, isLeft), ANNOTATION_MEMBER);
		
		factory.addModelAdapter(ANNOTATION, createNodeModelAdapter(factory, isLeft), ANNOTATION);
		factory.addModelAdapter(MEMBER_VALUE_PAIR, createNodeModelAdapter(factory, isLeft), MEMBER_VALUE_PAIR);
		factory.addModelAdapter(MODIFIER, createNodeModelAdapter(factory, isLeft), MODIFIER);
		factory.addModelAdapter(PARAMETER, createNodeModelAdapter(factory, isLeft), PARAMETER);
		
//		NodeModelAdapter cseAdapter = isLeft 
//				? new NodeModelAdapterLeft()
//				: new NodeModelAdapterAncestor();
//		cseAdapter.setModelAdapterFactory(factory);
//		cseAdapter.setEObjectConverter(rightFactory);
		
		// TODO fix this; all the adapters above should be wrapped in a left/ancestor
		factory.addModelAdapter(Node.class, createNodeModelAdapter(factory, isLeft), CodeSyncPlugin.FOLDER);
		factory.addModelAdapter(Node.class, createNodeModelAdapter(factory, isLeft), CodeSyncPlugin.FILE);
		
		return factory;
	}
	
	private IModelAdapter createNodeModelAdapter(ModelAdapterFactory factory, boolean isLeft) {
		return (isLeft ? new NodeModelAdapterLeft() : new NodeModelAdapterAncestor())
				.setOppositeModelAdapterFactory(rightFactory)
				.setModelAdapterFactory(factory)
				.setModelAdapterFactorySet(this);
	}
	
	private IModelAdapter createAstModelAdapter(IModelAdapter adapter) {
		return adapter.setModelAdapterFactorySet(this);
	}
	
}