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
package org.flowerplatform.flex_client.mindmap.renderer {
	import flash.events.Event;
	
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRenderer;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class NodeMindMapRenderer extends MindMapRenderer {
		
		public function NodeMindMapRenderer() {
			super();
			featureForValuesProvider = CoreConstants.MIND_MAP_FEATURE_FOR_VALUES_PROVIDER;
		}
		
		override protected function beginModelListen():void {
			super.beginModelListen();
			MindMapEditorDiagramShell(mindMapDiagramShell).editorFrontend.addEventListener(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER, showPropertiesInRendererChangedHandler);
			showPropertiesInRendererChangedHandler(null);
		}
		
		override protected function endModelListen():void {
			MindMapEditorDiagramShell(mindMapDiagramShell).editorFrontend.removeEventListener(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER, showPropertiesInRendererChangedHandler);
			Node(data).properties.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertiesChangedHandler);
			super.endModelListen();
		}
		
		protected function showPropertiesInRendererChangedHandler(event:Event):void {
			if (MindMapEditorDiagramShell(mindMapDiagramShell).editorFrontend.shouldDisplayPropertiesInRenderer(Node(data))) {
				// begin listening if not already listening
				Node(data).properties.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertiesChangedHandler);
				propertiesChangedHandler(null);
			} else {
				// end listening
				Node(data).properties.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertiesChangedHandler);
				propertyEntries = null;
			}
		}
		
		protected function propertiesChangedHandler(event:PropertyChangeEvent):void {
			var context:Object = null;
			if (MindMapEditorDiagramShell(mindMapDiagramShell).editorFrontend.showPropertiesInRendererInternal) {
				context = new Object();
				context[FlexUtilConstants.PROPERTIES_CONTEXT_INCLUDE_PROPERTIES_WITHOUT_DESCRIPTOR] = true;
			}
			propertyEntries = PropertiesPlugin.getInstance().propertiesHelper.getPropertyEntries(context, typeDescriptorRegistry, data);
		}
	}
}