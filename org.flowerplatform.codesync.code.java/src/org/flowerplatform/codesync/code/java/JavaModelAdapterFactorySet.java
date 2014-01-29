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
package org.flowerplatform.codesync.code.java;

import static org.flowerplatform.codesync.code.java.adapter.JavaAttributeModelAdapter.ATTRIBUTE;
import static org.flowerplatform.codesync.code.java.adapter.JavaOperationModelAdapter.OPERATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.ANNOTATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.CLASS;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.ENUM;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.INTERFACE;

import org.flowerplatform.codesync.NodeFeatureProvider;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.IModelAdapter;
import org.flowerplatform.codesync.ModelAdapterFactory;
import org.flowerplatform.codesync.ModelAdapterFactorySet;
import org.flowerplatform.codesync.NodeModelAdapter;
import org.flowerplatform.codesync.code.CodeSyncModelAdapterFactory;
import org.flowerplatform.codesync.code.adapter.FolderModelAdapter;
import org.flowerplatform.codesync.code.adapter.NodeModelAdapterAncestor;
import org.flowerplatform.codesync.code.adapter.NodeModelAdapterLeft;
import org.flowerplatform.codesync.code.java.adapter.JavaFileModelAdapter;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.mindmap.remote.Node;

/**
 * @author Mariana
 */
public class JavaModelAdapterFactorySet extends ModelAdapterFactorySet {

	@Override
	public void initialize(String limitedPath, boolean useUIDs) {
		super.initialize(limitedPath, useUIDs);
		
		// right - AST
		rightFactory = new ModelAdapterFactory();
		
//		IFileAccessController fileAccessController = EditorPlugin.getInstance().getFileAccessController();
		
		// folder adapter
		FolderModelAdapter folderModelAdapter = (FolderModelAdapter) createAstModelAdapter(new FolderModelAdapter());
		folderModelAdapter.setLimitedPath(limitedPath);
		Class fileClass = CorePlugin.getInstance().getFileAccessController().getFileClass();
		rightFactory.addModelAdapter(fileClass, folderModelAdapter, "", CodeSyncPlugin.FOLDER);
		
		// java specific adapters
		JavaFileModelAdapter fileModelAdapter = (JavaFileModelAdapter) createAstModelAdapter(new JavaFileModelAdapter());
		rightFactory.addModelAdapter(fileClass, fileModelAdapter, CodeSyncCodeJavaPlugin.TECHNOLOGY, CodeSyncPlugin.FILE);
//		rightFactory.addModelAdapter(AbstractTypeDeclaration.class, createAstModelAdapter(new JavaTypeModelAdapter()), JavaTypeModelAdapter.CLASS);
//		rightFactory.addModelAdapter(FieldDeclaration.class, createAstModelAdapter(new JavaAttributeModelAdapter()), JavaAttributeModelAdapter.ATTRIBUTE);
//		rightFactory.addModelAdapter(MethodDeclaration.class, createAstModelAdapter(new JavaOperationModelAdapter()), JavaOperationModelAdapter.OPERATION);
//		rightFactory.addModelAdapter(SingleVariableDeclaration.class, createAstModelAdapter(new JavaParameterModelAdapter()), JavaParameterModelAdapter.PARAMETER);
//		rightFactory.addModelAdapter(Annotation.class, createAstModelAdapter(new JavaAnnotationModelAdapter()), JavaAnnotationModelAdapter.ANNOTATION);
//		rightFactory.addModelAdapter(Modifier.class, createAstModelAdapter(new JavaModifierModelAdapter()), JavaModifierModelAdapter.MODIFIER);
//		rightFactory.addModelAdapter(MemberValuePair.class, createAstModelAdapter(new JavaMemberValuePairModelAdapter()), JavaMemberValuePairModelAdapter.MEMBER_VALUE_PAIR);
//		rightFactory.addModelAdapter(EnumConstantDeclaration.class, createAstModelAdapter(new JavaEnumConstantDeclarationModelAdapter()), JavaEnumConstantDeclarationModelAdapter.ENUM_CONSTANT);
//		rightFactory.addModelAdapter(AnnotationTypeMemberDeclaration.class, createAstModelAdapter(new JavaAnnotationTypeMemberDeclarationModelAdapter()), JavaAnnotationTypeMemberDeclarationModelAdapter.ANNOTATION_MEMBER);
//		rightFactory.addModelAdapter(String.class, createAstModelAdapter(new StringModelAdapter()), "String");
		
		// ancestor - CodeSyncElements
		this.ancestorFactory = createCodeSyncModelAdapterFactory(false);
		
		// left - CodeSyncElements
		leftFactory = createCodeSyncModelAdapterFactory(true);
		
		// feature providers
		NodeFeatureProvider featureProvider = new NodeFeatureProvider();
		addFeatureProvider(fileClass, featureProvider);
		addFeatureProvider(Node.class, featureProvider);
//		
//		JavaTypeFeatureProvider typeFeatureProvider = new JavaTypeFeatureProvider();
//		addFeatureProvider(AbstractTypeDeclaration.class, typeFeatureProvider);
//		addFeatureProvider(CLASS, typeFeatureProvider);
//		addFeatureProvider(INTERFACE, typeFeatureProvider);
//		addFeatureProvider(ENUM, typeFeatureProvider);
//		addFeatureProvider(ANNOTATION, typeFeatureProvider);
//		
//		JavaAttributeFeatureProvider attributeFeatureProvider = new JavaAttributeFeatureProvider();
//		addFeatureProvider(FieldDeclaration.class, attributeFeatureProvider);
//		addFeatureProvider(ATTRIBUTE, attributeFeatureProvider);
//		
//		JavaOperationFeatureProvider operationFeatureProvider = new JavaOperationFeatureProvider();
//		addFeatureProvider(MethodDeclaration.class, operationFeatureProvider);
//		addFeatureProvider(OPERATION, operationFeatureProvider);
//		
//		JavaEnumConstantDeclarationFeatureProvider enumCtFeatureProvider = new JavaEnumConstantDeclarationFeatureProvider();
//		addFeatureProvider(EnumConstantDeclaration.class, enumCtFeatureProvider);
//		addFeatureProvider(ENUM_CONSTANT, enumCtFeatureProvider);
//		
//		JavaAnnotationTypeMemberDeclarationFeatureProvider annotationMemberFeatureProvider = new JavaAnnotationTypeMemberDeclarationFeatureProvider();
//		addFeatureProvider(AnnotationTypeMemberDeclaration.class, annotationMemberFeatureProvider);
//		addFeatureProvider(ANNOTATION_MEMBER, annotationMemberFeatureProvider);
//		
//		JavaAnnotationFeatureProvider annotationFeatureProvider = new JavaAnnotationFeatureProvider();
//		addFeatureProvider(Annotation.class, annotationFeatureProvider);
//		addFeatureProvider(com.crispico.flower.mp.model.astcache.code.Annotation.class, annotationFeatureProvider);
//		
//		JavaMemberValuePairFeatureProvider annotationValueFeatureProvider = new JavaMemberValuePairFeatureProvider();
//		addFeatureProvider(MemberValuePair.class, annotationValueFeatureProvider);
//		addFeatureProvider(AnnotationValue.class, annotationValueFeatureProvider);
//		
//		JavaModifierFeatureProvider modifierFeatureProvider = new JavaModifierFeatureProvider();
//		addFeatureProvider(Modifier.class, modifierFeatureProvider);
//		addFeatureProvider(com.crispico.flower.mp.model.astcache.code.Modifier.class, modifierFeatureProvider);
//		
//		JavaParameterFeatureProvider parameterFeatureProvider = new JavaParameterFeatureProvider();
//		addFeatureProvider(SingleVariableDeclaration.class, parameterFeatureProvider);
//		addFeatureProvider(Parameter.class, parameterFeatureProvider);
//		
//		addFeatureProvider(String.class, new StringFeatureProvider());
	}
	
