package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public abstract class StylePropertyProvider extends AbstractController {
	
	public static final String STYLE_PROPERTY_PROVIDER = "stylePropertyProvider";
	
	public abstract Object getStylePropertyValue(Node node, String property); 

}
