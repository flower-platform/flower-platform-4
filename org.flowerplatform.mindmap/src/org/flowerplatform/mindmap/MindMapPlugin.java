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
import static org.flowerplatform.mindmap.MindMapConstants.MINDMAP_ICONS_WITH_BUTTON_DESCRIPTOR_TYPE;
import static org.flowerplatform.mindmap.MindMapConstants.MINDMAP_NODE_TYPE;
import static org.flowerplatform.mindmap.MindMapConstants.MINDMAP_STYLE_NAME_DESCRIPTOR_TYPE;
import static org.flowerplatform.mindmap.MindMapConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_NONE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_ROUND_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.STYLE_NAME;
import static org.flowerplatform.mindmap.MindMapConstants.TEXT;

import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.GenericValueDescriptor;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.resources.ResourcesPlugin;
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
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_NUMBER).setNameAs(MIN_WIDTH).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.min_width.title")).setCategoryAs(ResourcesPlugin.getInstance().getMessage("nodeShape")).setOrderIndexAs(300))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_NUMBER).setNameAs(MAX_WIDTH).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.max_width.title")).setCategoryAs(ResourcesPlugin.getInstance().getMessage("nodeShape")).setOrderIndexAs(310))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(MINDMAP_ICONS_WITH_BUTTON_DESCRIPTOR_TYPE).setNameAs(ICONS).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.icons")).setOrderIndexAs(-10000))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(MINDMAP_STYLE_NAME_DESCRIPTOR_TYPE).setNameAs(STYLE_NAME).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.style.name")).setOrderIndexAs(-20000))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setNameAs(FONT_FAMILY).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.font.family")).setPossibleValuesAs(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()))
				.setCategoryAs(ResourcesPlugin.getInstance().getMessage("mindmap.font")).setOrderIndexAs(200))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setNameAs(FONT_SIZE).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.font.size")).setPossibleValuesAs(FONT_SIZES).setReadOnlyAs(false).setCategoryAs(ResourcesPlugin.getInstance().getMessage("mindmap.font")).setOrderIndexAs(210))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setNameAs(FONT_BOLD).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.font.bold")).setCategoryAs(ResourcesPlugin.getInstance().getMessage("mindmap.font")).setOrderIndexAs(220))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setNameAs(FONT_ITALIC).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.font.italic")).setCategoryAs(ResourcesPlugin.getInstance().getMessage("mindmap.font")).setOrderIndexAs(230))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER).setNameAs(COLOR_TEXT).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.color.text")).setCategoryAs(ResourcesPlugin.getInstance().getMessage("mindmap.color")).setOrderIndexAs(100))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER).setNameAs(COLOR_BACKGROUND).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.color.background")).setCategoryAs(ResourcesPlugin.getInstance().getMessage("mindmap.color")).setOrderIndexAs(110))
				.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER).setNameAs(CLOUD_COLOR).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.cloud.color")).setCategoryAs(ResourcesPlugin.getInstance().getMessage("mindmap.cloud")).setOrderIndexAs(400))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setNameAs(CLOUD_SHAPE).setHasChangeCheckboxAs(true)
				.setTitleAs(ResourcesPlugin.getInstance().getMessage("mindmap.cloud.shape"))
				.setPossibleValuesAs(Arrays.asList(
						new Pair<String, String>(SHAPE_NONE, ResourcesPlugin.getInstance().getMessage("mindmap.shape.none")), 
						new Pair<String, String>(SHAPE_RECTANGLE, ResourcesPlugin.getInstance().getMessage("mindmap.shape.rectangle")), 
						new Pair<String, String>(SHAPE_ROUND_RECTANGLE, ResourcesPlugin.getInstance().getMessage("mindmap.shape.roundRectangle"))))
				.setReadOnlyAs(false).setCategoryAs(ResourcesPlugin.getInstance().getMessage("mindmap.cloud")).setOrderIndexAs(410))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(MINDMAP_NODE_TYPE).setLabelAs(ResourcesPlugin.getInstance().getMessage("mindmap.add")))
		// lower order index to override the default title property
		.addSingleController(CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(TEXT).setOrderIndexAs(-10000));
	}	
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// messages come from .resources
	}
		
}