	private CodeSyncModelAdapterFactory createCodeSyncModelAdapterFactory(boolean isLeft) {
		CodeSyncModelAdapterFactory factory = new CodeSyncModelAdapterFactory(this, rightFactory, isLeft);
		NodeModelAdapter typeModelAdapter = new NodeModelAdapter();
		factory.addModelAdapter(CLASS, typeModelAdapter, CLASS);
		factory.addModelAdapter(INTERFACE, typeModelAdapter, INTERFACE);
		factory.addModelAdapter(ENUM, typeModelAdapter, ENUM);
		factory.addModelAdapter(ANNOTATION, typeModelAdapter, ANNOTATION);
//		
//		factory.addModelAdapter(ATTRIBUTE, new AttributeModelAdapter(), ATTRIBUTE);
//		factory.addModelAdapter(OPERATION, new OperationModelAdapter(), OPERATION);
//		factory.addModelAdapter(ENUM_CONSTANT, new EnumConstantModelAdapter(), ENUM_CONSTANT);
//		factory.addModelAdapter(ANNOTATION_MEMBER, new AnnotationMemberModelAdapter(), ANNOTATION_MEMBER);
//		
//		factory.addModelAdapter(com.crispico.flower.mp.model.astcache.code.Annotation.class, new AnnotationModelAdapter(), JavaAnnotationModelAdapter.ANNOTATION);
//		factory.addModelAdapter(AnnotationValue.class, new AnnotationValueModelAdapter(), JavaMemberValuePairModelAdapter.MEMBER_VALUE_PAIR);
//		factory.addModelAdapter(com.crispico.flower.mp.model.astcache.code.Modifier.class, new ModifierModelAdapter(), JavaModifierModelAdapter.MODIFIER);
//		factory.addModelAdapter(Parameter.class, new ParameterModelAdapter(), JavaParameterModelAdapter.PARAMETER);
//		factory.addModelAdapter(String.class, new StringModelAdapter(), "String");
		
		NodeModelAdapter cseAdapter = isLeft 
				? new NodeModelAdapterLeft()
				: new NodeModelAdapterAncestor();
//		cseAdapter.setModelAdapterFactory(factory);
		cseAdapter.setEObjectConverter(rightFactory);
		
		// TODO fix this; all the adapters above should be wrapped in a left/ancestor
		factory.addModelAdapter(Node.class, cseAdapter, CodeSyncPlugin.FOLDER);
		factory.addModelAdapter(Node.class, cseAdapter, CodeSyncPlugin.FILE);
		
		return factory;
	}
	
	private IModelAdapter createAstModelAdapter(IModelAdapter adapter) {
		return adapter.setModelAdapterFactorySet(this);
	}
	
}