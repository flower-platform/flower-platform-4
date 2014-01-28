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
package org.flowerplatform.flexdiagram.renderer.connection {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.core.IDataRenderer;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.renderer.ConnectionRendererController;
	import org.flowerplatform.flexdiagram.event.UpdateConnectionEndsEvent;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	
	import spark.components.Label;
	
	/**
	 * The figure class for a connection. 
	 * The responsibilites of the current class are:
	 * <ul> 
	 * 		<li> compute the correct values for width/height properties
	 * 			by overriding the <code>measure()</code> method. </li>
	 * 		<li> notify and draw the bendpoints and source/target figures
	 * 			for a <code>ConnectionFigure</code> </li>
	 * 		<li> compute the middle point of a connection (that will later
	 * 			be used by its EditPart if another line is connected to the
	 * 			current one) </li>
	 * 		<li> manage listeners and dispatch events whenever the middle point
	 * 			has changed. </li>
	 * 		<li> provide functions that acess the segments in order to compute
	 * 			different information used by <code>ConnectionLabelEditPart</code>
	 * </ul>
	 * @author Georgi
	 * 
	 */
	public class ConnectionRenderer extends UIComponent implements IDataRenderer, IDiagramShellAware {
		
		private var _diagramShell:DiagramShell;
		
		private var _data:Object;
				
		private var _sourceEndType:String = ConnectionEnd.NONE;
		
		private var _targetEndType:String = ConnectionEnd.NONE;
		
		/**
		 * The source figure: arrow, triangle or diamond.
		 */
		private var sourceFigure:ConnectionEnd = new ConnectionEnd();
		
		/**
		 * The target figure: arrow, triangle or diamond.
		 */
		private var targetFigure:ConnectionEnd = new ConnectionEnd();
		
		/**
		 * Length of a segment if the line is dashed.
		 * 
		 */
		private var dashSegment:int = 0; 
		
		/**
		 * ArrayCollection containing <code>BindablePoint</code> elements which 
		 * represent the bendpoints for the current connection figure.
		 * 
		 */
		protected var _points:ArrayCollection = new ArrayCollection();
		
		/**
		 * ArrayCollection containing <code>ConnectionSegment</code> elements 
		 * which represent the segments that form the current connection figure.
		 * 
		 */
		protected var segments:ArrayCollection = new ArrayCollection();
		
		/**
		 * Style constants. See StyleName.
		 */
		private static const LINE_COLOR:String = "flowerLineColor";
			
		private static const LINE_THICKNESS:String = "flowerLineThickness";
		
		/**
		 * Source point for the connection figure. The (x, y) values are set 
		 * from <code>ConnectionEditPart.updateConnectionsHandler()</code>.
		 */
		public var _sourcePoint:BindablePoint = new BindablePoint(0, 0);
		
		/**
		 * Target point for the connection figure. The (x, y) values are set 
		 * from <code>ConnectionEditPart.updateConnectionsHandler()</code>.
		 */
		public var _targetPoint:BindablePoint = new BindablePoint(0, 0);
		
		/**
		 * If the connection has an odd number of segments,
		 * middle1 and middle2 are the source and target <code>BindablePoint</code>
		 * for the middle segment. Otherwise, if the connection has an even
		 * number of segments, middle1 is the <code>BindablePoint</code> that 
		 * connects the two middle segments.
		 * 
		 */	
		private var middle1:BindablePoint = _sourcePoint;
	
		/**
		 * 
		 */
		private var middle2:BindablePoint = _targetPoint;
		
		/**
		 * At first, the figure has 3 children: one segment (that connects the 
		 * source and target points)and two <code>ConnectionEnd</code> that 
		 * represent the ends (e.g. arrow figure for an association's navigable
		 * end).
		 */
		public function ConnectionRenderer() {
			super();
			super.setStyle(LINE_THICKNESS, 1);
			super.setStyle(LINE_COLOR, "#000000");
			_sourcePoint.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, pointModified);
			_targetPoint.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, pointModified);
			
			_targetPoint.x = _targetPoint.y = 100;
			
			segments.addItem(new ConnectionSegment(_sourcePoint, _targetPoint, getDashSegment(), thickness, color));
			addChild(segments[0]);
			addChild(sourceFigure);
			addChild(targetFigure);
			
			depth = int.MAX_VALUE;
		}
		
		public function get diagramShell():DiagramShell {
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
		}

		public function get data():Object {
			return _data;
		}
		
		public var middleConnectionLabel:Label;
		
		public function set data(value:Object):void {
			if (_data != null) {
				var controller:ConnectionRendererController = ConnectionRendererController(diagramShell.getControllerProvider(data).getRendererController(data));
				var sourceModel:Object = controller.getSourceModel(_data);
				var targetModel:Object = controller.getTargetModel(_data);
				if (sourceModel != null) {
					IEventDispatcher(sourceModel).removeEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
				}
				if (targetModel != null) {
					IEventDispatcher(targetModel).removeEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
				}

				if (middleConnectionLabel != null) {
					IVisualElementContainer(parent).removeElement(middleConnectionLabel);
				}
			}
			_data = value;
			if (_data != null) {
				controller = ConnectionRendererController(diagramShell.getControllerProvider(data).getRendererController(data));
				sourceModel = controller.getSourceModel(_data);
				targetModel = controller.getTargetModel(_data);
				if (sourceModel != null) {
					IEventDispatcher(sourceModel).addEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
				}
				if (targetModel != null) {
					IEventDispatcher(targetModel).addEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
				}		

				addOrUpdateMiddleLabel(controller);
			}
		}
		
		protected function addOrUpdateMiddleLabel(controller:ConnectionRendererController):void {
			if (controller.hasMiddleLabel(_data)) {
				if (middleConnectionLabel == null) {
					middleConnectionLabel = new Label();
					middleConnectionLabel.setStyle("lineBreak", "explicit");
					IVisualElementContainer(parent).addElement(middleConnectionLabel);
				}
				middleConnectionLabel.text = getConnectionLabel();
			}
		}
		
		protected function getConnectionLabel():String {
			return "O eticheta";
		}
		
		/**
		 * This listener is here because when the event is generated, we also need the model for the connection.
		 * From here we have access to it. If we had this code in the controller, then we wouldn't have access to it.
		 */
		protected function updateConnectionEndsHandler(event:UpdateConnectionEndsEvent):void {
			var controller:ConnectionRendererController = ConnectionRendererController(diagramShell.getControllerProvider(data).getRendererController(data));
			controller.updateConnectionEnds(data, event.target);
		}
		
		public function get sourceEndType():String {
			return _sourceEndType;
		}
		
		public function set sourceEndType(sourceEndType:String):void {
			this._sourceEndType = sourceEndType;
			updateSourceFigure();
			
		}
		
		public function get targetEndType():String {
			return _targetEndType;
		}
		
		public function set targetEndType(targetEndType:String):void {
			this._targetEndType = targetEndType;
			updateTargetFigure();
		}
		
		public function getDashSegment():int {
			return dashSegment;
		}
		
		public function setDashSegment(newDashSegment:int):void {
			var shouldRefresh:Boolean = (dashSegment != newDashSegment);
			dashSegment = newDashSegment;
			for (var i:int = 0; i < segments.length; i++) {
				ConnectionSegment(segments.getItemAt(i)).dashSegmentSize = newDashSegment;
				if (shouldRefresh) {
					ConnectionSegment(segments.getItemAt(i)).invalidateDisplayList();
				}
			}
		}
		
		public function get thickness():int {
			return getStyle(LINE_THICKNESS);
		}
		
		public function get color():uint {
			return getStyle(LINE_COLOR);
		}
		
		public function set thickness(lineThickenss:int):void {
			super.setStyle(LINE_THICKNESS, lineThickenss);
			// announce the composing segments that the style changed
			for (var i:int = 0; i < segments.length; i++) {
				ConnectionSegment(segments.getItemAt(i)).thickness = lineThickenss;
			}	
		}
		
		public function set color(lineColor:uint):void {
			super.setStyle(LINE_COLOR, lineColor);
			for (var i:int = 0; i < segments.length; i++) {
				ConnectionSegment(segments.getItemAt(i)).color = lineColor;
			}
		}
		
		override public function setStyle(styleProp:String, newValue:*):void {
			if (styleProp == LINE_THICKNESS) {
				thickness = int(newValue);
			} else if (styleProp == LINE_COLOR) {
				color = uint(newValue);				
			} else {
				super.setStyle(styleProp, newValue);
			}
		}
		
		/**
		 * If the given <code>BindablePoint</code> is 'outside' of the 
		 * current dimensions (width/height) of the figure, the 
		 * dimensions are updated.
		 */
		private function updateDimensions(bp:BindablePoint):void {
			if (bp.x > width)
				width = bp.x;
			if (bp.y > height)
				height = bp.y;
		}
		
		/**
		 * Whenever a bendpoint is added/deleted/removed, the <code>computeDimensions()</code> 
		 * function is called to update the width and height properties.
		 */
		private function computeDimensions():void {
			var crtPoint:BindablePoint;
			updateDimensions(_sourcePoint);
			updateDimensions(_targetPoint);
			for (var i:int = 0; i < points.length; i++) {
				crtPoint = BindablePoint(points.getItemAt(i));
				updateDimensions(crtPoint);
			}
		}
		
		/**
		 * Overrides the <code>measure()</code> method in order to set the correct
		 * values for width and height by taking in consideration the current
		 * bendpoints.
		 */
		override protected function measure():void {
			width = height = 0;
			computeDimensions();
		}
		
		/**
		 * Getter for the bendpoints array.
		 */
		public function get points():ArrayCollection {
			return _points;
		}
		
		/**
		 * The method creates a new <code>SegmentFigure</code> between the given
		 * <code>BindablePoint</code> and adds it to the given index position in 
		 * the segments' list.
		 */
		protected function addSegmentAtIndex(p1:BindablePoint, p2:BindablePoint, index:int):void {
			var segm:ConnectionSegment = new ConnectionSegment(p1, p2, getDashSegment(), thickness, color);
			segments.addItemAt(segm, index);
			addChildAt(segm, index);
		}
		
		/**
		 * The method removes the <code>SegmentFigure</code> from the given
		 * index position. 
		 */		
		protected function removeSegmentAtIndex(index:int):void {
			segments.removeItemAt(index);
			removeChildAt(index);
		}

		/**
		 * The method reuses a segment, by updating one of its ends.
		 */
		protected function modifySegmentAtIndex(index:int, bp:BindablePoint):void {
			var segm:ConnectionSegment = ConnectionSegment(segments.getItemAt(index));
			segm.targetPoint = bp;
		}		
		/**
		 * The method adds a bendpoint to the bendpoints' list and  
		 * register a PropertyChangeListener on the new point. Update
		 * the segments by:
		 * <ul>
		 * 		<li> modify the segment from the 'index' position by 
		 * 			changing its target point with the new point </li>	
		 * 		<li> adding a new segment at 'index + 1' position the 
		 * 			connects the bendpoint at 'index' position with the 
		 * 			bendpoint at 'index + 1' position. </li>
		 * </ul>
		 * 
		 * <p>If there isn't any point at 'index' position (index < 0 or 
		 * index > points' number) use the source/target point.
		 */
		public function addPointAt(bp:BindablePoint, index:int):void {
			_points.addItemAt(bp, index);
			//add listener on new point
			bp.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, pointModified);	
			//update segments
			modifySegmentAtIndex(index, BindablePoint(_points.getItemAt(index))); 
			if (_points.length > index + 1)
				addSegmentAtIndex(BindablePoint(_points.getItemAt(index)), BindablePoint(_points.getItemAt(index+1)), index+1);
			else
				addSegmentAtIndex(BindablePoint(_points.getItemAt(index)), _targetPoint, index+1);
			checkIfArrowsUpdateIsNeeded(index);		
			measure();
			updateMiddlePointsListeners();
		}
		
		/**
		 * The method unregisters the PropertyChangeListener from a 
		 * bendpoint and removes it from the points' list. Update the
		 * segments by:
		 * <ul>
		 * 		<li> removing the segment from 'index' position that 
		 * 			connects the bendpoint at 'index' position with the 
		 * 			bendpoint at 'index + 1' position.</li>
		 * 		<li> removing the segment from 'index' position that connects
		 * 			 the bendpoint at 'index + 1' position with the bendpoint 
		 * 			at 'index + 2' position.</li>
		 * 		<li> creating a new segment at 'index' position that connects 
		 * 			the bendpoint at 'index' position with the bendpoint from 
		 * 			'index + 2' position.</li>
		 *	</ul>
		 * 
		 * <p>If there isn't any point at 'index' position (index < 0 or 
		 * index > points' number) use the source/target point.
		 */
		public function removePointAt(index:int):void {
			if (index >= 0 && index < _points.length) {
				var bp:BindablePoint = BindablePoint(_points.getItemAt(index));
				//remove listener
				bp.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, pointModified);	
				_points.removeItemAt(index);
				//modify segments
				removeSegmentAtIndex(index);
				removeSegmentAtIndex(index);
				var sp:BindablePoint; 
				var tp:BindablePoint;
				if (index == 0)
					sp = _sourcePoint;
				else 
					sp = BindablePoint(_points.getItemAt(index - 1));
				if (index + 1 >= _points.length)
					tp = _targetPoint;
				else
					tp = BindablePoint(_points.getItemAt(index));
				addSegmentAtIndex(sp, tp, index);
				checkIfArrowsUpdateIsNeeded(index, 0);
				measure();
				updateMiddlePointsListeners();
			}
		}
		
		/**
		 * The method is called from <code>addPointAt()</code> method and
		 * <code>removePointAt()</code> method in order to update the middle1 and
		 * middle2 variables and their listeners.
		 */
		private function updateMiddlePointsListeners():void {
//			editPart.dispatchUpdateConnectionEndsEvent(new UpdateConnectionEndsEvent(editPart));
//			if (segments.length % 2 == 0) { // even number of segments
//				// remove old listeners
//				middle1.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, dispatchUpdateConnectionEndsEvent);
//				middle2.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, dispatchUpdateConnectionEndsEvent);
//				// compute new values for middle1, middle2
//				middle1 = BindablePoint(_points[_points.length / 2]);
//				middle2 = null;
//				// add new listener
//				middle1.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, dispatchUpdateConnectionEndsEvent);	
//			} else { // odd number of segments
//				//remove old listeners
//				middle1.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, dispatchUpdateConnectionEndsEvent);
//				// compute new values for middle1 and middle2
//				middle1 = BindablePoint(ConnectionSegment(segments[segments.length / 2]).sourcePoint);
//				middle2 = BindablePoint(ConnectionSegment(segments[segments.length / 2]).targetPoint);
//				// add new listeners
//				middle1.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, dispatchUpdateConnectionEndsEvent);
//				middle2.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, dispatchUpdateConnectionEndsEvent);
//			}
//		}
//		
//		/**
//		 * Called whenever a middle point has changed its properties. It
//		 * dispatches an <code>UpdateConnectionEndsEvent</code> event to
//		 * notify the other EditParts connected to this figure's EditPart
//		 * that some properties were changed.
//		 */
//		private function dispatchUpdateConnectionEndsEvent(event:PropertyChangeEvent):void {
//			editPart.dispatchUpdateConnectionEndsEvent(null);
		}
		
		/**
		 * The method modifies the (x, y) coordinates of a bendpoint based
		 * on the PropertyChangeEvent received as parameter.
		 */
		protected function pointModified(event:PropertyChangeEvent):void {
			var index:int;
			var point:BindablePoint = BindablePoint(event.source);
			if (event.property == "x")
				point.x = int(event.newValue);
			else if (event.property == "y")
				point.y = int(event.newValue);
			measure();
			
			if (BindablePoint(event.source) == _sourcePoint)
				index = 0;
			else if (BindablePoint(event.source) == _targetPoint)
				index = _points.length - 1;
			else 
				index = getPointIndex(point);
			checkIfArrowsUpdateIsNeeded(index);		
		}
		
		/**
		 * The function is called whenever a point is added/deleted/modified.
		 * If the modification can affect the arrows => the <code>updateFigureHandler</code>
		 * method is called.
		 */
		public function checkIfArrowsUpdateIsNeeded(index:int, operation:int = -1):void {
			if (index == 0 || index == points.length + operation) {
				updateFigureHandler();
			}
		}
		
		/**
		 * Returns the index for the given <code>ConnectionSegment</code> in
		 * the segments' list. Called from <code>ConnectionEditPart.beginDrag()</code>
		 * method.
		 */
		public function getSegmentIndex(segment:ConnectionSegment):int {
			for (var i:int = 0; i < segments.length; i++)
				if (segments.getItemAt(i) == segment)
					return i;
			return -1; //not found
		}
		
		/**
		 * Returns the index for the given <code>BindablePoint</code> in 
		 * the bendpoints' list. Called from <code>ConnectionEditPart</code>.
		 */
		public function getPointIndex(bpam:BindablePoint):int {
			for (var i:int = 0; i < _points.length; i++)
				if (BindablePoint(_points[i]) == bpam)
					return i;
			return -1; //not found
		}
		
		/**
		 * Returns the <code>BindablePoint</code> (if exists) with
		 * the given (x, y) coordinates. Called from 
		 * <code>ConnectionEditPart.beginDrag()</code> method.
		 */
		public function getBindablePoint(x:int, y:int):BindablePoint {
			for (var i:int = 0; i < _points.length; i++){
				var bp:BindablePoint = BindablePoint(_points[i]);
				if (bp.x == x && bp.y == y)
					return bp;	
			}
			return null;	
		}
		
		/**
		 * The method is called from <code>refreshVisualDetails()</code> 
		 * from its associated EditPart and from <code>checkIfArrowsUpdateIsNeeded()</code>.
		 * The angle and the point computed by the method are set to the 
		 * <code>ConnectionEnd</code> which re-displays its shape.
		 * 
		 */ 	
		public function updateFigureHandler():void  {
			updateSourceFigure();
			updateTargetFigure();
		}
		
		public function updateSourceFigure():void {
			var auxPoint:BindablePoint;
			sourceFigure.type = sourceEndType;
			if (sourceEndType != ConnectionEnd.NONE) {
				sourceFigure.point = _sourcePoint;
				if (_points.length > 0)
					auxPoint = BindablePoint(_points.getItemAt(0));
				else 
					auxPoint = _targetPoint;
				sourceFigure.angle = Math.atan2((sourceFigure.point.y - auxPoint.y), (sourceFigure.point.x - auxPoint.x));
				sourceFigure.angle += Math.PI;
			}
			sourceFigure.invalidateDisplayList();
			
			
		}
		
		public function updateTargetFigure() {
			var auxPoint:BindablePoint;
			targetFigure.type = targetEndType;
			if (targetEndType != ConnectionEnd.NONE) {
				targetFigure.point = _targetPoint;
				if (_points.length > 0) 
					auxPoint = BindablePoint(_points.getItemAt(_points.length - 1));
				else 
					auxPoint = _sourcePoint;
				targetFigure.angle = Math.atan2((targetFigure.point.y - auxPoint.y), (targetFigure.point.x - auxPoint.x));
				targetFigure.angle += Math.PI;
			}
			targetFigure.invalidateDisplayList();
			
		}
	
		/**
		 * Called from <code>ConnectionEditPart.getConnectionAnchorRect()</code>
		 * method in order to find the middle point of a line (which is the point
		 * at the half sum of the segments that create the connection);
		 * 
		 * 
		 */
		public function getMiddlePointRect():Array {
			var array:Array = getPointFromDistance() as Array;
			var p:BindablePoint = array[0];
			return [p.x, p.y, 0, 0];
		}
	
		/**
		 * The method checks if the given point of the segment is on the left/top 
		 * side as the other edge of the segment. The method is used to compute
		 * the position for the central label of a connection (if the connection
		 * allows labels).
		 * 
		 */
		public function isConnectionOnLeftOrTopOfTheDisplay(refPoint:BindablePoint, segmId:int, isHorizontal:Boolean):Boolean {
			var cs:ConnectionSegment = getSegmentAt(segmId);
			var otherPoint:BindablePoint;
			if (refPoint.x == cs.sourcePoint.x && refPoint.y == cs.sourcePoint.y)
				otherPoint = cs.targetPoint;
			else if (refPoint.x == cs.targetPoint.x && refPoint.y == cs.targetPoint.y)
				otherPoint = cs.sourcePoint;
			else {
				// the given point is the middle of the segment
				return (isHorizontal ? true : false);
			}
			if ((isHorizontal && refPoint.x < otherPoint.x) ||
				(!isHorizontal && refPoint.y < otherPoint.y))
				return true;
			return false;
		}

		/**
		 * The method returns the number of segments contained
		 * by a connection.
		 * 
		 */
		public function getNumberOfSegments():int {
			return segments.length;
		}
		
		/**
		 * The method returns the <code>ConnectionSegment</code>
		 * at the given index in the segments array.
		 * 
		 */		
		public function getSegmentAt(segmId:int):ConnectionSegment {
			return ConnectionSegment(segments.getItemAt(segmId));
		}
		
		/**
		 * The method computes the length of the connection by 
		 * computing the sum of the length of all its segments;
		 * 
		 */
		public function getConnectionLength():Number {
			var sum:Number = 0;
			for (var i:int = 0; i < segments.length; i++)
				sum += ConnectionSegment(segments.getItemAt(i)).getSegmentLength();
			return sum;
		}
		
		/**
		 * The method computes the (x, y) coordinates of the point 
		 * which is "in the middle" of the connection.
		 * 
		 * The method returns an array which contain the middle point 
		 * of the connection and the segment which the point belongs 
		 * to.
		 * 
		 * @see ConnectionSegment.getSegmentPoint()
		 * 
		 */
		public function getPointFromDistance():Array {
			var sum:Number = 0;
			var crtSegment:ConnectionSegment;
			var result:Array = new Array();
			// get the "middle" of the connection
			var distance:Number = getConnectionLength() / 2;
			for (var i:int = 0; i < segments.length; i++) {
				crtSegment = ConnectionSegment(segments.getItemAt(i));
				if (sum <= distance && (sum + crtSegment.getSegmentLength()) >= distance){
					result.push(crtSegment.getSegmentPoint(distance - sum));
					result.push(i);
					return result;
				}
				else 
					sum += crtSegment.getSegmentLength();
			}
			throw new Error("The middle point of a connection could not be computed correctly");
		}
		
	}
}