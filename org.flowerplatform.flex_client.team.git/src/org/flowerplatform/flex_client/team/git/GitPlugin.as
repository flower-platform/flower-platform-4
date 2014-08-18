/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
 * license-end
 */
package org.flowerplatform.flex_client.team.git {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.team.git.action.CheckoutAction;
	import org.flowerplatform.flex_client.team.git.action.CloneRepoAction;
	import org.flowerplatform.flex_client.team.git.action.ConfigureBranchAction;
	import org.flowerplatform.flex_client.team.git.action.ConfigureRemoteAction;
	import org.flowerplatform.flex_client.team.git.action.CreateBranchAction;
	import org.flowerplatform.flex_client.team.git.action.CreateStructureDiffFromGitCommitsAction;
	import org.flowerplatform.flex_client.team.git.action.DeleteBranchAction;
	import org.flowerplatform.flex_client.team.git.action.RenameBranchAction;
	import org.flowerplatform.flex_client.team.git.action.history.GitHistoryViewProvider;
	import org.flowerplatform.flex_client.team.git.action.history.action.ShowGitHistoryAction;
	import org.flowerplatform.flex_client.team.git.action.DeleteGitRepositoryAction;
	import org.flowerplatform.flex_client.team.git.action.DeleteRemoteAction;
	import org.flowerplatform.flex_client.team.git.action.LoginAction;
	import org.flowerplatform.flex_client.team.git.action.MergeAction;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flex_client.team.git.action.ResetAction;
	import org.flowerplatform.flex_client.team.git.remote.GitCredentials;
	import org.flowerplatform.flex_client.team.git.remote.GitRef;
	import org.flowerplatform.flex_client.team.git.action.GitStagingAction;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;

	import org.flowerplatform.flex_client.team.git.action.LoginAction;
	import org.flowerplatform.flex_client.team.git.action.ConfigureRemoteAction;

	/**
	 * @author Valentina-Camelia Bojan
	 */
	public class GitPlugin  extends AbstractFlowerFlexPlugin {
		protected static var INSTANCE:GitPlugin;
		
		public static function getInstance():GitPlugin {
			return INSTANCE;
		}
			
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) +
								" already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			CorePlugin.getInstance().serviceLocator.addService("GitService");
			CorePlugin.getInstance().serviceLocator.addService("HistoryService");
						
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(CreateStructureDiffFromGitCommitsAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(CreateBranchAction);		
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(DeleteBranchAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ConfigureRemoteAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ResetAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(RenameBranchAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ConfigureBranchAction);
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new GitHistoryViewProvider());
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ShowGitHistoryAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(MergeAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(DeleteGitRepositoryAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(CloneRepoAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(CheckoutAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(DeleteRemoteAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(LoginAction);

			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new GitStagingProperties());
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(GitStagingAction);
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
		/**
		 * @author Cristina Brinza
		 */
		override protected function registerClassAliases():void {
			super.registerClassAliases();
			registerClassAliasFromAnnotation(GitRef);
			registerClassAliasFromAnnotation(GitCredentials);
		}
	}
}
