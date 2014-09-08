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
package org.flowerplatform.core.preference;

import static org.flowerplatform.core.CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES;

import java.util.Collections;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.controller.UpdateController;
import org.flowerplatform.core.preference.remote.PreferencePropertyWrapper;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public class PreferencePropertySetter extends AbstractController implements	IPropertySetter {

	public PreferencePropertySetter() {
		// execute after persistence setter
		setOrderIndex(100000);
	}
	
	@Override
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
		// refresh all properties on client
		for (Map.Entry<String, Object> entry : node.getOrPopulateProperties(new ServiceContext<NodeService>(context.getService()).add(CoreConstants.POPULATE_WITH_PROPERTIES_FORCEFULLY, true)).entrySet()) {
			if (entry.getValue() instanceof PreferencePropertyWrapper) {
				context.getService().setProperty(node, entry.getKey(), entry.getValue(), new ServiceContext<NodeService>(context.getService()).add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
			}	
		}
		context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		// refresh all properties on client
		for (Map.Entry<String, Object> entry : node.getOrPopulateProperties(new ServiceContext<NodeService>(context.getService()).add(CoreConstants.POPULATE_WITH_PROPERTIES_FORCEFULLY, true)).entrySet()) {
			if (entry.getValue() instanceof PreferencePropertyWrapper) {
				context.getService().setProperty(node, entry.getKey(), entry.getValue(), new ServiceContext<NodeService>(context.getService()).add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
			}			
		}	
		context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
	}
	
}