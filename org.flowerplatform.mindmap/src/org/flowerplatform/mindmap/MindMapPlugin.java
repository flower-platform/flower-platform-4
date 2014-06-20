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
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_COLOR;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_HIDE;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_HORIZONTAL;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_LINEAR;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_SMOOTHLY_CURVED;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_STYLE;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_WIDTH;
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
import static org.flowerplatform.mindmap.MindMapConstants.PROPERTY_FOR_SIDE_DESCRIPTOR;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_NONE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_ROUND_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.SIDE;
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
		ResourcesPlugin resourcesPlugin = ResourcesPlugin.getInstance();
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MINDMAP_NODE_TYPE)
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_NUMBER).setNameAs(MIN_WIDTH).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.min_width.title")).setCategoryAs(resourcesPlugin.getMessage("nodeShape")).setOrderIndexAs(300))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_NUMBER).setNameAs(MAX_WIDTH).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.max_width.title")).setCategoryAs(resourcesPlugin.getMessage("nodeShape")).setOrderIndexAs(310))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(MINDMAP_ICONS_WITH_BUTTON_DESCRIPTOR_TYPE).setNameAs(ICONS).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.icons")).setOrderIndexAs(-10000))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(MINDMAP_STYLE_NAME_DESCRIPTOR_TYPE).setNameAs(STYLE_NAME).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.style.name")).setOrderIndexAs(-20000))	
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setNameAs(FONT_FAMILY).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.font.family")).setPossibleValuesAs(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()))
				.setCategoryAs(resourcesPlugin.getMessage("mindmap.font")).setOrderIndexAs(200))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setNameAs(FONT_SIZE).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.font.size")).setPossibleValuesAs(FONT_SIZES).setReadOnlyAs(false).setCategoryAs(resourcesPlugin.getMessage("mindmap.font")).setOrderIndexAs(210))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setNameAs(FONT_BOLD).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.font.bold")).setCategoryAs(resourcesPlugin.getMessage("mindmap.font")).setOrderIndexAs(220))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setNameAs(FONT_ITALIC).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.font.italic")).setCategoryAs(resourcesPlugin.getMessage("mindmap.font")).setOrderIndexAs(230))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER).setNameAs(COLOR_TEXT).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.color.text")).setCategoryAs(resourcesPlugin.getMessage("mindmap.color")).setOrderIndexAs(100))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER).setNameAs(COLOR_BACKGROUND).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.color.background")).setCategoryAs(resourcesPlugin.getMessage("mindmap.color")).setOrderIndexAs(110))
				.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER).setNameAs(CLOUD_COLOR).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.cloud.color")).setCategoryAs(resourcesPlugin.getMessage("mindmap.cloud")).setOrderIndexAs(400))
		// EDGE		
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_NUMBER).setNameAs(EDGE_WIDTH).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.edge.width")).setCategoryAs(resourcesPlugin.getMessage("mindmap.edges")).setOrderIndexAs(350))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTitleAs(resourcesPlugin.getMessage("mindmap.edge.style")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST)
				.setCategoryAs(resourcesPlugin.getMessage("mindmap.edges")).setNameAs(EDGE_STYLE).setHasChangeCheckboxAs(true)
				.setPossibleValuesAs(Arrays.asList(
						new Pair<String, String>(EDGE_SMOOTHLY_CURVED, resourcesPlugin.getMessage("mindmap.edge.smoothly.curved")), 
						new Pair<String, String>(EDGE_HIDE, resourcesPlugin.getMessage("mindmap.edge.hide")), 
						new Pair<String, String>(EDGE_HORIZONTAL, resourcesPlugin.getMessage("mindmap.edge.horizontal")),
						new Pair<String, String>(EDGE_LINEAR, resourcesPlugin.getMessage("mindmap.edge.linear"))))
				.setOrderIndexAs(360))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTitleAs(resourcesPlugin.getMessage("mindmap.edge.color")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER)
				.setCategoryAs(resourcesPlugin.getMessage("mindmap.edges")).setNameAs(EDGE_COLOR).setHasChangeCheckboxAs(true).setOrderIndexAs(370))
				
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST).setNameAs(CLOUD_SHAPE).setHasChangeCheckboxAs(true)
				.setTitleAs(resourcesPlugin.getMessage("mindmap.cloud.shape"))
				.setPossibleValuesAs(Arrays.asList(
						new Pair<String, String>(SHAPE_NONE, resourcesPlugin.getMessage("mindmap.shape.none")), 
						new Pair<String, String>(SHAPE_RECTANGLE, resourcesPlugin.getMessage("mindmap.shape.rectangle")), 
						new Pair<String, String>(SHAPE_ROUND_RECTANGLE, resourcesPlugin.getMessage("mindmap.shape.roundRectangle"))))
				.setCategoryAs(resourcesPlugin.getMessage("mindmap.cloud")).setOrderIndexAs(410))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(MINDMAP_NODE_TYPE).setLabelAs(resourcesPlugin.getMessage("mindmap.add")))
		// lower order index to override the default title property
		.addSingleController(CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(TEXT).setOrderIndexAs(-10000))
		.addSingleController(PROPERTY_FOR_SIDE_DESCRIPTOR, new GenericValueDescriptor(SIDE));
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
