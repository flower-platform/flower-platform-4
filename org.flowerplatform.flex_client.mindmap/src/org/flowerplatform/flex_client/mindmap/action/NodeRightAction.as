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
package org.flowerplatform.flex_client.mindmap.action {
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRightAction extends AbstractNavigateAction {
		
		public function NodeRightAction(appendNodesToCurrentSelection:Boolean = false) {
			super(Resources.getMessage("mindmap.right"), appendNodesToCurrentSelection);			
		}
		
		override protected function getNodes(node:Node, context:DiagramShellContext):Array {
			return getNodes_navigateLeftRight(node, context, MindMapDiagramShell.POSITION_RIGHT);
		}

	}
}