package org.flowerplatform.flexutil.spinner {
	import mx.controls.Image;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;

	/**
	 * Spinner with logo.
	 * @author Cristina Constantinescu
	 */ 
	public class LogoSpinner extends ModalSpinner {
	
		private var logoIcon:Object;
		
		public function LogoSpinner(logoIcon:Object) {
			this.logoIcon = logoIcon;
		}
					
		override protected function createChildren():void {
			super.createChildren();
			
			spinner.size = 46;
			
			var icon:Image = new Image();
			if (logoIcon is String)
				icon.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(logoIcon);
			else
				icon.source = logoIcon;
			addChildAt(icon, 0);			
		}
		
	}
}