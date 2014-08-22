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
package org.flowerplatform.team.git.history.internal;

import org.eclipse.jgit.revplot.PlotLane;

/**
 *	@author Cristina Constantinescu
 */
public class WebLane extends PlotLane {

	private static final long serialVersionUID = 1L;
	
	String color;
	
	public WebLane(String color) {
		this.color = color;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && color.equals(((WebLane) o).color);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ color.hashCode();
	}
	
}