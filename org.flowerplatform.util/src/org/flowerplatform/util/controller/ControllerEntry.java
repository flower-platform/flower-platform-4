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
package org.flowerplatform.util.controller;

/**
 * Entry object for the {@link TypeDescriptor#singleControllers} and {@link TypeDescriptor#additiveControllers} maps.
 * Keeps the controller (or list of controllers) originally set for the type, the controller (or list of controllers)
 * processed from the categories of the type and a flag that specifies if the processed controllers have been cached.
 * 
 * @author Mariana Gheorghe
 */
public class ControllerEntry<T> {

	private T selfValue;
	private T cachedValue;
	private boolean wasCached;
	
	public T getSelfValue() {
		return selfValue;
	}
	
	public void setSelfValue(T selfValue) {
		this.selfValue = selfValue;
	}
	
	public T getCachedValue() {
		return cachedValue;
	}
	
	public void setCachedValue(T cachedValue) {
		this.cachedValue = cachedValue;
	}
	
	/**
	 * @author see class
	 */
	public boolean wasCached() {
		return wasCached;
	}
	
	public void setCached(boolean cached) {
		this.wasCached = cached;
	}
	
}