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
package com.crispico.flower.util.spinner {
	
	/**
	 * Should be implemented by <code>Container</code> classes that don't have
	 * absolute layout, and that want to be able to host a <code>ModalSpinner</code>.
	 * 
	 * <p>
	 * Responsabilities:
	 * <ul>
	 * 	<li>have a backing field for the <code>modalSpinner</code> property
	 * 	<li>should override <code>updateDisplayList</code> and call <code>setActualSize()</code>
	 * 		for the <code>modalSpinner</code> property (if present). Other logic may be executed
	 * 		there as well (e.g. reposition the component to not cover the tile bar).
	 * <ul>
	 *
	 * @author Cristi
	 */
	public interface ModalSpinnerSupport {
		
		function get modalSpinner():ModalSpinner;
		
		function set modalSpinner(value:ModalSpinner):void;
	}
}