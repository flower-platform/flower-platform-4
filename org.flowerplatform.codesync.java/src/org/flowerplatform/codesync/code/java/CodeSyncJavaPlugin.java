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
package org.flowerplatform.codesync.code.java;

import static org.flowerplatform.codesync.CodeSyncConstants.FILE;
import static org.flowerplatform.codesync.CodeSyncConstants.FOLDER;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ANNOTATION;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ANNOTATION_MEMBER;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ANNOTATION_TYPE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ANNOTATION_VALUES;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ATTRIBUTE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.CATEGORY_CAN_CONTAIN_TYPES;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.CATEGORY_HAS_SUPER_INTERFACES;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.CATEGORY_MODIFIABLE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.CATEGORY_TYPE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.CLASS;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ENUM;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ENUM_CONSTANT;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ENUM_CONSTANT_ARGUMENT;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ENUM_CONSTANT_ARGUMENTS;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.EXTENSION_JAVA;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_ANNOTATION;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_FIELD;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_FILE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_LOCAL_VAR;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_METHOD;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_PACKAGE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_TYPE_ANNOTATION;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_TYPE_CLASS;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_TYPE_ENUM;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_TYPE_INTERFACE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_WIZ_PACKAGE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_WIZ_TYPE_ANNOTATION;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_WIZ_TYPE_CLASS;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_WIZ_TYPE_ENUM;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.IMG_WIZ_TYPE_INTERFACE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.INTERFACE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.JAVA;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.MEMBER_VALUE_PAIR;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.MODIFIER;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.MODIFIERS;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.OPERATION;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.OPERATION_PARAMETERS;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.PARAMETER;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.SUPER_INTERFACE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.SUPER_INTERFACES;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.TYPED_ELEMENT_TYPE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.TYPE_MEMBERS;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.getImagePath;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.getImagePathFromPublicResources;
import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST;

import java.util.Arrays;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.file.FileModelAdapterSet;
import org.flowerplatform.codesync.adapter.file.FolderModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaAnnotationModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaAnnotationTypeMemberDeclarationModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaAttributeModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaEnumConstantDeclarationModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaExpressionModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaFileModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaMemberValuePairModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaModifierModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaOperationModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaParameterModelAdapter;
import org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter;
import org.flowerplatform.codesync.code.java.controller.JavaIconPropertyProvider;
import org.flowerplatform.codesync.code.java.line_information_provider.JavaOperationLineProvider;
import org.flowerplatform.codesync.code.java.type_provider.JavaTypeProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana
 */
