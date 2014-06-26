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
package  org.flowerplatform.flex_client.core.editor {
	
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * Abstract class; should be subclassed.
	 * 
	 * <p>
	 * Acts as a <code>BasicEditorDescriptor</code> and as
	 * a <code>IViewProvider</code>.
	 * 
	 * <p>
	 * Holds specific information for an editor (e.g.
	 * name and icon URL that will appear in the 
	 * context menu item) and triggers the editor open,
	 * based on an editor input.
	 * 
	 * @author Cristi
	 * @author Mariana
	 * 
	 */
	public class EditorDescriptor extends BasicEditorDescriptor implements IViewProvider {
		
		private static const OPEN_FORCED_BY_SERVER_EDITOR_INPUT_MARKER:String = "???___open_forced_by_server";
		
		/**
		 * Abstract method.
		 * 
		 * 
		 */
		public function getId():String {
			throw new Error("This method should be implemented.");
		}
		
		/**
		 * Abstract method.
		 * 
		 * <p>
		 * Should create a new (unpopulated) instance of the corresponding
		 * <code>EditorFrontend</code>.
		 * 
		 * 
		 */
		protected function createViewInstance():EditorFrontend {
			throw new Error("This method should be implemented.");
		}
		
		/**
		 * Adds a new view in the global <code>Workbench</code>, which will then
		 * trigger the creation of the view (i.e. <code>EditorFrontend</code>).
		 * 
		 * @author Cristian Spiescu
		 * @author Mariana Gheorghe
		 * @author Cristina Constantinescu
		 */
		override public function openEditor(editableResourcePath:String, preferNewEditor:Boolean = false,
											forceNewEditor:Boolean = false, openForcedByServer:Boolean = false, 
											handleAsClientSubscription:Boolean = false):UIComponent {
			var viewLayoutData:ViewLayoutData = new ViewLayoutData();
			viewLayoutData.viewId = getId();
			viewLayoutData.customData = editableResourcePath;
			
			// we need to pass this info to .createView(), so using a suffix is the only way
			if (openForcedByServer) {
				viewLayoutData.customData += OPEN_FORCED_BY_SERVER_EDITOR_INPUT_MARKER;
			}
						
			viewLayoutData.isEditor = true;
			
			return FlexUtilGlobals.getInstance().workbench.addEditorView(viewLayoutData, true);		
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {			
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}

		/**
		 * Creates the view (i.e. <code>EditorFrontend</code>) and delegates to the
		 * <code>EditorFrontendController</code> for opening (creates one if none exists for the current
		 * editorInput).
		 * 
		 * 
		 */
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {	
			var editor:EditorFrontend = createViewInstance();
			
			var openForcedByServer:Boolean = false;
			if (viewLayoutData.customData.indexOf(OPEN_FORCED_BY_SERVER_EDITOR_INPUT_MARKER) >= 0) {
				// an open forced by server => cleanup the editorInput
				viewLayoutData.customData = viewLayoutData.customData.replace(OPEN_FORCED_BY_SERVER_EDITOR_INPUT_MARKER, "");
				openForcedByServer = true;
			}
						
			editor.editorInput = viewLayoutData.customData;
			return editor;
		}
		
	}
}
