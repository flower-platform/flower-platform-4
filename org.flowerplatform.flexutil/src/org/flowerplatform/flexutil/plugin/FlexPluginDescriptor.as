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
package org.flowerplatform.flexutil.plugin {
	import flash.display.Loader;
	import flash.events.Event;
	import flash.net.URLLoader;
	
	/**
	 * @author Cristi
	 */
	public class FlexPluginDescriptor {
		public var url:String;
		public var urlLoader:URLLoader;
		public var downloadFinished:Boolean;
		public var loader:Loader;
		public var errorObject:Object;
		public var flexPlugin:AbstractFlexPlugin;
	}
}