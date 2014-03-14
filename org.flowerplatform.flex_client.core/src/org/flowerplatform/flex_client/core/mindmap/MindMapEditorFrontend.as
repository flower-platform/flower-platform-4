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
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.DiagramEditorFrontend;
	import org.flowerplatform.flex_client.core.mindmap.action.AddChildActionProvider;
	import org.flowerplatform.flex_client.core.mindmap.update.MindMapNodeUpdateProcessor;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexutil.FactoryWithInitialization;

	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorFrontend extends DiagramEditorFrontend {
			
		public function MindMapEditorFrontend() {
			super();
			
			actionProvider.actionProviders.push(CorePlugin.getInstance().mindmapEditorClassFactoryActionProvider);
			actionProvider.actionProviders.push(new AddChildActionProvider());
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Sebastian Solomon
		 */
		override protected function createDiagramShell():DiagramShell {
			var diagramShell:MindMapEditorDiagramShell = new MindMapEditorDiagramShell();
			diagramShell.updateProcessor = new MindMapNodeUpdateProcessor(diagramShell);
			updateProcessor = diagramShell.updateProcessor;
			return diagramShell;
		}
		
		override protected function createChildren():void {			
			super.createChildren();
			
			var iconSideBarfactory:FactoryWithInitialization = new FactoryWithInitialization(CorePlugin.getInstance().iconSideBarClass, {diagramShell:diagramShell});
			editorArea.addElementAt(iconSideBarfactory.newInstance(false), 0);
		}	
		
	}
}
