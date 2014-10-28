package org.flowerplatform.flexdiagram.samples.properties
{
	import mx.messaging.channels.AMFChannel;
	
	import spark.components.Form;
	
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	import org.flowerplatform.flexdiagram.samples.mindmap.SampleMindMapDiagramShell;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	
	public class PropertiesForm extends Form {
		
		private var _shouldRefreshVisualChildren:Boolean;
		
		private var visualChildrenController:SequentialLayoutVisualChildrenController = new SequentialLayoutVisualChildrenController();
		
		private var sampleMindMapDiagramShell:SampleMindMapDiagramShell = new SampleMindMapDiagramShell();
		
		private var propertiesHelper:PropertiesHelper = new SamplePropertiesHelper();
		
		private var _data:Object;
		
		public function PropertiesForm()
		{
			super();
		}

		public function get data():Object
		{
			return _data;
		}

		public function set data(value:Object):void
		{
			_data = value;
			shouldRefreshVisualChildren = true;
			invalidateDisplayList();
		}

		public function get shouldRefreshVisualChildren():Boolean {
			return _shouldRefreshVisualChildren;
		}

		public function set shouldRefreshVisualChildren(value:Boolean):void {
			_shouldRefreshVisualChildren = value;
		}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			// this variable is managed by the controller only for the "normal" mode; i.e. not stand alone
			if (shouldRefreshVisualChildren) {
				visualChildrenController.refreshVisualChildrenDiagramOrStandAlone(null, sampleMindMapDiagramShell.registry, this, data,
					propertiesHelper.getPropertyEntries(sampleMindMapDiagramShell.registry, data, true));
				shouldRefreshVisualChildren = false;
			}
		}
	}
}