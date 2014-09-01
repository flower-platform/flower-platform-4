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
package org.flowerplatform.codesync.regex;

import static org.flowerplatform.codesync.CodeSyncConstants.CATEGORY_MODEL;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_NODE_TO_CURRENT_STATE_ACTION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CHECK_STATE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CLEAR_SPECIFIC_INFO;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE_PROPERTIES;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_DECREASE_NESTING_LEVEL;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ENTER_STATE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_EXIT_STATE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_INCREASE_NESTING_LEVEL;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_VALID_STATES_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.CATEGORY_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.CATEGORY_REGEX_ACTION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.END;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.FULL_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEXES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIGS_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIG_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MODEL_TREE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXTENSION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MACRO_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCHES_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_EXTENSION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MODEL_TREE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_NAME;
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
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_STRING;
import static org.flowerplatform.core.CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REPOSITORY_TYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.regex.controller.AttachNodeToCurrentStateConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.AttachSpecificInfoConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.CheckStateNodeConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.ClearSpecificInfoConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.CodeSyncRegexSubscribableResourceProvider;
import org.flowerplatform.codesync.regex.controller.CodeSyncRepositoryChildrenProvider;
import org.flowerplatform.codesync.regex.controller.CreateNodeConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.DecreaseNestingLevelConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.EnterStateConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.ExitStateConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.IncreaseNestingLevelConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.KeepSpecificInfoConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.RegexActionController;
import org.flowerplatform.codesync.regex.controller.RegexConfigsChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexConfigurationProcessor;
import org.flowerplatform.codesync.regex.controller.RegexController;
import org.flowerplatform.codesync.regex.controller.RegexMatchesChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexTechnologyChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexTechnologyPropertiesProvider;
import org.flowerplatform.codesync.regex.controller.RegexTestFileChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexTestFilePropertiesProvider;
import org.flowerplatform.codesync.regex.controller.RegexTestFilesChildrenProvider;
import org.flowerplatform.codesync.regex.controller.RegexWithActionsProcessor;
import org.flowerplatform.codesync.regex.controller.VirtualRegexChildrenProvider;
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

	public Map<String, RegexAction> getAction() {
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

		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(REGEX_CONFIG_TYPE)
				.addSingleController(CONFIG_NODE_PROCESSOR, new RegexConfigurationProcessor())
				.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexes.root")))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(REGEX_TYPE).setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.regex"))
								.setIconAs(ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(REGEX_MACRO_TYPE).setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.macro"))
								.setIconAs(ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/percent.png")));

		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(REGEX_MACRO_TYPE)
				.addCategory(CATEGORY_REGEX)
				.addAdditiveController(PROPERTIES_PROVIDER,
						new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/percent.png")));

		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(REGEX_TYPE)
				.addCategory(CATEGORY_REGEX)
				.addCategory(CATEGORY_CONFIG_SETTINGS)
				.addSingleController(CONFIG_NODE_PROCESSOR, new RegexWithActionsProcessor())
				.addAdditiveController(PROPERTIES_PROVIDER,
						new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_CREATE_NODE).setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.createNodeAction"))
								.setOrderIndexAs(100))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_ATTACH_NODE_TO_CURRENT_STATE_ACTION)
								.setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.attachNodeToCurrentStateAction")).setOrderIndexAs(200))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_CHECK_STATE).setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.checkStateAction"))
								.setOrderIndexAs(300))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_ATTACH_SPECIFIC_INFO)
								.setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.attachSpecificInfoAction")).setOrderIndexAs(400))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_KEEP_SPECIFIC_INFO)
								.setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.keepSpecificInfoAction")).setOrderIndexAs(500))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_CLEAR_SPECIFIC_INFO)
								.setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.clearSpecificInfoAction")).setOrderIndexAs(600))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_INCREASE_NESTING_LEVEL)
								.setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.increaseNestingLevelAction")).setOrderIndexAs(700))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_DECREASE_NESTING_LEVEL)
								.setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.decreaseNestingLevelAction")).setOrderIndexAs(800))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_ENTER_STATE).setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.enterStateAction"))
								.setOrderIndexAs(900))
				.addAdditiveController(
						ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(ACTION_TYPE_EXIT_STATE).setLabelAs(ResourcesPlugin.getInstance().getMessage("regex.exitStateAction"))
								.setOrderIndexAs(1000));

		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(ACTION_TYPE_CHECK_STATE)
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(ACTION_TYPE_VALID_STATES_PROPERTY).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.validStates"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true))
				.addSingleController(CONFIG_NODE_PROCESSOR, new CheckStateNodeConfigurationProcessor()).addCategory(CATEGORY_CONFIG_SETTINGS).addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(ACTION_TYPE_CREATE_NODE)
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(ACTION_TYPE_CREATE_NODE_PROPERTIES).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.properties"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.newNodeType"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true))
				.addSingleController(CONFIG_NODE_PROCESSOR, new CreateNodeConfigurationProcessor()).addCategory(CATEGORY_CONFIG_SETTINGS).addCategory(CATEGORY_REGEX_ACTION);
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_ATTACH_NODE_TO_CURRENT_STATE_ACTION)
				.addSingleController(CONFIG_NODE_PROCESSOR, new AttachNodeToCurrentStateConfigurationProcessor()).addCategory(CATEGORY_CONFIG_SETTINGS)
				.addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(ACTION_TYPE_ATTACH_SPECIFIC_INFO)
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY)
								.setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.attachInfoKey")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true)
								.setContributesToCreationAs(true))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY)
								.setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.isContainment")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true)
								.setContributesToCreationAs(true)).addSingleController(CONFIG_NODE_PROCESSOR, new AttachSpecificInfoConfigurationProcessor())
				.addCategory(CATEGORY_CONFIG_SETTINGS).addCategory(CATEGORY_REGEX_ACTION);
		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(ACTION_TYPE_CLEAR_SPECIFIC_INFO)
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.clearInfoKey"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true))
				.addSingleController(CONFIG_NODE_PROCESSOR, new ClearSpecificInfoConfigurationProcessor()).addCategory(CATEGORY_CONFIG_SETTINGS).addCategory(CATEGORY_REGEX_ACTION);
		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(ACTION_TYPE_KEEP_SPECIFIC_INFO)
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.keepInfoKey"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.isList"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setMandatoryAs(true).setContributesToCreationAs(true))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY)
								.setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.isContainment")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setMandatoryAs(true)
								.setContributesToCreationAs(true)).addSingleController(CONFIG_NODE_PROCESSOR, new KeepSpecificInfoConfigurationProcessor())
				.addCategory(CATEGORY_CONFIG_SETTINGS).addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_INCREASE_NESTING_LEVEL)
				.addSingleController(CONFIG_NODE_PROCESSOR, new IncreaseNestingLevelConfigurationProcessor()).addCategory(CATEGORY_CONFIG_SETTINGS)
				.addCategory(CATEGORY_REGEX_ACTION);
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_DECREASE_NESTING_LEVEL)
				.addSingleController(CONFIG_NODE_PROCESSOR, new DecreaseNestingLevelConfigurationProcessor()).addCategory(CATEGORY_CONFIG_SETTINGS)
				.addCategory(CATEGORY_REGEX_ACTION);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_ENTER_STATE)
				.addSingleController(CONFIG_NODE_PROCESSOR, new EnterStateConfigurationProcessor()).addCategory(CATEGORY_CONFIG_SETTINGS).addCategory(CATEGORY_REGEX_ACTION);
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(ACTION_TYPE_EXIT_STATE)
				.addSingleController(CONFIG_NODE_PROCESSOR, new ExitStateConfigurationProcessor()).addCategory(CATEGORY_CONFIG_SETTINGS).addCategory(CATEGORY_REGEX_ACTION);

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
				.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, "Regex Configs")).addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_TECHNOLOGY_NODE_TYPE)
				.addAdditiveController(CHILDREN_PROVIDER, new RegexTechnologyChildrenProvider().setOrderIndexAs(1000))
				.addAdditiveController(PROPERTIES_PROVIDER, new RegexTechnologyPropertiesProvider()).addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEXES_NODE_TYPE)
				.addAdditiveController(CHILDREN_PROVIDER, new CodeSyncRegexSubscribableResourceProvider())
				.addAdditiveController(PROPERTIES_PROVIDER, new CodeSyncRegexSubscribableResourceProvider()).addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_TEST_FILES_NODE_TYPE)
				.addAdditiveController(CHILDREN_PROVIDER, new RegexTestFilesChildrenProvider().setOrderIndexAs(1000))
				.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "test-files"))
				.addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_TEST_FILE_NODE_TYPE)
				.addAdditiveController(CHILDREN_PROVIDER, new RegexTestFileChildrenProvider())
				.addAdditiveController(PROPERTIES_PROVIDER, new RegexTestFilePropertiesProvider())
				.addCategory(CATEGORY_MODEL);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MODEL_TREE_NODE_TYPE)
				.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Model tree"));
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_EXPECTED_MODEL_TREE_NODE_TYPE)
				.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Expected model tree"));
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MATCHES_NODE_TYPE)
				.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Matches"));
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_EXPECTED_MATCHES_NODE_TYPE)
				.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(NAME, "Expected matches"));


		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_REGEX_ACTION)
				.addAdditiveController(PROPERTIES_PROVIDER, new RegexActionController());

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REPOSITORY_TYPE)
				.addAdditiveController(CHILDREN_PROVIDER, new CodeSyncRepositoryChildrenProvider());

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_REGEX)
				.addSingleController(PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(NAME))
				.addAdditiveController(PROPERTIES_PROVIDER, new RegexController())
				.addAdditiveController(PROPERTY_SETTER, new RegexController())
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(REGEX_WITH_MACROS).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.regex"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true).setOrderIndexAs(20))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(FULL_REGEX).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.full"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setReadOnlyAs(true).setOrderIndexAs(30))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.name")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING)
								.setMandatoryAs(true).setContributesToCreationAs(true).setOrderIndexAs(10));

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
				.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(REGEX_EXTENSION, "fpp", "mindmap", true));

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
				.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(REGEX_MATCH_EXTENSION, "fpp", "mindmap", true));

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REGEX_MATCHES_TYPE)
				.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("regexMatches.root")))
				.addAdditiveController(CHILDREN_PROVIDER, new RegexMatchesChildrenProvider().setOrderIndexAs(10000));

		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(VIRTUAL_REGEX_TYPE)
				.addSingleController(PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(REGEX_NAME))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.name")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING)
								.setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(10))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(FULL_REGEX).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.full"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setReadOnlyAs(true).setOrderIndexAs(20))
				.addAdditiveController(PROPERTIES_PROVIDER,
						new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/bricks.png")))
				.addAdditiveController(CHILDREN_PROVIDER, new VirtualRegexChildrenProvider());

		CorePlugin
				.getInstance()
				.getNodeTypeDescriptorRegistry()
				.getOrCreateTypeDescriptor(REGEX_MATCH_TYPE)
				.addSingleController(PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(NAME))
				.addAdditiveController(PROPERTIES_PROVIDER,
						new ConstantValuePropertyProvider(ICONS, ResourcesPlugin.getInstance().getResourceUrl("images/codesync.regex/brick.png")))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.name")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING)
								.setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(10))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(FULL_REGEX).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.full"))
								.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING).setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(20))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(START).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.start")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING)
								.setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(30))
				.addAdditiveController(
						PROPERTY_DESCRIPTOR,
						new PropertyDescriptor().setNameAs(END).setTitleAs(ResourcesPlugin.getInstance().getMessage("regex.end")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_STRING)
								.setMandatoryAs(true).setContributesToCreationAs(true).setReadOnlyAs(true).setOrderIndexAs(40));

		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(VIRTUAL_REGEX_TYPE);
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
