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
	import flash.display.LoaderInfo;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.events.TextEvent;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.system.ApplicationDomain;
	import flash.system.LoaderContext;
	import flash.utils.ByteArray;
	import flash.utils.getDefinitionByName;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * Loads a list of plugins.
	 * 
	 * <p>
	 * The first phase is downloading the SWF files. This is an async operation, so it is done in parallel.
	 * I.e. we start downloading all the SWF files at the same time.
	 * 
	 * <p>
	 * The second phase is doing the actual load of the byte code (i.e. merge it with the application). This is an async
	 * operation as well, but we do it sequentially. I.e. load a plugin, only after the previous plugin has finished downloading.
	 * We do this, because a plugin (e.g. index = 4) may depend on another plugin with a lower index (e.g. index = 3). If we did
	 * the loading in parallel, the plugin with index = 4 may load quicker than the one with index = 3. In this case, the Flash
	 * Player would generate a <code>VerifyError</code> because classes that were needed are not available. The API has methods
	 * for loading SWF content directly (i.e. phase 1 + 2), but the issue that we mentioned may occur.
	 * 
	 * <p>
	 * The thirds phase is starting the plugins (i.e. invoking the <code>start()</code> method. This is done after all plugins have
	 * finished loading.
	 * 
	 * <p>
	 * We try to handle the case when one/several plugins fail to load (e.g. url error, exception durint <code>start()</code>, etc.). However,
	 * if the failed plugin is being depended on, the app probably cannot work properly. 
	 * 
	 * <p>
	 * <b>NOTE: </b> Phase 3 waits for phase 2 to finish. Phase 2 doesn't wait for phase 1 to finish. When the first plugin has
	 * finished downloading, we start it's loading immediately (even if there are other plugins that are still being downloaded). 
	 * 
	 * @author Cristi
	 */
	public class FlexPluginLoadingSession	{
	
		protected var flexPluginManager:FlexPluginManager;
		
		protected var descriptors:ArrayCollection;
		
		protected var indexOfNextPluginToLoad:int;
		
		protected var callbackFunction:Function;
		
		protected var callbackObject:Object;
		
		public static const classNameRegEx:RegExp = new RegExp("/([^/]*?)\\.swc");
		
		public function FlexPluginLoadingSession(flexPluginManager:FlexPluginManager) {
			this.flexPluginManager = flexPluginManager;
		}
		
		protected function validateDuplicatesDontExist(pluginSwcUrls:ArrayCollection):void {
			var list:ArrayCollection = new ArrayCollection();
			for each (var entry:FlexPluginDescriptor in flexPluginManager.flexPluginEntries) {
				list.addItem(getClassNameForFlexPluginEntry(entry.url));
			}
			for each (var url:String in pluginSwcUrls) {
				list.addItem(getClassNameForFlexPluginEntry(url));
			}
			var sort:Sort = new Sort();
			sort.fields = [new SortField()];
			list.sort = sort;
			list.refresh();
			
			for (var i:int = 0; i < list.length - 1; i++) {
				if (list[i] == list[i + 1]) {
					throw new Error("Duplicate plugins exist: " + list[i] + ". Either among the plugins that you want to load, or among them and the plugins already loaded");
				}
			}
		}
		
		public function loadPlugins(pluginSwcUrls:ArrayCollection, callbackFunction:Function = null, callbackObject:Object = null):void {
			if (descriptors != null) {
				throw new Error("Invalid state. This plugin loading session has already been used. A new instance is necessary");
			} 
			
			descriptors = new ArrayCollection();
			this.callbackFunction = callbackFunction;
			this.callbackObject = callbackObject;
			
			validateDuplicatesDontExist(pluginSwcUrls);
			
			for (var i:int = 0; i < pluginSwcUrls.length; i++) {
				var descriptor:FlexPluginDescriptor = new FlexPluginDescriptor();
				descriptor.url = pluginSwcUrls[i];
				descriptors.addItem(descriptor);
				
				descriptor.urlLoader = new URLLoader();
				descriptor.urlLoader.dataFormat = URLLoaderDataFormat.BINARY;
				descriptor.urlLoader.addEventListener(Event.COMPLETE, downloadHandler);
				descriptor.urlLoader.addEventListener(IOErrorEvent.IO_ERROR, downloadHandler);
				descriptor.urlLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, downloadHandler);
				descriptor.urlLoader.load(new URLRequest(FlexUtilGlobals.getInstance().createAbsoluteUrl(descriptor.url)));
				
			}
		}

		/**
		 * Handler called for download events: good or bad.
		 */
		protected function downloadHandler(event:Event):void {
			var urlLoader:URLLoader = URLLoader(event.target);
			var descriptor:FlexPluginDescriptor = null;
			
			// find the correspnding entry
			for (var indexOfDownloadedPlugin:int = 0; indexOfDownloadedPlugin < descriptors.length; indexOfDownloadedPlugin++) {
				var currentEntry:FlexPluginDescriptor = FlexPluginDescriptor(descriptors[indexOfDownloadedPlugin]);
				if (currentEntry.urlLoader == urlLoader) {
					descriptor = currentEntry;
					break;
				}
			}
			if (descriptor == null) {
				throw new Error("Received a download event for a plugin descriptor that is not in the list");
			}
			
			// remove the listeners; maybe in the future we dispose the entry, so we need to remove these refs, for the garbage collector
			descriptor.urlLoader.removeEventListener(Event.COMPLETE, downloadHandler);
			descriptor.urlLoader.removeEventListener(IOErrorEvent.IO_ERROR, downloadHandler);
			descriptor.urlLoader.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, downloadHandler);
			
			descriptor.downloadFinished = true;
			
			if (event.type != Event.COMPLETE) {
				// one of the error events; mark the error
				descriptor.errorObject = event;
			}
			
			if (indexOfNextPluginToLoad == indexOfDownloadedPlugin) {
				loadDownloadedPluginThatWasExpected(descriptor);
			}
		}
		
		/**
		 * Loads the plugin bytecode, from urlLoader.bytes. If the download had a problem, this method
		 * works as well, trying to continue the flow. 
		 * 
		 * <p>
		 * However, if the failed plugin was dependend on => probably the app became invalid.
		 * 
		 * <p>
		 * Assertion: indexOfNextPluginToLoad == index of this plugin
		 */
		protected function loadDownloadedPluginThatWasExpected(descriptor:FlexPluginDescriptor):void {
			if (descriptor.errorObject != null) {
				// in this case, don't try to load it, and pass to the other one(s)
				// anyway, if other plugins depend on it, the app is useless, but we continue, hoping
				// that no other plugin was dependent on it
				loadHandler(null, descriptor);
			} else {
				// normal case: start the load (which is async)
				var c:LoaderContext = new LoaderContext();
				c.applicationDomain = ApplicationDomain.currentDomain;
				
				descriptor.loader = new Loader();
				descriptor.loader.contentLoaderInfo.addEventListener(Event.COMPLETE, loadHandler);
				descriptor.loader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, loadHandler);
				descriptor.loader.contentLoaderInfo.addEventListener(SecurityErrorEvent.SECURITY_ERROR, loadHandler);
				
				descriptor.loader.loadBytes(ByteArray(descriptor.urlLoader.data), c);
			}
		}
		
		/**
		 * Handler called for load events, good or bad. This method is also called directly 
		 * (i.e. not as a handler, for a failed download). In this case, event == null, but descriptor should be provided.
		 */
		protected function loadHandler(event:Event, descriptor:FlexPluginDescriptor = null):void {
			if (descriptor == null) {
				var info:LoaderInfo = LoaderInfo(event.target);
				descriptor = descriptors[indexOfNextPluginToLoad];
				
				if (descriptor.loader != info.loader) {
					throw new Error("We were expecting the load of plugin " + descriptor.url + " but another loader reported that load has finished. This is odd/illegal, because downloading is parallel, but loading is sequential");
				}
			}
			// remove the listeners; maybe in the future we dispose the entry, so we need to remove these refs, for the garbage collector
			if (descriptor.loader != null) {
				descriptor.loader.contentLoaderInfo.removeEventListener(Event.COMPLETE, loadHandler);
				descriptor.loader.contentLoaderInfo.removeEventListener(IOErrorEvent.IO_ERROR, loadHandler);
				descriptor.loader.contentLoaderInfo.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, loadHandler);
			}
			
			if (event != null && event.type != Event.COMPLETE) {
				// one of the error events; mark the error
				descriptor.errorObject = event;
			}
			
			indexOfNextPluginToLoad++;
			if (indexOfNextPluginToLoad >= descriptors.length) {
				// all plugins have finished loading
				startPlugins();
			} else {
				// peek to see if the next plugin has finished downloading, so that we can start loading
				var nextDescriptor:FlexPluginDescriptor = descriptors[indexOfNextPluginToLoad];
				if (nextDescriptor.downloadFinished) {
					// recurse to the next descriptor, i.e. simulate a complete event
					loadDownloadedPluginThatWasExpected(nextDescriptor);
				}
			}
			
		}
		
		protected function startPlugins():void {
			var failedEntries:ArrayCollection = new ArrayCollection();

			// setup extension points
			for each (var descriptor:FlexPluginDescriptor in descriptors) {
				if (descriptor.errorObject == null) {
					try {
						var className:String = getClassNameForFlexPluginEntry(descriptor.url);
						var clazz:Class = Class(getDefinitionByName(className));
						descriptor.flexPlugin = new clazz();
						descriptor.flexPlugin.flexPluginDescriptor = descriptor;
						descriptor.flexPlugin.preStart();
						
						flexPluginManager.flexPluginEntries.addItem(descriptor);
					} catch (e:Object) {
						descriptor.errorObject = e;
					}
				}
				
			}
			
			// start plugins
			for each (descriptor in descriptors) {
				if (descriptor.errorObject == null) {
					try {
						className = getClassNameForFlexPluginEntry(descriptor.url);
						clazz = Class(getDefinitionByName(className));
						descriptor.flexPlugin.start();
					} catch (e:Object) {
						descriptor.errorObject = e;
					}
				}
				
				// add to list to display it as a failed plugins
				if (descriptor.errorObject != null) {
					failedEntries.addItem(descriptor);
				}
			}
			
			flexPluginManager.currentLoadingSession = null;
			
			if (failedEntries.length > 0) {
				handlePluginsWithErrors(failedEntries);
			}
			if (callbackFunction != null) {
				callbackFunction.call(callbackObject, descriptors);
			}
		}
		
		protected function getClassNameForFlexPluginEntry(url:String):String {
			var groups:Array = classNameRegEx.exec(url);
			if (groups == null || groups.length != 2) {
				throw new Error("Error getting the class name from the url: " + url + "; tried to apply regex: " + classNameRegEx);
			} 
			var package_:String = groups[1];
			var individualPackages:Array = package_.split(".");
			var lastPackage:String = individualPackages[individualPackages.length - 1];
			
			// do we have custom plugin class?
			var customPluginClassRegEx:RegExp = new RegExp("[\\?&]pluginClass=(.*?)\\z|&");
			var customPluginClassRegExGroups:Array = customPluginClassRegEx.exec(url);
			if (customPluginClassRegExGroups != null && customPluginClassRegExGroups.length == 2) {
				return groups[1] + "." + customPluginClassRegExGroups[1];
			} else {
				// no custom class name specified
				var lastPackageWithFirstLetterCapitalized:String = lastPackage.charAt(0).toUpperCase() + lastPackage.substr(1);
				return groups[1] + "." + lastPackageWithFirstLetterCapitalized + "Plugin";
			}
		}
		
		protected function handlePluginsWithErrors(failedEntries:ArrayCollection):void {
			var text:String = "";
			for each (var entry:FlexPluginDescriptor in failedEntries) {
				if (entry.errorObject == null) {
					continue;
				}
				
				text += "Plugin " + getClassNameForFlexPluginEntry(entry.url) + " with url " + entry.url + " has failed to load. The reason:\n";
				if (entry.errorObject is Error) {
					// casting the other way, would display the current stack trace, instead of the stack trace when the error
					// happened
					text += (entry.errorObject as Error).message + "\n";
					text += (entry.errorObject as Error).getStackTrace() + "\n";
				} else if (entry.errorObject is TextEvent) {
					text += TextEvent(entry.errorObject).text + "\n";
				} else {
					text += entry.errorObject;
				}
				text += "\n";
			}
			
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(FlexUtilAssets.INSTANCE.getMessage("plugin.errorPopup.title"))
				.setText(text)
				.setWidth(800)
				.setHeight(500)
				.setWordWrap(false)
				.showMessageBox();
		}

	}
}