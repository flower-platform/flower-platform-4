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
package org.flowerplatform.freeplane.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.flowerplatform.core.CorePlugin;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.styles.IStyle;
import org.freeplane.features.styles.MapStyleModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapServiceRemote {

	public List<String> getStyles(String fullNodeId) {		
		MapModel mapModel = ((NodeModel) CorePlugin.getInstance().getResourceService().getNode(fullNodeId).getRawNodeData()).getMap();
			
		Set<IStyle> styles = MapStyleModel.getExtension(mapModel).getStyles();
		if (styles == null) {	
			return null;
		}
		List<String> children = new ArrayList<String>();
		for(IStyle style : styles) {
			children.add(style.toString());
		}		
		return children;		
	}
	
}
