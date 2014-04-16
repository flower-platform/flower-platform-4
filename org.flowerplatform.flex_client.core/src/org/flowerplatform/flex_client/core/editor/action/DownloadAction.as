package org.flowerplatform.flex_client.core.editor.action {
	
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.MultipleSelectionActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DownloadAction extends MultipleSelectionActionBase {
		
		public function DownloadAction() {
			super();
			label = Resources.getMessage("action.download");
			icon = Resources.downloadIcon;
			orderIndex = 300;
		}
		
		override protected function isVisibleForSelectedElement(element:Object):Boolean {
			return element is Node && Node(element).type == CoreConstants.FILE_NODE_TYPE;
		}
		
		override public function run():void {
			var fullNodeIds:ArrayCollection = new ArrayCollection();
			for (var i:int = 0; i < selection.length; i++) {
				fullNodeIds.addItem(Node(selection.getItemAt(i)).fullNodeId);				
			}
			
			if (selection.length > 1 || (selection.length == 1 && Node(selection.getItemAt(0)).properties[CoreConstants.FILE_IS_DIRECTORY])) {
				// single directory or multiple files -> zip archive will be created, show info to client
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("download.zip"))
					.setTitle(Resources.getMessage("info"))
					.setWidth(300)
					.setHeight(200)
					.addButton(FlexUtilAssets.INSTANCE.getMessage('dialog.ok'), function():void {prepareToDownload(fullNodeIds);})
					.showMessageBox();
			} else {
				prepareToDownload(fullNodeIds);
			}
		}
		
		protected function prepareToDownload(fullNodeIds:ArrayCollection):void {
			CorePlugin.getInstance().serviceLocator.invoke("downloadService.prepareDownload", [fullNodeIds], 
				function(result:Object):void {
					if (result != null) {
						// result = download URL
						var request:URLRequest = new URLRequest();
						request.url = FlexUtilGlobals.getInstance().rootUrl + String(result);		
						navigateToURL(request);
					}
				}
			);
		}
		
	}
}