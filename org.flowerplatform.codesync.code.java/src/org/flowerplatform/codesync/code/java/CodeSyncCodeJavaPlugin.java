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

import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.ICON;
import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.NAME;
import static org.flowerplatform.codesync.adapter.AbstractModelAdapter.MODEL_ADAPTER_RIGHT;
import static org.flowerplatform.codesync.code.CodeSyncCodePlugin.FILE;
import static org.flowerplatform.codesync.code.CodeSyncCodePlugin.FOLDER;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_ANNOTATION;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_FIELD;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_FILE;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_LOCAL_VAR;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_METHOD;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_PACKAGE;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_TYPE_ANNOTATION;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_TYPE_CLASS;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_TYPE_ENUM;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_TYPE_INTERFACE;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_WIZ_PACKAGE;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_WIZ_TYPE_ANNOTATION;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_WIZ_TYPE_CLASS;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_WIZ_TYPE_ENUM;
import static org.flowerplatform.codesync.code.java.JavaPropertiesConstants.IMG_WIZ_TYPE_INTERFACE;
import static org.flowerplatform.codesync.code.java.adapter.JavaAnnotationModelAdapter.ANNOTATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaAnnotationTypeMemberDeclarationModelAdapter.ANNOTATION_MEMBER;
import static org.flowerplatform.codesync.code.java.adapter.JavaAttributeModelAdapter.ATTRIBUTE;
import static org.flowerplatform.codesync.code.java.adapter.JavaEnumConstantDeclarationModelAdapter.ENUM_CONSTANT;
import static org.flowerplatform.codesync.code.java.adapter.JavaExpressionModelAdapter.ENUM_CONSTANT_ARGUMENT;
import static org.flowerplatform.codesync.code.java.adapter.JavaExpressionModelAdapter.SUPER_INTERFACE;
import static org.flowerplatform.codesync.code.java.adapter.JavaMemberValuePairModelAdapter.MEMBER_VALUE_PAIR;
import static org.flowerplatform.codesync.code.java.adapter.JavaModifierModelAdapter.MODIFIER;
import static org.flowerplatform.codesync.code.java.adapter.JavaOperationModelAdapter.OPERATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaParameterModelAdapter.PARAMETER;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.ANNOTATION_TYPE;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.CLASS;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.ENUM;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.INTERFACE;
import static org.flowerplatform.codesync.feature_provider.FeatureProvider.FEATURE_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.remote.AddChildDescriptor.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.DROP_DOWN_LIST;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.CodeSyncPropertiesConstants;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.code.adapter.FolderModelAdapter;
import org.flowerplatform.codesync.code.feature_provider.FolderFeatureProvider;
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
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAnnotationTypeMemberDeclarationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaAttributeFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaEnumConstantDeclarationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaFileFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaMemberValuePairFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaOperationFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaParameterFeatureProvider;
import org.flowerplatform.codesync.code.java.feature_provider.JavaTypeDeclarationFeatureProvider;
import org.flowerplatform.codesync.code.java.type_provider.JavaTypeProvider;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor;
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
		
		MemberOfChildCategoryDescriptor childrenDescriptor = new MemberOfChildCategoryDescriptor(CodeSyncPropertiesConstants.CHILDREN);
	
		createNodeTypeDescriptor(FOLDER, new FolderModelAdapter(), new FolderFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, childrenDescriptor)
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FOLDER).setLabelAs(getLabel("codesync.java.package"))
				.setIconAs(getResourceUrl(IMG_WIZ_PACKAGE)).setOrderIndexAs(10))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FILE).setLabelAs(getLabel("codesync.java.file"))
				.setIconAs(getResourceUrl(IMG_FILE)).setOrderIndexAs(20))
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICON, getResourcePath(IMG_PACKAGE)));
	
		String category_canContainTypes = "category.codesync-can-contain-types";
		
		createNodeTypeDescriptor(FILE, new JavaFileModelAdapter(), new JavaFileFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, childrenDescriptor)
		.addCategory(category_canContainTypes)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICON, getResourcePath(IMG_FILE)));
		
		PropertyDescriptor returnType = new PropertyDescriptor().setNameAs(JavaPropertiesConstants.TYPED_ELEMENT_TYPE).setReadOnlyAs(false);
		JavaTypeDeclarationModelAdapter typeModelAdapter = new JavaTypeDeclarationModelAdapter();
		JavaTypeDeclarationFeatureProvider typeFeatureProvider = new JavaTypeDeclarationFeatureProvider();
		
		String category_type = "category.codesync-type";
		String category_hasSuperInterfaces = "category.codesync-has-super-interfaces";
		String category_modifiable = "category.codesync-modifiable";
		MemberOfChildCategoryDescriptor typeMembersDescriptor = new MemberOfChildCategoryDescriptor(JavaPropertiesConstants.TYPE_MEMBERS);
		
		
		createNodeTypeDescriptor(CLASS, typeModelAdapter, typeFeatureProvider)
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
		.addCategory(category_type)
		.addCategory(category_canContainTypes)
		.addCategory(category_hasSuperInterfaces)
		.addCategory(category_modifiable)
		.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICON, getResourcePath(IMG_TYPE_CLASS)));
		
		createNodeTypeDescriptor(INTERFACE, typeModelAdapter, typeFeatureProvider)
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
		.addCategory(category_type)
		.addCategory(category_canContainTypes)
		.addCategory(category_hasSuperInterfaces)
		.addCategory(category_modifiable)
		.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICON, getResourcePath(IMG_TYPE_INTERFACE)));
	
		createNodeTypeDescriptor(ENUM, typeModelAdapter, typeFeatureProvider)
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
		.addCategory(category_type)
		.addCategory(category_canContainTypes)
		.addCategory(category_hasSuperInterfaces)
		.addCategory(category_modifiable)
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ENUM_CONSTANT).setLabelAs(getLabel("codesync.java.enum.constant"))
				.setIconAs(getResourceUrl(IMG_FIELD)).setOrderIndexAs(10))
		.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICON, getResourcePath(IMG_TYPE_ENUM)));
	
		createNodeTypeDescriptor(ANNOTATION_TYPE, typeModelAdapter, typeFeatureProvider)
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
		.addCategory(category_type)
		.addCategory(category_canContainTypes)
		.addCategory(category_modifiable)
		.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICON, getResourcePath(IMG_TYPE_ANNOTATION)));
		
		createNodeTypeDescriptor(ATTRIBUTE, new JavaAttributeModelAdapter(), new JavaAttributeFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
		.addCategory(category_modifiable)
		.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICON, getResourcePath(IMG_FIELD)))
		.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
		
		createNodeTypeDescriptor(OPERATION, new JavaOperationModelAdapter(), new JavaOperationFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
		.addCategory(category_modifiable)
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(PARAMETER).setLabelAs(getLabel("codesync.java.parameter"))
				.setIconAs(getResourceUrl(IMG_LOCAL_VAR)).setOrderIndexAs(10))
		.addAdditiveController(PROPERTIES_PROVIDER, new JavaIconPropertyProvider(ICON, getResourcePath(IMG_METHOD)))
		.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
	
		createNodeTypeDescriptor(ENUM_CONSTANT, new JavaEnumConstantDeclarationModelAdapter(), new JavaEnumConstantDeclarationFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ENUM_CONSTANT_ARGUMENT).setLabelAs(getLabel("codesync.java.enum.constant.argument")).setOrderIndexAs(10))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ANNOTATION).setLabelAs(getLabel("codesync.java.annotation"))
				.setIconAs(getResourceUrl(IMG_ANNOTATION)).setOrderIndexAs(20))
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICON, getResourcePath(IMG_FIELD)));
		
		createNodeTypeDescriptor(ENUM_CONSTANT_ARGUMENT, new JavaExpressionModelAdapter(ENUM_CONSTANT_ARGUMENT), new NodeFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(JavaPropertiesConstants.ENUM_CONSTANT_ARGUMENTS));
	
		createNodeTypeDescriptor(ANNOTATION_MEMBER, new JavaAnnotationTypeMemberDeclarationModelAdapter(), new JavaAnnotationTypeMemberDeclarationFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, typeMembersDescriptor)
		.addCategory(category_modifiable)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICON, getResourcePath(IMG_METHOD)))
		.addAdditiveController(PROPERTY_DESCRIPTOR, returnType);
		
		MemberOfChildCategoryDescriptor modifiers = new MemberOfChildCategoryDescriptor(JavaPropertiesConstants.MODIFIERS);
		
		createNodeTypeDescriptor(JavaAnnotationModelAdapter.ANNOTATION, new JavaAnnotationModelAdapter(), new JavaAnnotationFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, modifiers)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICON, getResourcePath(IMG_ANNOTATION)));
	
		createNodeTypeDescriptor(MEMBER_VALUE_PAIR, new JavaMemberValuePairModelAdapter(), new JavaMemberValuePairFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(JavaPropertiesConstants.ANNOTATION_VALUES));
		
		List<String> possibleValuesForModifiers = Arrays.asList(
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
				"strictfp");
		createNodeTypeDescriptor(MODIFIER, new JavaModifierModelAdapter(), new NodeFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, modifiers)
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTypeAs(DROP_DOWN_LIST).setPossibleValuesAs(possibleValuesForModifiers).setReadOnlyAs(false));
		
		createNodeTypeDescriptor(PARAMETER, new JavaParameterModelAdapter(), new JavaParameterFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(JavaPropertiesConstants.OPERATION_PARAMETERS))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(MODIFIER).setLabelAs(getLabel("codesync.java.modifier")).setOrderIndexAs(10))
		.addAdditiveController(PROPERTY_DESCRIPTOR, returnType)
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICON, getResourcePath(IMG_LOCAL_VAR)));
		
		createNodeTypeDescriptor(SUPER_INTERFACE, new JavaExpressionModelAdapter(SUPER_INTERFACE), new NodeFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(JavaPropertiesConstants.SUPER_INTERFACES));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(category_canContainTypes)
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(CLASS).setLabelAs(getLabel("codesync.java.type.class"))
				.setIconAs(getResourceUrl(IMG_WIZ_TYPE_CLASS)).setOrderIndexAs(10000))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(INTERFACE).setLabelAs(getLabel("codesync.java.type.interface"))
				.setIconAs(getResourceUrl(IMG_WIZ_TYPE_INTERFACE)).setOrderIndexAs(20000))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ENUM).setLabelAs(getLabel("codesync.java.type.enum"))
				.setIconAs(getResourceUrl(IMG_WIZ_TYPE_ENUM)).setOrderIndexAs(30000))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ANNOTATION_TYPE).setLabelAs(getLabel("codesync.java.type.annotation"))
				.setIconAs(getResourceUrl(IMG_WIZ_TYPE_ANNOTATION)).setOrderIndexAs(40000));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(category_type)
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ATTRIBUTE)
				.setIconAs(getResourceUrl(IMG_FIELD)).setLabelAs(getLabel("codesync.java.field")).setOrderIndexAs(100))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(OPERATION)
				.setIconAs(getResourceUrl(IMG_METHOD)).setLabelAs(getLabel("codesync.java.method")).setOrderIndexAs(200));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(category_hasSuperInterfaces)
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(SUPER_INTERFACE).setLabelAs(getLabel("codesync.java.super.interface")).setOrderIndexAs(250));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(category_modifiable)
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(MODIFIER).setLabelAs(getLabel("codesync.java.modifier")).setOrderIndexAs(1000))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ANNOTATION)
				.setIconAs(getResourceUrl(IMG_ANNOTATION)).setLabelAs(getLabel("codesync.java.annotation")).setOrderIndexAs(2000));
		
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
		descriptor.addSingleController(MODEL_ADAPTER_RIGHT, modelAdapterRight);
		descriptor.addSingleController(FEATURE_PROVIDER, featureProvider);
		return descriptor;
	}
	
	private String getLabel(String key) {
		return CodeSyncCodeJavaPlugin.getInstance().getMessage(key);
	}

	public FolderModelAdapter getFolderModelAdapter() {
		return folderModelAdapter;
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		INSTANCE = null;
	}

}
