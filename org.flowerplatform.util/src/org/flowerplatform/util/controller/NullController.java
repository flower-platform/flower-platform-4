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

/**
 * Works as an "ignore" controller.
 * 
 * <p>
 * When set as a controller with the lowest <code>orderIndex</code> in his list,
 * type descriptor will consider that it doesn't have any controllers registered for that type
 * and it will return <code>null</code> when calling {@link TypeDescriptor#getSingleController(String, Object)}.
 * 
 * <p>
 * Note: must be added ONLY as a single controller.
 * 
 * @see TypeDescriptor#getCachedSingleController(String, Object, boolean)
 * @author Cristina Constantinescu
 */
public class NullController extends AbstractController {

}