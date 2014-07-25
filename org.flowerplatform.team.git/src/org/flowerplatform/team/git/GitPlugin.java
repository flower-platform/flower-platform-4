package org.flowerplatform.team.git;

import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.REPOSITORY_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_BRANCH_SCHEME;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCHES_SCHEME;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTES_SCHEME;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCHES_SCHEME;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCHES_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REPO_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_SCHEME;
import static org.flowerplatform.team.git.GitConstants.GIT_TAGS_SCHEME;
import static org.flowerplatform.team.git.GitConstants.GIT_TAGS_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_TAG_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_LOCAL_BRANCH_TYPE;
import static org.flowerplatform.team.git.GitConstants.GIT_REMOTE_BRANCH_TYPE;

import org.eclipse.jgit.lib.Constants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.resource.BaseResourceHandler;
import org.flowerplatform.team.git.controller.GitBranchAndTagPropertiesProvider;
import org.flowerplatform.team.git.controller.GitBranchesAndTagsChildrenProvider;
import org.flowerplatform.team.git.controller.GitChildrenProvider;
import org.flowerplatform.team.git.controller.GitLocalBranchesPropertiesProvider;
import org.flowerplatform.team.git.controller.GitPropertiesProvider;
import org.flowerplatform.team.git.controller.GitRemoteBranchesPropertiesProvider;
import org.flowerplatform.team.git.controller.GitRemotesChildrenProvider;
import org.flowerplatform.team.git.controller.GitRemotesPropertiesProvider;
import org.flowerplatform.team.git.controller.GitResourceHandler;
import org.flowerplatform.team.git.controller.GitTagsPropertiesProvider;
import org.flowerplatform.team.git.controller.GitVirtualNodeResourceHandler;
import org.flowerplatform.team.git.controller.RepoChildrenProvider;
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
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REPOSITORY_TYPE)
		.addAdditiveController(CHILDREN_PROVIDER, new RepoChildrenProvider());
		
	
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REPO_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new GitPropertiesProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new GitChildrenProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_LOCAL_BRANCHES_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new GitLocalBranchesPropertiesProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new GitBranchesAndTagsChildrenProvider
				(Constants.R_HEADS,GIT_BRANCH_SCHEME,GIT_LOCAL_BRANCH_TYPE));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTE_BRANCHES_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new GitRemoteBranchesPropertiesProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new GitBranchesAndTagsChildrenProvider
				(Constants.R_REMOTES,GIT_BRANCH_SCHEME,GIT_REMOTE_BRANCH_TYPE));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_TAGS_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new GitTagsPropertiesProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new GitBranchesAndTagsChildrenProvider
				(Constants.R_TAGS,GIT_BRANCH_SCHEME,GIT_TAG_TYPE));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTES_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new GitRemotesPropertiesProvider())
		.addAdditiveController(CHILDREN_PROVIDER, new GitRemotesChildrenProvider());
		
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_LOCAL_BRANCH_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new GitBranchAndTagPropertiesProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_REMOTE_BRANCH_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new GitBranchAndTagPropertiesProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(GIT_TAG_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new GitBranchAndTagPropertiesProvider());		
		
		//to be modified later --> usage of only one scheme with multiple handlers
		//need to modify: ResourceService.java, addResourceHandler method
		CorePlugin.getInstance().getResourceService().addResourceHandler(GIT_SCHEME, new BaseResourceHandler(GIT_REPO_TYPE));
		CorePlugin.getInstance().getResourceService().addResourceHandler(GIT_LOCAL_BRANCHES_SCHEME, new GitVirtualNodeResourceHandler(GIT_LOCAL_BRANCHES_TYPE));
		CorePlugin.getInstance().getResourceService().addResourceHandler(GIT_REMOTE_BRANCHES_SCHEME, new GitVirtualNodeResourceHandler(GIT_REMOTE_BRANCHES_TYPE));
		CorePlugin.getInstance().getResourceService().addResourceHandler(GIT_TAGS_SCHEME, new GitVirtualNodeResourceHandler(GIT_TAGS_TYPE));
		CorePlugin.getInstance().getResourceService().addResourceHandler(GIT_REMOTES_SCHEME, new GitVirtualNodeResourceHandler(GIT_REMOTES_TYPE));
		CorePlugin.getInstance().getResourceService().addResourceHandler(GIT_BRANCH_SCHEME, new GitResourceHandler());
		
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
}
