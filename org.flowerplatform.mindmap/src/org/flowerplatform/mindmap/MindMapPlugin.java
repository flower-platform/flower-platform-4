package org.flowerplatform.mindmap;

import static org.flowerplatform.core.node.remote.AddChildDescriptor.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.ICONS;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu 
 */
public class MindMapPlugin extends AbstractFlowerJavaPlugin {

	public static final String MINDMAP_NODE_TYPE = "freeplaneNode";
	public static final String MINDMAP_CATEGORY = ResourceTypeDynamicCategoryProvider.CATEGORY_RESOURCE_PREFIX + "mm";
	
	protected static MindMapPlugin INSTANCE;
	
	public static MindMapPlugin getInstance() {
		return INSTANCE;
	}
		
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		TypeDescriptor mmTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MINDMAP_NODE_TYPE);		
		mmTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs("Number").setNameAs(MIN_WIDTH).setTitleAs(getMessage("mindmap.min_width.title")).setReadOnlyAs(false));
		mmTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs("Number").setNameAs(MAX_WIDTH).setTitleAs(getMessage("mindmap.max_width.title")).setReadOnlyAs(false));		
		mmTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs("MindMapIconsWithButton").setNameAs(ICONS).setTitleAs(getMessage("mindmap.icons")).setReadOnlyAs(false));		
		
		mmTypeDescriptor.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(MINDMAP_NODE_TYPE).setLabelAs(getMessage("mindmap.add")));
	}	
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
		
}
