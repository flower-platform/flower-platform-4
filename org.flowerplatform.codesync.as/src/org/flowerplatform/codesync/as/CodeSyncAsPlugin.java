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

import static org.flowerplatform.codesync.CodeSyncConstants.FILE;
import static org.flowerplatform.codesync.CodeSyncConstants.FOLDER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.ACTIONSCRIPT;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CATEGORY_FUNCTION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CATEGORY_VARIABLE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CONST;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.EXTENSION_AS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.EXTENSION_MXML;
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
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VARIABLE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.getImagePath;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.file.FileModelAdapterSet;
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
import org.flowerplatform.codesync.as.line_information_provider.AsFunctionLineProvider;
import org.flowerplatform.codesync.as.type_provider.AsTypeProvider;
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

	protected static CodeSyncAsPlugin instance;
	
	public static CodeSyncAsPlugin getInstance() {
		return instance;
	}
	
	/**
	 * @author see class
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		instance = this;
		
		CodeSyncPlugin.getInstance().addTechnologyForExtension(EXTENSION_AS, ACTIONSCRIPT);
		CodeSyncPlugin.getInstance().addTechnologyForExtension(EXTENSION_MXML, ACTIONSCRIPT);
		
		AsReferenceModelAdapter referenceModelAdapter = new AsReferenceModelAdapter();
		AsVariableModelAdapter varModelAdapter = new AsVariableModelAdapter();
		AsFunctionModelAdapter asFunctionModelAdapter = new AsFunctionModelAdapter();
		
		CodeSyncPlugin.getInstance().addModelAdapterSet(ACTIONSCRIPT, new FileModelAdapterSet()
				.setTypeProvider(new AsTypeProvider())
				.setLineProvider(new AsFunctionLineProvider())
				.setFileModelAdapterDelegate(new AsFileModelAdapter())
				.addModelAdapter(CLASS, new AsClassModelAdapter())
				.addModelAdapter(INTERFACE, new AsInterfaceModelAdapter())
				.addModelAdapter(SUPER_INTERFACE, referenceModelAdapter)
				.addModelAdapter(META_TAG, new AsMetaTagModelAdapter())
				.addModelAdapter(META_TAG_ATTRIBUTE, new AsMetaTagAttributeModelAdapter())
				.addModelAdapter(VARIABLE, varModelAdapter)
				.addModelAdapter(CONST, varModelAdapter)
				.addModelAdapter(FUNCTION, asFunctionModelAdapter)
				.addModelAdapter(GETTER, asFunctionModelAdapter)
				.addModelAdapter(SETTER, asFunctionModelAdapter)
				.addModelAdapter(MODIFIER, new AsModifierModelAdapter())
				.addModelAdapter(PARAMETER, new AsParameterModelAdapter()));
		
		createNodeTypeDescriptor(FOLDER);
		createNodeTypeDescriptor(FILE);
		
		MemberOfChildCategoryDescriptor statementsChildDescriptor = new MemberOfChildCategoryDescriptor(STATEMENTS);
		
		createNodeTypeDescriptor(CLASS)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, statementsChildDescriptor)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_TYPE_CLASS)));
		
		createNodeTypeDescriptor(INTERFACE)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, statementsChildDescriptor)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_TYPE_INTERFACE)));
		
		createNodeTypeDescriptor(SUPER_INTERFACE)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(SUPER_INTERFACES))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_INTERFACE_REALIZATION)));
		
		createNodeTypeDescriptor(META_TAG)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(META_TAGS));
		
		createNodeTypeDescriptor(META_TAG_ATTRIBUTE)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(META_TAG_ATTRIBUTES));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_VARIABLE)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, statementsChildDescriptor)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_VARIABLE)));
		
		createNodeTypeDescriptor(VARIABLE).addCategory(CATEGORY_VARIABLE);
		createNodeTypeDescriptor(CONST).addCategory(CATEGORY_VARIABLE);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_FUNCTION)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, statementsChildDescriptor)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_FUNCTION)));
	
		createNodeTypeDescriptor(FUNCTION).addCategory(CATEGORY_FUNCTION);
		createNodeTypeDescriptor(GETTER).addCategory(CATEGORY_FUNCTION);
		createNodeTypeDescriptor(SETTER).addCategory(CATEGORY_FUNCTION);
		
		createNodeTypeDescriptor(MODIFIER)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(MODIFIERS));
		
		createNodeTypeDescriptor(PARAMETER)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(FUNCTION_PARAMETERS));
	}
	
	private TypeDescriptor createNodeTypeDescriptor(String type) {
		return CodeSyncPlugin.getInstance().createCodeSyncTypeDescriptor(type);
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		instance = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
}