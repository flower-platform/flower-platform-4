package org.flowerplatform.flexutil.properties.property_renderer {
	import spark.formatters.DateTimeFormatter;
	
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	
	
	/**
	 * @author Balutoiu Diana
	 * @author Sebastian Solomon
	 * @author Cristina Constantinescu
	 */
	public class DatePropertyRenderer extends StringPropertyRenderer {
		
		
		override public function valueChangedHandler():void {
			var newValue:Object = PropertiesHelper.getInstance().propertyModelAdapter
				.getPropertyValue(_propertyLineRenderer.nodeObject, _propertyLineRenderer.propertyDescriptor.name);
			if (newValue is Date) {
				var dtf:DateTimeFormatter = new DateTimeFormatter();
				dtf.dateTimePattern = "yyyy-MM-dd HH:mm:ss";
				text = dtf.format(newValue);
			}		
		}
	}
}