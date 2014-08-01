package org.flowerplatform.flex_client.team.git.ui.component
{
	public class SelectableBranches {
		
		protected var _name:String;
		protected var _isSelected:Boolean;
		
		public function SelectableBranches()
		{
		}
		
		public function get name ():String {
			return _name;
		}
		
		public function set name (s:String):void {
			_name = s;
		}
		
		public function get isSelected ():Boolean {
			return _isSelected;
		}
		
		public function set isSelected (b:Boolean):void {
			_isSelected = b;
		}
	}
}