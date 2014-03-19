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
package org.flowerplatform.flex_client.core.mindmap {
	
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.NodePropertiesConstants;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeTypeProvider;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.update.MindMapNodeUpdateProcessor;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDragTool;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorDiagramShell extends MindMapDiagramShell {
		
		public var updateProcessor:MindMapNodeUpdateProcessor;
				
		public function MindMapEditorDiagramShell() {
			super();
						
			registerTools([ScrollTool, ZoomTool, SelectOnClickTool, MindMapDragTool, InplaceEditorTool]);
		}
	
	}
}
