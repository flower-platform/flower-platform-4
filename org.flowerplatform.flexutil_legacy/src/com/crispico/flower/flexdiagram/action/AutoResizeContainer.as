/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package  com.crispico.flower.flexdiagram.action {
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuUtils;
	
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.geom.Point;
	import flash.utils.Timer;
	
	import mx.containers.VBox;
	import mx.core.ScrollPolicy;
	import mx.effects.Fade;
	import mx.effects.Move;
	import mx.effects.Parallel;
	import mx.effects.Resize;
	import mx.effects.effectClasses.FadeInstance;
	import mx.events.EffectEvent;
	/**
	 * Container holding elements that can be resized on mouse over; used for holding menus, toolbars.
	 * By default the resize is done to the right; if there is not enough room, the resize is done towards
	 * left (using a combination of move + resize effects).
	 * 
	 * It has the following responsabilities:
	 * <ul>
	 * 	<li> Detect when the mouse is over the element, the container is resized to maximum width of its element(s), using a resize effect.
	 * 	<li> When the mouse is no longer over the element, the container is resized to the initial element, using a resize effect.
	 * </ul>
	 * 
	 * The user have the following responsabilities:
	 * <ul>
	 * 	<li> set the container of the AutoResizeContainer (see constructor)
	 * 	<li> call the <code>show()</code> method to set the visibility of this container
	 * </ul>
	 * 
	 * The user can also set different styles for the container by overriding the method <code>setStyles()</code> 
	 * 
	 * UPDATE : 
	 * A new functionality is added : when the container is in a minimized state, the resize "toBig" is done after a time delay.
	 * The container has three states :
	 * <ul>
	 * 	<li> minimized 
	 * 	<li> not minimized : resized "toBig"/"toSmall"  
	 * 	<li> minimized waiting for a resize "toBig"  
	 * </ul>
	 * On a rollOver event, if the container is in a minimized state then it passes in second state by starting a timer, otherwise 
	 * the normal behavior is executed.<p>
	 * On a rollOut event, if the timer started at rollOver is running (the expanding wasn't done yet), then it is stoped, 
	 * otherwise the normal behovior is executed.
	 * 
	 * New functionality : if the container is in minimized waiting for a resize "toBig" state and
	 * the mouse is moving in a small area then accelerate the expanding.
	 * 
	 * @see (FD_ARC) activity diagrams for more info
	 * 
	 * @author Ioana
	 * @author Cristina
	 * 
	 */
	public class AutoResizeContainer extends VBox {
		
		protected static const RESIZE_EFFECT_DURATION:Number = 300; //ms
		
		protected static const FADE_EFFECT_DURATION:Number = 300; //ms
		
		protected static const BACKGROUND_ALPHA:Number = 0.98;
		
		/**
		 * Default expand container delay.
		 */ 
		protected static const EXPAND_CONTAINER_DELAY_DEFAULT:Number = 1000; //ms
			
		/**
		 * Default accelerate expand container delay.
		 */
		protected static const ACCELERATE_EXPAND_CONTAINER_DELAY_DEFAULT:Number = 500; //ms
		
		/**
		 * Represents a minimum dimension used by :
		 * <ul>
		 * 	<li> <code>mouseMoveHandler(MouseEvent)</code> to know if the mouse is moving in an acceptable area.
		 * 	<li> <code>changeState()</code> to know where (left/right) the container is resizing. It is added in calculations
		 * 		so that the container doesn't be displayed at parentContainer's border.
		 * </ul> 
		 */ 
		protected static const MIN_DIMENSION:Number = 5; // px		
				
		/**
		 * It is a combination between the move and resize effect, needed to roll and resize the container to the left.
		 */
		protected var resizeToLeftEffect:Parallel;
		
		/**
		 * A normal resize effect.
		 */
		protected var resizeToRightEffect:Resize;
		
		protected var fadeEffect:Fade;
		
		/**
		 * Is true only when the element is minimized. If the resize transition begins
		 * it is set immediatly to false.
		 */
		protected var _isMinimized:Boolean = true;	
		
		/**
		 * If a resize to left has been decided, this flag is true and <code>resizeToLeftEffect</code>
		 * is used. Otherwise <code>resizeToRightEffect</code> is used.
		 */
		protected var isResizeToLeft:Boolean;
		
		protected var preferredResizeToRight:Boolean;
		
		/**
		 * The container where the <code>AutoResizeContainer<code> is added to.
		 * 
		 * 
		 */
		protected var parentContainer:DisplayObjectContainer;
				
		/**
		 * This width is saved on resize to expanded of the object, and is needed so the resize to minimized knows the width to resize to.
		 */
		protected var originalWidth:Number;
		
		/**
		 * This X is saved on resize to expanded of the object.
		 * Is needed so the resize to minimized knows where to replace the object minimized.
		 */ 
		protected var originalX:Number;
		
		protected var useFadeEffect:Boolean;

		/**
		* Needed to know if the resize effect is playing or not.
		*/
		protected var resizeStart:Boolean = false;	
		
		/**
		 * Timer used to delay the expanding state.
		 * Its purpose is to not let the container to expand immediately at mouse over.
		 * <p>
		 * It starts when a ROLL_OVER event is dispatched (mouse moves over this container)
		 * and the container is in minimized state.
		 
		 * <p>
		 * It stops when a ROLL_OUT event is dispached (mouse moves out the container)
		 * and it was already started 
		 * OR if the container is notified to be expended immediately, without waiting
		 * to pass this timer's delay.
		 * 
		 * @see #rollOutHandler()
		 * @see #rollOverHandler()
		 * @see #notifyToExpand()
		 * @author Cristina
		 */ 
		protected var _delayedExpandTimer:Timer;
		
		/**
		 * Timer used when the <code>_delayedExpandTimer</code> is running.
		 * If the mouse is moving in a small area, we want to accelerate the expanding.
		 * <p>
		 * It starts when the mouse is moved over container and the <code>_delayedExpandTimer</code> is running.
		 * 
		 * <p>
		 * As long as the user holds the mouse still, it isn't stopped. Otherwise, stops it.
		 * It is stopped also if a ROLL_OUT event is dispached and it is already started.
		 * 
		 * NOTE: by still, we mean actually "almost" still; a move delta <= 5 px
	 	 * is allowed, horizontally or vertically.
	 	 * 
	 	 * After it's delay is passes, sends a notifier to expand the container immediately.
		 * 
		 * @see #mouseMoveHandler()
		 * @see #rollOutHandler()
		 * @author Cristina
		 */ 
		private var _accelerateExpandTimer:Timer;

		/**
		 * Used by <code>mouseMoveHandler()</code> to know the mouse position where 
		 * the <code>_accelerateExpandTimer</code> was started.
		 * Based on this value a verification is made to know if other mouse positions are
		 * near this one in order to accelerate the expanding.
		 */ 
		protected var _lastMousePosition:Point;
		
		public var shouldExpand:Boolean;		
		
		public function isMinimized():Boolean {
			return _isMinimized;
		}

		public function isResizeEffectPlaying():Boolean {
			return resizeStart;
		}
		
		/**
		 * Returns the delay used before expanding the container.
		 */ 
		public function get expandDelay():Number {
			return _delayedExpandTimer.delay;
		}
		
		/**
		 * Sets the delay used before expanding the container.
		 */ 
		public function set expandDelay(delay:Number):void {			
			_delayedExpandTimer.delay = delay;			
		}
		
		/**
		 * Returns the delay used to accelerate the container expanding.
		 */ 
		public function get accelerateExpandDelay():Number {
			return _accelerateExpandTimer.delay;
		}
		
		/**
		 * Sets the delay used to accelerate the expanding.		
		 */ 
		public function set accelerateExpandDelay(delay:Number):void {			
			_accelerateExpandTimer.delay = delay;		
		}
		
		public function AutoResizeContainer(parentContainer:DisplayObjectContainer, useFadeEffect:Boolean, preferredResizeToRight:Boolean = true) {
			super();
			this.parentContainer = parentContainer;
			this.useFadeEffect = useFadeEffect;
			this.preferredResizeToRight = preferredResizeToRight;
			isResizeToLeft = !preferredResizeToRight;
			setStyles();
			
   			horizontalScrollPolicy = ScrollPolicy.OFF;
   			   			
			addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
			addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);

			resizeToLeftEffect = new Parallel(this);
			resizeToLeftEffect.duration = RESIZE_EFFECT_DURATION;
			
			resizeToRightEffect = new Resize(this);
			resizeToRightEffect.duration = RESIZE_EFFECT_DURATION;
			
			fadeEffect = new Fade(this);
			fadeEffect.duration = FADE_EFFECT_DURATION;
			visible  = false;
			fadeEffect.addEventListener(EffectEvent.EFFECT_END, effectEndHandler);
			resizeToRightEffect.addEventListener(EffectEvent.EFFECT_END, effectEndHandler);
			resizeToLeftEffect.addEventListener(EffectEvent.EFFECT_END, effectEndHandler);
			
			_delayedExpandTimer = new Timer(EXPAND_CONTAINER_DELAY_DEFAULT, 1);
			_delayedExpandTimer.addEventListener(TimerEvent.TIMER_COMPLETE, changeState);
			
			_accelerateExpandTimer = new Timer(ACCELERATE_EXPAND_CONTAINER_DELAY_DEFAULT, 1);
			_accelerateExpandTimer.addEventListener(TimerEvent.TIMER_COMPLETE, notifyToExpand);
		}
		
		/**
		 * Define styles for the container. 
		 * Any of the extended classes can override this method to set it's own styles.
		 */ 
		protected function setStyles():void {
			setStyle("backgroundAlpha", BACKGROUND_ALPHA);
		}
		
		/**
		 * This is the method called when the <code>AutoResizeContainer</code> should appear on screen, or disappear. 
		 * The caller can choose if a fade effect is used.
		 * 
		 * If the fade effect is playing (and it needs to be used), it is "reversed".
		 */
		public function show(isVisible:Boolean, shouldRemoveChildren:Boolean = true):void {
			if (isVisible) {
				originalWidth = width;
				originalX = x;
				if (fadeEffect.isPlaying) {
					fadeEffect.stop();
				}
				if(shouldRemoveChildren) {
					removeAllChildren();
					// need to explicitely reset height to 0, as it remains at a previous value	
					height = 0;
					explicitHeight = NaN;
				}
				
				ContextMenuUtils.flex4CompatibleContainer_addChild(parentContainer, this);
			
				if (useFadeEffect) {
					fadeEffect.repeatDelay = 0;
					fadeEffect.alphaFrom = 0;
					fadeEffect.alphaTo = BACKGROUND_ALPHA;
					fadeEffect.play();
				}
			} else {
				_isMinimized = true;
				isResizeToLeft = !preferredResizeToRight;
				resizeToLeftEffect.stop();
				resizeToRightEffect.stop();
				if (useFadeEffect) {
					if (fadeEffect.isPlaying) {
						fadeEffect.reverse();
						fadeEffect.repeatDelay = 1;
					}
					else {
						if (fadeEffect.alphaTo != 0 && fadeEffect.repeatDelay == 0) { 
							//no need to start again the fade "OFF" effect if it was already started
							fadeEffect.repeatDelay = 0;
							//fadeEffect.stop();
							fadeEffect.alphaFrom = BACKGROUND_ALPHA;
							fadeEffect.alphaTo = 0;							
							fadeEffect.play();
						}						
					}
				} else {
					ContextMenuUtils.flex4CompatibleContainer_removeChild(parentContainer, this);
				}
			}
				
		}
		
		/**
		 * If the container is minimized, it is decided on which part the transition will be made.
		 * By default it is right, but if there is no space, it is done to the left. The corresponding
		 * effect is started.
		 * <p>
		 * Otherwise (i.e. an effect is already running), it is reversed.
		 *
		 * The code was moved here from <code>rollOverHandler()</code>
		 * in order to add the functionality described in UPDATE.
		 * <p>
		 * NOTE : The method only chages the state, it doesn't depend on TimerEvent parameter.
		 * @author Cristina
		 */ 
		private function changeState(event:TimerEvent = null):void {	
			if (!shouldExpand) {
				return;
			};		
			var oldMinimized:Boolean = _isMinimized;
			_isMinimized = false;
			resizeStart = true;

			if (!oldMinimized) { //an effect is already running
				if (isResizeToLeft)
					resizeToLeftEffect.reverse();
				else
					resizeToRightEffect.reverse();
			} else {
				var hasNotPlaceInRight:Boolean = (originalX + measuredWidth) >= (parentContainer.width - MIN_DIMENSION);
				// we have the following cases: 
				//   the container is expanded to the left if it has not place in right 
				//   the container is expanded to the left if it has place in both left and right, but the default is to resize to the left
				//   the container is otherwise expanded to the right 
				if (!hasNotPlaceInRight && !preferredResizeToRight)
					isResizeToLeft = originalX + width - measuredWidth + MIN_DIMENSION > 0;
				else
					isResizeToLeft = hasNotPlaceInRight;	
				
				if (isResizeToLeft) {
					resizeToLeftEffect.children	= new Array();
					var resize:Resize = new Resize(this);
					var move:Move = new Move(this);
					resize.widthFrom = width;
					resize.widthTo = measuredWidth;
					resizeToLeftEffect.addChild(resize);
					move.xTo = originalX + originalWidth - measuredWidth;
					resizeToLeftEffect.addChild(move);
					resizeToLeftEffect.play();					
				} else {
					resizeToRightEffect.end();
					resizeToRightEffect.widthFrom = originalWidth;
					resizeToRightEffect.widthTo = measuredWidth;					
					resizeToRightEffect.play();
				}
			}
		}

		/**
		 * If the container is minimized before the roll over event, 
		 * the resize "toBig" is done after a time delay (see UPDATE).
		 * <p>
		 * Otherwise, the normal behavior is used.
		 * @author Cristina 
		 */ 
		public function rollOverHandler(event:MouseEvent):void {						
			addEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
			removeEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
				
			if (isMinimized()) {
				shouldExpand = true;											
				_delayedExpandTimer.start();				
			}
		}
				
		/**
		 * If the roll out event is dispached when the container is waiting to be resized "toBig" (see UPDATE),
		 * the waiting stops and nothing else is done.
		 * <p> 
		 * Otherwise, the last used effect is reversed.
		 */
		public function rollOutHandler(event:MouseEvent):void {		
			removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
			addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
		
			if (_accelerateExpandTimer.running) {
				_accelerateExpandTimer.reset();
			}			
			if (_delayedExpandTimer.running) {
				_delayedExpandTimer.reset();	
				return;		
			}		
						
			if (isResizeToLeft) {
				if (resizeToLeftEffect.isPlaying) {
					Resize(resizeToLeftEffect.children[0]).widthBy = Resize(resizeToLeftEffect.children[0]).widthFrom - width;;
					resizeToLeftEffect.reverse(); //the reverse() is called ONLY if the current effect is playing	
				} else {
					resizeToLeftEffect.end();
					Resize(resizeToLeftEffect.children[0]).widthTo = originalWidth;
					Resize(resizeToLeftEffect.children[0]).widthFrom = measuredWidth;
					// need to compute also widthBy as widthFrom can be equal to widthTo, 
					// and in this case there is no difference on the effectEnd for resize toBig or resize ToSmall
					Resize(resizeToLeftEffect.children[0]).widthBy = originalWidth - measuredWidth;
					Move(resizeToLeftEffect.children[1]).xTo = originalX;						
					resizeToLeftEffect.play();
				}
			} else {
				if (resizeToRightEffect.isPlaying) {
					resizeToRightEffect.widthBy = resizeToRightEffect.widthFrom - width;
					resizeToRightEffect.reverse(); //the reverse() is called ONLY if the current effect is playing
				}
				else {
					resizeToRightEffect.end();
					resizeToRightEffect.widthTo = originalWidth;
					resizeToRightEffect.widthFrom = measuredWidth;
					resizeToRightEffect.widthBy = measuredWidth - originalWidth;
					resizeToRightEffect.play();
				}
			}
		}		
		
		/**
		 * The method implements a new functionality : if the container is waiting for expanding and
		 * the mouse is moving in a small area then accelerate the expanding.
		 * @author Cristina			
		 */ 
		public function mouseMoveHandler(event:MouseEvent):void {
			// the accelerate expanding is taken in consideration only if _delayedExpandTimer is running
			if (!_delayedExpandTimer.running) 
				return;	
			// if the accelerate timer isn't yet started and the mouse is moved inside this container,
			// start the timer and save the mouse position	
			if (!_accelerateExpandTimer.running && event.currentTarget == this) {
				_accelerateExpandTimer.start();
				_lastMousePosition = new Point(mouseX, mouseY);
			}
			// if the accelerate timer was started and the mouse moves outside the accepatable area, 
			// stops the acceleration
			if (_lastMousePosition != null && (Math.abs(mouseX - _lastMousePosition.x) > MIN_DIMENSION || Math.abs(mouseY - _lastMousePosition.y) > MIN_DIMENSION)) {
				_accelerateExpandTimer.reset();
			} 				
		}
				
		/**
		 * Different processing when the effects end. 
		 */
		protected function effectEndHandler(event:EffectEvent):void {				
			if (event.effectInstance is FadeInstance) {
				// if a reverse fade effect, then alphaTo can be != 0 even if it's fading off;
				if (FadeInstance(event.effectInstance).alphaTo == 0 || 
				Fade(event.currentTarget).repeatDelay > 0) { 
					ContextMenuUtils.flex4CompatibleContainer_removeChild(parentContainer, this);
				} 
			}
			if ((event.currentTarget == resizeToLeftEffect || event.currentTarget == resizeToRightEffect) && shouldExpand) {
				var widthBy:Number;
				if (event.currentTarget == resizeToRightEffect)
					widthBy = resizeToRightEffect.widthBy;
				else	
					widthBy = Resize(resizeToLeftEffect.children[0]).widthBy;
				if (width == originalWidth && !isNaN(widthBy)) {					
					_isMinimized = true;					
				}				
				resizeStart = false;	
			}
			// the classes that extends can not access the effects, but they might still need to know when the effect has ended	
			dispatchEvent(event);			
		}
		
		public override function addChildAt(child:DisplayObject, index:int):DisplayObject {
			visible = true;
			return super.addChildAt(child, index);
		}
		
		/**
		 * Returns whether the <code>fadeEffect</code> is playing 
		 * and the <code>AutoResizeContainer</code> is becoming invisible. 
		 */ 
		public function isFadingOff():Boolean {
			return fadeEffect.isPlaying && fadeEffect.alphaTo == 0;
		}

		/**
		 * The method porpose is to notify the container to expand.
		 * If the timer that delays the expanding is running, then it must be stoped.
		 * @author Cristina
		 */ 
		public function notifyToExpand(event:TimerEvent = null):void {
			if (_delayedExpandTimer.running)
				_delayedExpandTimer.reset();
			if (_accelerateExpandTimer.running)
				_accelerateExpandTimer.reset();
			if (isMinimized()) {				
				changeState();
			}
		}
	}
}