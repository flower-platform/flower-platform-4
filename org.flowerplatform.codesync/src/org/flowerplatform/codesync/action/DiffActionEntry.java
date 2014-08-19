/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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


/**
 * @author Mariana Gheorghe
 */
public class DiffActionEntry {
	private int actionType = -1;
	
	private String label;
	
	private int diffIndex = -1;
	
	private boolean enabled;
	
	private boolean default1;

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionId) {
		this.actionType = actionId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getDiffIndex() {
		return diffIndex;
	}

	public void setDiffIndex(int diffIndex) {
		this.diffIndex = diffIndex;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDefault1() {
		return default1;
	}

	public void setDefault1(boolean default1) {
		this.default1 = default1;
	}
}