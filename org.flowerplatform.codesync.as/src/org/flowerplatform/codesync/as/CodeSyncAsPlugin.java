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
package org.flowerplatform.codesync.as;

import static org.flowerplatform.codesync.CodeSyncConstants.FEATURE_PROVIDER;
import static org.flowerplatform.codesync.CodeSyncConstants.MODEL_ADAPTER_ANCESTOR;
import static org.flowerplatform.codesync.CodeSyncConstants.MODEL_ADAPTER_LEFT;
import static org.flowerplatform.codesync.CodeSyncConstants.MODEL_ADAPTER_RIGHT;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CATEGORY_FUNCTION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CATEGORY_VARIABLE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CONST;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.FUNCTION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.FUNCTION_PARAMETERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.GETTER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_FUNCTION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_INTERFACE_REALIZATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_TYPE_CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_TYPE_INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_VARIABLE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAGS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG_ATTRIBUTE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG_ATTRIBUTES;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SETTER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.STATEMENTS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACES;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TECHNOLOGY;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VARIABLE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.getImagePath;
import static org.flowerplatform.codesync.code.CodeSyncCodeConstants.FILE;
import static org.flowerplatform.codesync.code.CodeSyncCodeConstants.FOLDER;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsClassModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsFileModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsFunctionModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsInterfaceModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsMetaTagAttributeModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsMetaTagModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsModifierModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsParameterModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsReferenceModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsVariableModelAdapter;
import org.flowerplatform.codesync.as.feature_provider.AsClassFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsFileFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsFunctionFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsInterfaceFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsMetaTagAttributeFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsMetaTagFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsParameterFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsVariableFeatureProvider;
import org.flowerplatform.codesync.as.line_information_provider.AsFunctionLineProvider;
import org.flowerplatform.codesync.as.type_provider.AsTypeProvider;
import org.flowerplatform.codesync.code.adapter.FolderModelAdapter;
import org.flowerplatform.codesync.code.feature_provider.FolderFeatureProvider;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncAsPlugin extends AbstractFlowerJavaPlugin {

protected static CodeSyncAsPlugin INSTANCE;
	
	public static CodeSyncAsPlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CodeSyncPlugin.getInstance().addTypeProvider(TECHNOLOGY, new AsTypeProvider());
		CodeSyncPlugin.getInstance().addLineProvider(new AsFunctionLineProvider());
		
		createNodeTypeDescriptor(FOLDER, new FolderModelAdapter(), new FolderFeatureProvider());
		createNodeTypeDescriptor(FILE, new AsFileModelAdapter(), new AsFileFeatureProvider());
	
		MemberOfChildCategoryDescriptor statementsChildDescriptor = new MemberOfChildCategoryDescriptor(STATEMENTS);
		
		createNodeTypeDescriptor(CLASS, new AsClassModelAdapter(), new AsClassFeatureProvider())
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, statementsChildDescriptor)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_TYPE_CLASS)));
		
		createNodeTypeDescriptor(INTERFACE, new AsInterfaceModelAdapter(), new AsInterfaceFeatureProvider())
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, statementsChildDescriptor)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_TYPE_INTERFACE)));
		
		AsReferenceModelAdapter referenceModelAdapter = new AsReferenceModelAdapter();
		NodeFeatureProvider referenceFeatureProvider = new NodeFeatureProvider();
		
		createNodeTypeDescriptor(SUPER_INTERFACE, referenceModelAdapter, referenceFeatureProvider)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(SUPER_INTERFACES))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_INTERFACE_REALIZATION)));
		
		createNodeTypeDescriptor(META_TAG, new AsMetaTagModelAdapter(), new AsMetaTagFeatureProvider())
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(META_TAGS));
		
		createNodeTypeDescriptor(META_TAG_ATTRIBUTE, new AsMetaTagAttributeModelAdapter(), new AsMetaTagAttributeFeatureProvider())
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(META_TAG_ATTRIBUTES));
		
		createCategoryTypeDescriptor(CATEGORY_VARIABLE, new AsVariableModelAdapter(), new AsVariableFeatureProvider())
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, statementsChildDescriptor)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_VARIABLE)));
		
		createNodeTypeDescriptor(VARIABLE).addCategory(CATEGORY_VARIABLE);
		createNodeTypeDescriptor(CONST).addCategory(CATEGORY_VARIABLE);
		
		createCategoryTypeDescriptor(CATEGORY_FUNCTION, new AsFunctionModelAdapter(), new AsFunctionFeatureProvider())
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, statementsChildDescriptor)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_FUNCTION)));
	
		createNodeTypeDescriptor(FUNCTION).addCategory(CATEGORY_FUNCTION);
		createNodeTypeDescriptor(GETTER).addCategory(CATEGORY_FUNCTION);
		createNodeTypeDescriptor(SETTER).addCategory(CATEGORY_FUNCTION);
		
		createNodeTypeDescriptor(MODIFIER, new AsModifierModelAdapter(), new NodeFeatureProvider())
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(MODIFIERS));
		
		createNodeTypeDescriptor(PARAMETER, new AsParameterModelAdapter(), new AsParameterFeatureProvider())
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(FUNCTION_PARAMETERS));
	}
	
	private TypeDescriptor createNodeTypeDescriptor(String type) {
		return createNodeTypeDescriptor(type, null, null);
	}
	
	private TypeDescriptor createNodeTypeDescriptor(String type, AbstractModelAdapter modelAdapterRight, FeatureProvider featureProvider) {
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(type);
		descriptor.addCategory(CodeSyncConstants.CATEGORY_CODESYNC);
		if (modelAdapterRight != null && featureProvider != null) {
			registerAdapterAndFeatureProvider(descriptor, modelAdapterRight, featureProvider);
		}
		return descriptor;
	}
	
	private TypeDescriptor createCategoryTypeDescriptor(String category, AbstractModelAdapter modelAdapterRight, FeatureProvider featureProvider) { 
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(category);
		registerAdapterAndFeatureProvider(descriptor, modelAdapterRight, featureProvider);
		return descriptor;
	}
	
	private void registerAdapterAndFeatureProvider(TypeDescriptor descriptor, AbstractModelAdapter modelAdapterRight, FeatureProvider featureProvider) {
		descriptor.addSingleController(MODEL_ADAPTER_LEFT, modelAdapterRight);
		descriptor.addSingleController(MODEL_ADAPTER_ANCESTOR, modelAdapterRight);
		descriptor.addSingleController(MODEL_ADAPTER_RIGHT, modelAdapterRight);
		descriptor.addSingleController(FEATURE_PROVIDER, featureProvider);
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
}