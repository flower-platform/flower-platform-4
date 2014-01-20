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
	
	import flash.net.registerClassAlias;
	
	import mx.utils.DescribeTypeCache;
	
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class AbstractFlexPlugin {
	
		protected var _flexPluginDescriptor:FlexPluginDescriptor;
		
		/**
		 * @see handleConnectedToServer()
		 */
		protected var serverParameters:Object;
		
		public function get flexPluginDescriptor():FlexPluginDescriptor {
			return _flexPluginDescriptor;
		}
		
		public function set flexPluginDescriptor(value:FlexPluginDescriptor):void {
			_flexPluginDescriptor = value;
		}
		
		/**
		 * The start mechanism is: call preStart() for all plugins and then call start() for all plugins.
		 * 
		 * <p>
		 * The plugin should init (and check) it's singleton access point (i.e. INSTANCE / getInstance()).
		 * It should create any extension points (e.g. create list of IDataProvider), and declare extensions
		 * (e.g. add an IDataProvider into a plugin that it depends on).
		 * 
		 * <p>
		 * Plugins MUST NOT process the extension points here. This should be done in start()
		 * 
		 * <p>
		 * Subclasses should call super at the beginning.
		 */
		public function preStart():void {
			// nothing to do (yet)
		}
		
		/**
		 * The start mechanism is: call preStart() for all plugins and then call start() for all plugins.
		 * 
		 * <p>
		 * If the plugin declares extension points, it should process them here (e.g. iterate the list
		 * of IDataProvider and do something with those elements). The start mechanism (see preStart()) ensures that
		 * by the time this method is called, all other plugins have registered already their extensions.
		 *
		 * <p> 
		 * Subclasses should call super at the beginning.
		 */
		public function start():void {
			// nothing to do (yet)
		}
		
		/**
		 * Registers the given class with an alias defined in the RemoteClass annotation (e.g. 
		 * <code>[RemoteClass(alias="my.custom.alias")]</code>. If alias is not defined,
		 * it uses the fully qualified name of the given class as alias (e.g. <code>
		 * [RemoteClass]</code>.
		 * 
		 * <p>
		 * <b>NOTE: </b> Don't forget to add to the current SWC the following
		 * compiler option: -keep-as3-metadata+=RemoteClass
		 */ 
		protected function registerClassAliasFromAnnotation(flexClass:Class):void {
			var flexClassInfo:XML = DescribeTypeCache.describeType(flexClass).typeDescription;
			var remoteClassAnnotation:XMLList = flexClassInfo.factory.metadata.(@name == "RemoteClass");
			
			if (remoteClassAnnotation.length() == 0) {
				throw new Error("The class " + Utils.getClassNameForObject(flexClass, true) + " should be annotated with '[RemoteClass]' or '[RemoteClass(alias='my.custom.alias')]'");
			}
					
			var alias:String = remoteClassAnnotation.arg.(@key == "alias").@value.toString();
			
			if (alias == null || alias.length == 0) {
				// no RemoteClass annotation => use the fq name of this class
				alias = Utils.getClassNameForObject(flexClass, true).replace("::", ".");
			}
			registerClassAlias(alias, flexClass);
		}
		
		/**
		 * Called when the client is connected to the server.
		 * Extending plugins should override if they need
		 * any initial server parameters.
		 * 
		 * @author Mariana Gheorghe
		 */
		public function handleConnectedToServer():void {
		}
	}
}