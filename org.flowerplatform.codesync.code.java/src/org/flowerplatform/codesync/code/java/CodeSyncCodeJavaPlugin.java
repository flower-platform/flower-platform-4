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

import static org.flowerplatform.codesync.code.java.adapter.JavaAnnotationModelAdapter.ANNOTATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaAnnotationTypeMemberDeclarationModelAdapter.ANNOTATION_MEMBER;
import static org.flowerplatform.codesync.code.java.adapter.JavaAttributeModelAdapter.ATTRIBUTE;
import static org.flowerplatform.codesync.code.java.adapter.JavaEnumConstantDeclarationModelAdapter.ENUM_CONSTANT;
import static org.flowerplatform.codesync.code.java.adapter.JavaMemberValuePairModelAdapter.MEMBER_VALUE_PAIR;
import static org.flowerplatform.codesync.code.java.adapter.JavaModifierModelAdapter.MODIFIER;
import static org.flowerplatform.codesync.code.java.adapter.JavaOperationModelAdapter.OPERATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaParameterModelAdapter.PARAMETER;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.ANNOTATION_TYPE;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.CLASS;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.ENUM;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.INTERFACE;
import static org.flowerplatform.codesync.code.java.adapter.JavaExpressionModelAdapter.EXPRESSION;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.code.CodeSyncCodePlugin;
import org.flowerplatform.codesync.code.adapter.FolderModelAdapter;
import org.flowerplatform.codesync.code.feature_provider.FileFeatureProvider;
import org.flowerplatform.codesync.code.java.adapter.JavaAnnotationModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaAnnotationTypeMemberDeclarationModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaAttributeModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaEnumConstantDeclarationModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaFileModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaMemberValuePairModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaModifierModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaOperationModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaParameterModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaExpressionModelAdapter;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationTypeMemberDeclarationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAttributeFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaEnumConstantDeclarationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaFeaturesConstants;
import org.flowerplatform.codesync.code.java.feature_provider.JavaMemberValuePairFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaModifierFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaOperationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaParameterFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaTypeDeclarationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaExpressionFeatureProvider;
import org.flowerplatform.codesync.code.java.type_provider.JavaTypeProvider;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana
 */
public class CodeSyncCodeJavaPlugin extends AbstractFlowerJavaPlugin {

	public static final String TECHNOLOGY = "java";
	
	protected static CodeSyncCodeJavaPlugin INSTANCE;
	
	private FolderModelAdapter folderModelAdapter;
	
	public static CodeSyncCodeJavaPlugin getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @author Mariana Gheorghe
	 * @author Mircea Negreanu
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CodeSyncPlugin.getInstance().addTypeProvider(TECHNOLOGY, new JavaTypeProvider());
		
		FileFeatureProvider fileFeatureProvider = new FileFeatureProvider();
		createNodeTypeDescriptor(CodeSyncCodePlugin.FOLDER, new FolderModelAdapter(), fileFeatureProvider);
		createNodeTypeDescriptor(CodeSyncCodePlugin.FILE, new JavaFileModelAdapter(), fileFeatureProvider);
		
		PropertyDescriptor returnType = new PropertyDescriptor()
			.setNameAs(JavaFeaturesConstants.TYPED_ELEMENT_TYPE).setReadOnlyAs(false);
		
		JavaTypeDeclarationModelAdapter typeModelAdapter = new JavaTypeDeclarationModelAdapter();
		JavaTypeDeclarationFeatureProvider typeFeatureProvider = new JavaTypeDeclarationFeatureProvider();
		createNodeTypeDescriptor(CLASS, typeModelAdapter, typeFeatureProvider);
		createNodeTypeDescriptor(INTERFACE, typeModelAdapter, typeFeatureProvider);
		createNodeTypeDescriptor(ENUM, typeModelAdapter, typeFeatureProvider);
		createNodeTypeDescriptor(ANNOTATION_TYPE, typeModelAdapter, typeFeatureProvider);
		
