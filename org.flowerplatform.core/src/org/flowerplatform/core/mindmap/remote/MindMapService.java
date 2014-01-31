/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.core.mindmap.remote;
		
import java.util.Arrays;
import java.util.List;

import org.flowerplatform.core.mindmap.MindMapNodeDAO;

/**
 * @author Cristina Constantinescu
 */
public class MindMapService {
	
	private MindMapNodeDAO dao = new MindMapNodeDAO();
	
	public Node getNode(String nodeId) {
		return dao.getNode(nodeId);
	}	
	
	public List<Object> getChildrenForNodeId(String nodeId) {
		return dao.getChildren(nodeId);		
	}
	
	public void reload() {
		try {
			dao.load(null);
		} catch (Exception e) {
			// TODO CC: To log
			e.printStackTrace();
		}
	}

	public List<Object> refresh(String nodeId) {
		return Arrays.asList(nodeId, dao.getNode(nodeId));
	}
	
	public void setBody(String nodeId, String newBodyValue) {
		dao.setBody(nodeId, newBodyValue);
	}
	
	public void setProperty(String nodeId, String propertyName, String propertyValue) {
		// TODO should check to add propertyName or propertyName.original
		dao.setProperty(nodeId, propertyName, propertyValue);
	}
	
	public Node addNode(String parentNodeId, String type) {
		return dao.addNode(parentNodeId, type);
	}
	
	public void removeNode(String nodeId) {		
		dao.removeNode(nodeId);
	}	
	
	public void moveNode(String nodeId, String newParentId, int newIndex) {
		dao.moveNode(nodeId, newParentId, newIndex);
	}
	
	public void save() {		
		dao.save();
	}
	
	public List<Property> getPropertiesData(String nodeType) {		
		return dao.getPropertiesData(nodeType);		
	}
	
}
