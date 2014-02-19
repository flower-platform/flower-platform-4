package org.flowerplatform.mindmap;

import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu 
 */
public class MindMapPlugin extends AbstractFlowerJavaPlugin {

	public static final int DEFAULT_MIN_WIDTH = 1;
	public static final int DEFAULT_MAX_WIDTH = 600;
	
	public static final String MIN_WIDTH = "min_width";
	public static final String MAX_WIDTH = "max_width";
	
	public static final String MINDMAP_CATEGORY = ResourceTypeDynamicCategoryProvider.CATEGORY_RESOURCE_PREFIX + "mm";
	
	protected static MindMapPlugin INSTANCE;
	
	public static MindMapPlugin getInstance() {
		return INSTANCE;
	}
		
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		TypeDescriptor mmTypeDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(MINDMAP_CATEGORY);		
		mmTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs("Number").setNameAs(MIN_WIDTH).setTitleAs(getMessage("mindmap.min_width.title")).setReadOnlyAs(false));
		mmTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs("Number").setNameAs(MAX_WIDTH).setTitleAs(getMessage("mindmap.max_width.title")).setReadOnlyAs(false));		
	}	
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
		
}
