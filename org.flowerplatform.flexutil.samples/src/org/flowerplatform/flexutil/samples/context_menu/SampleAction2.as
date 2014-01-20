package org.flowerplatform.flexutil.samples.context_menu {
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.samples.renderer.MultipleIconItemRendererSample;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleAction2 extends ActionBase {
		public function SampleAction2() {
			super();
			icon = MultipleIconItemRendererSample.infoImage;
			label = "Sample Action 2";
		}
	}
}