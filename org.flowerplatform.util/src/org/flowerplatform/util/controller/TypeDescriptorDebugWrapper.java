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
package org.flowerplatform.util.controller;

import java.util.List;
import java.util.Set;

/**
 * Provides further access to a {@link TypeDescriptor}'s registered {@link ControllerEntry}s,
 * without modifying the registry (i.e. controllers are not cached).
 * 
 * <p>
 * For debug purposes only. Users should <b>not</b> modify the contents of the controllers lists
 * or the controllers themselves.
 * 
 * @author Mariana Gheorghe
 */
public class TypeDescriptorDebugWrapper {

	private TypeDescriptor descriptor;
	
	/**
	 * @author see class
	 */
	public TypeDescriptorDebugWrapper(TypeDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	public Set<String> getSingleControllersKeys() {
		return descriptor.singleControllers.keySet();
	}
	
	/**
	 * @author see class
	 */
	public boolean isCachedSingleController(String type) {
		return descriptor.singleControllers.get(type).wasCached();
	}
	
	/**
	 * @author see class
	 */
	public Object getSelfSingleController(String type) {
		return descriptor.singleControllers.get(type).getSelfValue();
	}
	
	/**
	 * @author see class
	 */
	public Object getCachedSingleController(String type) {
		return descriptor.getCachedSingleController(type, null, false, false);
	}
	
	public Set<String> getAdditiveControllersKeys() {
		return descriptor.additiveControllers.keySet();
	}
	
	/**
	 * @author see class
	 */
	public boolean isCachedAdditiveController(String type) {
		return descriptor.additiveControllers.get(type).wasCached();
	}
	
	/**
	 * @author see class
	 */
	public List<? extends IController> getSelfAdditiveControllers(String type) {
		return descriptor.additiveControllers.get(type).getSelfValue();
	}
	
	/**
	 * @author see class
	 */
	public List<? extends IController> getCachedAdditiveControllers(String type) {
		return descriptor.getCachedAdditiveControllers(type, null, false, false);
	}
	
}
