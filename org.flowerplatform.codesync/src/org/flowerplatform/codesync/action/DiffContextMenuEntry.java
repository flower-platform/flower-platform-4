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
package org.flowerplatform.codesync.action;

import java.util.List;


/**
 *@author Mariana Gheorghe
 **/
public class DiffContextMenuEntry {

	private String label;
	
	private List<DiffActionEntry> actionEntries;

	private boolean isRight;
	
	private int color = 0xFFFFFF;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<DiffActionEntry> getActionEntries() {
		return actionEntries;
	}

	public void setActionEntries(List<DiffActionEntry> actionEntries) {
		this.actionEntries = actionEntries;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRightValue) {
		this.isRight = isRightValue;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}