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

import static org.flowerplatform.codesync.CodeSyncConstants.CATEGORY_MODEL;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_CREATE_NODE_NEW_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_CREATE_NODE_PROPERTIES;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_ENTER_STATE_IF_PROPERTY_SET;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_IS_CONTAINMENT;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_IS_LIST;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_KEY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_VALID_STATES_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_NODE_TO_CURRENT_STATE_ACTION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CHECK_STATE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CLEAR_SPECIFIC_INFO;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_DECREASE_NESTING_LEVEL;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ENTER_STATE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_EXIT_STATE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_INCREASE_NESTING_LEVEL;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.CATEGORY_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.CATEGORY_REGEX_ACTION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.END;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.FULL_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEXES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_ACTION_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIGS_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIG_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MODEL_TREE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXTENSION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCHES_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_EXTENSION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MODEL_TREE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_NAME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_RESULT_EXTENSION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_RESULT_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TECHNOLOGY_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_WITH_MACROS;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.VIRTUAL_REGEX_TYPE;
import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.CATEGORY_CONFIG_SETTINGS;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.CONFIG_NODE_PROCESSOR;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REPOSITORY_TYPE;
import static org.flowerplatform.util.UtilConstants.FEATURE_PROPERTY_DESCRIPTORS;
import static org.flowerplatform.util.UtilConstants.PROPERTY_EDITOR_TYPE_BOOLEAN;
import static org.flowerplatform.util.UtilConstants.PROPERTY_EDITOR_TYPE_STRING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.regex.controller.CodeSyncRegexRepositoryChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexActionController;
import org.flowerplatform.codesync.regex.controller.RegexConfigsChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.RegexController;
import org.flowerplatform.codesync.regex.controller.RegexExpectedMatchesController;
import org.flowerplatform.codesync.regex.controller.RegexExpectedModelTreeController;
import org.flowerplatform.codesync.regex.controller.RegexMatchesChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexModelTreeController;
import org.flowerplatform.codesync.regex.controller.RegexTechnologyChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexTechnologyPropertiesProvider;
import org.flowerplatform.codesync.regex.controller.RegexTestFileChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexTestFilePropertiesProvider;
import org.flowerplatform.codesync.regex.controller.RegexTestFilesChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexTestMatchesController;
import org.flowerplatform.codesync.regex.controller.RegexWithActionsProcessor;
import org.flowerplatform.codesync.regex.controller.RegexesController;
import org.flowerplatform.codesync.regex.controller.VirtualRegexChildrenProvider;
import org.flowerplatform.codesync.regex.controller.action.AddAsChildOfStateNodeConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.action.AttachSpecificInfoConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.action.CheckStateNodeConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.action.ClearSpecificInfoConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.action.CreateNodeConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.action.DecreaseNestingLevelConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.action.EnterStateConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.action.ExitStateConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.action.IncreaseNestingLevelConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.action.KeepSpecificInfoConfigurationProcessor;
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
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REPOSITORY_TYPE)
			.addAdditiveController(CHILDREN_PROVIDER, new CodeSyncRegexRepositoryChildrenProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_CONFIG_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexes.root")))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(REGEX_TYPE).setIconAs(ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addSingleController(CONFIG_NODE_PROCESSOR, new RegexConfigurationProcessor());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_TYPE)
			.addCategory(CATEGORY_REGEX)
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addSingleController(CONFIG_NODE_PROCESSOR, new RegexWithActionsProcessor())
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_CREATE_NODE).setOrderIndexAs(100))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_ATTACH_NODE_TO_CURRENT_STATE_ACTION).setOrderIndexAs(200))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_CHECK_STATE).setOrderIndexAs(300))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_ATTACH_SPECIFIC_INFO).setOrderIndexAs(400))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_KEEP_SPECIFIC_INFO).setOrderIndexAs(500))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_CLEAR_SPECIFIC_INFO).setOrderIndexAs(600))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_INCREASE_NESTING_LEVEL).setOrderIndexAs(700))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_DECREASE_NESTING_LEVEL).setOrderIndexAs(800))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_ENTER_STATE).setOrderIndexAs(900))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_EXIT_STATE).setOrderIndexAs(1000));

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_CHECK_STATE)
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_VALID_STATES_PROPERTY)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true))
			.addSingleController(CONFIG_NODE_PROCESSOR, new CheckStateNodeConfigurationProcessor())
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_CREATE_NODE)
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_CREATE_NODE_PROPERTIES)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_CREATE_NODE_NEW_NODE_TYPE)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true))
			.addSingleController(CONFIG_NODE_PROCESSOR, new CreateNodeConfigurationProcessor())
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_ATTACH_NODE_TO_CURRENT_STATE_ACTION)
			.addSingleController(CONFIG_NODE_PROCESSOR, new AddAsChildOfStateNodeConfigurationProcessor())
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_ATTACH_SPECIFIC_INFO)
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_INFO_KEY)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true)
					.setLabelAs(ResourcesPlugin.getInstance().getLabelForProperty(REGEX_ACTION_TYPE + "." + ACTION_PROPERTY_INFO_KEY)))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_INFO_IS_CONTAINMENT)
					.setTypeAs(PROPERTY_EDITOR_TYPE_BOOLEAN).setContributesToCreationAs(true)
					.setLabelAs(ResourcesPlugin.getInstance().getLabelForProperty(REGEX_ACTION_TYPE + "." + ACTION_PROPERTY_INFO_IS_CONTAINMENT)))
			.addSingleController(CONFIG_NODE_PROCESSOR, new AttachSpecificInfoConfigurationProcessor())
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_CLEAR_SPECIFIC_INFO)
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_INFO_KEY)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true)
					.setLabelAs(ResourcesPlugin.getInstance().getLabelForProperty(REGEX_ACTION_TYPE + "." + ACTION_PROPERTY_INFO_KEY)))
			.addSingleController(CONFIG_NODE_PROCESSOR, new ClearSpecificInfoConfigurationProcessor())
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_KEEP_SPECIFIC_INFO)
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_INFO_KEY)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true)
					.setLabelAs(ResourcesPlugin.getInstance().getLabelForProperty(REGEX_ACTION_TYPE + "." + ACTION_PROPERTY_INFO_KEY)))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_INFO_IS_CONTAINMENT)
					.setTypeAs(PROPERTY_EDITOR_TYPE_BOOLEAN).setContributesToCreationAs(true)
					.setLabelAs(ResourcesPlugin.getInstance().getLabelForProperty(REGEX_ACTION_TYPE + "." + ACTION_PROPERTY_INFO_IS_CONTAINMENT)))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_INFO_IS_LIST)
					.setTypeAs(PROPERTY_EDITOR_TYPE_BOOLEAN).setContributesToCreationAs(true)
					.setLabelAs(ResourcesPlugin.getInstance().getLabelForProperty(REGEX_ACTION_TYPE + "." + ACTION_PROPERTY_INFO_IS_LIST)))
			.addSingleController(CONFIG_NODE_PROCESSOR, new KeepSpecificInfoConfigurationProcessor())
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_INCREASE_NESTING_LEVEL)
			.addSingleController(CONFIG_NODE_PROCESSOR, new IncreaseNestingLevelConfigurationProcessor())
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_DECREASE_NESTING_LEVEL)
			.addSingleController(CONFIG_NODE_PROCESSOR, new DecreaseNestingLevelConfigurationProcessor())
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_ENTER_STATE)
			.addSingleController(CONFIG_NODE_PROCESSOR, new EnterStateConfigurationProcessor())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ACTION_PROPERTY_ENTER_STATE_IF_PROPERTY_SET)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING))
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_EXIT_STATE)
			.addSingleController(CONFIG_NODE_PROCESSOR, new ExitStateConfigurationProcessor())
			.addCategory(CATEGORY_CONFIG_SETTINGS)
			.addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(REGEX_CONFIGS_NODE_TYPE);
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(REGEX_TECHNOLOGY_NODE_TYPE);
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(REGEXES_NODE_TYPE);
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(REGEX_TEST_FILES_NODE_TYPE);
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(REGEX_TEST_FILE_NODE_TYPE);

		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(REGEX_MODEL_TREE_NODE_TYPE);
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(REGEX_EXPECTED_MODEL_TREE_NODE_TYPE);
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(REGEX_MATCHES_NODE_TYPE);
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(REGEX_EXPECTED_MATCHES_NODE_TYPE);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_CONFIGS_NODE_TYPE)
			.addAdditiveController(CHILDREN_PROVIDER, new RegexConfigsChildrenProvider().setOrderIndexAs(1000))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, "Regex Configs"))
			.addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_TECHNOLOGY_NODE_TYPE)
			.addAdditiveController(CHILDREN_PROVIDER, new RegexTechnologyChildrenProvider().setOrderIndexAs(1000))
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexTechnologyPropertiesProvider())
			.addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEXES_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "RegExes"))
			.addAdditiveController(CHILDREN_PROVIDER, new RegexesController())
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexesController())
			.addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_TEST_FILES_NODE_TYPE)
			.addAdditiveController(CHILDREN_PROVIDER, new RegexTestFilesChildrenProvider().setOrderIndexAs(1000))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Test Files")) // TODO from messages
			.addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_TEST_FILE_NODE_TYPE)
			.addAdditiveController(CHILDREN_PROVIDER, new RegexTestFileChildrenProvider())
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexTestFilePropertiesProvider())
			.addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MODEL_TREE_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Model tree"))
			.addAdditiveController(CHILDREN_PROVIDER, new RegexModelTreeController())
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexModelTreeController());
			
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_EXPECTED_MODEL_TREE_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Expected model tree"))
			.addAdditiveController(CHILDREN_PROVIDER, new RegexExpectedModelTreeController())
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexExpectedModelTreeController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MATCHES_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Matches"))
			.addAdditiveController(CHILDREN_PROVIDER, new RegexTestMatchesController())
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexTestMatchesController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_EXPECTED_MATCHES_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Expected matches"))
			.addAdditiveController(CHILDREN_PROVIDER, new RegexExpectedMatchesController())
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexExpectedMatchesController());


		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_REGEX_ACTION)
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexActionController());

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_REGEX)
			.addAdditiveController(PROPERTIES_PROVIDER, new RegexController())
			.addAdditiveController(PROPERTY_SETTER, new RegexController())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(REGEX_WITH_MACROS)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true).setOrderIndexAs(20))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FULL_REGEX).setTypeAs(PROPERTY_EDITOR_TYPE_STRING)
					.setReadOnlyAs(true).setOrderIndexAs(30))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true).setOrderIndexAs(10));

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(REGEX_EXTENSION, "fpp", "mindmap", true))
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(REGEX_MATCH_EXTENSION, "fpp", "mindmap", true))
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(REGEX_RESULT_EXTENSION, "fpp", "mindmap", true));

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MATCHES_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexMatches.root")))
			.addAdditiveController(CHILDREN_PROVIDER, new RegexMatchesChildrenProvider());

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_RESULT_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexResults.root")));


		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(VIRTUAL_REGEX_TYPE)
			.addSingleController(CoreConstants.MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + CoreConstants.BASE_RENDERER_TEXT, new GenericDescriptor(REGEX_NAME))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(10))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FULL_REGEX)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setReadOnlyAs(true).setOrderIndexAs(20))
			.addAdditiveController(PROPERTIES_PROVIDER,	new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addAdditiveController(CHILDREN_PROVIDER, new VirtualRegexChildrenProvider());

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MATCH_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER,	new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/brick.png")))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(10))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FULL_REGEX)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(20))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(START)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(30))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor()
			.setNameAs(END).setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(40))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexMatches.root")));
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(VIRTUAL_REGEX_TYPE)
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(10))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FULL_REGEX)
					.setTypeAs(PROPERTY_EDITOR_TYPE_STRING).setReadOnlyAs(true).setOrderIndexAs(20))
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
			.addAdditiveController(CHILDREN_PROVIDER, new VirtualRegexChildrenProvider());
	
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(VIRTUAL_REGEX_TYPE);
	}

	/**
	 * @author Cristina Constantinescu
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		instance = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// messages come from .resources
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
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
