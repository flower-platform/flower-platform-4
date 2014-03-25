package org.flowerplatform.mindmap;

import static org.flowerplatform.core.CorePlugin.PROPERTY_FOR_TITLE_DESCRIPTOR;
import static org.flowerplatform.core.NodePropertiesConstants.ICONS;
import static org.flowerplatform.core.node.remote.AddChildDescriptor.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.BOOLEAN;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.COLOR_PICKER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.DROP_DOWN_LIST;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.NUMBER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.CLOUD_COLOR;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.CLOUD_SHAPE;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.COLOR_BACKGROUND;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.COLOR_TEXT;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.FONT_BOLD;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.FONT_FAMILY;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.FONT_ITALIC;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.FONT_SIZE;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.FONT_SIZES;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.TEXT;

import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.GenericValueDescriptor;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu 
 */
public class MindMapPlugin extends AbstractFlowerJavaPlugin {

	public static final String FREEPLANE_PERSISTENCE_NODE_TYPE_KEY = "nodeType";
	
	public static final String FREEPLANE_MINDMAP_RESOURCE_KEY = "freePlaneMindMap";
	public static final String FREEPLANE_PERSISTENCE_RESOURCE_KEY = "freePlanePersistence";
	
	public static final String MINDMAP_NODE_TYPE = "freeplaneNode";
	public static final String FREEPLANE_MINDMAP_CATEGORY = ResourceTypeDynamicCategoryProvider.CATEGORY_RESOURCE_PREFIX + FREEPLANE_MINDMAP_RESOURCE_KEY;
	public static final String FREEPLANE_PERSISTENCE_CATEGORY = ResourceTypeDynamicCategoryProvider.CATEGORY_RESOURCE_PREFIX + FREEPLANE_PERSISTENCE_RESOURCE_KEY;
		
	protected static MindMapPlugin INSTANCE;
	
	public static MindMapPlugin getInstance() {
		return INSTANCE;
	}
		
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MINDMAP_NODE_TYPE)
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(NUMBER).setNameAs(MIN_WIDTH).setTitleAs(getMessage("mindmap.min_width.title")).setReadOnlyAs(false).setCategoryAs(getMessage("nodeShape")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(NUMBER).setNameAs(MAX_WIDTH).setTitleAs(getMessage("mindmap.max_width.title")).setReadOnlyAs(false).setCategoryAs(getMessage("nodeShape")))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs("MindMapIconsWithButton").setNameAs(ICONS).setTitleAs(getMessage("mindmap.icons")).setReadOnlyAs(false))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(DROP_DOWN_LIST).setNameAs(FONT_FAMILY).setTitleAs(getMessage("mindmap.font.family")).setPossibleValuesAs(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.font")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(DROP_DOWN_LIST).setNameAs(FONT_SIZE).setTitleAs(getMessage("mindmap.font.size")).setPossibleValuesAs(FONT_SIZES).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.font")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(BOOLEAN).setNameAs(FONT_BOLD).setTitleAs(getMessage("mindmap.font.bold")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.font")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(BOOLEAN).setNameAs(FONT_ITALIC).setTitleAs(getMessage("mindmap.font.italic")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.font")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(COLOR_PICKER).setNameAs(COLOR_TEXT).setTitleAs(getMessage("mindmap.color.text")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.color")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(COLOR_PICKER).setNameAs(COLOR_BACKGROUND).setTitleAs(getMessage("mindmap.color.background")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.color")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(COLOR_PICKER).setNameAs(CLOUD_COLOR).setTitleAs(getMessage("mindmap.cloud.color")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.cloud")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(DROP_DOWN_LIST).setNameAs(CLOUD_SHAPE).setTitleAs(getMessage("mindmap.cloud.shape")).setPossibleValuesAs(Arrays.asList("", "Rectangle", "Round Rectangle")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.cloud")))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(MINDMAP_NODE_TYPE).setLabelAs(getMessage("mindmap.add")))
		// lower order index to override the default title property
		.addSingleController(PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(TEXT).setOrderIndexAs(-10000));
	}	
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
		
}
