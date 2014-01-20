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
package  com.crispico.flower.util.layout.persistence {
	import mx.collections.ArrayCollection;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.layout.LayoutData;
	
	
	/**
	 * An implmentation of <code>ILayoutSerializer</code> that uses the XML format to serialize/deserialize.
	 * <p>
	 * The XML string must have the below structure or must contain a section of it:
	 * 
	 * <workbenchLayoutData direction="..." ratios="..." mrmRatios="...">
	 * 	<sashLayoutData direction="..." ratios="..." mrmRatios="...">	
	 * 		<sashLayoutData direction="..." ratios="..." mrmRatios="...">
	 * 			...
	 * 		</sashLayoutData>
	 * 		...	
	 * 		<stackLayoutData minimized="..." mrmState="" mrmSide="...">
	 * 			<viewLayoutData viewId="..." customData="..." />
	 * 			...
	 * 		</stackLayoutData>
	 * 		...
	 * 	</sashLayoutData>
	 * 	...
	 * 	<stackLayoutData minimized="..." mrmState="" mrmSide="...">
	 * 		<viewLayoutData viewId="..." customData="..." />
	 * 		...
	 * 	</stackLayoutData>
	 * 	...
	 * 	<viewLayoutData isUndocked="true"/>
	 *  ... 
	 * </workbenchLayoutData>
	 * 
	 * direction -> 0/1
	 * ratios -> 50/50, 20/40/40
	 * mrmState -> 0/1/2/3 (normal/user_minimized/forced_minimized/maximized)
	 * mrmSide -> 0/1/2 (left/right/bottom)
	 * mrmRatios -> ratios for minimized children, format: 20/30/50
	 * 
	 * @author Cristina
	 * 
	 */
	public class XMLStringLayoutSerializer implements ILayoutSerializer {
		
		/**
		 * Serializes the <code>LayoutData</code> by creating a string having XML format.
		 * Calls <code>serializeXML</code> for layout data and returns the method's result as string.
		 * 
		 * @see serializeXML()
		 */
		public function serialize(layoutData:LayoutData):String {			
			return serializeXML(layoutData).toXMLString();
		}
				
		/**
		 * Given a <code>LayoutData</code> creates its corresponding XML node and calls the method recursively for each layout child.
		 * Cases:
		 * <ul>
		 * 	<li> layoutData is <code>SashLayoutData</code>
		 * 		- if layoutData is <code>WorkbenchLayoutData</code>- creates a workbenchLayoutData, otherwise creates a sashLayoutData node
		 * 		- attributes:
		 * 			+ "direction" -> layout direction
		 * 			+ "ratios" -> a string representing all children ratios
		 * 			+ "mrmRatios" -> a string representing all children minimized ratios 
		 * 		- calls the method for each layout child. 
		 * 	<li> layoutData is <code>StackLayoutData</code> 
		 * 		- creates a stackLayoutData node
		 * 		- attributes:
		 * 			+ "mrmState" -> see StackLayoutData#mrmState (NORMAL, USER_MINIMIZED, FORCED_MINIMIZED, MAXIMIZED)
		 * 			+ "mrmSide" -> see StackLayoutData#mrmSide (NONE, LEFT, RIGHT, BOTTOM). 
		 * 							If this layoutData isn't minimized, then sets this attribute to empty string.
		 * 		- calls the method for each layout child.
		 * 	<li> layoutData is <code>ViewLayoutData</code>
		 * 		- creates a viewLayoutData node
		 * 		- attributes:
		 * 			+ "viewId"
		 * 			+ "customData"
		 * 			+ "isEditor"
		 * 			+ "isUndocked"
		 * </ul> 
		 * 
		 */
		private function serializeXML(layoutData:LayoutData):XML {
			var xmlNode:XML;
			if (layoutData is ViewLayoutData) {
				xmlNode = <viewLayoutData></viewLayoutData>;
				xmlNode.@viewId = ViewLayoutData(layoutData).viewId;
				xmlNode.@customData = ViewLayoutData(layoutData).customData;
				xmlNode.@isEditor = ViewLayoutData(layoutData).isEditor;
				xmlNode.@isUndocked = ViewLayoutData(layoutData).isUndocked;
				var dimensions:String = "";
				for (var i:int = 0; i < ViewLayoutData(layoutData).dimensions.length; i++) {
					if (dimensions != "") {
						dimensions += "/";
					}
					dimensions += Number(ViewLayoutData(layoutData).dimensions.getItemAt(i)).toFixed(2);
				}
				xmlNode.@dimensions = dimensions;
				return xmlNode;
			}
			if (layoutData is SashLayoutData) {
				if (layoutData is WorkbenchLayoutData) {
					xmlNode = <workbenchLayoutData></workbenchLayoutData>;
					// append all undocked views to workbench node
					// this way we know exactly where to look when deserializing
					for each (var undockView:LayoutData in WorkbenchLayoutData(layoutData).undockedViews) {
						xmlNode.appendChild(serializeXML(undockView));
					}
				} else {
					xmlNode = <sashLayoutData></sashLayoutData>;
				}
				xmlNode.@direction = SashLayoutData(layoutData).direction;	
				xmlNode.@isEditor = SashLayoutData(layoutData).isEditor;
						
				// set ratios with format : a/b/c/..
				var ratios:String = "";
				for (var i:int = 0; i < SashLayoutData(layoutData).ratios.length; i++) {
					if (ratios != "") {
						ratios += "/";
					}
					ratios += Number(SashLayoutData(layoutData).ratios.getItemAt(i)).toFixed(2);
				}
				xmlNode.@ratios = ratios;
				
				// set mrmRatios with format : a/b/c/..
				ratios = "";
				for (var j:int = 0; j < SashLayoutData(layoutData).mrmRatios.length; j++) {
					if (ratios != "") {
						ratios += "/";
					}
					ratios += Number(SashLayoutData(layoutData).mrmRatios.getItemAt(j)).toFixed(2);
				}
				xmlNode.@mrmRatios = ratios;						
			} 
			if (layoutData is StackLayoutData) {
				xmlNode = <stackLayoutData></stackLayoutData>;
				xmlNode.@mrmState = StackLayoutData(layoutData).mrmState;							
				switch (StackLayoutData(layoutData).mrmSide) {
					case StackLayoutData.LEFT:
						xmlNode.@mrmSide = "left"
						break;
					case StackLayoutData.RIGHT:
						xmlNode.@mrmSide = "right";
						break;
					case StackLayoutData.BOTTOM:
						xmlNode.@mrmSide = "bottom";
						break;
					default:
						xmlNode.@mrmSide = "";
				}
				var closedViews:String = "";
				for each (var viewId:String in StackLayoutData(layoutData).closedViews) {
					if (closedViews != "") {
						closedViews += "/";
					}
					closedViews += viewId;					
				}
				xmlNode.@closedViews = closedViews;			
			}
			for each (var child:LayoutData in layoutData.children) {
				xmlNode.appendChild(serializeXML(child));
			}
			return xmlNode;			
		}
		
		/**
		 * Deserializes a string with XML format and returns corresponding <code>LayoutData</code>.
		 * Creates a XML object based on data given as string and calls <code>deserializeXML</code> for xml root.
		 * 
		 * @see deserializeXML()
		 */
		public function deserialize(data:String):LayoutData {	
			var xmlRoot:XML = new XML(data);			
			return deserializeXML(xmlRoot, null);
		}
		
		/**
		 * Given a XML node, creates corresponding <code>LayoutData</code> and calls the method recursively for each child node.
		 * Cases :
		 * <ul>
		 * 	<li> xmlNode.name is workbenchLayoutData - creates a <code>WorkbenchLayoutData</code> 
		 * 	<li> xmlNode.name is sashLayoutData - creates a <code>SashLayoutData</code>
		 * 	<li> xmlNode.name is stackLayoutData - creates a <code>StackLayoutData</code>
		 * 	<li> xmlNode.name is viewLayoutData - creates a <code>ViewLayoutData</code>
		 * </ul>
		 * For each object, sets their attributes with corresponding values from xml node.
		 * After resursively calling the method, the <code>LayoutData</code> parent must be set.
		 * 
		 * @param xmlNode - xml node used to deserialize
		 * @param rootLayout - the workbenchLayoutData; it is stored when created and used later to fill its minimized stacks arrays.
		 * 
		 */
		private function deserializeXML(xmlNode:XML, rootLayout:WorkbenchLayoutData):LayoutData {
			var layoutData:LayoutData;
			if (xmlNode.name() == "viewLayoutData") {
				layoutData = new ViewLayoutData();
				ViewLayoutData(layoutData).viewId = xmlNode.@viewId;
				ViewLayoutData(layoutData).customData = xmlNode.@customData;
				ViewLayoutData(layoutData).isEditor = xmlNode.@isEditor == "true";
				ViewLayoutData(layoutData).isUndocked = xmlNode.@isUndocked == "true";
				var dimensions:String = xmlNode.@dimensions;
				if (dimensions != "") {
					ViewLayoutData(layoutData).dimensions = new ArrayCollection(dimensions.split("/"));	
				}
				return layoutData;
			}
			if (xmlNode.name() == "workbenchLayoutData" || xmlNode.name() == "sashLayoutData") {
				if (xmlNode.name() == "workbenchLayoutData") {
					layoutData = new WorkbenchLayoutData();
					rootLayout = WorkbenchLayoutData(layoutData);
				} else {
					layoutData = new SashLayoutData();
				}
				SashLayoutData(layoutData).direction = xmlNode.@direction;
				SashLayoutData(layoutData).isEditor = xmlNode.@isEditor == "true";
				var ratios:String = xmlNode.@ratios;
				if (ratios != "") {
					SashLayoutData(layoutData).ratios = new ArrayCollection(ratios.split("/"));	
				}
				ratios = xmlNode.@mrmRatios;
				if (ratios != "") {
					SashLayoutData(layoutData).mrmRatios = new ArrayCollection(ratios.split("/"));	
				}
			}
			if (xmlNode.name() == "stackLayoutData") {
				layoutData = new StackLayoutData();								
				StackLayoutData(layoutData).mrmState = xmlNode.@mrmState;				
				StackLayoutData(layoutData).mrmSide = StackLayoutData.NONE;				
				if (rootLayout != null) {								
					if (xmlNode.@mrmSide == "left") {						
						StackLayoutData(layoutData).mrmSide = StackLayoutData.LEFT;
						rootLayout.minimizedStacks.addItem(layoutData);
					} else if (xmlNode.@mrmSide == "right") {						
						StackLayoutData(layoutData).mrmSide = StackLayoutData.RIGHT;
						rootLayout.minimizedStacks.addItem(layoutData);
					} else if (xmlNode.@mrmSide == "bottom") {						
						StackLayoutData(layoutData).mrmSide = StackLayoutData.BOTTOM;
						rootLayout.minimizedStacks.addItem(layoutData);
					}
				}
				var closedViews:String = xmlNode.@closedViews;
				if (closedViews != "") {
					StackLayoutData(layoutData).closedViews = new ArrayCollection(closedViews.split("/"));	
				}				
			}
						
			for each (var child:XML in xmlNode.children()) {
				var layoutChild:LayoutData = deserializeXML(child, rootLayout);
				// don't add undock view to workbench as child
				// add it in the corresponding list
				if (layoutChild is ViewLayoutData && ViewLayoutData(layoutChild).isUndocked) {
					rootLayout.undockedViews.addItem(layoutChild);
					continue;
				}
				layoutData.children.addItem(layoutChild);
				layoutChild.parent = layoutData;
			}			
			return layoutData;
		}
	}
	
}