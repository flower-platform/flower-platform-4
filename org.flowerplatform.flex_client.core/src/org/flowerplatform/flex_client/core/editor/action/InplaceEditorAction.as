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
package org.flowerplatform.flex_client.core.editor.action {
	import flash.ui.Keyboard;
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.core.editor.DiagramEditorFrontend;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.shortcut.Shortcut;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InplaceEditorAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.core.editor.action.InplaceEditorAction";
		
		public function InplaceEditorAction() {
			super();
			
			FlexUtilGlobals.getInstance().keyBindings.registerBinding(new Shortcut(false, false, false, Keyboard.F2), id);
		}
			
		override public function get showInMenu():Boolean {
			return false;
		}
		
		override public function get visible():Boolean {
			// get current active editor
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
			var editor:UIComponent = workbench.getEditorFromViewComponent(workbench.getActiveView());
			
			if (!(editor is DiagramEditorFrontend)) { // not a diagram editor
				return false;
			}
			
			var diagramShell:DiagramShell = DiagramEditorFrontend(editor).diagramShell;
			return diagramShell != null && // may happen in mobile, at startup
				diagramShell.mainSelectedItem != null && // has at least one node selected 
				diagramShell.mainSelectedItem is Node &&
				ControllerUtils.getInplaceEditorController(diagramShell.getNewDiagramShellContext(), diagramShell.mainSelectedItem) != null;
		}	
		
		override public function run():void {
			// get current active editor
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
			var editor:DiagramEditorFrontend = DiagramEditorFrontend(workbench.getEditorFromViewComponent(workbench.getActiveView()));
			
			editor.diagramShell.mainTool = editor.diagramShell.tools[InplaceEditorTool];
		}
		
	}
}