		createNodeTypeDescriptor(ATTRIBUTE, new JavaAttributeModelAdapter(), new JavaAttributeFeatureProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
		createNodeTypeDescriptor(OPERATION, new JavaOperationModelAdapter(), new JavaOperationFeatureProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
		createNodeTypeDescriptor(ENUM_CONSTANT, new JavaEnumConstantDeclarationModelAdapter(), new JavaEnumConstantDeclarationFeatureProvider());
		createNodeTypeDescriptor(ANNOTATION_MEMBER, new JavaAnnotationTypeMemberDeclarationModelAdapter(), new JavaAnnotationTypeMemberDeclarationFeatureProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
		
		createNodeTypeDescriptor(ANNOTATION, new JavaAnnotationModelAdapter(), new JavaAnnotationFeatureProvider());
		createNodeTypeDescriptor(MEMBER_VALUE_PAIR, new JavaMemberValuePairModelAdapter(), new JavaMemberValuePairFeatureProvider());
		createNodeTypeDescriptor(MODIFIER, new JavaModifierModelAdapter(), new JavaModifierFeatureProvider());
		createNodeTypeDescriptor(PARAMETER, new JavaParameterModelAdapter(), new JavaParameterFeatureProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
		
		createNodeTypeDescriptor(EXPRESSION, new JavaExpressionModelAdapter(), new JavaExpressionFeatureProvider());
		
		createNodeTypeDescriptor(CodeSyncPlugin.CATEGORY, null, null);
		
		// wrap the descriptor register code in a runnable
//		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new Runnable() {
//			@Override
//			public void run() {
//				CodeSyncPlugin.getInstance().addSrcDir("src");
//				
//				// TODO reactivate java later
//				CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType("javaClass")
//						.setLabel("Class")
//						.setIconUrl("images/obj16/SyncClass.gif")
//						.setDefaultName("NewJavaClass")
//						.setExtension("java")
//						.addChildrenCodeSyncTypeCategory("javaClassMember")
//						.addFeature(DOCUMENTATION)
//						.addFeature(VISIBILITY)
//						.addFeature(IS_ABSTRACT)
//						.addFeature(IS_STATIC)
//						.addFeature(IS_FINAL)
//						.addFeature(SUPER_CLASS)
//						.addFeature(SUPER_INTERFACES));
//				CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType("javaOperation")
//						.setLabel("Operation")
//						.setIconUrl("images/obj16/SyncOperation_public.gif")
//						.setDefaultName("newOperation")
//						.addCodeSyncTypeCategory("javaClassMember")
//						.addFeature(DOCUMENTATION)
//						.addFeature(VISIBILITY)
//						.addFeature(IS_ABSTRACT)
//						.addFeature(IS_STATIC)
//						.addFeature(IS_FINAL)
//						.addFeature(TYPE));
//				CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType("javaAttribute")
//						.setLabel("Attribute")
//						.setIconUrl("images/obj16/SyncProperty_public.gif")
//						.setDefaultName("newAttribute")
//						.addCodeSyncTypeCategory("javaClassMember")
//						.addFeature(DOCUMENTATION)
//						.addFeature(VISIBILITY)
//						.addFeature(IS_ABSTRACT)
//						.addFeature(IS_STATIC)
//						.addFeature(IS_FINAL)
//						.addFeature(TYPE)
//						.addFeature(INITIALIZER));
//				
//				CodeSyncPlugin.getInstance().getFeatureAccessExtensions().add(new JavaFeatureAccessExtension());
				
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass", new JavaClassProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.title", new JavaClassTitleProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.javaAttribute", new JavaClassAttributeProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.javaOperation", new JavaClassOperationProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("scenarioInterraction", new JavaScenarioElementProcessor());
//				
//				AbstractFeatureChangesProcessor processor = new AbstractFeatureChangesProcessor();
//				// if model element removed => remove view
//				processor.getDependentFeatures().add(new DependentFeature(EObject.class, NotationPackage.eINSTANCE.getView_DiagrammableElement()));
//				// if model element removed => remove relations
//				processor.getDependentFeatures().add(new DependentFeature(CodeSyncElement.class, CodeSyncPackage.eINSTANCE.getRelation_Source()));
//				processor.getDependentFeatures().add(new DependentFeature(CodeSyncElement.class, CodeSyncPackage.eINSTANCE.getRelation_Target()));
//				// if view removed => remove edges
//				processor.getDependentFeatures().add(new DependentFeature(View.class, NotationPackage.eINSTANCE.getView_SourceEdges()));
//				processor.getDependentFeatures().add(new DependentFeature(View.class, NotationPackage.eINSTANCE.getView_TargetEdges()));
//				
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass", processor);
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.javaAttribute", processor);
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.javaOperation", processor);
//				
//				CodeSyncPlugin.getInstance().getFullyQualifiedNameProvider().addDelegateProvider(new JavaFullyQualifiedNameProvider());
//				ResourcesPlugin.getWorkspace().addResourceChangeListener(new JavaResourceChangeListener());
//				JavaCore.addElementChangedListener(new JavaElementChangedListener(), ElementChangedEvent.POST_RECONCILE);
//			}
//		});
		
	}
	
	private TypeDescriptor createNodeTypeDescriptor(String type, AbstractModelAdapter modelAdapterRight, FeatureProvider featureProvider) {
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(type);
		descriptor.addCategory("category.codeSync");
		descriptor.addCategory("category.persistence-codeSync");
		descriptor.addCategory("category.all");
		descriptor.addSingleController(AbstractModelAdapter.MODEL_ADAPTER_RIGHT, modelAdapterRight);
		descriptor.addSingleController(FeatureProvider.FEATURE_PROVIDER, featureProvider);
		return descriptor;
	}

	public FolderModelAdapter getFolderModelAdapter() {
		return folderModelAdapter;
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		INSTANCE = null;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}

}
