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
package org.flowerplatform.core.node.remote;

import java.util.List;

/**
 * @see NodeServiceRemote#refresh(FullNodeIdWithChildren)
 * @author Cristina Constantinescu
 */
public class FullNodeIdWithChildren {

	private String fullNodeId;
	
	private List<FullNodeIdWithChildren> visibleChildren;

	public String getFullNodeId() {
		return fullNodeId;
	}

	public void setFullNodeId(String fullNodeId) {
		this.fullNodeId = fullNodeId;
	}

	public List<FullNodeIdWithChildren> getVisibleChildren() {
		return visibleChildren;
	}

	public void setVisibleChildren(List<FullNodeIdWithChildren> visibleChildren) {
		this.visibleChildren = visibleChildren;
	}
	
}