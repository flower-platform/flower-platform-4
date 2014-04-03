package org.flowerplatform.flexutil.spinner {
	import mx.controls.Image;

	/**
	 * Spinner with logo.
	 * @author Cristina Constantinescu
	 */ 
	public class LogoSpinner extends ModalSpinner {
	
		private var logoIcon:Class;
		
		public function LogoSpinner(logoIcon:Class) {
			this.logoIcon = logoIcon;
		}
					
		override protected function createChildren():void {
			super.createChildren();
			
			spinner.size = 46;
			
			var icon:Image = new Image();
			icon.source = logoIcon;
			addChildAt(icon, 0);			
		}
		
	}
}