package org.flowerplatform.flex_client.properties {
	import flash.events.FocusEvent;
	
	import mx.graphics.SolidColorStroke;
	
	import org.flowerplatform.flex_client.properties.property_renderer.BasicPropertyRenderer;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.layouts.HorizontalLayout;
	import spark.primitives.Line;

	/**
	 * @author Razvan Tache
	 */
	public class PropertiesItemRenderer extends DataRenderer {
		public var nameOfProperty:Label;
				
		public var itemRenderer:Class;
		
		public var itemRenderedInstance:BasicPropertyRenderer;
		
		public var colors:Array = new Array();
		public var alphas:Array = new Array();
		
		public var ratios:Array = new Array();
		public var gradientBoxRotation:Number = 0;
		
		public function PropertiesItemRenderer() {
			super();	
			
			colors.push(0xCCCCCC);
			alphas.push(0.4);
		}	
		
		override public function set data(value:Object):void {
			// data being bindable, whenenver it changes it enters here and loses all past informations
			// by checkig this, we make sure that the past event doesn't happen
			if (super.data == value) {
				return;
			}
			
			super.data = value;
			nameOfProperty.text = data.name;
			
			if (itemRenderedInstance != null) {
				removeElement(itemRenderedInstance);
			}
			var factory:FactoryWithInitialization = FactoryWithInitialization(PropertiesPlugin.getInstance().propertyRendererClasses[data.type]);
			if (factory == null) {
				factory = PropertiesPlugin.getInstance().propertyRendererClasses[null];
			}
			if (factory != null) {
				itemRenderedInstance = factory.newInstance(false);
			}
			addElement(itemRenderedInstance);
			
		}
		
		override protected function createChildren():void {
			super.createChildren();		
			
			var horizLayout:HorizontalLayout = new HorizontalLayout;
			horizLayout.gap = 0;
			layout = horizLayout;
			nameOfProperty = new Label;
			
			nameOfProperty.percentWidth = 50;
			nameOfProperty.percentHeight = 100;
			nameOfProperty.setStyle("paddingTop", "5");
			nameOfProperty.setStyle("paddingLeft", "3");
				
			var line:Line = new Line;
			line.percentHeight = 100;
			line.stroke = new SolidColorStroke();
			
			addElement(nameOfProperty);
			addElement(line);
		}
				
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();	
			
			graphics.lineStyle(1);
			
			drawPlaceHolder(unscaledWidth, unscaledHeight);
		}
		
		protected function drawPlaceHolder(width:Number, height:Number):void {
			graphics.drawRect(0, 0, nameOfProperty.width, height);
		}
		
	}
}