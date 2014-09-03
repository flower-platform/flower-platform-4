package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexutil.controller.IPropertyModelAdapter;

	/**
	 * @author Diana Balutoiu
	 */
	public class SampleMindMapModelPropertyAdapter implements IPropertyModelAdapter {
		
		public function getProperties(model:Object):Object{
			return SampleMindMapModel(model).subModelsDict;	
		}
		
		public function getPropertyValue(model:Object, property:String):Object{
			return SampleMindMapModel(model).subModelsDict[property];	
		}
		
		public function getPropertyValueOrWrapper(model:Object,property:String):*{
			//TODO: needs implementation
			return null;
		}
		
		public function commitPropertyValue(model:Object,propertyValueOrWrapper:Object, value:Object,propertyDescriptorName:String, callbackHandler:Function = null):void{
			//TODO: needs implementation
		}
		
	}
}