public class CodeSyncJavaPlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncJavaPlugin INSTANCE;
	
	private FolderModelAdapter folderModelAdapter;
	
	public static CodeSyncJavaPlugin getInstance() {
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
		
		CodeSyncPlugin.getInstance().addFileModelAdapterDelegate(EXTENSION_JAVA, JAVA);
		
		JavaTypeDeclarationModelAdapter typeModelAdapter = new JavaTypeDeclarationModelAdapter();
		JavaExpressionModelAdapter expressionModelAdapter = new JavaExpressionModelAdapter();
		CodeSyncPlugin.getInstance().addModelAdapterSet(JAVA, new FileModelAdapterSet()
				.setTypeProvider(new JavaTypeProvider())
				.setLineProvider(new JavaOperationLineProvider())
				.setFileModelAdapterDelegate(new JavaFileModelAdapter())
				.addModelAdapter(CLASS, typeModelAdapter)
				.addModelAdapter(INTERFACE, typeModelAdapter)
				.addModelAdapter(ENUM, typeModelAdapter)
				.addModelAdapter(ANNOTATION_TYPE, typeModelAdapter)
				.addModelAdapter(ATTRIBUTE, new JavaAttributeModelAdapter())
				.addModelAdapter(OPERATION, new JavaOperationModelAdapter())
				.addModelAdapter(ENUM_CONSTANT, new JavaEnumConstantDeclarationModelAdapter())
				.addModelAdapter(ENUM_CONSTANT_ARGUMENT, expressionModelAdapter)
				.addModelAdapter(ANNOTATION_MEMBER, new JavaAnnotationTypeMemberDeclarationModelAdapter())
				.addModelAdapter(ANNOTATION, new JavaAnnotationModelAdapter())
				.addModelAdapter(MEMBER_VALUE_PAIR, new JavaMemberValuePairModelAdapter())
				.addModelAdapter(MODIFIER, new JavaModifierModelAdapter())
				.addModelAdapter(PARAMETER, new JavaParameterModelAdapter())
				.addModelAdapter(SUPER_INTERFACE, expressionModelAdapter));
		
		MemberOfChildCategoryDescriptor childrenDescriptor = new MemberOfChildCategoryDescriptor(CodeSyncConstants.CHILDREN);
	
		createNodeTypeDescriptor(FOLDER)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, childrenDescriptor)
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FOLDER).setLabelAs(getLabel("codesync.java.package"))
					.setIconAs(getImagePathFromPublicResources(IMG_WIZ_PACKAGE)).setOrderIndexAs(10))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FILE).setLabelAs(getLabel("codesync.java.file"))
					.setIconAs(getImagePathFromPublicResources(IMG_FILE)).setOrderIndexAs(20))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_PACKAGE)));
	
		createNodeTypeDescriptor(FILE)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, childrenDescriptor)
			.addCategory(CATEGORY_CAN_CONTAIN_TYPES)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_FILE)));
		
		PropertyDescriptor returnType = new PropertyDescriptor().setNameAs(TYPED_ELEMENT_TYPE);
		
		MemberOfChildCategoryDescriptor typeMembersDescriptor = new MemberOfChildCategoryDescriptor(TYPE_MEMBERS);
		
		createNodeTypeDescriptor(CLASS)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
			.addCategory(CodeSyncJavaConstants.CATEGORY_TYPE)
			.addCategory(CATEGORY_CAN_CONTAIN_TYPES)
			.addCategory(CATEGORY_HAS_SUPER_INTERFACES)
			.addCategory(CATEGORY_MODIFIABLE)
			.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICONS, getImagePath(IMG_TYPE_CLASS)));
		
		createNodeTypeDescriptor(INTERFACE)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
			.addCategory(CATEGORY_TYPE)
			.addCategory(CATEGORY_CAN_CONTAIN_TYPES)
			.addCategory(CATEGORY_HAS_SUPER_INTERFACES)
			.addCategory(CATEGORY_MODIFIABLE)
			.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICONS, getImagePath(IMG_TYPE_INTERFACE)));
	
		createNodeTypeDescriptor(ENUM)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
			.addCategory(CATEGORY_TYPE)
			.addCategory(CATEGORY_CAN_CONTAIN_TYPES)
			.addCategory(CATEGORY_HAS_SUPER_INTERFACES)
			.addCategory(CATEGORY_MODIFIABLE)
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ENUM_CONSTANT).setLabelAs(getLabel("codesync.java.enum.constant"))
					.setIconAs(getImagePathFromPublicResources(IMG_FIELD)).setOrderIndexAs(10))
			.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICONS, getImagePath(IMG_TYPE_ENUM)));
	
		createNodeTypeDescriptor(ANNOTATION_TYPE)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
			.addCategory(CATEGORY_TYPE)
			.addCategory(CATEGORY_CAN_CONTAIN_TYPES)
			.addCategory(CATEGORY_MODIFIABLE)
			.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICONS, getImagePath(IMG_TYPE_ANNOTATION)));
		
		createNodeTypeDescriptor(ATTRIBUTE)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
			.addCategory(CATEGORY_MODIFIABLE)
			.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICONS, getImagePath(IMG_FIELD)))
			.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
		
		createNodeTypeDescriptor(OPERATION)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
			.addCategory(CATEGORY_MODIFIABLE)
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(PARAMETER).setLabelAs(getLabel("codesync.java.parameter"))
					.setIconAs(getImagePathFromPublicResources(IMG_LOCAL_VAR)).setOrderIndexAs(10))
			.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICONS, getImagePath(IMG_METHOD)))
			.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
		
		createNodeTypeDescriptor(ENUM_CONSTANT)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ENUM_CONSTANT_ARGUMENT).setLabelAs(getLabel("codesync.java.enum.constant.argument")).setOrderIndexAs(10))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ANNOTATION).setLabelAs(getLabel("codesync.java.annotation"))
					.setIconAs(getImagePathFromPublicResources(IMG_ANNOTATION)).setOrderIndexAs(20))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_FIELD)));
		
		createNodeTypeDescriptor(ENUM_CONSTANT_ARGUMENT)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(ENUM_CONSTANT_ARGUMENTS));
	
		createNodeTypeDescriptor(ANNOTATION_MEMBER)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
			.addCategory(CATEGORY_MODIFIABLE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_METHOD)))
			.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
		
		MemberOfChildCategoryDescriptor modifiers = new MemberOfChildCategoryDescriptor(MODIFIERS);
		
		createNodeTypeDescriptor(ANNOTATION)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, modifiers)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_ANNOTATION)));
	
		createNodeTypeDescriptor(MEMBER_VALUE_PAIR)
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(ANNOTATION_VALUES));
		
		createNodeTypeDescriptor(MODIFIER)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, modifiers)
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CoreConstants.NAME).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setPossibleValuesAs(Arrays.asList(
					"public",
					"protected",
					"private",
					"static",
					"abstract",
					"final",
					"native",
					"synchronized",
					"transient",
					"volatile",
					"strictfp")));
		
		createNodeTypeDescriptor(PARAMETER)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(OPERATION_PARAMETERS))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(MODIFIER).setLabelAs(getLabel("codesync.java.modifier")).setOrderIndexAs(10))
			.addAdditiveController(PROPERTY_DESCRIPTOR, returnType)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_LOCAL_VAR)));
		
		createNodeTypeDescriptor(SUPER_INTERFACE)
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(SUPER_INTERFACES));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_CAN_CONTAIN_TYPES)
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(CLASS).setLabelAs(getLabel("codesync.java.type.class"))
					.setIconAs(getImagePathFromPublicResources(IMG_WIZ_TYPE_CLASS)).setOrderIndexAs(10000))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(INTERFACE).setLabelAs(getLabel("codesync.java.type.interface"))
					.setIconAs(getImagePathFromPublicResources(IMG_WIZ_TYPE_INTERFACE)).setOrderIndexAs(20000))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ENUM).setLabelAs(getLabel("codesync.java.type.enum"))
					.setIconAs(getImagePathFromPublicResources(IMG_WIZ_TYPE_ENUM)).setOrderIndexAs(30000))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ANNOTATION_TYPE).setLabelAs(getLabel("codesync.java.type.annotation"))
					.setIconAs(getImagePathFromPublicResources(IMG_WIZ_TYPE_ANNOTATION)).setOrderIndexAs(40000));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_TYPE)
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ATTRIBUTE)
					.setIconAs(getImagePathFromPublicResources(IMG_FIELD)).setLabelAs(getLabel("codesync.java.field")).setOrderIndexAs(100))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(OPERATION)
					.setIconAs(getImagePathFromPublicResources(IMG_METHOD)).setLabelAs(getLabel("codesync.java.method")).setOrderIndexAs(200));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_HAS_SUPER_INTERFACES)
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(SUPER_INTERFACE).setLabelAs(getLabel("codesync.java.super.interface")).setOrderIndexAs(250));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_MODIFIABLE)
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(MODIFIER).setLabelAs(getLabel("codesync.java.modifier")).setOrderIndexAs(1000))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ANNOTATION)
					.setIconAs(getImagePathFromPublicResources(IMG_ANNOTATION)).setLabelAs(getLabel("codesync.java.annotation")).setOrderIndexAs(2000));
		
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
	
	private TypeDescriptor createNodeTypeDescriptor(String type) {
		return CodeSyncPlugin.getInstance().createCodeSyncTypeDescriptor(type);
	}
	
	private String getLabel(String key) {
		return ResourcesPlugin.getInstance().getMessage(key);
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
		// messages come from .resources
	}
	
}