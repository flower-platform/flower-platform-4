package org.flowerplatform.codesync.template;

import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.CODESYNC_TEMPLATE_ROOT;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATES;
import static org.flowerplatform.core.CoreConstants.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;

import org.flowerplatform.core.CorePlugin;
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
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CODESYNC_TEMPLATE_ROOT);
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(INNER_TEMPLATE)
				.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(INNER_TEMPLATES));
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
