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
package org.flowerplatform.flex_client.core.node {
	
	/**
	 * Implemented within the host system. The implementation should invoke the 
	 * service methods on the server.
	 * 
	 * @author Cristina Constantinescu
	 */
	// TODO CS: rename IHostServiceInvocator
	public interface IServiceInvocator {
	
		function invoke(serviceIdAndMethodName:String, parameters:Array = null, resultCallback:Function = null, faultCallback:Function = null):void;
		
	}
}