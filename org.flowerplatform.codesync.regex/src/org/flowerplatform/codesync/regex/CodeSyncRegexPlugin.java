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
import static org.flowerplatform.util.UtilConstants.FEATURE_PROPERTY_DESCRIPTORS;
import static org.flowerplatform.util.UtilConstants.PROPERTY_EDITOR_TYPE_STRING;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.regex.controller.RegexController;
import org.flowerplatform.codesync.regex.controller.RegexMatchesChildrenProvider;
import org.flowerplatform.codesync.regex.controller.VirtualRegexChildrenProvider;
import org.flowerplatform.codesync.regex.remote.CodeSyncRegexService;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.GenericDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class CodeSyncRegexPlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncRegexPlugin instance;
	
	public static CodeSyncRegexPlugin getInstance() {
		return instance;
	}
		
	private CodeSyncRegexService regexService = new CodeSyncRegexService();
	
	private Map<String, RegexAction> actions = new HashMap<String, RegexAction>();
		
	public Map<String, RegexAction> getActions() {
		return actions;
	}
	
	/**
	 *@author see class
	 **/
	public void addRegexAction(RegexAction action) {
		actions.put(action.getName(), action);
	}
		
	/**
	 *@author see class
	 **/
	public void clearRegexActionsAndCompiledRegexConfigurations() {
		actions.clear();
	}
	
	public CodeSyncRegexService getRegexService() {
		return regexService;
	}

	/**
	 *@author see class
	 **/
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		instance = this;
				
		CorePlugin.getInstance().getServiceRegistry().registerService("codeSyncRegexService", regexService);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_CONFIG_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexes.root")))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(REGEX_TYPE)
					.setIconAs(ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(REGEX_MACRO_TYPE)
					.setIconAs(ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/percent.png")));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MACRO_TYPE)
			.addCategory(CATEGORY_REGEX)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance()
					.getResourceUrl("images/codesync.regex/percent.png")));
			
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_TYPE)
			.addCategory(CATEGORY_REGEX)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION).setTypeAs(REGEX_ACTIONS_DESCRIPTOR_TYPE).setOrderIndexAs(40));
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_REGEX)
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexController())
			.addAdditiveController(PROPERTY_SETTER, new RegexController())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(REGEX_WITH_MACROS).setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setOrderIndexAs(20))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FULL_REGEX).setTypeAs(PROPERTY_EDITOR_TYPE_STRING)
					.setReadOnlyAs(true).setOrderIndexAs(30))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setOrderIndexAs(10));
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)			
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(REGEX_EXTENSION, "fpp", "mindmap", true));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)			
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(CodeSyncRegexConstants.REGEX_MATCH_EXTENSION, "fpp", "mindmap", true));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MATCHES_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexMatches.root")))
			.addAdditiveController(CHILDREN_PROVIDER, new RegexMatchesChildrenProvider());
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(VIRTUAL_REGEX_TYPE)
			.addSingleController(CoreConstants.MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + CoreConstants.BASE_RENDERER_TEXT, new GenericDescriptor(REGEX_NAME))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setTypeAs(PROPERTY_EDITOR_TYPE_STRING)
					.setReadOnlyAs(true).setOrderIndexAs(10))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FULL_REGEX).setTypeAs(PROPERTY_EDITOR_TYPE_STRING)
					.setReadOnlyAs(true).setOrderIndexAs(20))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addAdditiveController(CHILDREN_PROVIDER, new VirtualRegexChildrenProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MATCH_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/brick.png")))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setTypeAs(PROPERTY_EDITOR_TYPE_STRING)
					.setOrderIndexAs(10))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FULL_REGEX).setTypeAs(PROPERTY_EDITOR_TYPE_STRING)
					.setReadOnlyAs(true).setOrderIndexAs(20))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(START).setTypeAs(PROPERTY_EDITOR_TYPE_STRING)
					.setReadOnlyAs(true).setOrderIndexAs(30))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(END).setTypeAs(PROPERTY_EDITOR_TYPE_STRING)
					.setReadOnlyAs(true).setOrderIndexAs(40));
			
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(VIRTUAL_REGEX_TYPE);
				
		// TODO: TO DELETE (added only for RegEx tests)
		addRegexAction(new RegexAction() {
			@Override
			public void executeAction(RegexProcessingSession param) {				
			}
		} .setName("action1").setDescription("description 1"));
		
		// TODO: TO DELETE (added only for RegEx tests)
		addRegexAction(new RegexAction() {
			@Override
			public void executeAction(RegexProcessingSession param) {				
			}
		} .setName("action2").setDescription("description 2"));
	}	
	
	/**
	 *@author see class
	 **/
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		instance = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// messages come from .resources
	}
	
	/**
	 *@author see class
	 **/
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
