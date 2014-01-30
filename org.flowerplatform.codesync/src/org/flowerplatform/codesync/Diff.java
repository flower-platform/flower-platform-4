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
package org.flowerplatform.codesync;

/**
 * 
 */
public class Diff {

	private Object feature;
	
	private Match parentMatch;
	
	private boolean isLeftModified;
	
	private boolean isRightModified;
	
	/**
	 * 
	 */
	private boolean isConflict;

	@Override
	public String toString() {
		return String.format("Diff [feature = %s, conflict = %b, leftModif = %b, rightModif = %b", 
				getFeature(),
				isConflict(),
				isLeftModified(),
				isRightModified());
	}

	public Object getFeature() {
		return feature;
	}

	public void setFeature(Object feature) {
		this.feature = feature;
	}

	public Match getParentMatch() {
		return parentMatch;
	}

	public boolean isLeftModified() {
		return isLeftModified;
	}

	public void setLeftModified(boolean isLeftModified) {
		this.isLeftModified = isLeftModified;
	}

	public boolean isRightModified() {
		return isRightModified;
	}

	public void setRightModified(boolean isRightModified) {
		this.isRightModified = isRightModified;
	}

	public boolean isConflict() {
		return isConflict;
	}

	public void setConflict(boolean isConflict) {
		this.isConflict = isConflict;
	}

	public void setParentMatch(Match parentMatch) {
		this.parentMatch = parentMatch;
	}
	
}