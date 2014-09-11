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
package org.flowerplatform.util;


/**
 * Callback similar to {@link Runnable} or {@link Callable}, 
 * that takes a param and returns a value.
 * 
 * @author Cristian Spiescu
 * @param <R> The type of the result. Can be {@link Void} if not used.
 * @param <P> The type of the parameter. Can be {@link Void} if not used.
 */
public interface RunnableWithParam<R, P> {
	/**
	 *@author see class
	 **/
	R run(P param);

}