package org.flowerplatform.codesync.template;

import static org.flowerplatform.codesync.CodeSyncConstants.FILE;
import static org.flowerplatform.codesync.CodeSyncConstants.FOLDER;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATES;
import static org.flowerplatform.core.CoreConstants.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.ModelAdapterSet;
import org.flowerplatform.codesync.template.adapter.GeneratedFileModelAdapter;
import org.flowerplatform.codesync.template.adapter.GeneratedFolderModelAdapter;
import org.flowerplatform.codesync.template.controller.GeneratedFileSyncPropertiesProvider;
import org.flowerplatform.codesync.template.controller.GeneratedFileSyncPropertySetter;
import org.flowerplatform.codesync.type_provider.FileTypeProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor;
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
			.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(INNER_TEMPLATES));
		
		GeneratedFileSyncPropertiesProvider provider = new GeneratedFileSyncPropertiesProvider();
		GeneratedFileSyncPropertySetter setter = new GeneratedFileSyncPropertySetter();
		addSyncPropertiesControllers(FOLDER, provider, setter);
		addSyncPropertiesControllers(FILE, provider, setter);
		
		CodeSyncPlugin.getInstance().addModelAdapterSet("gen", new ModelAdapterSet()
			.addModelAdapter(FOLDER, new GeneratedFolderModelAdapter())
			.addModelAdapter(FILE, new GeneratedFileModelAdapter())
			.setTypeProvider(new FileTypeProvider()));
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
