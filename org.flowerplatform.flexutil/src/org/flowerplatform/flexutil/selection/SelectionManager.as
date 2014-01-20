package org.flowerplatform.flexutil.selection {
	import flash.events.EventDispatcher;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	/**
	 * Holds the active <code>ISelectionProvider</code>, and receives notifications of VH activated and selection
	 * changed, delegating to the corresponding <code>IViewHost</code> and then dispatching events.
	 * 
	 * @author Cristian Spiescu
	 */
	public class SelectionManager extends EventDispatcher {

		public var activeSelectionProvider:ISelectionProvider;
		
		public var timestamp:Number;
		
		public var timerAfterFocusIn:Timer = new Timer(250, 1);
		
		protected var viewHostPreparedForSelectionChangedInvocation:IViewHost;
		
		protected var viewContentPreparedForSelectionChangedInvocation:IViewContent;
		
		public function SelectionManager() {
			timerAfterFocusIn.addEventListener(TimerEvent.TIMER_COMPLETE, timerAfterFocusInHandler);
		}
		
		/**
		 * Invoked by <code>IViewHosts</code> when a view becomes active: on init, on view change (if there are 
		 * multiple VCs) or via focus in style handler. 
		 * 
		 * <p>
		 * When <code>viaFocusIn</code> is <code>true</code> the <code>timerAfterFocusIn</code> is started.
		 */
		public function viewContentActivated(viewHost:IViewHost, viewContent:IViewContent, viaFocusIn:Boolean):void {
			if (viewHost != null) {
				if (viewContent != null) {
					viewContent.viewHost = viewHost;
				}
				if (viewContent is ISelectionProvider) {
					activeSelectionProvider = ISelectionProvider(viewContent);
				}		
			} else {
				// VC closed
				activeSelectionProvider = null;
			}
			
			if (viaFocusIn) {
				if (timerAfterFocusIn.running) {
					timerAfterFocusIn.reset();
				}
				timerAfterFocusIn.start();
			}
			selectionChanged(viewHost, viewContent);
		}
		
		/**
		 * Invoked by <code>IViewHost</code>s when the view closes.
		 */ 
		public function viewContentRemoved(viewHost:IViewHost, viewContent:IViewContent):void {
			if (activeSelectionProvider == viewContent) {
				viewContentActivated(null, null, true);
			}
		}
		
		protected function timerAfterFocusInHandler(event:TimerEvent):void {
			selectionChanged(viewHostPreparedForSelectionChangedInvocation, viewContentPreparedForSelectionChangedInvocation);
			viewHostPreparedForSelectionChangedInvocation = null;
			viewContentPreparedForSelectionChangedInvocation = null;
		}
		
		/**
		 * Delegates to the VH (so that the actions and selection can be retrieved/processed) and 
		 * dispatches <code>SelectionChangedEvent</code>.
		 * 
		 * <p>
		 * If <code>timerAfterFocusIn</code> is running, i.e. an activation via focus in has just been made,
		 * then the call will be rescheduled. If during the timer period, there are other invocations of this method,
		 * then the last VC will be remebered (and used when the timer expires.
		 * 
		 * <p>
		 * This timer mechanism exists, because when clicking on a VH, usually we get 2 calls: the first via the focus
		 * in handler, and the second via the selection changed handler. In this case, we want to optimize, and have only
		 * one processing for the selection/actions. However, there are cases, when clicking on a VH doesn't trigger the 
		 * selection changed listener; so we need the focus in mechanism as well.
		 */
		public function selectionChanged(viewHost:IViewHost, selectionProvider:IViewContent):void {
			if (timerAfterFocusIn.running) {
				// an invocation caused by "focusIn" has recently been fired; don't do anything
				// now; reschedule
				viewHostPreparedForSelectionChangedInvocation = viewHost;
				viewContentPreparedForSelectionChangedInvocation = selectionProvider;
				return;
			}
			
			if (viewHost != null) {
				var selection:IList = viewHost.selectionChanged();
			}
			
			if (activeSelectionProvider == selectionProvider) {
				// this "if" is intended for a case like the following: there are 2 ViewContent/SelectionProviders;
				// the user clicks on VC1 and selects/deselects. So VC1 is the activeSelectionProvider.
				// Meanwhile, the selection is changed programmatically in VC2. In this case, we don't need to dispatch,
				// because the "global" selection still comes from VC1
				var event:SelectionChangedEvent = new SelectionChangedEvent();
				event.selectionProvider = activeSelectionProvider;
				event.selection = selection;
				if (selectionProvider is ISelectionForServerProvider) {
					event.selectionForServer = ISelectionForServerProvider(selectionProvider).convertSelectionToSelectionForServer(selection);
				}
				dispatchEvent(event);
			}
		}
		
	}
}