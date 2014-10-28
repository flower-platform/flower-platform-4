package org.flowerplatform.flexutil.properties
{
	import spark.components.Form;
	import spark.layouts.FormLayout;
	
	public class PropertiesForm extends Form
	{
		public function PropertiesForm()
		{
			super();
		}
		
		override protected function createChildren():void {
			super.createChildren();
			var layout:FormLayout = new FormLayout();
			layout.gap = -14;
			layout.paddingLeft = 0;
			layout.paddingRight = 0;
			this.layout = layout;
			percentWidth = 100;
			contentGroup.setStyle("top", 0);
			contentGroup.setStyle("bottom", 0);	
		}
		
	}
}