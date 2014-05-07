package org.flowerplatform.flex_client.properties.property_renderer {
	
	import flash.events.MouseEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.controls.Spacer;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import spark.components.Button;
	import spark.layouts.HorizontalLayout;
	
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.renderer.IIconsComponentExtensionProvider;
	import org.flowerplatform.flexutil.renderer.IconsComponentExtension;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class IconsWithButtonPropertyRenderer extends BasicPropertyRenderer implements IIconsComponentExtensionProvider, IDialogResultHandler {

		protected var iconsComponentExtension:IconsComponentExtension;
		
		/**
		 * Signature: function clickHandler(itemRendererHandler:IDialogResultHandler, propertyName:String, propertyValue:Object):void
		 */ 
		public var clickHandler:Function;
		
		/**
		 * Signature: function getNewIconsPropertyHandler(dialogResult:Object):String
		 */ 
		public var getNewIconsPropertyHandler:Function;
		
		private var currentValue:String;
		
		public function IconsWithButtonPropertyRenderer() {
			super();
			iconsComponentExtension = new IconsComponentExtension(this);
			
			var hLayout:HorizontalLayout = new HorizontalLayout();
			hLayout.gap = 5;
			hLayout.paddingBottom = 2;
			hLayout.paddingTop = 2;
			hLayout.paddingLeft = 2;
			hLayout.paddingRight = 2;
			hLayout.verticalAlign = "middle";
			
			this.layout = hLayout;
			
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);			
		}
		
		public function getMainComponent():UIComponent {
			return this;
		}
		
		public function get icons():FlowerArrayList {
			return iconsComponentExtension.icons;
		}
		
		public function set icons(value:FlowerArrayList):void {
			iconsComponentExtension.icons = value;
		}
		
		protected function creationCompleteHandler(event:FlexEvent):void {
			BindingUtils.bindSetter(iconsChanged, data, "value");
		}

		public function iconsChanged(value:Object):void {
			if (value != null) {
				icons = new FlowerArrayList(String(value).split(Utils.ICONS_SEPARATOR));
			} else {
				icons = null;
			}
			currentValue = propertyDescriptor.value as String;
		}
		
		override public function set data(value:Object):void {
			super.data = value;	
			
			if (data != null) {			
				if (propertyDescriptor.value != null) {					
					icons = new FlowerArrayList(String(propertyDescriptor.value).split(Utils.ICONS_SEPARATOR));
				} else {
					icons = null;
				}
			}			
		}
		
		override protected function createChildren():void {
			super.createChildren();

			var spacer:Spacer = new Spacer();	
			spacer.percentWidth = 100;
			addElementAt(spacer, 0);
			
			var button:Button = new Button();			
			button.percentHeight = 100;
			button.label = "...";
			button.width = 32;
			button.addEventListener(MouseEvent.CLICK, clickHandlerInternal);
			
			addElementAt(button, 1);
		}
		
		private function clickHandlerInternal(event:MouseEvent):void {
			clickHandler(this, data.name, data.value);
		}
		
		public function handleDialogResult(result:Object):void {
			currentValue = propertyDescriptor.value as String;
			oldValue = currentValue;
			if (result == null) {
				return;
			}
			var newValue:String = Utils.computeStringTokens(currentValue, Utils.ICONS_SEPARATOR, result.type, result.iconUrl)			

			if (currentValue != newValue) {
				currentValue = newValue;
				saveProperty();
			}
		}
				
		public function newIconIndex():int {
			if (PropertyDescriptor(data).hasChangeCheckbox) {
				return numElements - 3;
			}
			return numElements - 2;
		}
		
		override protected function getValue():Object {
			return currentValue;	
		}
		
		override public function validateDisplayList():void {
			super.validateDisplayList();			
			iconsComponentExtension.validateDisplayList();
		}
		
		override public function validateProperties():void {
			super.validateProperties();
			iconsComponentExtension.validateProperties();
		}
		
		override public function validateSize(recursive:Boolean=false):void	{
			iconsComponentExtension.validateSize();
			super.validateSize(recursive);
		}
		
	}
}
