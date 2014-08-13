package org.flowerplatform.core.preference;

import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;

import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
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
		if (context.getBooleanValue(CoreConstants.EXECUTE_ONLY_FOR_UPDATER)) {
			return;
		}
		
		// refresh all properties on client
		for (Map.Entry<String, Object> entry : node
				.getOrPopulateProperties(new ServiceContext<NodeService>(context.getService()).add(CoreConstants.POPULATE_WITH_PROPERTIES_FORCEFULLY, true)).entrySet()) {
			if (entry.getValue() instanceof PreferencePropertyWrapper) {
				context.getService().setProperty(node, entry.getKey(), entry.getValue(), new ServiceContext<NodeService>(context.getService()).add(EXECUTE_ONLY_FOR_UPDATER, true));
			}	
		}
		context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		if (context.getBooleanValue(CoreConstants.EXECUTE_ONLY_FOR_UPDATER)) {
			return;
		}
		
		// refresh all properties on client
		for (Map.Entry<String, Object> entry : node.getOrPopulateProperties(new ServiceContext<NodeService>(context
				.getService()).add(CoreConstants.POPULATE_WITH_PROPERTIES_FORCEFULLY, true)).entrySet()) {
			if (entry.getValue() instanceof PreferencePropertyWrapper) {
				context.getService().setProperty(node, entry.getKey(), entry.getValue(), new ServiceContext<NodeService>(context.getService()).add(EXECUTE_ONLY_FOR_UPDATER, true));
			}			
		}	
		context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
	}
	
}
