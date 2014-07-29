package org.flowerplatform.flex_client.team.git.view
{
	import mx.controls.Button;
	import mx.controls.Label;
	import mx.controls.List;
	import mx.controls.TextInput;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.popup.Wizard;

	public class CloneRepoWizardView extends Wizard {
		
		protected var selectRepo:SelectRepository;
		protected var branchesDescription:Label;
		protected var branchesFilter:TextInput;
		protected var branchesList:List;
		
		public function CloneRepoWizardView() {
			super();
		}
		
		override protected function createChildren():void {
			super.createChildren();
			createSelectRepo();
			createbranchesDescription();
			createbranchesFilter();
			createBranchesList();
			contentArea.addElement(selectRepo);
			contentArea.addElement(branchesDescription);
			contentArea.addElement(branchesFilter);
			contentArea.addElement(branchesList);
		}
		
		private function createSelectRepo():void {
			selectRepo = new SelectRepository();
			selectRepo.localRepoGroup.visible = false;
			selectRepo.localRepoGroup.includeInLayout = false;
			selectRepo.selectCommands.visible = false;
			selectRepo.selectCommands.includeInLayout = false;
		}
		private function createbranchesDescription():void {
			branchesDescription = new Label();
			branchesDescription.text = Resources.getMessage('git.cloneRepo.branches.description');
			branchesDescription.visible = false;
			branchesDescription.includeInLayout = false;
		}
		
		private function createBranchesList():void {
			branchesList = new List();
			branchesList.visible = false;
			branchesList.includeInLayout = false;
			// TODO AB: populate with branches
		}
		
		private function createbranchesFilter():void {
			branchesFilter = new TextInput();
			branchesFilter.text = Resources.getMessage('git.filter');
			branchesFilter.visible = false;
			branchesFilter.includeInLayout = false;
			// TODO AB: onkeyup => filter text
		}
	}
}