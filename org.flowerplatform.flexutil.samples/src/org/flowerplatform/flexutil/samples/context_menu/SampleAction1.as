package org.flowerplatform.flexutil.samples.context_menu {
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.samples.renderer.MultipleIconItemRendererSample;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleAction1 extends ActionBase {
		public function SampleAction1() {
			super();
			icon = MultipleIconItemRendererSample.infoImage;
			preferShowOnActionBar = true;
		}
		
		override public function get label():String {
			if (selection == null || selection.length == 0) {
				return "Action for: empty";
			} else {
				return "Action for: " + selection.getItemAt(0);
			}
		}
		
	}
}