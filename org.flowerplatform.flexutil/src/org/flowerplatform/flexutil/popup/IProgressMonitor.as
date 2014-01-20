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
package org.flowerplatform.flexutil.popup {
	
	/**
	 * @author Cristina Contantinescu
	 */
	public interface IProgressMonitor {
		
		function setTitle(value:String):IProgressMonitor;
		function setAllowCancellation(value:Boolean):IProgressMonitor;
		function setLabel(value:String):IProgressMonitor;
		function setWorked(value:Number):IProgressMonitor;
		function setTotalWork(value:Number):IProgressMonitor;
		function setHandler(value:IProgressMonitorHandler):IProgressMonitor;
		
		function show():void;
		function close():void;
	}
}