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
package org.flowerplatform.core.config_processor;

import static org.flowerplatform.core.CoreConstants.CONFIG_SETTING_DISABLED;

import java.util.Collections;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.controller.UpdateController;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.GenericDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * Property controller to gray out the text if the configuration setting node is disabled.
 * 
 * <p>
 * As a provider: provide the text color if disabled and the text color is not yet set.
 * 
 * <p>
 * As a setter: set/unset the text color if disabled or not, and the text color is not yet set.
 * Important: make sure to invoke only for updates, to avoid getting the text color property persisted.
 * 
 * @author Mariana Gheorghe
 */
public class ConfigSettingsPropertiesController extends AbstractController implements IPropertySetter, IPropertiesProvider {

	private static String disabledTextColor = "#CCCCCC";
	
	public ConfigSettingsPropertiesController() {
		// after persistence controllers
		setOrderIndex(10000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		String textColorProperty = getTextColorProperty(node);
		Boolean disabled = (Boolean) node.getProperties().get(CONFIG_SETTING_DISABLED);
		if (disabled != null && disabled) {
			// overwrite text color if not already set
			Object textColor = node.getProperties().get(textColorProperty);
			if (textColor == null) {
				node.getProperties().put(textColorProperty, disabledTextColor);
			}
		}
	}

	@Override
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {
		for (String property : properties.keySet()) {
			if (configSetingDisabledChanged(node, property, context)) {
				break;
			}
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		configSetingDisabledChanged(node, property, context);
	}

	private boolean configSetingDisabledChanged(Node node, String property, ServiceContext<NodeService> context) {
		if (!CONFIG_SETTING_DISABLED.equals(property)) {
			return false;
		}
		
		Boolean disabled = (Boolean) node.getProperties().get(CONFIG_SETTING_DISABLED);
		String textColorProperty = getTextColorProperty(node);
		Object textColor = node.getProperties().get(textColorProperty);
		if (disabled != null && disabled) {
			// set text color property; only for updates
			if (textColor == null) {
				context.getService().setProperty(node, textColorProperty, disabledTextColor, onlyForUpdates(context));
			}
		} else {
			// unset text color property; only for updates
			if (disabledTextColor.equals(textColor)) {
				context.getService().setProperty(node, textColorProperty, null, onlyForUpdates(context));
			}
		}
		return true;
	}
	
	private String getTextColorProperty(Node node) {
		TypeDescriptor typeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry()
				.getExpectedTypeDescriptor(node.getType());
		GenericDescriptor descriptor = typeDescriptor.getSingleController(
				CoreConstants.MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + CoreConstants.BASE_RENDERER_TEXT_COLOR,
				node);
		return (String) descriptor.getValue();
	}
	
	private ServiceContext<NodeService> onlyForUpdates(ServiceContext<NodeService> context) {
		ServiceContext<NodeService> onlyForUpdates = new ServiceContext<NodeService>(context.getService());
		onlyForUpdates.getContext().put(CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, 
				Collections.singletonList(UpdateController.class));
		return onlyForUpdates;
	}

}
