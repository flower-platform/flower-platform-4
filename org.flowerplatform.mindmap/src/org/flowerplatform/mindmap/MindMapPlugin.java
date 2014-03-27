package org.flowerplatform.mindmap;

import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER;
import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_COLOR;
import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_SHAPE;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_TEXT;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_BOLD;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_FAMILY;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_ITALIC;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_SIZE;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_SIZES;
import static org.flowerplatform.mindmap.MindMapConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.MINDMAP_NODE_TYPE;
import static org.flowerplatform.mindmap.MindMapConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_NONE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_ROUND_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.TEXT;

import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.GenericValueDescriptor;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu 
 */
public class MindMapPlugin extends AbstractFlowerJavaPlugin {

	protected static MindMapPlugin INSTANCE;
	
	public static MindMapPlugin getInstance() {
		return INSTANCE;
	}
		
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MINDMAP_NODE_TYPE)
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_NUMBER).setNameAs(MIN_WIDTH).setTitleAs(getMessage("mindmap.min_width.title")).setReadOnlyAs(false).setCategoryAs(getMessage("nodeShape")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_NUMBER).setNameAs(MAX_WIDTH).setTitleAs(getMessage("mindmap.max_width.title")).setReadOnlyAs(false).setCategoryAs(getMessage("nodeShape")))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs("MindMapIconsWithButton").setNameAs(ICONS).setTitleAs(getMessage("mindmap.icons")).setReadOnlyAs(false))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setNameAs(FONT_FAMILY).setTitleAs(getMessage("mindmap.font.family")).setPossibleValuesAs(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.font")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setNameAs(FONT_SIZE).setTitleAs(getMessage("mindmap.font.size")).setPossibleValuesAs(FONT_SIZES).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.font")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setNameAs(FONT_BOLD).setTitleAs(getMessage("mindmap.font.bold")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.font")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setNameAs(FONT_ITALIC).setTitleAs(getMessage("mindmap.font.italic")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.font")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER).setNameAs(COLOR_TEXT).setTitleAs(getMessage("mindmap.color.text")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.color")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER).setNameAs(COLOR_BACKGROUND).setTitleAs(getMessage("mindmap.color.background")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.color")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER).setNameAs(CLOUD_COLOR).setTitleAs(getMessage("mindmap.cloud.color")).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.cloud")))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setNameAs(CLOUD_SHAPE).setTitleAs(getMessage("mindmap.cloud.shape")).setPossibleValuesAs(Arrays.asList(new Pair<String, String>(SHAPE_NONE, getMessage("mindmap.shape.none")), new Pair<String, String>(SHAPE_RECTANGLE, getMessage("mindmap.shape.rectangle")), new Pair<String, String>(SHAPE_ROUND_RECTANGLE, getMessage("mindmap.shape.roundRectangle")))).setReadOnlyAs(false).setCategoryAs(getMessage("mindmap.cloud")))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(MINDMAP_NODE_TYPE).setLabelAs(getMessage("mindmap.add")))
		// lower order index to override the default title property
		.addSingleController(CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(TEXT).setOrderIndexAs(-10000));
	}	
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
		
}
