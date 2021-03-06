/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
import static org.flowerplatform.util.UtilConstants.FEATURE_PROPERTY_DESCRIPTORS;
import static org.flowerplatform.util.UtilConstants.PROPERTY_EDITOR_TYPE_BOOLEAN;
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

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.team.git.controller.GitChildrenProvider;
import org.flowerplatform.team.git.controller.GitPropertiesProvider;
import org.flowerplatform.team.git.controller.GitRefPropertiesProvider;
import org.flowerplatform.team.git.controller.GitRefsChildrenProvider;
import org.flowerplatform.team.git.controller.GitRemotePropertiesProvider;
import org.flowerplatform.team.git.controller.GitRemotesChildrenProvider;
import org.flowerplatform.team.git.controller.GitResourceHandler;
import org.flowerplatform.team.git.controller.GitVirtualChildPropertiesProvider;
import org.flowerplatform.team.git.controller.RepoChildrenProvider;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Valentina-Camelia Bojan
 * @author Cojocea Marius Eduard
 */
public class GitPlugin extends AbstractFlowerJavaPlugin {
	
	protected static GitPlugin instance;
		
	public static GitPlugin getInstance() {
		return instance;
	}
		
	/**
	 *@author see class
	 **/
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		instance = this;

		CorePlugin.getInstance().getServiceRegistry().registerService("GitService", new GitService());
		CorePlugin.getInstance().getServiceRegistry().registerService("gitHistoryService", new GitHistoryService());

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(GIT_CATEGORY);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(GIT_REF)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitRefPropertiesProvider())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setOrderIndexAs(0))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FULL_NAME).setReadOnlyAs(true).setOrderIndexAs(1))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(COMMIT_ID).setReadOnlyAs(true).setOrderIndexAs(2))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(COMMIT_MESSAGE).setReadOnlyAs(true).setOrderIndexAs(3))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(IS_CHECKEDOUT)
					.setTypeAs(PROPERTY_EDITOR_TYPE_BOOLEAN).setReadOnlyAs(true).setOrderIndexAs(4))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(CONFIG_UPSTREAM_BRANCH).setReadOnlyAs(true).setOrderIndexAs(5))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(CONFIG_REMOTE).setReadOnlyAs(true).setOrderIndexAs(6))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor()
					.setNameAs(CONFIG_REBASE).setTypeAs(PROPERTY_EDITOR_TYPE_BOOLEAN).setReadOnlyAs(true).setOrderIndexAs(7));

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REPOSITORY_TYPE)
			.addAdditiveController(CHILDREN_PROVIDER, new RepoChildrenProvider());
			
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REPO_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitPropertiesProvider())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setOrderIndexAs(0))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(CURRENT_BRANCH).setReadOnlyAs(true).setOrderIndexAs(1))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(CURRENT_COMMIT).setReadOnlyAs(true).setOrderIndexAs(2))
			.addAdditiveController(CHILDREN_PROVIDER, new GitChildrenProvider())
			.addCategory(GIT_CATEGORY);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_LOCAL_BRANCHES_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitVirtualChildPropertiesProvider())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setOrderIndexAs(0))
			.addAdditiveController(CHILDREN_PROVIDER, new GitRefsChildrenProvider())
			.addCategory(GIT_CATEGORY);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTE_BRANCHES_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitVirtualChildPropertiesProvider())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setOrderIndexAs(0))
			.addAdditiveController(CHILDREN_PROVIDER, new GitRefsChildrenProvider())
			.addCategory(GIT_CATEGORY);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_TAGS_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitVirtualChildPropertiesProvider())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setOrderIndexAs(0))
			.addAdditiveController(CHILDREN_PROVIDER, new GitRefsChildrenProvider())
			.addCategory(GIT_CATEGORY);
			
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTES_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitVirtualChildPropertiesProvider())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setOrderIndexAs(0))
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

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new GitRemotePropertiesProvider())
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setOrderIndexAs(0))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FETCH_REF_SPECS).setReadOnlyAs(true).setOrderIndexAs(1))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(PUSH_REF_SPECS).setReadOnlyAs(true).setOrderIndexAs(2))
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(REMOTE_URIS).setReadOnlyAs(true).setOrderIndexAs(3));

		CorePlugin.getInstance().getResourceService().addResourceHandler(GIT_SCHEME, new GitResourceHandler());
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
}
