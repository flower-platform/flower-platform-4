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
package org.flowerplatform.flex_client.web {
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.layout.MindMapEditorProvider;
	import org.flowerplatform.flex_client.core.mindmap.layout.MindMapPerspective;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import spark.components.Button;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class WebPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:WebPlugin;
		
		public static function getInstance():WebPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;			
		}
		
		override public function start():void {
			super.start();
								
			CorePlugin.getInstance().getPerspective(MindMapPerspective.ID).resetPerspective(FlexUtilGlobals.getInstance().workbench);
			
			var hBox:HBox = new HBox();
			hBox.percentWidth = 100;
						
			var btn:Button = new Button();
			btn.label = "Open Editor";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				var view:ViewLayoutData = new ViewLayoutData();
				view.isEditor = true;
				view.viewId = MindMapEditorProvider.ID;
				FlexUtilGlobals.getInstance().workbench.addEditorView(view, true);
			});
			hBox.addChild(btn);
						
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(hBox, 0);			
		}
		
		override protected function registerMessageBundle():void {			
		}	
	
	}
}
