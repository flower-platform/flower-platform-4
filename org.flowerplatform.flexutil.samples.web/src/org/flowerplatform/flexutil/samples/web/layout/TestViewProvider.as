package org.flowerplatform.flexutil.samples.web.layout {
	
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.view.ITabCustomizer;
	import com.crispico.flower.util.layout.view.ViewPopupWindow;
	
	import mx.core.Container;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.samples.web.layout.CustomView;
	import org.flowerplatform.flexutil.samples.web.layout.EditorView;
	import org.flowerplatform.flexutil.samples.web.layout.NavigatorView;
	import org.flowerplatform.flexutil.samples.web.layout.ViewChart;
	import org.flowerplatform.flexutil.samples.web.layout.ViewDataGrid;
	import org.flowerplatform.flexutil.samples.web.layout.ViewForm;
	
	public class TestViewProvider implements IViewProvider {
		
		private var workbench:Workbench;
		
		public function TestViewProvider(workbench:Workbench) {
			this.workbench = workbench;
		}
		
		public function getId():String {
			return null;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {			
			if (viewLayoutData.isEditor) {
				return new EditorView();
			}
			if (viewLayoutData.viewId == "navigator") {
				return new NavigatorView();
			}			
			if (viewLayoutData.viewId == "view_chart") {
				return new ViewChart();
			}
			if (viewLayoutData.viewId == "view_grid") {
				return new ViewDataGrid();
			}
			if (viewLayoutData.viewId == "view_form") {
				return new ViewForm();
			}
			if (viewLayoutData.viewId == "normal_view") {
				return new CustomView();
			}
			throw new Error("No graphic component provided for view layout data " + viewLayoutData.viewId);
		}

		public function getTitle(viewLayoutData:ViewLayoutData = null):String {
			if (viewLayoutData.isEditor) {
				var component:Container = Container(workbench.layoutDataToComponent[viewLayoutData]);
				if (component != null && component.parent != null && component.label.charAt(0) != "*") {
					return "*" + viewLayoutData.viewId;
				} else {
					return viewLayoutData.viewId;
				}
			}
			return viewLayoutData.viewId;
		}
		 
		public function getIcon(viewLayoutData:ViewLayoutData = null):Object {		
			if (viewLayoutData.viewId == "navigator") {
				return "../icons/project.gif";
			}			
			if (viewLayoutData.viewId == "view_chart") {
				return "../icons/folder.gif"
			}
			if (viewLayoutData.viewId == "view_grid") {
				return "../icons/folder.gif"
			}			
			return "../icons/file.gif";
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}

	}
}