package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.ArrayList;
	import mx.utils.object_proxy;
	
	/**
	 * Wrapper for the <code>rootModel</code> of a <code>MindMapDiagramShell</code>.
	 * 
	 * <p>
	 * Stores:
	 * <ul>
	 * 	<li> the initial model given as rootModel
	 * 	<li> the list of visual children displayed as children for <code>DiagramRenderer</code>.
	 * </ul>
	 * 
	 * @see MindMapRootModelChildren.getChildren(model)
	 * @see MindMapDiagramShell.set rootModel(value)
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class MindMapRootModelWrapper {
		
		public static const ID:String = "mindMapRootModelWrapper";
		
		public var model:Object;
		
		public var children:ArrayList;
		
		public function MindMapRootModelWrapper(model:Object) {
			this.model = model;	
		}
		
	}
}