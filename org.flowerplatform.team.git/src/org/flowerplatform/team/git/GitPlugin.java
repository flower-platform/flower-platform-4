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
package org.flowerplatform.team.git;

import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN;
import static org.flowerplatform.core.CoreConstants.REPOSITORY_TYPE;
import static org.flowerplatform.team.git.GitConstants.COMMIT_ID;
import static org.flowerplatform.team.git.GitConstants.COMMIT_MESSAGE;
import static org.flowerplatform.team.git.GitConstants.CONFIG_REBASE;
import static org.flowerplatform.team.git.GitConstants.CONFIG_REMOTE;
import static org.flowerplatform.team.git.GitConstants.CONFIG_UPSTREAM_BRANCH;
import static org.flowerplatform.team.git.GitConstants.CURRENT_BRANCH;
import static org.flowerplatform.team.git.GitConstants.CURRENT_COMMIT;
import static org.flowerplatform.team.git.GitConstants.FETCH_REF_SPECS;
import static org.flowerplatform.team.git.GitConstants.FULL_NAME;
import static org.flowerplatform.team.git.GitConstants.GIT_CATEGORY;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REF;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REPO_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_SCHEME;
import static org.flowerplatform.team.git.GitConstants.GIT_TAGS_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAG_TYPE;
import static org.flowerplatform.team.git.GitConstants.IS_CHECKEDOUT;
import static org.flowerplatform.team.git.GitConstants.NAME;
import static org.flowerplatform.team.git.GitConstants.PUSH_REF_SPECS;
import static org.flowerplatform.team.git.GitConstants.REMOTE_URIS;

import org.eclipse.jgit.lib.Constants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.resource.BaseResourceHandler;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.team.git.controller.GitChildrenProvider;
import org.flowerplatform.team.git.controller.GitVirtualChildPropertiesProvider;
import org.flowerplatform.team.git.controller.GitPropertiesProvider;
import org.flowerplatform.team.git.controller.GitRefPropertiesProvider;
import org.flowerplatform.team.git.controller.GitRefsChildrenProvider;
import org.flowerplatform.team.git.controller.GitRemotePropertiesProvider;
import org.flowerplatform.team.git.controller.GitRemotesChildrenProvider;
import org.flowerplatform.team.git.controller.GitResourceHandler;
import org.flowerplatform.team.git.controller.RepoChildrenProvider;
import org.flowerplatform.team.git.history.internal.GitHistoryConstants;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Valentina-Camelia Bojan
 * @author Cojocea Marius Eduard
 */
public class GitPlugin extends AbstractFlowerJavaPlugin {
	
	protected static GitPlugin INSTANCE;
		
	public static GitPlugin getInstance() {
		return INSTANCE;
	}
		
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
			
		CorePlugin.getInstance().getServiceRegistry().registerService("GitService", new GitService());
		CorePlugin.getInstance().getServiceRegistry().registerService("HistoryService", new HistoryService());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(GIT_CATEGORY);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(GIT_REF)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitRefPropertiesProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.name")).setOrderIndexAs(0))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FULL_NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.fullName")).setReadOnlyAs(true).setOrderIndexAs(1))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(COMMIT_ID).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.commitID")).setReadOnlyAs(true).setOrderIndexAs(2))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(COMMIT_MESSAGE).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.commitMessage")).setReadOnlyAs(true).setOrderIndexAs(3))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(IS_CHECKEDOUT).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.isCheckout")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setReadOnlyAs(true).setOrderIndexAs(4))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CONFIG_UPSTREAM_BRANCH).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.configUpstreamBranch")).setReadOnlyAs(true).setOrderIndexAs(5))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CONFIG_REMOTE).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.configRemote")).setReadOnlyAs(true).setOrderIndexAs(6))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CONFIG_REBASE).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.configRebase")).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setReadOnlyAs(true).setOrderIndexAs(7));;

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REPOSITORY_TYPE)
			.addAdditiveController(CHILDREN_PROVIDER, new RepoChildrenProvider());
			
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REPO_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitPropertiesProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.name")).setOrderIndexAs(0))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CURRENT_BRANCH).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.currentBranch")).setReadOnlyAs(true).setOrderIndexAs(1))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CURRENT_COMMIT).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.currentCommit")).setReadOnlyAs(true).setOrderIndexAs(2))
			.addAdditiveController(CHILDREN_PROVIDER, new GitChildrenProvider())
			.addCategory(GIT_CATEGORY);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_LOCAL_BRANCHES_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitVirtualChildPropertiesProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.name")).setOrderIndexAs(0))
			.addAdditiveController(CHILDREN_PROVIDER, new GitRefsChildrenProvider())
			.addCategory(GIT_CATEGORY);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTE_BRANCHES_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitVirtualChildPropertiesProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.name")).setOrderIndexAs(0))
			.addAdditiveController(CHILDREN_PROVIDER, new GitRefsChildrenProvider())
			.addCategory(GIT_CATEGORY);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_TAGS_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitVirtualChildPropertiesProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.name")).setOrderIndexAs(0))
			.addAdditiveController(CHILDREN_PROVIDER, new GitRefsChildrenProvider())
			.addCategory(GIT_CATEGORY);
			
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTES_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitVirtualChildPropertiesProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.name")).setOrderIndexAs(0))
			.addAdditiveController(CHILDREN_PROVIDER, new GitRemotesChildrenProvider())
			.addCategory(GIT_CATEGORY);
				
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_LOCAL_BRANCH_TYPE)
			.addCategory(GIT_REF)
			.addCategory(GIT_CATEGORY);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTE_BRANCH_TYPE)
			.addCategory(GIT_REF)
			.addCategory(GIT_CATEGORY);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_TAG_TYPE)
			.addCategory(GIT_REF)
			.addCategory(GIT_CATEGORY);

		//CorePlugin.getInstance().getResourceService().addResourceHandler(GIT_SCHEME, new BaseResourceHandler(GIT_REPO_TYPE));

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitRemotePropertiesProvider())
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.name")).setOrderIndexAs(0))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FETCH_REF_SPECS).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.fetchRefSpecs")).setReadOnlyAs(true).setOrderIndexAs(1))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(PUSH_REF_SPECS).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.pushRefSpecs")).setReadOnlyAs(true).setOrderIndexAs(2))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(REMOTE_URIS).setTitleAs(ResourcesPlugin.getInstance().getMessage("git.remoteURIs")).setReadOnlyAs(true).setOrderIndexAs(3));

		CorePlugin.getInstance().getResourceService().addResourceHandler(GIT_SCHEME, new GitResourceHandler());
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
}
