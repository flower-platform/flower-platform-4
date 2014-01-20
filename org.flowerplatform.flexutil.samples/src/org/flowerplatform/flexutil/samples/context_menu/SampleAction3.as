package org.flowerplatform.flexutil.samples.context_menu {
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.samples.renderer.MultipleIconItemRendererSample;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleAction3 extends ActionBase {
		public function SampleAction3() {
			super();
			icon = MultipleIconItemRendererSample.defaultImage;
			label = "Sample Action 3";
		}
	}
}