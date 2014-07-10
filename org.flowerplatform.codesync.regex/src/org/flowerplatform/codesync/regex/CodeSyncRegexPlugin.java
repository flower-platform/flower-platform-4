package org.flowerplatform.codesync.regex;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.CATEGORY_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.END;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.FULL_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_ACTIONS_DESCRIPTOR_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIG_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXTENSION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MACRO_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCHES_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_NAME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_SCHEME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_WITH_MACROS;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.VIRTUAL_REGEX_TYPE;
import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_STRING;
import static org.flowerplatform.core.CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.regex.controller.RegexController;
import org.flowerplatform.codesync.regex.controller.RegexMatchesChildrenProvider;
import org.flowerplatform.codesync.regex.controller.VirtualRegexChildrenProvider;
import org.flowerplatform.codesync.regex.controller.VirtualRegexResourceHandler;
import org.flowerplatform.codesync.regex.remote.CodeSyncRegexService;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.GenericValueDescriptor;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class CodeSyncRegexPlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncRegexPlugin INSTANCE;
	
	public static CodeSyncRegexPlugin getInstance() {
		return INSTANCE;
	}
		
	private CodeSyncRegexService regexService = new CodeSyncRegexService();
	
	private Map<String, RegexAction> actions = new HashMap<String, RegexAction>();
		
	public Map<String, RegexAction> getActions() {
		return actions;
	}
	
	public void addRegexAction(RegexAction action) {
		actions.put(action.getName(), action);
	}
		
	public void clearRegexActionsAndCompiledRegexConfigurations() {
		actions.clear();
	}
	
	public CodeSyncRegexService getRegexService() {
		return regexService;
	}

	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
				
		CorePlugin.getInstance().getServiceRegistry().registerService("codeSyncRegexService", regexService);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_CONFIG_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexes.root")))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(REGEX_TYPE).setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.regex")).setIconAs(ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(REGEX_MACRO_TYPE).setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.macro")).setIconAs(ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/percent.png")));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MACRO_TYPE)
			.addCategory(CATEGORY_REGEX)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/percent.png")));
			
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_TYPE)
			.addCategory(CATEGORY_REGEX)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(ACTION).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.action")).setTypeAs(REGEX_ACTIONS_DESCRIPTOR_TYPE).setContributesToCreationAs(true).setMandatoryAs(true).setOrderIndexAs(40));
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_REGEX)
			.addSingleController(PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(NAME))			
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexController())
			.addAdditiveController(PROPERTY_SETTER, new RegexController())
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(REGEX_WITH_MACROS).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.regex")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true).setOrderIndexAs(20))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FULL_REGEX).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.full")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setReadOnlyAs(true).setOrderIndexAs(30))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.name")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true).setOrderIndexAs(10));
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)			
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(REGEX_EXTENSION, "fpp", "mindmap", true));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)			
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(CodeSyncRegexConstants.REGEX_MATCH_EXTENSION, "fpp", "mindmap", true));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MATCHES_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexMatches.root")))
			.addAdditiveController(CHILDREN_PROVIDER, new RegexMatchesChildrenProvider());
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(VIRTUAL_REGEX_TYPE)
			.addSingleController(PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(REGEX_NAME))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.name")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(10))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FULL_REGEX).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.full")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setReadOnlyAs(true).setOrderIndexAs(20))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addAdditiveController(CHILDREN_PROVIDER, new VirtualRegexChildrenProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MATCH_TYPE)
			.addSingleController(PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(NAME))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/brick.png")))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.name")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(10))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FULL_REGEX).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.full")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(20))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(START).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.start")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(30))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(END).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.end")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(40));
			
		CorePlugin.getInstance().getResourceService().addResourceHandler(REGEX_SCHEME, new VirtualRegexResourceHandler());
				
		// TODO: TO DELETE (added only for RegEx tests)
		addRegexAction(new RegexAction() {
			@Override
			public void executeAction(RegexProcessingSession param) {				
			}
		}.setName("action1").setDescription("description 1"));
		
		// TODO: TO DELETE (added only for RegEx tests)
		addRegexAction(new RegexAction() {
			@Override
			public void executeAction(RegexProcessingSession param) {				
			}
		}.setName("action2").setDescription("description 2"));
	}	
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// messages come from .resources
	}
	
	public List<Node> getChildren(Node resourceNode, String type) {		
		List<Node> children = CorePlugin.getInstance().getNodeService().getChildren(resourceNode, new ServiceContext<>(CorePlugin.getInstance().getNodeService()));
		
		List<Node> regexes = new ArrayList<>();		
		for (Node child : children) {
			if (child.getType().equals(type)) {
				regexes.add(child);
			}
		}
		return regexes;
	}

}