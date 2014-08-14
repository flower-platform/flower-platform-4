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
 * @author Mariana Gheorghe
 */
public interface IController extends Comparable<IController> {

	/**
	 * For additive controllers, specifies the order. Lower values are invoked first.
	 * 
	 * <p>
	 * It's recommended to use big values e.g. 50 000, -100 000. So that new indexes can be
	 * added in the future.
	 */
	int getOrderIndex();

	/**
	 * @see #getOrderIndex()
	 */
	void setOrderIndex(int orderIndex);

}