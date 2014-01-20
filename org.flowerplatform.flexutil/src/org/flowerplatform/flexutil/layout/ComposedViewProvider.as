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
package  org.flowerplatform.flexutil.layout {
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	
	/**
	 * Singleton class representing a registry that stores data about
	 * layout (e.g. perspectives, view providers, etc.)
	 * 
	 * <p>
 	 * Implements of <code>IViewProvider</code> and delegates 
	 * to <code>IViewProvider</code>s registered by plugins.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 
	 */
	public class ComposedViewProvider implements IViewProvider {
		
		/**
		 * @see Getter doc.
		 * 
		 * 
		 */
		private var _perspectives:ArrayCollection = new ArrayCollection();
		
		/**
		 * @see Getter doc.
		 * 
		 * 
		 */
		private var _viewProviders:ArrayCollection = new ArrayCollection();
		
		/**
		 * Holds a list of available <code>IViewProvider</code>s.
		 * 
		 * <p>
		 * The view providers registered here should accept <code>null</code>
		 * as parameter for <code>IViewProvider.getTitle()</code> and 
		 * <code>IViewProvider.getIcon()</code> and return not <code>null</code>
		 * results. Except editors, that should return <code>null</code>.
		 * 
		 * @see #addViewProvider()
		 * 
		 */
		public function get viewProviders():ArrayCollection {
			return _viewProviders;
		}
		
		/**
		 * Used by plugins to add new <code>IViewProvide</code>s.
		 * 
		 * @see #viewProviders
		 * 
		 */
		public function addViewProvider(viewProvider:IViewProvider):void {
			viewProviders.addItem(viewProvider);
		}
		
		/**
		 * Returns null, it isn't used.
		 * 
		 * 
		 */
		public function getId():String {
			return null;	
		}
		
		/**
		 * Creates the graphical component (specified by the parameter) by
		 * iterating through all <code>viewProviders</code> in searching for the
		 * corresponding view provider id and returns <code>IViewProvider#createView()</code>.
		 * 
		 * <p>
		 * If none exists, returns null.
		 * 
		 * 
		 */
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {	
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {
					return viewProvider.createView(viewLayoutData);	
				}							
			}
			return null;
		}
		
		/**
		 * Gets the title of the view (corresponding to the parameter) by
		 * iterating through all <code>viewProviders</code> in searching for the
		 * corresponding view provider id and returns <code>IViewProvider#getTitle()</code>.
		 * 
		 * <p>
		 * If none exists, returns null.
		 * 
		 * <p>
		 * If the component this view provides implements the <code>IDirtyStateProvider</code>
		 * interface, and it is dirty, then a "*" is appended to the original title
		 * 
		 * @author Sebastian Solomon
		 * 
		 */
		public function getTitle(viewLayoutData:ViewLayoutData = null):String {	
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {			
					var title:String = viewProvider.getTitle(viewLayoutData);
					var component:UIComponent = FlexUtilGlobals.getInstance().workbench.getComponent(viewLayoutData.viewId, viewLayoutData.customData);
					if (component is IViewHost) {
						component = UIComponent(IViewHost(component).activeViewContent);
					}
					if (component is ITitleDecorator) {
						title = ITitleDecorator(component).decorateTitle(title); 
					}
					return title;
				}			
			}
			return null;
		}
		
		/**
		 * Gets the icon of the view (corresponding to the parameter) by
		 * iterating through all <code>viewProviders</code> in searching for the
		 * corresponding view provider id and returns <code>IViewProvider#getIcon()</code>.
		 * 
		 * <p>
		 * If none exists, returns null.
		 * 
		 * 
		 */
		public function getIcon(viewLayoutData:ViewLayoutData = null):Object {	
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {					
					return viewProvider.getIcon(viewLayoutData);	
				}			
			}
			return null;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {					
					return viewProvider.getTabCustomizer(viewLayoutData);	
				}			
			}
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (viewLayoutData.viewId == viewProvider.getId()) {					
					return viewProvider.getViewPopupWindow(viewLayoutData);	
				}			
			}
			return null;
		}
		
		public function getViewProvider(id:String):IViewProvider {
			for each (var viewProvider:IViewProvider in viewProviders) {
				if (id == viewProvider.getId()) {					
					return viewProvider;	
				}			
			}
			return null;
		}
		
	}
	
}