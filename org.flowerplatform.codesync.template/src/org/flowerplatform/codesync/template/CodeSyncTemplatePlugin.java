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
package org.flowerplatform.codesync.template;

import static org.flowerplatform.codesync.CodeSyncConstants.FILE;
import static org.flowerplatform.codesync.CodeSyncConstants.FOLDER;
import static org.flowerplatform.codesync.CodeSyncConstants.SRC_DIR;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.GEN;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATES;
import static org.flowerplatform.core.CoreConstants.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.ModelAdapterSet;
import org.flowerplatform.codesync.template.adapter.GeneratedFileModelAdapter;
import org.flowerplatform.codesync.template.adapter.GeneratedFolderModelAdapter;
import org.flowerplatform.codesync.template.config_loader.TemplatesCodeSyncConfigLoader;
import org.flowerplatform.codesync.template.controller.GeneratedFileSyncPropertiesProvider;
import org.flowerplatform.codesync.template.controller.GeneratedFileSyncPropertySetter;
import org.flowerplatform.codesync.type_provider.FileTypeProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor;
import org.flowerplatform.util.UtilConstants;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncTemplatePlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncTemplatePlugin instance;
	
	public static CodeSyncTemplatePlugin getInstance() {
		return instance;
	}
	
	private CodeSyncTemplateService codeSyncTemplateService = new CodeSyncTemplateService();
	
	public CodeSyncTemplateService getCodeSyncTemplateService() {
		return codeSyncTemplateService;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
		
		CorePlugin.getInstance().getServiceRegistry().registerService("codeSyncTemplateService", codeSyncTemplateService);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(INNER_TEMPLATE)
			.addCategory(UtilConstants.CATEGORY_HAS_LOCAL_TYPE_DESCRIPTOR_REGISTRY)
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(INNER_TEMPLATES));
		
		GeneratedFileSyncPropertiesProvider provider = new GeneratedFileSyncPropertiesProvider();
		GeneratedFileSyncPropertySetter setter = new GeneratedFileSyncPropertySetter();
		addSyncPropertiesControllers(FOLDER, provider, setter);
		addSyncPropertiesControllers(FILE, provider, setter);
		
		CodeSyncPlugin.getInstance().addModelAdapterSet(GEN, new ModelAdapterSet()
			.addModelAdapter(SRC_DIR, new GeneratedFolderModelAdapter())
			.addModelAdapter(FOLDER, new GeneratedFolderModelAdapter())
			.addModelAdapter(FILE, new GeneratedFileModelAdapter())
			.setTypeProvider(new FileTypeProvider()));
		
		// register a config loader
		CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getCodeSyncConfigLoaders().add(new TemplatesCodeSyncConfigLoader());		
	}
	
	private void addSyncPropertiesControllers(String type, IPropertiesProvider provider, IPropertySetter setter) {
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(type)
			.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, provider)
			.addAdditiveController(CoreConstants.PROPERTY_SETTER, setter);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		instance = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// messages come from .resources
	}
	
}
